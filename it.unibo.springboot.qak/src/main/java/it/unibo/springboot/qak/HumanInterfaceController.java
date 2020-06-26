package it.unibo.springboot.qak;

import java.io.FileNotFoundException;
import java.io.IOException;
//import java.net.InetAddress;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.context.event.ApplicationReadyEvent;
//import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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
public class HumanInterfaceController { 
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
    
    public HumanInterfaceController(ResourceLoader resourceLoader) {
    	this.resourceLoader = resourceLoader;
        robotMoves.addAll( Arrays.asList(new String[] {"w","s","h","r","l","z","x"}) );        	
        fp = new FactoryProtocol(null,"TCP","spring"); 	 
        coapSupport  = new connQakCoap(
        		sysConnKb.INSTANCE.getHostAddr(),sysConnKb.INSTANCE.getPort(),sysConnKb.INSTANCE.getQakdestination());
        coapSupport.createConnection();
    }
  
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
  }//readFromFile
  
//  @GetMapping( path = "/w") 			//Shortcut for @RequestMapping(method = RequestMethod.GET) 
//  public String robotMoveGet(Model model) {
//	  System.out.println("GET w");
//	  model.addAttribute("robot", "move=w");
//      return entry(model);
//  } 
  
}

