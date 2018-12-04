package it.unibo.rx.intro17;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public class ObservablesSimpleMerge {
	 
 	private Observable<String> s1 = Observable.fromArray("1","2","3" );
 	private Observable<String> s2 = Observable.fromArray("a","b" );
 
 	
	public void doJob(){
		Utils.log("Starts");
 		s2.mergeWith(s1) 
			.subscribe( Utils.logObserver("Merged A: "));
 		
 		s2
 			.subscribeOn(Schedulers.computation())
 			.mergeWith(s1.subscribeOn(Schedulers.computation())) 
 			.subscribe( Utils.logObserver("Merged B: "));
		Utils.log("Ends");
	}
	
/*
 * MAIN	
 */
public static void main( String[] args){
	new ObservablesSimpleMerge().doJob();
}
}
