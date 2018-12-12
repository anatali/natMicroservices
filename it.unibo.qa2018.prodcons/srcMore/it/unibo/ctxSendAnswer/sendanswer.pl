%====================================================================================
% Context ctxSendAnswer  SYSTEM-configuration: file it.unibo.ctxSendAnswer.sendAnswer.pl 
%====================================================================================
pubsubserveraddr("").
pubsubsystopic("unibo/qasys").
%%% -------------------------------------------
context(ctxsendanswer, "localhost",  "TCP", "8028" ).  		 
%%% -------------------------------------------
qactor( caller , ctxsendanswer, "it.unibo.caller.MsgHandle_Caller"   ). %%store msgs 
qactor( caller_ctrl , ctxsendanswer, "it.unibo.caller.Caller"   ). %%control-driven 
qactor( called1 , ctxsendanswer, "it.unibo.called1.MsgHandle_Called1"   ). %%store msgs 
qactor( called1_ctrl , ctxsendanswer, "it.unibo.called1.Called1"   ). %%control-driven 
qactor( called2 , ctxsendanswer, "it.unibo.called2.MsgHandle_Called2"   ). %%store msgs 
qactor( called2_ctrl , ctxsendanswer, "it.unibo.called2.Called2"   ). %%control-driven 
%%% -------------------------------------------
%%% -------------------------------------------

