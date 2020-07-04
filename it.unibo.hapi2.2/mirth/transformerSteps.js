// revise to version 2.4
msg['MSH']['MSH.12']['MSH.12.1'] = '2.4';

// sender id
msg['MSH']['MSH.3']['MSH.3.1'] = 'MIRTH';

// update datetime
msg['MSH']['MSH.7']['MSH.7.1'] = DateUtil.getCurrentDate('yyyyMMddhhmmss');

// Add two digits of seconds if they aren't there.
if (msg['EVN']['EVN.2']['EVN.2.1'].toString().length <= 12)
{
msg['EVN']['EVN.2']['EVN.2.1'] = PadString('00000000000000', msg['EVN']['EVN.2']['EVN.2.1'].toString(), 0);
}

// Check whether or not gender meets requirements of the destination system. Set to 'O' if not.
switch(msg['PID']['PID.8']['PID.8.1'].toString().toUpperCase())
{
case 'F':
msg['PID']['PID.8']['PID.8.1'] = 'F';
break;
case 'M':
msg['PID']['PID.8']['PID.8.1'] = 'M';
break;
case 'O':
msg['PID']['PID.8']['PID.8.1'] = 'O';
break;
case 'U':
msg['PID']['PID.8']['PID.8.1'] = 'U';
break;
case 'A':
msg['PID']['PID.8']['PID.8.1'] = 'A';
break;
case 'N':
msg['PID']['PID.8']['PID.8.1'] = 'N';
break;

default: msg['PID']['PID.8']['PID.8.1'] = 'O';
}

// Set admit reason to all upper case
msg['PV2']['PV2.3']['PV2.3.2'] = msg['PV2']['PV2.3']['PV2.3.2'].toString().toUpperCase();
//