package it.unibo.platform.mqtt;

import org.eclipse.paho.client.mqttv3.MqttException;

import it.unibo.is.interfaces.protocols.IConnInteraction;

public interface IMqttConnection {
	public IConnInteraction connectAsSender(String  clientid, String brokerAddr, String topic) throws MqttException;
	public IConnInteraction connectAsReceiver(String  clientid, String brokerAddr, String topic) throws MqttException;
}
