package it.unibo.webspring.intro;

import java.net.InetAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.Environment;
import it.unibo.ctxprj0.MainCtxprj0Kt;

@SpringBootApplication
public class Application implements ApplicationListener<ApplicationReadyEvent>{
	
public static String myipAddr = "";
public static String myport   = "0";

//private static boolean activated = false;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
	
    @Autowired
    private ApplicationContext applicationContext;
	
	 @Override
	  public void onApplicationEvent(ApplicationReadyEvent event) {
	      try {
	          String ip = InetAddress.getLocalHost().getHostAddress();
	          int port = applicationContext.getBean(Environment.class).getProperty("server.port", Integer.class, 8080);
	          System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%" );
	          System.out.printf("IP=%s:PORT=%d", ip,port );
	          myipAddr = ip;
	          myport   = "8080"; 
	          System.out.println("\n%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%" );
//	          if( ! activated ) {
//	        	  MainCtxprj0Kt.main();
//	        	  activated = true;
//	          }
	      } catch ( Exception e) {
	          e.printStackTrace();
	      }
	  }
	
}
