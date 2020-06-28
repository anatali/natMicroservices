package connQak
import org.json.JSONObject
import java.io.File
import java.net.InetAddress

object sysConnKb{
 
//MQTT broker	
//	@JvmStatic var mqtthostAddr    	= "broker.hivemq.com"
	@JvmStatic var mqtthostAddr    	= "localhost"
	@JvmStatic var mqttport    		= "1883"

//Basicrobot  'service'
	@JvmStatic var robotname       = "basicrobot"
	@JvmStatic var ctxrobot        = "ctxbasicrobot"
	@JvmStatic var robothostAddr   = "localhost"
	@JvmStatic var robotport 	   = "8020"

//Internal 'nanoservice'
	@JvmStatic val applhostAddr 	= "localhost"
	@JvmStatic val applport   		= "8080"

	@JvmStatic var hostAddr 		= "localhost"
	@JvmStatic var port     		= "8095"
	@JvmStatic var qakdestination 	= "qa0"
	@JvmStatic var ctxqadest       = "ctxprj0"
	@JvmStatic var stepsize			= "350" 

//Page
	@JvmStatic public var pageTemplate		= "robotGuiSocket"

  	
	@JvmStatic public fun configure(){	//to be used by Java
		try{
			val configfile =   File("pageConfig.json")
			val config     =   configfile.readText()	//charset: Charset = Charsets.UTF_8
			//println( "		--- sysConnKb | config=$config" )
			val jsonObject	=  JSONObject( config )			
			pageTemplate 	=  jsonObject.getString("page") 
			robothostAddr   =  jsonObject.getString("host") 
			robotport    	=  jsonObject.getString("port")
			robotname       =  jsonObject.getString("qakdest")
			ctxrobot		=  jsonObject.getString("ctxqadest")
			stepsize		=  jsonObject.getString("stepsize")
			System.out.println("System IP Address : " + (InetAddress.getLocalHost().getHostAddress()).trim()); 
			System.out.println( "		--- sysConnKb | configfile path=${configfile.getPath()} pageTemplate=$pageTemplate" )
			System.out.println( "		--- sysConnKb | hostAddr=$hostAddr port=$port" )
			System.out.println( "		--- sysConnKb | robothostAddr=$robothostAddr robotport=$robotport robotname=$robotname ctxrobot=$ctxrobot stepsize=$stepsize" )
		}catch(e:Exception){
			System.out.println( " &&& SORRY pageConfig.json NOT FOUND ")
 			System.out.println( "		--- sysConnKb | pageTemplate=$pageTemplate hostAddr=$hostAddr port=$port stepsize=$stepsize" )
		}	
	}
}
 