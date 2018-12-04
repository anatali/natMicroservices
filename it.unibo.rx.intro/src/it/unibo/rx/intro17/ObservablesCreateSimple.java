package it.unibo.rx.intro17;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class ObservablesCreateSimple { 
	private Observable<Integer> s0 = Observable.create( subscriber -> {
		Utils.log("Create Observable<Integer> ");
		subscriber.onNext(1);
		subscriber.onNext(2);
		subscriber.onComplete();
		Utils.log("Observable<Integer> completed");
	});
	private Observable<String> s1 = Observable.create( Utils.sourceABC );
	
	public void doJob(){
		Utils.log("START");
//		s0.subscribe( v -> System.out.println(" 	+++ "+v)  );
//		s0.subscribe( new Observer<Integer>(){
//			@Override
//			public void onSubscribe(Disposable d) { }
//			@Override
//			public void onNext(Integer t) { System.out.println(" 	%%% "+t);}
//			@Override
//			public void onError(Throwable e) { }
//			@Override
//			public void onComplete() { }			
//		} );
		s0.subscribe( v -> Utils.log("Step0: "+v)  );
		Utils.log(" --------- STEP 1");
 		s1.subscribe( Utils.logObserver("Step1: ") );
		Utils.log(" --------- STEP 2");
		s1.subscribe( Utils.logObserver("Step2: " ) );		
  		Utils.log("ENDS");
	}
//MAIN	
	public static void main( String[] args){
		new ObservablesCreateSimple().doJob();
	}
}
/*
14	|main	|START
31	|main	|Create Observable<Integer> 
31	|main	|Step0: 1
31	|main	|Step0: 2
31	|main	|Observable<Integer> completed
31	|main	| --------- STEP 1
37	|main	|Step1: subscribes null
538	|main	|Step1: A
1038	|main	|Step1: B
1538	|main	|Step1: C
2038	|main	|Step1: completed
2038	|main	| --------- STEP 2
2038	|main	|Step2: subscribes null
2538	|main	|Step2: A
3038	|main	|Step2: B
3538	|main	|Step2: C
4039	|main	|Step2: completed
4039	|main	|ENDS
*/
