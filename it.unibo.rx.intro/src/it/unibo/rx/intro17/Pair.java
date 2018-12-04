package it.unibo.rx.intro17;

public class Pair<T> { 
	private T v1, v2;
	public Pair(T v1, T v2){
		this.v1 = v1;
		this.v2 = v2;
	}
	public String toString(){
		return "(" + v1+","+ v2 + ")";
	}
}
