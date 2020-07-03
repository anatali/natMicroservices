package connQak

import it.unibo.kactor.MsgUtil
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.delay

fun sendUsingCoap(){
	val conn = connQakCoap("localhost","8091","qa0")
	conn.createConnection()
	val m = MsgUtil.buildDispatch("aliencoap", "msg1", "msg1(x)","qa0"); 
	conn.forward(m)
}

fun sendUsingTcp(){
	//val conn = connQakTcp(hostAddr,port,qakdestination)
	//val conn = connQakTcp("localhost","8095","qa0")
	val conn = connQakTcp("192.168.1.22","7002","basicrobot")
	conn.createConnection()
	val m = MsgUtil.buildDispatch("alientcp", "cmd", "cmd(r)","basicrobot");
	println("sending: $m")
	conn.forward(m)
 
}

fun main() = runBlocking{
	//val conn = connQakCoap(hostAddr,port,qakdestination)
	sendUsingTcp()
//	sendUsingCoap()
	delay(2000)
	println("BYE:  ")
	//%%% QakContextServer serverctxprj0 | handleConnection: Connection reset
 }