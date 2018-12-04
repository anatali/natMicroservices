package it.unibo.rx.intro17;
import io.reactivex.Observable;

public class ObservablesSimple {
	private String[] a            = new String[]{"1","2","3","4"};
 	private Observable<String> s1 = Observable.fromArray( a );
// 	private Observable<String> s1 = Observable.fromArray("1","2","3","4"); //ok
 	public void doJob(){
 		Utils.log("At start:" + s1 );  
  		s1.subscribe( //lambda
  				(String v)    -> { Utils.log("Items A: " + v); },	
  				(Throwable e) -> { Utils.log("Error A: " + e ); },
  				()            -> { Utils.log("Completed A"  ); } 
  		);
  		s1.subscribe( //lambda without explicit type
  				v -> { Utils.log("Items B: " + v); },	
  				e -> { Utils.log("Error B: " + e ); },
  				() ->{ Utils.log("Completed B"  ); } 
  		);
  		s1.subscribe(	//Java8 method references
  			System.out::println,
  			Throwable::printStackTrace
    	);
		s1
			.skip(2)
			.take(2)
			.subscribe( 
					Utils.logConsumer("Item C: "),
					Utils.logThrowable("Error"),
					Utils.logComplete("Completed C")
					);
		s1
			.takeLast(2)
			.subscribe(  Utils.logObserver("Items D: ")  );
		s1
			.takeLast(2)
			.map( v ->  'A' + v  )
			.subscribe( v -> Utils.log("Last=" + v ) );
 	}	
	/*
	 * MAIN	
	 */
	public static void main( String[] args){
		new ObservablesSimple().doJob();
	}
}
/*
1	|main	|At start:io.reactivex.internal.operators.observable.ObservableFromArray@bebdb06
67	|main	|Items A: 1
67	|main	|Items A: 2
67	|main	|Items A: 3
67	|main	|Items A: 4
67	|main	|Completed A
70	|main	|Items B: 1
70	|main	|Items B: 2
71	|main	|Items B: 3
71	|main	|Items B: 4
71	|main	|Completed B
1
2
3
4
81	|main	|Item C: 3
81	|main	|Item C: 4
81	|main	|Completed C
83	|main	|Items D: subscribes []
83	|main	|Items D: 3
83	|main	|Items D: 4
83	|main	|Items D: completed
88	|main	|Last=A3
88	|main	|Last=A4
*/