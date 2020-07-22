
//From https://stackoverflow.com/questions/20327053/can-i-convert-hl7-v2-message-to-a-fhir-resource-directly

/*
segments are not naturally identified, and resources must be (that's the core of the RESTful part). 
And identifying segments usefully is a business problem - it must be done based on the contents of the segment, 
it's context in the message, and local identification etc practices applicable to the source of the message
*/
function A31ToPerson(aEvent)
{
    // this function mines a A04 message to create a FHIR Patient resource
    // Mostly the Person resource only covers the information in the PID segment.
    //
    // the actual message is the one found in the v2.5 standard (section 3.5.3):
//MSH|^~\&|REGADT|MCM|IFENG||199112311501||ADT^A04^ADT_A01|000001|P|2.5|||
//EVN|A04|200301101500|200301101400|01||200301101410
//PID|||191919^^^GENHOS^MR~371-66-9256^^^USSSA^SS|253763|MASSIE^JAMES^A||19560129|M|||171 ZOBER-LEIN^^ISHPEMING^MI^49849^""^||(900)485-5344|(900)485-5344||S|C|10199925^^^GENHOS^AN|371-66-9256||<cr>
//NK1|1|MASSIE^ELLEN|SPOUSE|171 ZOBERLEIN^^ISHPEMING^MI^49849^""^|(900)485-5344|(900)545-1234~(900)545-1200|EC1^FIRST EMERGENCY CONTACT<cr>
//NK1|2|MASSIE^MARYLOU|MOTHER|300 ZOBERLEIN^^ISHPEMING^MI^49849^""^|(900)485-5344|(900)545-1234~(900)545-1200|EC2^SECOND EMERGENCY CONTACT<cr>
//NK1|3<cr>
//NK1|4|||123 INDUSTRY WAY^^ISHPEMING^MI^49849^""^||(900)545-1200|EM^EMPLOYER|19940605||PROGRAMMER|||ACME SOFTWARE COMPANY<cr>
//PV1||O|O/R||||0148^ADDISON,JAMES|0148^ADDISON,JAMES|0148^ADDISON,JAMES|AMB|||||||0148^ADDISON,JAMES|S|1400|A|||||||||||||||||||GENHOS|||||199501101410|<cr>
//PV2||||||||200301101400||||||||||||||||||||||||||200301101400<cr>
//OBX||ST|1010.1^BODY WEIGHT||62|kg|||||F<cr>
//OBX||ST|1010.1^HEIGHT||190|cm|||||F<cr>
//DG1|1|19||BIOPSY||00|<cr>
//GT1|1||MASSIE^JAMES^""^""^""^""^||171 ZOBERLEIN^^ISHPEMING^MI^49849^""^|(900)485-5344|(900)485-5344||||SE^SELF|371-66-925||||MOOSES AUTO CLINIC|171 ZOBER-LEIN^^ISHPEMING^MI^49849^""|(900)485-5344|<cr>
//IN1|0|0|BC1|BLUE CROSS|171 ZOBERLEIN^^ISHPEMING^M149849^""^||(900)485-5344|90||||||50 OK|<cr>
//IN1|2|""|""<cr>

   var v2 = aEvent.Source.Message;
   var p = FHIR.newPatient;
   var r = FHIR.makeRequest();
   r.Resource = p;
   aEvent.Dest.FhirRequest = r;

   // fill out the request   
   r.id = makePatientId(v2);
   if (r.id == null || r.id == "")
     r.id = "test";
   r.ResourceType = frtPatient;
   r.CommandType = fcmdUpdate;
   r.Lang = "en";
   

   // now we have to build the Person resource.
   fillOutPatientDetails(p, v2);
   buildNarrative(p, v2);
  }
  
function makePatientId(v2)
{
  // this is one of the hardest parts of a message to resource conversion. The id is a stable reference that never changes.
  // the patients MRN - as provided in the PID segment - is not a suitable identifier. MRNs are managed and merged, where as
  // resource IDs are stable and permanent. It's not generally good to use human identifiers for resource ids. We might be able
  // to use it if we are (talking to) the primary patient management system, and MRNs are managed appropriately.
  //
  // The SSN number is even less suitable.
  //
  // The Account number might be suitable - some sites use the account number to represent the primary key, while others
  // use it for the primary key of the record of the patient (that's cause the definition is so good: "An identifier that is unique to an account").
  //
  // if we wanted to use the account number:
  //   return v2.Element("PID-19-1").AsString;
  //  if we wanted to use the MRN:
  //   return v2.Element("PID-3[@5 = \"MR\"].1").AsString;
  //
  // if we decide that we can't use one of them, then we have to make one up. How do we do that?
  // well, we could simply search the destination repository, but what do we do if we get multiple matches?
  // A more likely alternative is to maintain some database somewhere, and source the id from that database.
  // typically this database would be built by observing an HL7 messages stream, likely the one that this
  // message is part of. That's going to have local logic, no general solution is possible.
  //
  // Finally this message example has the otherwise undocumented PID-4 ("253763"). For the purposes
  // of this example, we are going to make the assertion that this is an identifier suitable for use
  // as the resource id. (probably not true in real life)
  return v2.Element("PID-4").AsString;
}

function fillOutPatientDetails(p, v2)
{
  // MRN. We need to assign a global scope to the mrn. It has to be known based on local config
   p.identifierList.addItem(FHIR.makeIdentifierWithUse(
     "http://www.health.acme.com/"+v2.Element("PID-3[@5 = \"MR\"].4").AsString+"/patients", v2.Element("PID-3[@5 = \"MR\"].1").AsString,
     "usual", "mrn"));

  // SSN. 
   p.identifierList.addItem(FHIR.makeIdentifierWithUse("urn:hl7-org:sid/us-ssn", v2.Element("PID-3[@5 = \"SS\"].1").AsString, "official", "ssn"));

  // acccount number. the implication of the identifier space is that all acme healthcare institutions share the same account details
   p.identifierList.addItem(FHIR.makeIdentifierWithUse("http://www.health.acme.com/accounts", v2.Element("PID-19.1").AsString, "", "account"));

  // we don't know anything about links

  // MASSIE^JAMES^A
  var n2 = v2.element("PID-5");
  var n = FHIR.makeHumanNameText("", n2.component(2).AsString+" "+n2.component(3).AsString+" "+n2.component(1).AsString); // no known use code in this case
  n.familyList.addItem(FHIR.makeString(n2.component(1).AsString));
  n.givenList.addItem(FHIR.makeString(n2.component(2).AsString));
  n.givenList.addItem(FHIR.makeString(n2.component(3).AsString));
  p.nameList.addItem(n);

  // 171 ZOBER-LEIN^^ISHPEMING^MI^49849^""^
  var a2 = v2.element("PID-11");
  var a = FHIR.makeAddressText("", a2.component(1).AsString+"\r\n"+a2.component(3).AsString+" "+a2.component(4).AsString+" "+a2.component(5).AsString);
  a.lineList.addItem(FHIR.makeString(a2.component(1).AsString));
  a.cityST = a2.component(3).AsString;
  a.stateST = a2.component(4).AsString;
  a.zipST = a2.component(5).AsString;
  p.addressList.addItem(a);

  // (900)485-5344|(900)485-5344
  p.telecomList.addItem(FHIR.makeContact("phone", v2.element("PID-13").AsString, "home"));
  p.telecomList.addItem(FHIR.makeContact("phone", v2.element("PID-14").AsString, "work"));

  p.birthDate = v2.element("PID-7").AsString;
  // gender & religion: code is also original text. That may not be appropriate, depending on the source of the message
  p.gender = FHIR.makeCodeableConcept(FHIR.makeCoding("http://hl7.org/fhir/v3/AdministrativeGender", v2.element("PID-8").AsString, v2.element("PID-8").DisplayForCode), v2.element("PID-8").AsString);
  // note that religion code in example message has "C" which is illegal. correct to "CHR" to make this work
  var cc = FHIR.makeCodeableConcept(FHIR.makeCoding("urn:hl7-org:sid/v2-0006", v2.element("PID-17").AsString, v2.element("PID-17").DisplayForCode), v2.element("PID-17").AsString);
  var ext = FHIR.makeExtension("http://www.healthintersections.com.au/fhir/extensions#religion", false, cc);
  p.extensionList.addItem(ext);

  // no qualifications, or language information
  
//  // related people built from the NK1s:
//  for (i = 0; i < v2.CountSegment("NK1"); i++) {
//    nk1 = v2.Segment("NK1", i);
//    if (nk1.Field(2).Defined) {
//      var r = FHIR.newPersonRelatedPerson;
//      p.relatedPerson.addItem(r);
//      r.role = FHIR.makeCodeableConcept(FHIR.makeCoding("urn:hl7-org:sid/v2-0063", nk1.Field("3").AsString, nk1.Field("3").DisplayForCode), nk1.Field("3").AsString);
//
//      var n2 = nk1.field(2);
//      var n = FHIR.makeHumanName("", n2.component(2).AsString+" "+n2.component(1).AsString);
//      n.part.addItem(FHIR.makeHumanNamePart(tnptFamily, n2.component(1).AsString));
//      n.part.addItem(FHIR.makeHumanNamePart("given", n2.component(2).AsString));
//      r.name = n;
//      r.contact.addItem(FHIR.makeContact("phone", nk1.field(5).AsString, "home"));
//      r.contact.addItem(FHIR.makeContact("phone", nk1.field(6).AsString, "work"));
//    } else if (nk1.Field(13).Defined) {
//      // employer - not added to FHIR resource for now.
//    }
//  }
}

function buildNarrative(p, v2)
{
  p.text = FHIR.newNarrative;
  p.text.statusST = NarrativeStatusGenerated;

  var n2 = v2.element("PID-5");
  var name = n2.component(2).AsString+" "+n2.component(3).AsString+" "+n2.component(1).AsString;
  var a2 = v2.element("PID-11");
  var address = a2.component(1).AsString+", "+a2.component(3).AsString+" "+a2.component(4).AsString+" "+a2.component(5).AsString;
  var mrn = v2.Element("PID-3[@5 = \"MR\"].1").AsString;
  var dob = v2.element("PID-7").AsString;
  var gender = v2.element("PID-8").DisplayForCode;;


  p.text.div = FHIR.parseHtml("<div>Patient "+name+" ("+mrn+"): "+gender+", born "+dob+".<br/>Address: "+address+"</div>");
}