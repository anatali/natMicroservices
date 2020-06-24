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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

 
@Controller 
public class HumanInterfaceController { 
    private final ResourceLoader resourceLoader;
	private static String RESOURCE_ROOT = "src/main/resources";
	private static String JAVA_ROOT     = "src/main/java";

	@Value("${human.logo}")
    String appName;
    
    Set<String> robotMoves = new HashSet<String>(); 
    
//    @Autowired
//    Environment environment;

    
    public HumanInterfaceController(ResourceLoader resourceLoader) {
    	this.resourceLoader = resourceLoader;
        robotMoves.addAll( Arrays.asList(new String[] {"w","s","h","r","l","z","x"}) );        	
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
  
  @GetMapping( path = "/w") 		 
  public String robotMoveGet(Model model) {
	  System.out.println("GET w");
	  model.addAttribute("robot", "move=w");
      return entry(model);
  } 
  @PostMapping( path = "/w") 		 
  public String robotMove(Model model) {
	  System.out.println("POST w");
	  model.addAttribute("robot", "move=w");
      return entry(model);
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

