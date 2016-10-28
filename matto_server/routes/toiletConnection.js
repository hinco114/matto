var express = require('express');
var request = require('request');
var async = require('async');
var router = express.Router();

var models = require('../models');
var auth = require('./auth');

var ResultModel = function(status, reason, data) {
	this.status = status;
	this.reason = reason;
	this.resultData = data;
};


//요청 객체
var commandRequest = function(info, cmd){
	this.info = info
/*	this.member = null
	this.toilet = null*/
	this.cmd = cmd
	commandRequest.prototype.setMember = function(member){
		this.member = member;
	}
	commandRequest.prototype.setToilet = function(toilet){
		this.toilet = toilet;
	}
	commandRequest.prototype.getMember = function(){
		return this.member;
	}
	commandRequest.prototype.getToilet = function(){
		return this.toilet;
	}
};


router.post('/:cmd/:beaconId',auth.isAuthenticated(), function(req, res) {
	var info = req.body;
	console.log(req.user);
	var member = req.user.info;
	
	//명령어 요청 객체 생성
	var cmdRq = new commandRequest(info, req.params.cmd);
	console.log(cmdRq);
	//cmdReq.setMember(member);
	
	//async.waterfall 2차비밀번호 유효 확인 -> 화장실 정보 획득 -> 접근 권환 확인 -> 화장실에 전송
	async.waterfall([ async.apply(matchPassword,member.id, cmdRq), getToiletInfo, validAccess, requestToilet],
		function(err){
			if(err){
				res.status(400).json(new ResultModel('F', err, null));
			}else{
				res.status(200).json(new ResultModel('S', null, null));
			}});
});


function matchPassword(id, cmdReq, callback){
	models.Member.findById(id).then(function(member){
		var flag = member.matchNdPwd(cmdReq.info.ndPwd);
		if(flag){
			cmdReq.setMember(member);
			callback(null,cmdReq);
		}else {
			callback('not match password');
		}
	}, function(err){
		callback(err.message);
	});
}

//화장실 정보를 얻어 다음 콜백에 넘겨준다.
function getToiletInfo(cmdReq, callback) {
	models.Toilet.findOne({
		where : {
			beaconId : cmdReq.info.beaconId
		}
	}).then(function(ret) {
		if (ret != null) {
			cmdReq.setToilet(ret);
			callback(null, cmdReq);
		}else{
			callback('not find toilet');
		}
	}, function(err) {
		callback(err.message);
	});
}

//접근권한을 얻어 다음 콜백에 넘겨준다.
function validAccess(cmdReq, callback){	
	var gender = cmdReq.getMember().gender;
	var type = cmdReq.getToilet().type;
	if(gender == type)
		callback(null, cmdReq);
	else
		callback('access denied');
}


//화장실에 요청을 보낸다.
function requestToilet(cmdReq, callback){
	var ipAddr = cmdReq.getToilet().ipAddr;
	var url = 'http://'+ipAddr+'/'+cmdReq.cmd;
	request.get(url, function (err,resp,body){
		console.log(body);
		if(err){
			callback(err.message);
		}else if(body.status = 'F'){
			callback(body.reason);
		}else{
			callback();
		}
	})
};

//function saveGenderInfo(cmdReq, callback){
//	var 
//}
function test(){
	request({
		url : 'http://'+toilet.ipAddr+'/'+cmd,
		method : 'GET',
		header : {
			'Content-Type' : 'application/json'
		}
	}, function(err, resp, body) {
		if (err) {
			console.log(err);
			res.end('FAIL');
		} else {
			console.log(res.statusCode, body);
			res.end(body);
			
		}
	});
}


module.exports = router;
