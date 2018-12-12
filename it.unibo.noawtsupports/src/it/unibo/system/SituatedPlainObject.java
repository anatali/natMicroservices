package it.unibo.system;
import java.util.Observable;
import it.unibo.is.interfaces.IBasicUniboEnv;
import it.unibo.is.interfaces.IObservable;
import it.unibo.is.interfaces.IObserver;
import it.unibo.is.interfaces.IOutputEnvView;
import it.unibo.is.interfaces.IOutputView;
/*
 * 
 */
public class SituatedPlainObject extends Observable implements IObservable {
	protected IBasicUniboEnv env = null;
	protected boolean debug = false;
	protected IOutputEnvView outEnvView = null;
	protected IOutputView outView = null;
	protected String name = "sitPojo";

	// Empty constructor required by GoogleGuice
	public SituatedPlainObject() {
		name = "sitPojo";
	}
	public SituatedPlainObject(String name) {
		this.name = name;
		setDebug();
	}	
	public String getName(){
		return name;
	}
	public SituatedPlainObject(IBasicUniboEnv env) {
		this.env = env;
		if( env != null ) this.outView = env.getOutputView();
		setDebug();
	}
	public SituatedPlainObject(String name, IBasicUniboEnv env) {
		this.name = name;
		this.env = env;
		if( env != null )  this.outView = env.getOutputView();
		setDebug();
	}
	public SituatedPlainObject(IOutputView outView) {
		this();
		this.outView = outView;
		setDebug();
	}
	public SituatedPlainObject(String name, IOutputView outView) {
		this.name = name;
		this.outView = outView;
		setDebug();
	}
	public SituatedPlainObject(String name, IOutputEnvView outEnvView) {
		this.name = name;
		this.outEnvView = outEnvView;
		this.outView = outEnvView;
		this.env     = outEnvView.getEnv();
		setDebug();
	}
	public SituatedPlainObject(IOutputEnvView outEnvView) {
		this.outEnvView = outEnvView;
		this.outView = outEnvView;
		setDebug();
	}
	protected void setDebug(){
		if (System.getProperty("debugOn") != null)
			debug = System.getProperty("debugOn").equals("set");		
	}
	public IBasicUniboEnv getEnv() {
		return env;
	}
	@Override
	public void addObserver(IObserver arg0) {
		super.addObserver(arg0);
	}
    protected void notifyTheObservers(String msg) {
    	this.setChanged();
        notifyObservers(msg);
    }
	protected void println(String msg) {
		if (env != null)
			env.println(msg);
		else if (outView != null)
			outView.addOutput(msg);
		else
			if (outEnvView != null)
				outEnvView.addOutput(msg);
			else System.out.println(msg);
	}
	protected void showMsg(String msg) {
		if (debug)
			println(msg);
	}
}
