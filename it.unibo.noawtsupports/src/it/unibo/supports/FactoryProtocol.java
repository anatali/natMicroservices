package it.unibo.supports;
//import gnu.io.SerialPortEventListener;
import jssc.SerialPortEventListener;
import it.unibo.is.interfaces.IObserver;
import it.unibo.is.interfaces.IOutputView;
import it.unibo.is.interfaces.protocols.IConnInteraction;
import it.unibo.supports.serial.FactorySerialProtocol;
import it.unibo.supports.tcp.FactoryTcpProtocol;
import it.unibo.supports.udp.FactoryUdpProtocol;
import it.unibo.system.SituatedPlainObject;
 

public class FactoryProtocol extends SituatedPlainObject{
protected String protocol = null;
protected String worker = null;
public static final String TCP = "TCP";
public static final String UDP = "UDP";
public static final String SERIAL = "SERIAL";
	
	public FactoryProtocol(IOutputView outView, String protocol, String worker) {
		super(outView);
		this.protocol = protocol;
		this.worker   = worker;
// 		println("FactoryProtocol CREATED for " + protocol);
	} 	
	public  IConnInteraction createSerialProtocolSupport(  
			String portName, IObserver observer  ) throws Exception{
// 		println("FactoryProtocol createSerialProtocolSupport for " + portName);
		FactorySerialProtocol factory = new FactorySerialProtocol( outView, worker);
		return factory.createSerialProtocolSupport(  portName, observer );		
	}
	public  IConnInteraction createSerialProtocolSupport(  String portName ) throws Exception{
// 		println("FactoryProtocol createSerialProtocolSupport for " + portName);
		FactorySerialProtocol factory = new FactorySerialProtocol( outView, worker);
		return factory.createSerialProtocolSupport(  portName, null );		
	}
	public  IConnInteraction createClientProtocolSupport(  String hostName, int portNum ) throws Exception{
// 		println("FactoryProtocol createClientProtocolSupport for " + hostName+":"+portNum);
 		if( protocol.equals( "TCP" )){
			FactoryTcpProtocol factory = new FactoryTcpProtocol( outView, worker);
			return factory.createClientProtocolSupport(  hostName, portNum );
		} else if( protocol.equals( "UDP" )){
			FactoryUdpProtocol factory = new FactoryUdpProtocol( outView, worker);
			return factory.createClientProtocolSupport(  hostName, portNum );
		} else throw new Exception("protocol unknown");
	}	
 	public IConnInteraction createServerProtocolSupport( int portNum ) throws Exception{
// 		println("FactoryProtocol createServerProtocolSupport for " + portNum);
 		if( protocol.equals( "TCP" )){
			FactoryTcpProtocol factory = new FactoryTcpProtocol( outView, worker);
			return factory.createServerProtocolSupport(   portNum );
		} else if( protocol.equals( "UDP" )){
			FactoryUdpProtocol factory = new FactoryUdpProtocol( outView, worker);
			return factory.createServerProtocolSupport( portNum );
		} else throw new Exception("protocol unknown");		
	}
	/*
	 * Used in Android applications to allow reconnection (e.g. at resume)
	 * see it.unibo.android.basic
	 */
 	public IConnInteraction createServerProtocolSupportNoMemo( int portNum ) throws Exception{
// 		println("FactoryProtocol createServerProtocolSupportNoMemo for " + portNum);
 		if( protocol.equals( "TCP" )){
			FactoryTcpProtocol factory = new FactoryTcpProtocol( outView, worker);
			return factory.createServerProtocolSupportNoMemo( portNum );
		} else throw new Exception("protocol unknown");		
	}
	
}
