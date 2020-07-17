SET NAMES utf8mb4;

INSERT INTO `PERSON` (`ID`, `USERNAME`, `FIRSTNAME`, `LASTNAME`, `ORGANIZATION`, `INDUSTRY`, `EMAIL`, `PHONENUMBER`, `DESCRIPTION`, `LAST_LOGIN`, `GRACE_PERIOD_START`, `STRIKE_COUNT`, `LAST_STRIKE_TIME`, `LOGGED_IN`) VALUES
(1,	'admin',	'Antonio',	'Natali',	'DISI - University of Bologna',	'University',	'antonio.natali@unibo.it',	'',	'',	'2020-07-17 15:16:12',	NULL,	0,	NULL,	CONV('1', 2, 10) + 0);
