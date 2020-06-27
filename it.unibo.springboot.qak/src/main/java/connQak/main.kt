package connQak

import it.unibo.kactor.MsgUtil
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.delay

fun sendUsingCoap(){
	//val conn = connQakCoap("localhost","8095","qa0")
	val conn = connQakCoap(sysConnKb.robothostAddr,sysConnKb.robotport,sysConnKb.robotname,sysConnKb.ctxrobot)
	conn.createConnection()
	val m = MsgUtil.buildDispatch("aliencoap", "cmd", "cmd(l)",sysConnKb.robotname); 
	conn.forward(m)
}

fun sendUsingTcp(){
 	//val conn = connQakTcp("localhost","8095","qa0")
	val conn = connQakTcp(sysConnKb.robothostAddr,sysConnKb.robotport,sysConnKb.robotname )
	conn.createConnection()
	val m = MsgUtil.buildDispatch("alientcp", "cmd", "cmd(r)",sysConnKb.robotname); 
	conn.forward(m)
	 
}

fun main() = runBlocking{
	//val conn = connQakCoap(hostAddr,port,qakdestination)
	sendUsingTcp()
	sendUsingCoap()
	delay(5000)
	//%%% QakContextServer serverctxprj0 | handleConnection: Connection reset
 }