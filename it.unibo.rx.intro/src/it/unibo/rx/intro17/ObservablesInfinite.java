package it.unibo.rx.intro17;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ObservablesInfinite {
	private Observable<Integer> naturals      = Observable.create( Utils.allNaturals ); 
	private Observable<String> clickSource    = Observable.create( Utils.clicks ); 
//	private	Disposable disposableNats      =  naturals
//			.subscribeOn(Schedulers.computation())	
//			.subscribe( 
//					v -> Utils.log("Value: " + v) ,
//					e -> { Utils.log("Error: " + e ); },
//					() -> Utils.log("Completed: "  )
//				);
//	
//	private	Disposable disposableClicks      =  clickSource
//			.subscribeOn (Schedulers.computation() )	
//			.subscribe( v -> Utils.log("ValueClick: " + v) ); // Utils.logObserver("a")

	public void doZip(){
		System.out.println("START");
		clickSource
			.subscribeOn (Schedulers.computation() )
			.zipWith( naturals.subscribeOn (Schedulers.computation() ), 
					(v1,v2) -> new Pair<String>(""+v1,""+v2) )
			.subscribe( p -> System.out.println("Pair: "+p) );
		Utils.delay(3500);
		System.out.println("END");		
	}
	
	public void doJob(){
		System.out.println("START");
// 		Utils.delay(2500);
//		System.out.println("DISPOSE");
//		disposableNats.dispose();
//		disposableClicks.dispose();
// 		Utils.delay(3500);
		System.out.println("BYE");
	}
//MAIN	
	public static void main( String[] args){
		new ObservablesInfinite().doZip(); //.doJob();
	}
}
/*
START
683	|RxComputationThreadPool-1	|Value: 0
1183	|RxComputationThreadPool-1	|Value: 1
1683	|RxComputationThreadPool-1	|Value: 2
2183	|RxComputationThreadPool-1	|Value: 3
DISPOSE
BYE
*/