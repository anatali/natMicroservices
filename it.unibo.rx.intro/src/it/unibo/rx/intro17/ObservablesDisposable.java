package it.unibo.rx.intro17;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ObservablesDisposable {
	private Observable<String> s1 = Observable.create( Utils.sourceABC );
	private Observable<String> s2 = Observable.create( Utils.sourceItemAsycnh );
	private Disposable s2d        = s2.subscribe( Utils.logConsumer("		%%% disposable: ") );
 	
	public void doJob(){
		Utils.log("START");
 		Utils.log(" --------- STEP 1");
  		s1
			.subscribeOn(Schedulers.computation())		//asynch: BETTER DESIGN
			.subscribe( Utils.logObserver("StepA1:") );
  		s1
			.subscribeOn(Schedulers.computation())		//asynch: BETTER DESIGN
			.subscribe( Utils.logObserver("Step1B:") );
 		Utils.log(" --------- STEP 2");
		s2.subscribe( Utils.logObserver("Step2:") );
		Utils.delay(1200);
 		Utils.log(" --------- STEP 3");
 		//Disposing the subscriber
		s2d.dispose();
		Utils.log("ENDS");
	}
// MAIN	
	public static void main( String[] args){
		new ObservablesDisposable().doJob();
	}
}

/*
235	|main	|START
236	|main	| --------- STEP 1
270	|main	|StepA1:subscribes null
273	|main	|Step1B:subscribes null
274	|main	| --------- STEP 2
274	|main	|Step2:subscribes null
739	|Thread-0	|		%%% disposable: item1
773	|RxComputationThreadPool-1	|StepA1:A
775	|RxComputationThreadPool-2	|Step1B:A
775	|Thread-1	|Step2:item1
1239	|Thread-0	|		%%% disposable: item2
1273	|RxComputationThreadPool-1	|StepA1:B
1276	|RxComputationThreadPool-2	|Step1B:B
1276	|Thread-1	|Step2:item2
1475	|main	| --------- STEP 3
1475	|main	|ENDS
1773	|RxComputationThreadPool-1	|StepA1:C
1776	|RxComputationThreadPool-2	|Step1B:C
1776	|Thread-1	|Step2:item3
2274	|RxComputationThreadPool-1	|StepA1:completed
2277	|Thread-1	|Step2:item4
2277	|RxComputationThreadPool-2	|Step1B:completed
2777	|Thread-1	|Step2:item5
2777	|Thread-1	|Step2:completed
*/
