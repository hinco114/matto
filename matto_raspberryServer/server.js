var express = require('express');
var app = express();
var SerialPort = require('serialport');
var bodyParser = require('body-parser');
var logger = require('morgan');

app.listen(3000);
app.use(logger('dev'));
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({
	extended : false
}));

app.get('/:cmd', function(req, res) {
	var cmd = '{"cmd" : ' + req.params.cmd + ', "data" : "' + req.query.data + '" }';
	console.log(cmd);
	var serial = new SerialPort('/dev/ttyACM0', {
		baudrate : 9600,
		lock : true,
		parser : SerialPort.parsers.readline('\n')
	}, function(err) {
		if (err) {
			res.end('err');
		} else {
			serial.write(cmd);
			serial.on('data', function(data) {
				console.log('Read Data : ' + data);
				res.end(data);
				serial.close();
			});
			serial.on('error', function(err) {
				console.log('Error: ', err.message);
				res.end(err);
			});
		}

	});

});
