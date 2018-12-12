package it.unibo.supports.udp;
import java.net.DatagramSocket;
import java.net.InetAddress;
import it.unibo.is.interfaces.IOutputView;
import it.unibo.is.interfaces.protocols.IConnInteraction;
import it.unibo.is.interfaces.protocols.IUdpConnection;
import it.unibo.is.interfaces.protocols.IUdpInteraction;
import it.unibo.platform.udp.SocketUdpConnSupport;
import it.unibo.platform.udp.SocketUdpSupport;
import it.unibo.supports.FactoryProtocol;

public class FactoryUdpProtocol extends FactoryProtocol{
 	private SocketUdpSupport udpSupport ;
 	protected IOutputView outView = null;
 	
	public FactoryUdpProtocol(IOutputView outView, String worker ){
		super(outView, "UDP", worker);  
 		udpSupport = new SocketUdpSupport( "UDP", outView );
	 	if( System.getProperty("UdpTrace") != null ) 
			debug = System.getProperty("UdpTrace").equals("set") ;
	}
	
/**
 * CLIENT SITE
 */
	@Override
	public  IConnInteraction createClientProtocolSupport( String hostName, int portNum ) throws Exception{
		IUdpInteraction connection ;
//		showMsg("createClientProtocolSupport " + hostName + ":" + portNum);
		DatagramSocket sock = udpSupport.connectAsClient( hostName,portNum );	//bloccante
		InetAddress ia = InetAddress.getByName(hostName);
		if( env != null )
  			connection  = new SocketUdpConnSupport(  sock, portNum, ia, env.getOutputView());
		else 
			connection  = new SocketUdpConnSupport(  sock, portNum, ia, null);
		showMsg("createClientProtocolSupport connection " + connection );
		return connection;		
	}
 
	
/**
* SERVER SITE
*/
	@Override
	public  IConnInteraction createServerProtocolSupport( int portNum ) throws Exception{
		IUdpConnection supportUdp;
		IUdpInteraction kernelUdp;
		if( env != null )
			supportUdp =  new SocketUdpSupport(worker, env.getOutputView());
		else 
			supportUdp =  new SocketUdpSupport(worker, null);
		DatagramSocket sock = supportUdp.connectAsReceiver( portNum);
		if( env != null )
 			kernelUdp = new SocketUdpConnSupport(sock,  portNum, null, env.getOutputView() );
		else 
			kernelUdp = new SocketUdpConnSupport(sock,  portNum, null, null );
		return kernelUdp;  		 
  	}
	public  IUdpConnection createServerSupport(  ) throws Exception{
 		IUdpConnection supportUdp = new SocketUdpSupport(worker, env.getOutputView()); 
 		return supportUdp;  		 
	} 


	protected void showMsg(String msg){
		System.out.println(msg);
	}
	
}
