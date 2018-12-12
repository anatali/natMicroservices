package it.unibo.platform.mqtt;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import it.unibo.is.interfaces.protocols.IConnInteraction;

public class MqttSupport {
private static final String fromSender="/sendtorec";
private static final String fromReceiver="/rectosend";

	protected String clientid = null;
 	protected String brokerAddr;
	protected String topic ;
	protected int qos        = 1 ; //qos=0 fire and forget; qos=1 at least once(default);qos=2 exactly once
	protected boolean retain = false;
	protected  MqttClient mqttclientSender   = null;
	protected  MqttClient mqttclientReceiver = null;
  
	public IConnInteraction connectAsSender( String clientid, String brokerAddr, String topic ) throws MqttException{
		mqttclientSender = connect( clientid, brokerAddr, topic+fromSender);
//		subscribe( clientid, brokerAddr, topic+fromReceiver);
		mqttclientSender.subscribe(topic+fromReceiver);
		MqttConnSupport conn = new MqttConnSupport(mqttclientSender, topic+fromSender, topic+fromReceiver);
		mqttclientSender.setCallback(conn);
		return conn;
	}
	public IConnInteraction connectAsReceiver(String clientid, String brokerAddr, String topic) throws MqttException{
		mqttclientReceiver = connect( clientid, brokerAddr, topic+fromReceiver);
		//subscribe( clientid, brokerAddr, topic+fromSender);	
		mqttclientReceiver.subscribe(topic+fromSender);
		MqttConnSupport conn = new MqttConnSupport(mqttclientReceiver, topic+fromReceiver, topic+fromSender);
		mqttclientReceiver.setCallback(conn);
		return conn;		
	}
	
 	public MqttClient connect(String brokerAddr, String topic ) throws MqttException{
// 		println("			%%% MqttSupport connect/2 " );
 		clientid = MqttClient.generateClientId();
 		return connect(clientid,   brokerAddr,   topic, 1, retain);		
	}
	public MqttClient connect(String clientid, String brokerAddr, String topic ) throws MqttException{
// 		println("			%%% MqttSupport connect/3 " );
 		return  connect(clientid,   brokerAddr,   topic, 1, retain );
 	}
	public MqttClient connect(String clientid, String brokerAddr, String topic, int qos, boolean retain ) throws MqttException{
		this.clientid	= clientid;
		this.brokerAddr = brokerAddr;
		this.topic      = topic;
		this.qos		= qos; 
		this.retain     = retain;
		println("			%%% MqttSupport connect "+ clientid + " topic=" + topic + " qos=" + qos +" retain=" + retain);
		MqttClient mqttclient      = new MqttClient(brokerAddr, clientid);
		MqttConnectOptions options = new MqttConnectOptions();
		options.setWill("unibo/clienterrors", "crashed".getBytes(), 2, true);
		mqttclient.connect(options);		
		return mqttclient;
	}
	public void subscribe(MqttClient mqttClient, String topic) throws MqttException {
 		mqttClient.subscribe(topic);  
  	}
	public void disconnect( ) throws MqttException{
		println("			%%% MqttSupport disconnect "+ mqttclientSender );
		if( mqttclientSender != null )   mqttclientSender.disconnect();
		if( mqttclientReceiver != null ) mqttclientReceiver.disconnect();
	}	
	
	public void println(String msg){
			System.out.println(msg);
	}

}
