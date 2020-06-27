package connQak

import it.unibo.kactor.*
import alice.tuprolog.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.eclipse.californium.core.CoapHandler
import org.eclipse.californium.core.CoapResponse
import org.eclipse.californium.core.CoapClient
	
object coapObserver {

    private val client = CoapClient()
	
	private val ipaddr      = "${sysConnKb.hostAddr}:${sysConnKb.port}"		//5683 default
	private val context     = sysConnKb.ctxqadest			 
 	private val destactor   = sysConnKb.qakdestination 		 
	private val msgId       = "cmd"
	private val tt          = "               coapObserver | "
	fun init(){
       val uriStr = "coap://$ipaddr/$context/$destactor"
	  println("$tt START uriStr: $uriStr")
       client.uri = uriStr
       client.observe(object : CoapHandler {
            override fun onLoad(response: CoapResponse) {
                //println("$tt GET RESP-CODE= " + response.code + " content:" + response.responseText)
				println("$tt  ${response.responseText}" )
            }
            override fun onError() {
                println("$tt  FAILED")
            }
        })		
	}

 }

 
 fun main( ) {
		coapObserver.init()
		System.`in`.read()   //to avoid exit
 }