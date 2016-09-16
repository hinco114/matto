var winston = require('winston');
winston.transports.DailyRotateFile = require('winston-daily-rotate-file');
require('date-utils');

var logger = new (winston.Logger)({
	transports : [ new (winston.transports.Console)({
		timestamp : function() {
			return new Date().toFormat('YYYY-MM-DD HH24:MI:SS');
		}
	}), new (winston.transports.DailyRotateFile)({
		level : 'debug',
		json : false,
		filename : './logs/logs-',
		datePattern : 'yyyy-MM-dd.log'
	})]
});

module.exports = logger;