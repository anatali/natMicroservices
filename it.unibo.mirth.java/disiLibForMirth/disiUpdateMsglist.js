var disiUpdateMsglist = function(){
	var msgList = globalMap.get("L1" );
	if( msgList == null ){
		logger.info( "msgList is empty" );
		msgList = com.mirth.connect.userutil.Lists.list();
		globalMap.put("L1", msgList );
	}
	else {
	 	logger.info( "msgList PRE=\n" + msgList );
	}
	var input = connectorMessage.getRawData();
	logger.info( "disiUpdateMsglist input="+ input );
	 
	var R1  = Packages.it.unibo.mirth.java.MirthJava0.getText(input);   //Java code	  
	msgList = msgList.append( R1 );
	globalMap.put("L1", msgList);
	var l = globalMap.get("L1" );
	logger.info( "msgList POST="+l );
}