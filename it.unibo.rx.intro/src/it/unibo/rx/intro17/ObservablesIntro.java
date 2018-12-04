package it.unibo.rx.intro17;

import java.util.concurrent.TimeUnit;

import org.reactivestreams.Subscription;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

/*
 * A stream is a sequence of ongoing events ordered in time.
--a---b-c---d---X---|->

a, b, c, d are emitted values
X is an error
| is the 'completed' signal
---> is the timeline
 */

public class ObservablesIntro {
	 
private Observable<String> hello = Observable.just("Hello just Observable");

	
private void log( Object msg ){
	System.out.println(""+Thread.currentThread().getName()+"| "+ msg);
}
private void delay( int dt ){
	try {
		Thread.sleep(dt);
//		TimeUnit.SECONDS.sleep(10);
	} catch (InterruptedException e) {
 		e.printStackTrace();
	}
}
private Observable<Integer> s0 = Observable.create( subscriber -> {
	log("Create Observable<Integer> ");
	subscriber.onNext(1);
	subscriber.onNext(2);
	subscriber.onComplete();
});
private Observable<Integer> s100 = Observable.create( subscriber -> {
	Runnable r =() ->{
		log("Create integers 0-100 ");
		int v = 0; 
		while( /* !subscriber.isCancelled() && */ v <= 10){ //
			subscriber.onNext(v);
			delay(100);
			v = v + 1;
		}
		subscriber.onComplete();
	};
	new Thread(r).start();
});	
	public void doJob(){
		hello.subscribe(System.out::println);	
		s0.subscribe( v -> log("Element fisrt : " + v));
		s0.subscribe( v -> log("Element second: " + v));
//		Subscription sbs1 = s100.subscribe( v -> log("Natural A: " + v) );
//		delay(1);
 		s100.blockingSubscribe();
		s100.subscribe( v -> log("Natural B: " + v) );
	}
	public static void main( String[] args){
		new ObservablesIntro().doJob();
	}
} 
