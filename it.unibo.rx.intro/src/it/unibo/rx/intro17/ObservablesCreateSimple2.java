package it.unibo.rx.intro17;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class ObservablesCreateSimple2 {
	 
private Observable<String> hello = Observable.just("Hello just Observable");

private Observable<Integer> s0 = Observable.create( subscriber -> {
	Utils.log("Create Observable<Integer> ");
	subscriber.onNext(1);
	subscriber.onNext(2);
	subscriber.onComplete();
});

private ObservableOnSubscribe<Integer> source = new ObservableOnSubscribe<Integer>(){
	@Override
	public void subscribe(ObservableEmitter<Integer> e) throws Exception {
		e.onNext(1);
		e.onComplete();
 	}	
};

private ObservableOnSubscribe<Integer> sourceAsynch = new ObservableOnSubscribe<Integer>(){
	@Override
	public void subscribe(ObservableEmitter<Integer> e) throws Exception {
		Runnable r =() ->{
			Utils.log("Create integers 0-100 ");
			int v = 0; 
			while(  ! e.isDisposed() &&  v <= 5){ //
				e.onNext(v);
				Utils.delay(1500);
				v = v + 1;
			}
			e.onComplete();
		};
		new Thread(r).start();
		
 	}	
};

private Consumer<Integer>   obs = new Consumer<Integer>(){
	@Override
	public void accept(Integer t) throws Exception {
 		System.out.println("Consumer accept:"+t);
	}
	
};

private Observable<Integer> s1 = Observable.create( source );
private Observable<Integer> s2 = Observable.create( sourceAsynch );
private Disposable s2ad        = s2.subscribe( v -> Utils.log("s2ad v=" + v));
private Disposable s2d         = s2.subscribe( obs );

private Observable<Integer> s100 = Observable.create( subscriber -> {
	Runnable r =() ->{
		Utils.log("Create integers 0-100 ");
		int v = 0; 
		while( /* !subscriber.isCancelled() && */ v <= 10){ //
			subscriber.onNext(v);
			Utils.delay(100);
			v = v + 1;
		}
		subscriber.onComplete();
	};
	new Thread(r).start();

});	
	public void doJob(){
		hello.subscribe(System.out::println);	
		s1.subscribe( v -> Utils.log("s1 v=" + v));
//		s2.subscribe( v -> Utils.log("s2 v=" + v));
		Utils.delay(1000);
		s2d.dispose();
//		s0.subscribe( v -> Utils.log("Element fisrt : " + v));
//		s0.subscribe( v -> Utils.log("Element second: " + v));
////		Subscription sbs1 = s100.subscribe( v -> log("Natural A: " + v) );
// 		s100.blockingSubscribe();
//		s100.subscribe( v -> Utils.log("Natural B: " + v) );
	}
	
/*
 * MAIN	
 */
public static void main( String[] args){
	new ObservablesCreateSimple2().doJob();
}
}

/*
Scheduler scheduler = Schedulers.from(Executors.newFixedThreadPool(2));
events.flatMap(x ->
     Observable.fromCallable(() -> process(x))
               .subscribeOn(scheduler))
    .subscribe(subscriber); 
*/
