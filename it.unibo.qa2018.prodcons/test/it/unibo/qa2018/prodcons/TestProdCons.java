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

public class TestProdCons {
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
	
	private void checkSolution(SolveInfo solProd, SolveInfo solCons) {
		if( ! solProd.isSuccess() || ! solCons.isSuccess()  ) {
			System.out.println("test1 NO SOLUTION FOUND");
			fail("NO SOLUTION FOUND");
		}
	}
 	@Test
	public void test(){
		System.out.println("&&&&&&&&&&&&&&& test &&&&&&&&&&&&&&&&&&&&&&&");
		try {
			while( producer == null || consumer == null ) { System.out.print(".");Thread.sleep(400);}
			Thread.sleep(1000); //give time to produce/consume 
			Prolog engineProd = producer.getPrologEngine();  
			Prolog engineCons = consumer.getPrologEngine();
			SolveInfo solProd = engineProd.solve("produced(ITEM).");
			SolveInfo solCons = engineCons.solve("consumed(ITEM).");
			checkSolution(solProd,  solCons);
 			String itemProd   = solProd.getVarValue("ITEM").toString();
			String itemCons   = solCons.getVarValue("ITEM").toString();
			System.out.println("test-1 itemProd="+itemProd + " itemCons="+ itemCons );
			assertTrue( "test", itemProd.equals(itemCons) );
			while( engineProd.hasOpenAlternatives() && engineCons.hasOpenAlternatives() ) {
				solProd    = engineProd.solveNext();
				solCons    = engineCons.solveNext();
				checkSolution(solProd,  solCons);
				itemProd   = solProd.getVarValue("ITEM").toString();
				itemCons   = solCons.getVarValue("ITEM").toString();
				System.out.println("test-2 itemProd="+itemProd + " itemCons="+ itemCons );
				assertTrue( "test", itemProd.equals(itemCons) );
			}
			boolean b1 = engineProd.hasOpenAlternatives();
			boolean b2 = engineProd.hasOpenAlternatives();			 
			if( (b1 && ! b2) || (! b1 && b2 ) ) fail("test: DIFFERENT NUMBER OF ITEMS ");
		} catch (Exception e) {
			System.out.println("test ERROR: "+ e.getMessage() );
 			fail("test ERROR: "+ e.getMessage());
		}
	}


}
