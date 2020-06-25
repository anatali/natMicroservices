package it.unibo.webspring.intro;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import connQak.connQakCoap;
import it.unibo.ctxprj0.MainCtxprj0Kt;
import it.unibo.is.interfaces.protocols.IConnInteraction;
import it.unibo.kactor.ActorBasic;
import it.unibo.kactor.ApplMessage;
import it.unibo.kactor.MsgUtil;
import it.unibo.kactor.QakContext;
import it.unibo.kactor.sysUtil;
import it.unibo.supports.FactoryProtocol;

 
@Controller 
public class HumanInterfaceController { 
    private final ResourceLoader resourceLoader;
	private static String RESOURCE_ROOT = "src/main/resources";
	private static String JAVA_ROOT     = "src/main/java";

	private IConnInteraction conn       = null;
	private FactoryProtocol fp			= null;
	private connQakCoap support         = null;
	
	@Value("${human.logo}")
    String appName;
    
    Set<String> robotMoves = new HashSet<String>(); 
    
//    @Autowired
//    Environment environment;

    
    public HumanInterfaceController(ResourceLoader resourceLoader) {
    	this.resourceLoader = resourceLoader;
        robotMoves.addAll( Arrays.asList(new String[] {"w","s","h","r","l","z","x"}) );        	
  	    fp = new FactoryProtocol(null,"TCP","spring"); 	 
//  	    support  = new connQakCoap("localhost","8095","qa0");
//  	    support.createConnection();
    }

  String applicationModelRep="waiting";

  
  
  @GetMapping("/") 		 
  public String entry(Model model) {
      addIpInfoInModel(model);
      return "welcome";
  } 
  
  @GetMapping("/welcome") 
  public String getWelcome(Model model) {	   
	   String filename = "templates/welcome.html";
 	   String outS = readFromFile(RESOURCE_ROOT, filename);
	   model.addAttribute("info", outS);
	   addIpInfoInModel( model );
	   return "welcome";
  }
  @GetMapping("/properties") 
  public String getProperties(Model model) {	   
	   String filename = "application.properties";
 	   String outS = readFromFile(RESOURCE_ROOT, filename);
	   model.addAttribute("info", outS);
	   addIpInfoInModel( model );
	   return "welcome";
  }
  @GetMapping("/humanInterfaceController") 
  public String getHumanInterfaceController(Model model) {	   
	   String filename = "it/unibo/webspring/intro/HumanInterfaceController.java";
 	   String outS = readFromFile(JAVA_ROOT, filename);
	   model.addAttribute("info", outS);
	   addIpInfoInModel( model );
	   return "welcome";
  }
  
  private void addIpInfoInModel(Model model) {
      model.addAttribute("arg", appName);
      model.addAttribute("address", Application.myipAddr);
      model.addAttribute("port", Application.myport);	  
  }

  private String readFromFile( String root, String filename ) {
	  String outS = "";
	  Resource r = resourceLoader.getResource("file:" + root + "/" +  filename);
	   try {
		Scanner myReader = new Scanner(r.getFile());
		while (myReader.hasNextLine()) {
			outS += myReader.nextLine()+"\n";
		}
		myReader.close();
		return outS;
	} catch (FileNotFoundException e) {
		return "FileNotFoundException:" + e.getMessage();
	} catch (IOException e) {
		return "IOException:" + e.getMessage();
	}
  }
  
  @GetMapping( path = "/w") 			//Shortcut for @RequestMapping(method = RequestMethod.GET) 
  public String robotMoveGet(Model model) {
	  System.out.println("GET w");
	  model.addAttribute("robot", "move=w");
      return entry(model);
  } 
  
  //curl --request POST http://localhost:8080/w
   
  @PostMapping( path = "/w") 			//specialized version of @RequestMapping
  //@RequestMapping(value = "/w", method = RequestMethod.POST)
  public String robotCmd(Model model) { 
	  System.out.println("POST w");
	  model.addAttribute("robot", "move=w");
 	  //MsgUtil.INSTANCE.sendMsg("msg1","msg1(1)","qa0", null, null);
	  emit("alarm", "alarm(fire)");
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
		if(support != null) support.forward(m);
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
  /*   
  @GetMapping("/model")
  @ResponseBody
  public String halt(Model model) {
	  model.addAttribute("arg", appName);
      return String.format("HumanInterfaceController text normal state= " + applicationModelRep );      
  }     
	
	
    @RequestMapping(value = "/w", 
    		method = RequestMethod.POST,
    		consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public @ResponseBody String doMove( String move ) {
		applicationModelRep = "w";
        return  "doMove APPLICATION_FORM_URLENCODED_VALUE move="+move+ " Current Robot State:"+applicationModelRep; 
    }    

	@PostMapping( path = "/move" ) 
	public String doMove( 
		@RequestParam(name="move", required=false, defaultValue="h") 
		//binds the value of the query string parameter name into the moveName parameter of the  method
		String moveName, Model viewmodel) {
		if( robotMoves.contains(moveName) ) {
			applicationModelRep = moveName;
			viewmodel.addAttribute("arg", "Current Robot State:"+applicationModelRep);
		}else {
			viewmodel.addAttribute("arg", "Sorry: move unknown - Current Robot State:"+applicationModelRep );
		}		
		return "guiMoves";
	}	
    
	@PostMapping( path = "/movegui" ) 
	public String doHalt( Model viewmodel ) {
 		viewmodel.addAttribute("arg", "Current Robot State:" + applicationModelRep);
		return "guiMoves";		
	}
	

    @ExceptionHandler 
    public ResponseEntity<String> handle(Exception ex) {
    	HttpHeaders responseHeaders = new HttpHeaders();
        return new ResponseEntity<String>(
        		"HumanInterfaceController ERROR " + ex.getMessage(), responseHeaders, HttpStatus.CREATED);
    }
*/
}

