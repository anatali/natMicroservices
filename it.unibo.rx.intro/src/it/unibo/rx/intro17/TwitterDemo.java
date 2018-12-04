package it.unibo.rx.intro17;

 
//import org.apache.log4j.Logger;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;

public class TwitterDemo {
//private Logger log = Logger.getLogger(TwitterDemo.class.getName());
private TwitterStream ts = new TwitterStreamFactory().getInstance();

private void delay( int dt ){
	try {
		Thread.sleep(dt);
//		TimeUnit.SECONDS.sleep(10);
	} catch (InterruptedException e) {
 		e.printStackTrace();
	}
}
	public void doJob() {
 		ts.addListener(new twitter4j.StatusListener(){

			@Override
			public void onException(Exception ex) {
				System.out.println("Error callback: {}" + ex);				
			}

			@Override
			public void onStatus(Status status) {
//				log.info( "Status: {}" + status);		
				System.out.println("Status: {}" + status);	
			}
 			@Override
			public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
			}
			@Override
			public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
			}
			@Override
			public void onScrubGeo(long userId, long upToStatusId) {
			}
			@Override
			public void onStallWarning(StallWarning warning) {
			}
 			
 		});
 		ts.sample();
 		delay(100);
 		ts.shutdown();
	}

	public static void main( String[] args){
		new TwitterDemo().doJob();
	}


}
