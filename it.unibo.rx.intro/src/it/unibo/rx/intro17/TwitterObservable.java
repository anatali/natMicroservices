package it.unibo.rx.intro17;

//import org.reactivestreams.Subscription;
import io.reactivex.Observable;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;

public class TwitterObservable {
//private Logger log = Logger.getLogger(TwitterDemo.class.getName());
private TwitterStream ts = new TwitterStreamFactory().getInstance();

private Observable<Status> observe(){
	return Observable.create( subscriber -> {
		TwitterStream ts = new TwitterStreamFactory().getInstance();
 		ts.addListener(new twitter4j.StatusListener(){
			@Override
			public void onException(Exception ex) {
				System.out.println("Error callback: {}" + ex);	
				subscriber.onError(ex);
			}
			@Override
			public void onStatus(Status status) {
//				log.info( "Status: {}" + status);		
				System.out.println("Status: {}" + status);	
				subscriber.onNext(status);
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
// 		subscriber.add( Subscription.create( twitterStream::shutdown ) );
	} );
};


private void delay( int dt ){
	try {
		Thread.sleep(dt);
//		TimeUnit.SECONDS.sleep(10);
	} catch (InterruptedException e) {
 		e.printStackTrace();
	}
}
	public void doJob() {
		observe().subscribe(
			status -> System.out.println("STATUS:" + status),
			ex -> System.out.println("ERROR: " + ex)
		);
	}

	public static void main( String[] args){
		new TwitterObservable().doJob();
	}

}
