package it.unibo.system;
import it.unibo.is.interfaces.IOutputView;

public class StandardOutView implements IOutputView {
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
}
