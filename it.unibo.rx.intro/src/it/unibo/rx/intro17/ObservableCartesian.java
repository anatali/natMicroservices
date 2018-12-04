package it.unibo.rx.intro17;
import io.reactivex.Observable;

public class ObservableCartesian {
	 
 	private Observable<Integer> vals  = Observable.range(1,2);
    private Observable<String>  rows  = vals.map( Object::toString );
    private Observable<String>  cols = 
    		vals
	    		.map( v ->  'a' + v - 1 )
	    		.map( a -> (char)a.intValue() )
	    		.map( ch -> Character.toString(ch) );
    
	public void doJob(){
// 		cols.flatMap( f -> rows.map( v -> f + v )).subscribe(Utils.logConsumer("cartesian: "));
   	}
	
/*
 * MAIN	
 */
public static void main( String[] args){
	new ObservableCartesian().doJob();
}
}
