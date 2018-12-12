package it.unibo.system;
import java.util.concurrent.ScheduledExecutorService;
import it.unibo.iot.interfaces.ISituatedActiveObject;
import it.unibo.is.interfaces.IBasicUniboEnv;
import it.unibo.is.interfaces.IOutputEnvView;
import it.unibo.is.interfaces.IOutputView;

public abstract class SituatedActiveObject extends Thread implements ISituatedActiveObject{
	protected IBasicUniboEnv env = null;
 	protected IOutputEnvView outEnvView = null;
	protected IOutputView outView = null;
	protected boolean debug = false;
	//Empty constructor (required by GoogleGuice)
	public SituatedActiveObject(){		
	}
	public SituatedActiveObject(IBasicUniboEnv env, String name) {
		this.env = env;
		setName(name);
	}
	public SituatedActiveObject(IOutputView outView, String name) {
		this.outView = outView;
		setName(name);
	}
	public SituatedActiveObject(IOutputEnvView outEnvView, String name) {
		this.env        = outEnvView.getEnv();
		this.outEnvView = outEnvView;
		if( env != null ) this.outView    = env.getOutputView();
		else outView = outEnvView;
		setName(name);
	}
	public IBasicUniboEnv getEnv() {
		return env;
	}
	public void activate(ScheduledExecutorService sched){
//		sched.execute(this);
		sched.submit(this);
	}
	@Override
	public void run() {
		try {
 			startWork();
			doJob();
			endWork();
 		} catch (Exception e) {
 			//System.out.println("SituatedActiveObject ERROR " + e.getMessage());
 		}
	}
	protected void println(String msg) {
		if (env != null)
			env.println(msg);
		else if (outView != null)
			outView.addOutput(msg);
		else
			System.out.println(msg);
	}
	protected void showMsg(String msg) {
		if (debug)
			println(msg);
	}
	protected abstract void doJob() throws Exception;
	protected abstract void startWork() throws Exception;
	protected abstract void endWork() throws Exception;
}
