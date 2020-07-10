package it.unibo.mirth.java;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class XmlUtil {
	
	public static String toPrettyString(String xml, int indent) {
	    try {
	        // Turn xml string into a document
	        Document document = DocumentBuilderFactory.newInstance()
	                .newDocumentBuilder()
	                .parse(new InputSource(new ByteArrayInputStream(xml.getBytes("utf-8"))));

	        // Remove whitespaces outside tags
	        document.normalize();
	        XPath xPath = XPathFactory.newInstance().newXPath();
	        NodeList nodeList = (NodeList) xPath.evaluate("//text()[normalize-space()='']",
	                                                      document,
	                                                      XPathConstants.NODESET);

	        for (int i = 0; i < nodeList.getLength(); ++i) {
	            Node node = nodeList.item(i);
	            node.getParentNode().removeChild(node);
	        }

	        // Setup pretty print options
	        TransformerFactory transformerFactory = TransformerFactory.newInstance();
	        transformerFactory.setAttribute("indent-number", indent);
	        Transformer transformer = transformerFactory.newTransformer();
	        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
	        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
	        transformer.setOutputProperty(OutputKeys.INDENT, "yes");	
	        // Return pretty print xml string
	        StringWriter stringWriter = new StringWriter();
	        transformer.transform(new DOMSource(document), new StreamResult(stringWriter));
	        return stringWriter.toString();
	    } catch (Exception e) {
	        throw new RuntimeException(e);
	    }
	}	
	
	public static void xml(String xmldoc) throws SAXException, IOException, ParserConfigurationException {
	    System.out.println( toPrettyString( xmldoc, 5) );

	    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
 		DocumentBuilder builder = factory.newDocumentBuilder();	
		InputStream targetStream = new ByteArrayInputStream(xmldoc.getBytes());
		Document document = builder.parse( targetStream );
		
//		Schema schema = null;
//		try {
//		  String language = XMLConstants.W3C_XML_SCHEMA_NS_URI;
//		  SchemaFactory factory = SchemaFactory.newInstance(language);
//		  schema = factory.newSchema(new File(name));
//		} catch (Exception e) {
//		    e.printStackTrace();
//		}
//		Validator validator = schema.newValidator();
//		validator.validate(new DOMSource(document));		
		
	      //Normalize the XML Structure; It's just too important !!
	      document.getDocumentElement().normalize();
	      
	      //Here comes the root node
	      Element root = document.getDocumentElement();
	      System.out.println(root.getNodeName());	      
	      //Get all employees
	      NodeList nList = document.getElementsByTagName("list");
	      System.out.println("============================");
//	      visitChildNodes(nList,"user");
	}
	
	   private static void visitChildNodes(NodeList nList, String rootName){
	 	      for (int temp = 0; temp < nList.getLength(); temp++){
	 	         Node node = nList.item(temp);
		         if (node.getNodeType() == Node.ELEMENT_NODE) {
		        	 if( node.getNodeName().equals(rootName) ) {
		        		 System.out.println("Node:" + node.getNodeName()   );
		                 if (node.hasChildNodes()) {
		                     //We got more childs; Let's visit them as well
		                     visitChildNodes(node.getChildNodes());
		                  }else {//no child
		                	  	System.out.println("Node:" + node.getNodeName() + "\t\tvalue=" + node.getTextContent() );		                	  
		                  }
		        	 }else {//not rootName
			             if (node.hasChildNodes()) {
			                 //We got more childs; Let's visit them as well
			                 visitChildNodes(node.getChildNodes(), rootName);
			             }		        	 
		        	 }
		         }		
	 	      }//for
	   }
	   private static void visitChildNodes(NodeList nList){
//		   System.out.println("visitChildNodes n=" + nList.getLength()  );
 	      for (int temp = 0; temp < nList.getLength(); temp++)
	      {
	         Node node = nList.item(temp);
	         if (node.getNodeType() == Node.ELEMENT_NODE) {
//	            System.out.println("Node Name = " + node.getNodeName() + "; Value = " + node.getTextContent());
	        	 
	            System.out.println("Node Name:" + node.getNodeName() + "\t\t=" + node.getTextContent() );
	            //Check all attributes
	            if (node.hasAttributes()) {
	               // get attributes names and values
	               NamedNodeMap nodeMap = node.getAttributes();
	               for (int i = 0; i < nodeMap.getLength(); i++)
	               {
	                   Node tempNode = nodeMap.item(i);
	                   System.out.println("Attr name : " + tempNode.getNodeName()+ "; Value = " + tempNode.getNodeValue());
	               }
//	               if (node.hasChildNodes()) {
//	                  //We got more childs; Let's visit them as well
//	                  visitChildNodes(node.getChildNodes());
//	               }
	           }
	         }
	         
             if (node.hasChildNodes()) {
                 //We got more childs; Let's visit them as well
                 visitChildNodes(node.getChildNodes());
              }
	         
	      }
	   }
	   
	   private static void showChildNode( Node  node ){
		   
	   }

}
