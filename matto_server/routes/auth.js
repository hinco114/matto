'use strict'

var jwt = require('jsonwebtoken');
var compose = require('composable-middleware');
var SECRET_KEY = 'matto_access_token';// 임시
var EXPIRES = "1h";


//express-jwt를 이용해 토큰을 분석한다.
var validateJwt = require('express-jwt')({
	secret : SECRET_KEY
});

// 아이디 기반으로 토큰 생성
function signToken(member) {
	
	var token = {};
	for(var key in member.dataValues){
		if(key !='pwd' && key!='ndPwd' && key!='salt'&&typeof(member[key])==='string'){
			token[key] = member[key];
			console.log(key+":::::::::::::::::::::"+token[key]);
		}
		
	}
	return jwt.sign({
		info : token
	}, SECRET_KEY, {
		expiresIn : EXPIRES
	});
}

// 토큰 해석
/*function isAuthenticated() {
	return compose().use(function(req, res, next) {
		var decodes = jwt.verify(req.headers.authoriztion, SECRET_KEY);
		console.log(decodes);
		req.user = decodes;
	}).use(function(req, res, next) {
		req.uesr = {
			id : req.member.id,
			name : req.member.name
		};
		next();
	});
}*/


function isAuthenticated() {
	return compose().use(function(req, res, next) {
		//쿼리스트링으로 엑세스 토큰을 넣었으면 헤더에 토큰 삽입.
		//express-jwt는 header.authorization에 있는 토큰을 기반으로 validate를 함.
		if(req.query && req.query.hasOwnProperty('access_token')){
			req.headers.authorization = 'Bearer '+req.query.access_token;			
		}
		validateJwt(req,res,next);
	}).use(function(req,res,next){
		next();
	});
}

exports.signToken = signToken;
exports.isAuthenticated = isAuthenticated;