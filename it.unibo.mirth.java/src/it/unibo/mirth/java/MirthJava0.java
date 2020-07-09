package it.unibo.mirth.java;
public class MirthJava0 {
private static int n = 0;

	public void doJob() {
		System.out.println("MirthJava0  doJob");
	}
	public static String getText() {
		return "MirthJava0  hello " + n++;
	}
	public static String getText(String inputData) {
		return "MirthJava0  received: " + inputData;
	}
	public static String getHtmlText() {
		return "<html><p>Hello From MirthJava0:"+ n++ +"</p></html>" ;
	}
	public static String getHtmlText(String inputData) {
		return "<html><p>MirthJava0 Input=</p><p>"+  inputData +"</p></html>" ;
	}
	
//	public static void main(String[] args) {
//		new MirthJava0( ).doJob();
//	}
}
