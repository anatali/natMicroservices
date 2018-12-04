package it.unibo.rx.intro17;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers; 

public class ObservablesObserveOn {
	private Observable<Integer> s1 = Observable.create( Utils.source1_5 );
 	
	public void doJob(){
		Utils.log("START");
 		Utils.log(" --------- STEP 1");
  		s1 
   			.subscribeOn(Schedulers.computation())
			.subscribe( v -> Utils.log("Step1: " + v ) );
 		Utils.log(" --------- STEP 2");
 		s1
 			.map( v -> v+100 )
			.doOnNext( v -> Utils.log("Step2 after map: " + v ))
 			.observeOn(Schedulers.computation())
 			.map( v -> v-100 )
  			.subscribe( v -> Utils.log("Step2: " + v ) );
 		Utils.log(" --------- STEP 3");
  		Utils.delay(3000);	//to avoid premature system.exit
		Utils.log("END");
 	}
// MAIN	
	public static void main( String[] args){
		new ObservablesObserveOn().doJob();
	}
}

/*
105	|main	|START
105	|main	| --------- STEP 1
192	|main	| --------- STEP 2
693	|RxComputationThreadPool-1	|Step1: 0
772	|main	|Step2 after map: 100
773	|RxComputationThreadPool-2	|Step2: 0
1193	|RxComputationThreadPool-1	|Step1: 1
1273	|main	|Step2 after map: 101
1273	|RxComputationThreadPool-2	|Step2: 1
1693	|RxComputationThreadPool-1	|Step1: 2
1774	|main	|Step2 after map: 102
1774	|RxComputationThreadPool-2	|Step2: 2
2193	|RxComputationThreadPool-1	|Step1: 3
2274	|main	|Step2 after map: 103
2274	|RxComputationThreadPool-2	|Step2: 3
2693	|RxComputationThreadPool-1	|Step1: 4
2774	|main	|Step2 after map: 104
2774	|RxComputationThreadPool-2	|Step2: 4
2774	|main	| --------- STEP 3
5775	|main	|END
*/
