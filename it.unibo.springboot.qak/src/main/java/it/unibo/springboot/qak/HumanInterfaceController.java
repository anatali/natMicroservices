/*
 * ==========================================================================
 * HumanInterfaceController
 * ==========================================================================
 */

package it.unibo.springboot.qak;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


 
@Controller 
public class HumanInterfaceController  { 
    private final ResourceLoader resourceLoader;
	private static String RESOURCE_ROOT = "src/main/resources";
	private static String JAVA_ROOT     = "src/main/java";
 	
	@Value("${project}")
    String projectName;
    
     
    public HumanInterfaceController(ResourceLoader resourceLoader) {
    	this.resourceLoader = resourceLoader;
    }
  
  @GetMapping("/") 		 
  public String entry(Model model) {
	  model.addAttribute("destname", "qa0");	 //overridden by CommandController after a move
	  model.addAttribute("movename", "r");		 //overridden by CommandController after a move
      addInfoInModel(model);
      return "welcome";
  } 
  @GetMapping("/model") 		 
  public String getModel(Model model) {	  
	  model.addAttribute("info", model.toString());
      return "welcome";
  } 
  
  @GetMapping("/welcome") 
  public String getWelcome(Model model) {	   
	   String filename = "templates/welcome.html";
 	   String outS = readFromFile(RESOURCE_ROOT, filename);
	   model.addAttribute("info", outS);
	   addInfoInModel( model );
	   return "welcome";
  }
  @GetMapping("/properties") 
  public String getProperties(Model model) {	   
	   String filename = "application.properties";
 	   String outS = readFromFile(RESOURCE_ROOT, filename);
	   model.addAttribute("info", outS);
	   addInfoInModel( model );
	   return "welcome";
  }
  @GetMapping("/humanInterfaceController") 
  public String getHumanInterfaceController(Model model) {	   
	   String filename = "it/unibo/springboot/qak/HumanInterfaceController.java";
 	   String outS = readFromFile(JAVA_ROOT, filename);
	   model.addAttribute("info", outS);
	   addInfoInModel( model );
	   return "welcome";
  }
  
/*
 * --------------------------------------------------------
 * UTILITIES  
 * --------------------------------------------------------
 */
  
  private void addInfoInModel(Model model) {
      model.addAttribute("project", projectName);
      model.addAttribute("address", ApplicationWithQak.myipAddr);
      model.addAttribute("port", ApplicationWithQak.myport);	  
  }

  private String readFromFile( String root, String filename ) {
	  String outS = "";
	  Resource r = resourceLoader.getResource("file:" + root + "/" +  filename);
	  System.out.println("resourceLoader="+ resourceLoader + " r="+r.exists());
	  if(!  r.exists()) { return "Sorry, not able to find:" + filename; }
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
  
 
  
}

