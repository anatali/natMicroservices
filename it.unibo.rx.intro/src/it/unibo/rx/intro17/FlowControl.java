package it.unibo.rx.intro17;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;

public class FlowControl {
	
	public void doJob(){
		long startTime= System.currentTimeMillis();
		Observable<Long> source = Observable.interval(7,TimeUnit.MILLISECONDS);
		source 
			.timestamp()
			.sample(1, TimeUnit.SECONDS)
			.map( ts -> "delta="+ ( ts.time() - startTime ) +  "ms "  )
			.take(5)
			.subscribe( System.out::println );
	}

	/*
	 * MAIN
	 */
	public static void main(String[] args) throws InterruptedException {
		new FlowControl().doJob();
		Utils.delay(6000);  //avoid immediate termination
	}

}
