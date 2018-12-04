package it.unibo.rx.intro17;

import java.util.concurrent.TimeUnit;
import io.reactivex.Observable;
 
public class ObservableInterval {
 	private Observable<Long> source = Observable.interval(500,TimeUnit.MILLISECONDS); 
 	//interval operates by default on the computation Scheduler
 	
	public void doJob(){
		source.subscribe( (Long v) -> Utils.log("observer1: "+v) );
		source.subscribe( (Long v) -> Utils.log("observer2: "+v) );
		
		long startTime = System.currentTimeMillis();	
		source
			.timestamp() 
			.sample(100, TimeUnit.MILLISECONDS)
			.map( ts -> "delta="+ ( ts.time() - startTime ) +  "ms "  )
// 			.map( ts -> ts.getTimestampMillis() - startTime + "ms: " + ts.getValue() )
			.take(5)
			.subscribe( System.out::println );
	 }

public static void main( String[] args){
	new ObservableInterval().doJob();
	Utils.delay(4000);  //avoid immediate termination
	System.out.println("BYE");
}
}
