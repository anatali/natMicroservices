package it.unibo.supports.tcp;
import java.net.ServerSocket;
import java.net.Socket;
import it.unibo.is.interfaces.IOutputView;
import it.unibo.is.interfaces.protocols.IConnInteraction;
import it.unibo.is.interfaces.protocols.ITcpConnection;
import it.unibo.is.interfaces.protocols.ITcpInteraction;
import it.unibo.platform.tcp.SocketTcpConnSupport;
import it.unibo.platform.tcp.SocketTcpSupport;
import it.unibo.supports.FactoryProtocol;

public class FactoryTcpProtocol extends FactoryProtocol{
private SocketTcpSupport tcpSupport ;
	
	public FactoryTcpProtocol(IOutputView outView, String worker ){
		super(outView, "TCP", worker);
		tcpSupport = new SocketTcpSupport( "TCP", outView );
	 	if( System.getProperty("TcpTrace") != null ) 
			debug = System.getProperty("TcpTrace").equals("set") ;
 	}
	
/**
 * CLIENT SITE
 */
	@Override
	public  IConnInteraction createClientProtocolSupport( String hostName, int portNum ) throws Exception{
//		println("createClientProtocolSupport " + hostName + ":" + portNum);
  		Socket sock = tcpSupport.connectAsClient( hostName,portNum );	//bloccante
		ITcpInteraction connection  = new SocketTcpConnSupport("TcpOut"+worker, sock, env);		  
		//showMsg("createClientProtocolSupport connection " + connection );
		return connection;		
	}
/**
* SERVER SITE
*/
	@Override
	public  IConnInteraction createServerProtocolSupport( int portNum ) throws Exception{
//		println("createServerProtocolSupport " + portNum  );
		ServerSocket serverSocket = tcpSupport.connectAsReceiver( portNum );
 		IConnInteraction conn = acceptAConnection(serverSocket); //blocking
		return conn;  		 
  	}
	public  IConnInteraction createServerProtocolSupportNoMemo( int portNum ) throws Exception{
//		println("createServerProtocolSupportNoMemo " + portNum  );
		ServerSocket serverSocket = tcpSupport.connectAsReceiverNoMemo(portNum);
		//showMsg("createServerProtocolSupport " + serverSocket );
		IConnInteraction conn = acceptAConnection(serverSocket); //blocking
		return conn;  		 
  	}
 	
	protected ITcpInteraction acceptAConnection(ServerSocket serverSocket) throws Exception{
		//showMsg( "waits for a connection " +  tcpSupport);	
		Socket socket 	= tcpSupport.acceptAConnection(serverSocket);
		//showMsg("acceptAConnection connection=" + socket );
		return new SocketTcpConnSupport(worker,socket,env);
	}
	public  ITcpConnection createServerSupport(  ) throws Exception{
 		ITcpConnection supportUdp = new SocketTcpSupport(worker, env ); 
 		return supportUdp;  		 
	} 
	protected void showMsg(String msg){
		System.out.println(msg);
	}
	
}
