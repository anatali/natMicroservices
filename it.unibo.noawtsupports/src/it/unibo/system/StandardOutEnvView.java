package it.unibo.system;
import it.unibo.is.interfaces.IBasicEnvAwt;
import it.unibo.is.interfaces.IOutputEnvView;

public class StandardOutEnvView implements IOutputEnvView {
protected String curVal = "";

	public void println(String msg) {
		curVal = msg;
		System.out.println(curVal);
	}
	@Override
	public String getCurVal() {
		return curVal;
	}
	@Override
	public void addOutput(String msg) {
		println(msg);
	}
	@Override
	public void setOutput(String msg) {
		println(msg);
	}
	@Override
	public IBasicEnvAwt getEnv() {
 		return null;
	}
}
