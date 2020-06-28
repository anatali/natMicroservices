package it.unibo.springboot.qak;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import connQak.connQakCoap;
import connQak.sysConnKb;
import it.unibo.ctxprj0.MainCtxprj0Kt;
import it.unibo.is.interfaces.protocols.IConnInteraction;
import it.unibo.kactor.ActorBasic;
import it.unibo.kactor.ApplMessage;
import it.unibo.kactor.MsgUtil;
import it.unibo.kactor.sysUtil;
import it.unibo.supports.FactoryProtocol;

 
@Controller 
public class CommandController   { 
	@Value("${project}") String projectName;

	private IConnInteraction connTcp    = null;
 	private FactoryProtocol fp			= null;
  	private connQakCoap coapSupport     = null;
  	private connQakCoap robotcoapSupport= null;
    private boolean internalNanoservce  = false;      
    
    private String robotName   = "";
    private String robotHost   = "";
    private String robotPort   = "";
    private String robotCtx    = "";

    private String applName    = sysConnKb.getQakdestination();
    private String applHost    = sysConnKb.getApplhostAddr();
    private String applPort    = sysConnKb.getApplport();
    private String applCtx     = sysConnKb.getCtxqadest();

	public CommandController( ) {   
	  fp = new FactoryProtocol(null,"TCP","spring"); 
	  sysConnKb.configure();
	  robotName    = sysConnKb.getRobotname();
	  robotHost    = sysConnKb.getRobothostAddr();
	  robotPort    = sysConnKb.getRobotport();
	  robotCtx     = sysConnKb.getCtxrobot();
	}
	
  //curl --request POST http://localhost:8080/w
   
  //@RequestMapping(value = "/w", method = RequestMethod.POST)
  //@PostMappingspecialized version of @RequestMapping
  @PostMapping( path = "/w")  public String robotmd_wW(Model model) {  return doMove(model,"w"); } 
  @PostMapping( path = "/s")  public String robotCmd_s(Model model) {  return doMove(model,"s"); } 
  @PostMapping( path = "/h")  public String robotCmd_h(Model model) {  return doMove(model,"h"); } 
  @PostMapping( path = "/r")  public String robotCmd_r(Model model) {  return doMove(model,"r"); } 
  @PostMapping( path = "/l")  public String robotCmd_l(Model model) {  return doMove(model,"l"); } 
  @PostMapping( path = "/x")  public String robotCmd_x(Model model) {  return doMove(model,"x"); } 
  @PostMapping( path = "/z")  public String robotCmd_z(Model model) {  return doMove(model,"z"); } 
  @PostMapping( path = "/p")  public String robotCmd_p(Model model) {  return doMove(model,"p"); } 
  
  private String doMove( Model model, String move) {
	  System.out.println("POST | doMove:"+move);
	  model.addAttribute("robot", "move(Tcp)="+move );	
	  String destName = "";
	  if( internalNanoservce ) destName = sysConnKb.getQakdestination();
	  else destName = robotName;
 	  if( ! forwardTcp("cmd", "cmd("+move+")", destName) ) {
		  model.addAttribute("robot", "forwardTcp failure" );
		  model.addAttribute("info", "perhaps internal nano service not active" );
	  };
	  return getAnswer(model);
  }
  
  /*
   *  @RequestBody annotation maps the HttpRequest body to a transfer or domain object, 
   *  enabling automatic deserialization of the inbound HttpRequest body onto a Java object.
   */  
  @PostMapping( path = "/move") 			//specialized version of @RequestMapping
  public String robotMove(
	//@RequestParam binds the value of the query string param name into the moveName parameter of the  method		  
		   @RequestParam(name="say",  required=false, defaultValue="h") String moveName, 
		   @RequestParam(name="dest", required=false, defaultValue="basicrobot") String destName, 
		   Model model 
	) { 
	  System.out.println("POST | move=" + moveName);
	  model.addAttribute("robot", robotName);
	  model.addAttribute("destnameinput", destName);
	  model.addAttribute("movenameinput", moveName);
	  model.addAttribute("movename", "move(Coap)="+moveName);
	  sendUsingCoap(moveName, destName);
	  return getAnswer(model);
  } 
  
  @PostMapping( path = "/runqak") 			//specialized version of @RequestMapping
  public String runqak(Model model) { 
	  if( internalNanoservce ) {
		  model.addAttribute("info", "qak nanoservice already started" );
		  return getAnswer(model);
	  }
	  model.addAttribute("robot", "move=runqak");
	  new Thread() {
		  public void run() {
			  MainCtxprj0Kt.main();			
		  } 
	  }.start();
	  internalNanoservce = true;	  
	  attemptQakConnections();
	  model.addAttribute("info", "qak nanoservice started" );
  	  return getAnswer(model);
  } 
  
  /*
   * --------------------------------------------------------
   * UTILITIES  
   * --------------------------------------------------------
   */  
  private String getAnswer(Model model) {
	  addInfoInModel(model);
 	  return "welcome";	  
  }
  
  private void addInfoInModel(Model model) {
      model.addAttribute("project", projectName);
      model.addAttribute("address", ApplicationWithQak.myipAddr);
      model.addAttribute("port", ApplicationWithQak.myport);	  
  }
 
  private void  sendUsingCoap(String move, String destName){
//      System.out.println( "		--- sendUsingCoap | robotHost="+robotHost+" robotPort="+robotPort+" robotName="+robotName
//    		  			+" robotCtx="+robotCtx+" destName="+destName );
	  if( destName.equals( robotName  )) {
		  if( robotcoapSupport == null ) {
			  robotcoapSupport  = new connQakCoap(robotHost,robotPort,robotName,robotCtx );	
			  robotcoapSupport.createConnection();
		  }
		  ApplMessage m = MsgUtil.buildDispatch("aliencoap", "cmd", "cmd("+move+")",destName); 
		  robotcoapSupport.forward(m);
	  }else {
 		ApplMessage m = MsgUtil.buildDispatch("aliencoap", "cmd", "cmd("+move+")",destName); 
		if(coapSupport != null) coapSupport.forward(m);
	  }
  }
  
  private boolean forwardTcp( String msgId, String msg, String destName ) {
	ApplMessage m = MsgUtil.buildDispatch("alienTcp", msgId, msg,destName); 
 	try {
 		if( connTcp != null ) { connTcp.sendALine(m.toString());			};
	} catch (Exception e) {
 		//e.printStackTrace();
	}
	return (connTcp!=null);
  }  
  
  private void attemptQakConnections( ) {
	  if( internalNanoservce	) {
		  System.out.println("attemptQakConnection " + applHost + ":" + applPort);
		  coapSupport  = new connQakCoap( applHost,applPort,applName,applCtx );
		  coapSupport.createConnection();
		  while( connTcp == null ) {
			  try {
				  Thread.sleep(500); //give the time to start ....
				  int port = Integer.parseInt(sysConnKb.getPort());
				  connTcp = fp.createClientProtocolSupport(sysConnKb.getHostAddr(),port);
			  } catch (Exception e) {
				  	e.printStackTrace();
			  }		  
			  }		  
	  }
  }
  
  
   
  /*
   * --------------------------------------------------------
   * DIRECT INSERTION IN ACTIR QUEUE: better avod
    * --------------------------------------------------------
  */
  private void emit( String evId, String msg ) {
	  ApplMessage m = MsgUtil.buildEvent("spring", evId, msg); 
	  ActorBasic a = sysUtil.getActor("qa0"); 
	  if( a != null ) a.getActor().send(m, null);
	  System.out.println("emit: " + evId);
  }
  private void forward( String msgId, String msg ) {
	  ApplMessage m = MsgUtil.buildDispatch("spring", msgId, msg,"qa0"); 
	  ActorBasic a  = sysUtil.getActor("qa0"); 
	  System.out.println("forward: " + msgId + " to:" + a); 
	  if( a != null ) a.getActor().send(m, null);
   }   
  
}

