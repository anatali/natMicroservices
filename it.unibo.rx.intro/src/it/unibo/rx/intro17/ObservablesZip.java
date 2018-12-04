package it.unibo.rx.intro17;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public class ObservablesZip {
 	private Observable<Integer> temperature = Observable.range(10,5);
 	private Observable<Integer>  wind       = Observable.range(70,5);

 	private Observable<String> s1 = Observable.fromArray("1","2","3" );
 	private Observable<String> s2 = Observable.fromArray("a","b","c" );

	public void doZip(){
		Observable<Integer> naturals      = Observable.create( Utils.allNaturals ); 
		Observable<String> clickSource    = Observable.create( Utils.clicks ); 
		System.out.println("START doZip");
		clickSource
			.subscribeOn (Schedulers.computation() )
			.zipWith( naturals.subscribeOn (Schedulers.computation() ), 
					(v1,v2) -> new Pair<String>(""+v1,""+v2) )
			.subscribe( p -> System.out.println("Pair: "+p) );
		Utils.delay(3500);
		System.out.println("END");		
	}

	public void doZipSynch(){ 
		Utils.log("START doZipSynch");
		temperature
			.zipWith( wind, (v1,v2) -> new Pair<Integer>(v1,v2) )
			.subscribe( p -> System.out.println("Pair: "+p) );
		Utils.log("END doZipSynch");
   	}	

	public void doZipAsynch(){ 
		Utils.log("START doZipAsynch");
		s1
			.zipWith( s2, (v1,v2) -> new Pair<String>(v1,v2) )
			.subscribe( p -> System.out.println("Pair: "+p) );
		Utils.log("doZipAsynch 1");
  		Observable< Pair<String> > s3 = 
				Observable.zip(
						s1.subscribeOn(Schedulers.computation()),
						s2.subscribeOn(Schedulers.computation()),
						(v1,v2) -> new Pair<String>(v1,v2)
				);
		s3.subscribe( (Pair<String> p ) -> System.out.println("Pair with schedule A: "+p)  );
		s3.subscribe( (Pair<String> p ) -> System.out.println("Pair with schedule B: "+p)  );
		//.subscribe( (Pair<String> p ) -> Utils.logObserver("Pair shed: " + p )  );
		Utils.log("END doZipAsynch");
   	}	
	public void doJob(){
		Utils.log("		START doJob");
		doZipSynch();
		doZipAsynch();
		Utils.delay(1000);
		Utils.log("		END doJob");
	}
/*
 * MAIN	
 */
public static void main( String[] args){
	new ObservablesZip().doJob();
}
}
