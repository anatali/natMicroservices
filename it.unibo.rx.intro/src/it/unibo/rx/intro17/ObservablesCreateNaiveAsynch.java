package it.unibo.rx.intro17;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ObservablesCreateNaiveAsynch {
	private Observable<String> s1 = Observable.create( Utils.sourceABC );
	private Observable<String> s2 = Observable.create( Utils.sourceItemAsycnh );
  	
	public void doJob(){
		Utils.log("START");
 		Utils.log(" --------- STEP 1");
  		s1.subscribe( Utils.logObserver("StepA1:") );
		Utils.log(" --------- STEP 2");
 		s2.subscribe( Utils.logObserver("StepA2:") );
 		Utils.log(" --------- STEP 3");
		s2.subscribe( Utils.logObserver("StepA3:") );
   		Utils.log("ENDS");
	}
// MAIN	
	public static void main( String[] args){
		new ObservablesCreateNaiveAsynch().doJob();
	}
}

/*
126	|main	|START
127	|main	| --------- STEP 1
133	|main	|StepA1:subscribes null
635	|main	|StepA1:A
1135	|main	|StepA1:B
1635	|main	|StepA1:C
2135	|main	|StepA1:completed
2135	|main	| --------- STEP 2
2135	|main	|StepA2:subscribes null
2203	|main	| --------- STEP 3
2203	|main	|StepA3:subscribes null
2203	|main	|ENDS
2703	|Thread-0	|StepA2:item1
2703	|Thread-1	|StepA3:item1
3203	|Thread-0	|StepA2:item2
3204	|Thread-1	|StepA3:item2
3703	|Thread-0	|StepA2:item3
3704	|Thread-1	|StepA3:item3
4203	|Thread-0	|StepA2:item4
4204	|Thread-1	|StepA3:item4
4703	|Thread-0	|StepA2:item5
4703	|Thread-0	|StepA2:completed
4704	|Thread-1	|StepA3:item5
4704	|Thread-1	|StepA3:completed
*/
