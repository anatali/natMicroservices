package it.unibo.springboot.qak;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.context.event.ApplicationReadyEvent;
//import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.ExceptionHandler;
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
public class CommandController { 
    private final ResourceLoader resourceLoader;
	private static String RESOURCE_ROOT = "src/main/resources";
	private static String JAVA_ROOT     = "src/main/java";

	private IConnInteraction conn       = null;
	private FactoryProtocol fp			= null;
 	private connQakCoap coapSupport         = null;
	  
	
	String applicationModelRep="waiting";
	
	@Value("${human.logo}")
    String appName;
    
    Set<String> robotMoves = new HashSet<String>(); 
    
    public CommandController(ResourceLoader resourceLoader) {
    	this.resourceLoader = resourceLoader;
        robotMoves.addAll( Arrays.asList(new String[] {"w","s","h","r","l","z","x"}) );        	
        fp = new FactoryProtocol(null,"TCP","spring"); 	 
        coapSupport  = new connQakCoap(
        		sysConnKb.INSTANCE.getHostAddr(),sysConnKb.INSTANCE.getPort(),sysConnKb.INSTANCE.getQakdestination());
        coapSupport.createConnection();
    }
    
    public String entry(Model model) {
        //addIpInfoInModel(model);
        return "welcome";
    } 

  
  
  //curl --request POST http://localhost:8080/w
   
  //@RequestMapping(value = "/w", method = RequestMethod.POST)
  @PostMapping( path = "/w") 			//specialized version of @RequestMapping
  public String robotCmd(Model model) { 
	  System.out.println("POST w");
	  model.addAttribute("robot", "move=w");
 	  //MsgUtil.INSTANCE.sendMsg("msg1","msg1(1)","qa0", null, null);
	  //emit("alarm", "alarm(fire)");
      return entry(model);
  } 
  
  /*
   *  @RequestBody annotation maps the HttpRequest body to a transfer or domain object, 
   *  enabling automatic deserialization of the inbound HttpRequest body onto a Java object.
   */
  
  @PostMapping( path = "/move") 			//specialized version of @RequestMapping
  public String robotMove(
		  //@RequestBody String move
		   @RequestParam(name="say", required=false, defaultValue="h") String moveName, 
		   Model model 
			//binds the value of the query string parameter name into the moveName parameter of the  method		  
	) { 
	  System.out.println("POST move=" + moveName);
	  model.addAttribute("robot", "move="+moveName);
	  sendUsingCoap();
//	  if( ! forwardTcp("msg1", "msg1("+moveName+")") ) {
//		  model.addAttribute("robot", "forwardTcp failure" );
//	  };
      return entry(model);
  } 
  
  @PostMapping( path = "/runqak") 			//specialized version of @RequestMapping
  public String runqak(Model model) { 
	  System.out.println("POST runqak");
	  model.addAttribute("robot", "move=runqak");
	  new Thread() {
		  public void run() {
			  MainCtxprj0Kt.main();				  
		  } 
	  }.start();
	  attemptQakConnection("localhost",8095);
      return entry(model);
  } 
  
  private void attemptQakConnection(String host, int port) {
	  while( conn == null ) {
	  try {
		  System.out.println("attemptQakConnection " + host + ":" + port);
		  Thread.sleep(500); //give the time to start ....
		  conn = fp.createClientProtocolSupport(host,port);
	  } catch (Exception e) {
		  	e.printStackTrace();
	  }		  
	  }
  }
  
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
  
  private void  sendUsingCoap(){
	    //connQakCoap support = new connQakCoap("localhost","8095","qa0");
	    //support.createConnection();
		ApplMessage m = MsgUtil.buildDispatch("aliencoap", "msg1", "msg1(x)","qa0"); 
		if(coapSupport != null) coapSupport.forward(m);
  }
  
  private boolean forwardTcp( String msgId, String msg ) {
	  ApplMessage m = MsgUtil.buildDispatch("spring", msgId, msg,"qa0"); 
 	try {
 		if( conn != null ) { conn.sendALine(m.toString());			};
	} catch (Exception e) {
 		//e.printStackTrace();
	}
	return (conn!=null);
  }  
  
  
}

