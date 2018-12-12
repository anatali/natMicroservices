package it.unibo.system;

import it.unibo.is.interfaces.IOutputEnvView;
import it.unibo.is.interfaces.IOutputView;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class SituatedSysKb {
	public  static int numberOfCores = Runtime.getRuntime().availableProcessors();
	public static  ScheduledExecutorService  executorOneThread      = Executors.newScheduledThreadPool(1);
	public static  ScheduledExecutorService  executorNumberOfCores  = Executors.newScheduledThreadPool(numberOfCores);
	public static  ScheduledExecutorService  executor4Thread        = Executors.newScheduledThreadPool(4);	
	public static  ScheduledExecutorService  executorManyThread     = Executors.newScheduledThreadPool(20);

	public static final void cleanAll(){
		executorOneThread.shutdownNow();
		executorNumberOfCores.shutdownNow();
		executor4Thread.shutdownNow();		
	}
	public static final void init(){
		executorOneThread      = Executors.newScheduledThreadPool(1);
		executorNumberOfCores  = Executors.newScheduledThreadPool(numberOfCores);
		executor4Thread        = Executors.newScheduledThreadPool(4);	
		executorManyThread     = Executors.newScheduledThreadPool(20);
	}
	
 	public static final String serialPortWindows = "COM49";
	public static final String serialPortLinux = "/dev/ttyUSB0";  
	public static final String serialPortMacOs = "/dev/tty.usbserial-A9007UX1";  
	
	public static final IOutputView standardOutView        = new StandardOutView();
	public static final IOutputEnvView standardOutEnvView  = new StandardOutEnvView();

}
