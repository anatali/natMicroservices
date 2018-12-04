package it.unibo.rx.intro17;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public class ObservablesSubscribeOn {
	private Observable<String> s1 = Observable.create( Utils.sourceABC );
 	
	public void doJob(){
		Utils.log("START");
 		Utils.log(" --------- STEP 1");
  		s1
			.subscribeOn(Schedulers.computation())		//asynch: BETTER DESIGN
			.subscribe( Utils.logObserver("Step1:") );
 		Utils.log(" --------- STEP 2");
 		s1
			.subscribeOn(Schedulers.computation())		//asynch: BETTER DESIGN
			.subscribe( Utils.logObserver("Step2:") );
 		Utils.log(" --------- STEP 3");
 		Utils.delay(3000);	//to avoid premature system.exit
		Utils.log("END");
 	}
// MAIN	
	public static void main( String[] args){
		new ObservablesSubscribeOn().doJob();
	}
}

/*
91	|main	|START
91	|main	| --------- STEP 1
113	|main	|Step1:subscribes null
119	|main	| --------- STEP 2
119	|main	|Step2:subscribes null
120	|main	| --------- STEP 3
620	|RxComputationThreadPool-1	|Step1:A
620	|RxComputationThreadPool-2	|Step2:A
1120	|RxComputationThreadPool-1	|Step1:B
1121	|RxComputationThreadPool-2	|Step2:B
1620	|RxComputationThreadPool-1	|Step1:C
1621	|RxComputationThreadPool-2	|Step2:C
2121	|RxComputationThreadPool-1	|Step1:completed
2122	|RxComputationThreadPool-2	|Step2:completed
3120	|main	|END
*/
