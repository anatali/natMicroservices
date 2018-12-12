package it.unibo.platform.mqtt;

import org.eclipse.paho.client.mqttv3.MqttCallback;
import java.util.concurrent.LinkedBlockingQueue;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import it.unibo.is.interfaces.protocols.IConnInteraction;

public class MqttConnSupport implements MqttCallback, IConnInteraction{
private static final int queuecapacity=100;

	protected String clientid = null;
 	protected String brokerAddr;
	protected String topicForsend;
	protected String topicForReceive ;
	protected int qos = 1 ;
	protected boolean retain = true;
	protected  MqttClient mqttclient = null;
	
	protected LinkedBlockingQueue<String> msgqueue = new LinkedBlockingQueue<String>(queuecapacity);
		
	public MqttConnSupport(MqttClient mqttclient,  String topicForsend, String topicForReceive){
		this.topicForsend    = topicForsend;
		this.topicForReceive = topicForReceive;
		this.mqttclient      = mqttclient;
	}


	public void publish( String msg ) throws MqttException{
		println("			%%% MqttSupport publish msg length="+ msg.length() + " qos=" + qos + " retain=" + retain );
		MqttMessage message = new MqttMessage();
 		message.setRetained(retain);
		if( qos == 0 || qos == 1 || qos == 2){//qos=0 fire and forget; qos=1 at least once(default);qos=2 exactly once
			message.setQos(0);
		}
		if( msg.length()>=0 )  message.setPayload(msg.getBytes());
		println("			%%% MqttSupport publish content length="+ msg.getBytes().length );
		mqttclient.publish(topicForsend, message);
	}	
	public void publish( String topic, String msg, int qos, boolean retain) throws MqttException{
 		MqttMessage message = new MqttMessage();
		message.setRetained(retain);
		if( qos == 0 || qos == 1 || qos == 2){//qos=0 fire and forget; qos=1 at least once(default);qos=2 exactly once
			message.setQos(0);
		}
		if( msg.length()>=0 ) message.setPayload(msg.getBytes());
		println("			%%% MqttSupport publish long: "+ message );
		mqttclient.publish(topic, message);
	}	
    

	@Override
	public   void connectionLost(Throwable cause) {
		println("			%%% MqttSupport connectionLost  = "+ cause.getMessage() );
	}
	@Override
	public   void deliveryComplete(IMqttDeliveryToken token) {
//		println("			%%% MqttSupport deliveryComplete token= "+ token );
	}
	@Override
	public void messageArrived(String topic, MqttMessage msg) throws Exception {
		String content  = "";
//		println("messageArrived on "+ topic + "="+msg.toString() );
//		String mqttmsg = "mqttmsg(" + topic +"," + msg.toString() +")";
//		println("			%%% MqttSupport messageArrived mqttmsg "+ mqttmsg);
		if(msg.getPayload().length>0){
			content = new String(msg.getPayload());
			msgqueue.put(content); //exclude clean messages
		}
		println("			%%% MqttSupport " + this + " messageArrived content length="+ content.length() + " queed=" + msgqueue.size());
// 		if( actor != null ) actor.sendMsg("mqttmsg", actor.getName().replaceAll("_ctrl", ""), "dispatch", mqttmsg);
	}	
	
	public void println(String msg){
		System.out.println(msg);
	}
	
/*
 * IConnInteraction
 */
		@Override
		public void sendALine(String msg) throws Exception {
			publish( msg ) ;			
		}
		@Override
		public void sendALine(String msg, boolean isAnswer) throws Exception {
			publish(   msg ) ;
		}
		@Override
		public String receiveALine() throws Exception {
			println("			%%% MqttSupport " + this + " receiveALine: queed=" + msgqueue.size());
			return msgqueue.take();
 		}
		@Override
		public void closeConnection() throws Exception {
			mqttclient.disconnect();
		}	
		
/*
 * Main for testing
 */
		public static void main(String[] args) throws Exception{
			System.out.println("APPL STARTS " );
			final int n = 2;
			MqttSupport mqttsupport = new MqttSupport();
			/*
			 * SENDER
			 */
			new Thread(){
				public void run(){
					try{
						IConnInteraction connSend = 
								mqttsupport.connectAsSender("unibopublisher", "tcp://m2m.eclipse.org:1883", "unibo/mqtt");
						for( int i=1; i<=n; i++ ){
							System.out.println("SENDER send sensordata " + i );
							connSend.sendALine( "sensordata(d"+i+")" );
							Thread.sleep(1000);
							
						}
						for( int i=1; i<=n; i++ ){
							String mr = connSend.receiveALine();
							System.out.println("SENDER receives reply: " + mr);							
						}
						
						Thread.sleep(2000);
						System.out.println("SENDER cleans its topic "   );
			 			connSend.sendALine( "" );	//removes the topic from storage on the broker 
						connSend.closeConnection();
					}catch(Exception e){
						System.out.println("SENDER ERROR: " + e.getMessage());
					}
					
				}
			}.start();
			

			/*
			 * RECEIVER
			 */
			new Thread(){
				public void run(){
					try{
						IConnInteraction connRec = 
								mqttsupport.connectAsReceiver("uniboobserver", "tcp://m2m.eclipse.org:1883", "unibo/mqtt");
						for( int i=1; i<=n; i++ ){
							String mr = connRec.receiveALine();
							System.out.println("RECEIVER receives: " + mr);	
							connRec.sendALine("answer_" + i);
 						}
						Thread.sleep(2000);
						System.out.println("RECEIVER cleans its topic "   );
						connRec.sendALine( "" );	//removes the topic from storage on the broker 
						connRec.closeConnection();
					}catch(Exception e){
						System.out.println("APPL ERROR: " + e.getMessage());
					}
					
				}
			}.start();
			
		
			
//			String msg = msgqueue.poll();
//			mqttsupport.disconnect();

			
			System.in.read();		//avoid termination
			System.out.println("APPL ENDS " );
		}


}
