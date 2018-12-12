/*
 * The system works correctly if each produced item is consumed in the same order.
 * For each produced item, the producer stores in its actorKb the fact produced( item(X) )
 * For each consumed item, the consumer stores in its actorKb the fact consumed( item(X) )
 */

package it.unibo.qa2018.prodcons;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.Test;
import alice.tuprolog.Prolog;
import alice.tuprolog.SolveInfo;
import it.unibo.consumer.Consumer;
import it.unibo.producer.Producer;
import it.unibo.qactors.QActorUtils;

public class ProdConsTestBad {
private Producer producer;
private Consumer consumer;

	@Before
	public void setUp(){  
		
			System.out.println("================== setUp  ==================");
					try { 
						System.out.println("setUp STARTS producer="+producer   );
						it.unibo.ctxBasicProdCons.MainCtxBasicProdCons.initTheContext();
						Thread.sleep( 1000 );  //give the time to start and execute
						producer = (Producer) QActorUtils.getQActorForTesting("producer");
			 			consumer = (Consumer) QActorUtils.getQActorForTesting("consumer");	
			 			System.out.println("setUp ENDS producer="+producer   );
					} catch (Exception e) {
			 			fail( e.getMessage() ) ;
			 		}	
	}
	
	private boolean checkSolutionAndRemove(SolveInfo solProd, SolveInfo solCons) {
		if(  solProd.isSuccess() ||  solCons.isSuccess()  ) {
			//WARNING: we destroy knowledge
			removeFacts( );
			return true;
		}
 		return false;
	}
	private void removeFacts( ) {
		producer.solveGoal("removeRule( produced(_) )");
		consumer.solveGoal("removeRule( consumed(_) )");		
	}
	@Test
	public void test1(){
		System.out.println("%%%%%%%%%%%%%%%% test1  %%%%%%%%%%%%%%%%%%%%%");		 
		try {
			while( producer == null || consumer == null ) { System.out.print(".");Thread.sleep(400);}
			boolean goon = true;
			do {
				SolveInfo solProd = producer.solveGoal("produced(ITEM)");
				SolveInfo solCons = consumer.solveGoal("consumed(ITEM)");
				goon = checkSolutionAndRemove(solProd,  solCons);
				if( !goon ) break;
  				String itemProd   = solProd.getVarValue("ITEM").toString();
				String itemCons   = solCons.getVarValue("ITEM").toString();
				System.out.println("test1 itemProd="+itemProd + " itemCons="+ itemCons );
				assertTrue( "test1", itemProd.equals(itemCons) );
			}while( goon ) ; 
 		} catch (Exception e) {
			System.out.println("test1 ERROR: "+ e.getMessage() );
 			fail("test1 ERROR: "+ e.getMessage());
		}
	}
	


}
