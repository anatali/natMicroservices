package it.unibo.rx.intro17;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers; 

public class ObservablesConcurMerge {
	private Observable<Integer> s1 = Observable.create( Utils.source1_5 );
 	
	public void doJob(){
		Utils.log("START");
   		s1 
   			.subscribeOn(Schedulers.computation())
   			.mergeWith(s1
   					.delay(1000, TimeUnit.MILLISECONDS)
   					.map( v -> v + 100)
   					.doOnNext( v -> Utils.log("		after map: " + v ))
   					.observeOn(Schedulers.computation())
   					)
			.subscribe( v -> Utils.log("Merged: " + v ) );
   		Utils.delay(4000);	//to avoid premature system.exit
		Utils.log("END");
 	}
// MAIN	
	public static void main( String[] args){
		new ObservablesConcurMerge().doJob();
	}
}

/*
93	|main	|START
757	|RxComputationThreadPool-1	|Merged: 0
1257	|RxComputationThreadPool-1	|Merged: 1
1758	|RxComputationThreadPool-1	|Merged: 2
1768	|RxComputationThreadPool-2	|		after map: 100
1768	|RxComputationThreadPool-3	|Merged: 100
2258	|RxComputationThreadPool-1	|Merged: 3
2268	|RxComputationThreadPool-2	|		after map: 101
2268	|RxComputationThreadPool-3	|Merged: 101
2759	|RxComputationThreadPool-1	|Merged: 4
2769	|RxComputationThreadPool-2	|		after map: 102
2769	|RxComputationThreadPool-3	|Merged: 102
3269	|RxComputationThreadPool-2	|		after map: 103
3269	|RxComputationThreadPool-3	|Merged: 103
3769	|RxComputationThreadPool-2	|		after map: 104
3769	|RxComputationThreadPool-3	|Merged: 104
6769	|main	|END
*/
