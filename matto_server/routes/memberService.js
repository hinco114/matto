var express = require('express');
var router = express.Router();
var async = require('async');
var models = require('../models');
var passport = require('passport');
var auth = require('./auth');
var logger = require('../utils/logger');

var ResultModel = function(status, reason, data) {
	this.status = status;
	this.reason = reason;
	this.resultData = data;
};

// 로그인
router.post('/login', getAccessToken);

router.get('/login_check',auth.isAuthenticated(),function(req,res){
	res.send(req.user);	
});


// 회원가입
router.post('/members', registMember);
// 회원정보 수정 waterfall 적용
router.put('/members/:id', auth.isAuthenticated(), checkId, modifyMemberInfo);
// 마또관리자 회원정보 수정 
router.put('/members/authentic/:id', auth.isAuthenticated(), checkAuth, modifyMemberInfo_authentic);
// 회원 정보 전체 조회 관리자
router.get('/members', auth.isAuthenticated(), findAllUsers, getAllMemberInfo);
// 내 정보 조회
router.get('/members/me', auth.isAuthenticated(), getMyInfo);
// 회원 정보 조회
router.get('/members/:id', auth.isAuthenticated(), findAllUsers, getMemberInfo);
// 회원 정보 전체 조회 관리자 *
router.get('/members', auth.isAuthenticated(), findUsers, getAllMemberInfo);
// 내 정보 조회 *
router.get('/members/me', auth.isAuthenticated(), getMyInfo);
// 회원 정보 조회 *
router.get('/members/:id', auth.isAuthenticated(), findUsers, getMemberInfo);
// 회원 정보 삭제 
router.delete('/members/:id', deleteMemberInfo);

// 테스트 주석 
// 로그인-토큰획득
function getAccessToken(req,res,next){
		// custom callback
		passport.authenticate('local', function(err, member, info){
			var error = err||info
			if(err){ 
				return res.status(400).json(new ResultModel('F', error, null));
			}
			if(!member) {
				return res.status(400).json(new ResultModel('F', 'not exists', null)); 
			}
			var token = auth.signToken(member);
			res.json(new ResultModel('S', null, {access_token : token}));
			
		})(req,res,next);
}

// matto 관리자 확인
function checkAuth(req, res, next){
	var userAuth = req.user.info.auth;

	if(userAuth != "M"){
		res.status(400).json(new ResultModel('F', '접근권한이 없습니다.', null));
	} else {
		next();
	}
}
// 접근 id 비교 (자신의 아이디만 수정가능)
function checkId(req, res, next){
	var userId = req.user.info.id;
	var paramsId = req.params.id;

	if(userId != paramsId){
		res.status(400).json(new ResultModel('F', 'checkAuth. no Authority', null));
	} else {
		next();
	}
}
// 관리자 확인(개별조회, 전체조회) 
function findAllUsers(req, res, next){
	var userId = req.user.info.id;
	
	if(userId != 'root'){
		res.status(400).json(new ResultModel('F', 'findAllUsers. no Authority', null));
	}
};
// 관리자 확인(개별조회, 전체조회) 
function findUsers(req, res, next){
	var userId = req.user.info.id;
	
	if(userId != 'root'){
		res.status(400).json(new ResultModel('F', 'no Authority', null));
	} else {
		next();
	}
}

// 회원 가입 함수
function registMember(req, res) {
	var member = req.body;
	member.salt = models.Member.createSalt();
	member.pwd = models.Member.createHashPwd(member.pwd, member.salt);
	models.Member.create(member).then(function() {
		res.status(200).json(new ResultModel('S', 'Seccessfully Registered !'));
	}, function(err) {
		res.status(400).json(new ResultModel('F', err.message, null));
	});
}

//회원 정보 수정
function modifyMemberInfo(req, res) {
	var member = req.body;
	// 조건문
	var where = {where : {id : req.params.id}};

	// 비동기 waterfall
	async.waterfall([function(callback) {
		// 아이디 존재 유무 확인
		models.Member.findById(req.params.id).then(function(memInfo) {
			callback(null, memInfo)
		}, function() {
			callback('not exists member');
		});
	}, function(memInfo, callback) {
		// 비밀번호 존재 유무 확인
		console.log(memInfo);
		console.log(member.pwd);
		console.log(memInfo.pwd);
		if (!memInfo.matchPassword(member.pwd)) {
			callback('no match password');
		}else{
			callback(null, memInfo);
		}
	}, function(memInfo, callback) {
		// 수정 시간 갱신
		console.log(memInfo);
		console.log(member.ndPwd);
		member.updatedAt = models.Sequelize.fn('NOW');
		if(member.ndPwd != undefined) memInfo.setNdPwd(member.ndPwd);
		if(member.chgPwd != undefined) memInfo.changePassword(member.chgPwd);
		memInfo.setNewInfo(member);
		console.log(member);
		models.Member.update(memInfo.dataValues, where).then(function() {
			callback();
		}, function(err) {
			console.log("에러 ::::"+err);
			callback(err.message);
		});
	} ], function(err) {
		// 에러시 에러 출력
		if (err != null) {
			res.status(400).json(new ResultModel('F',err ,null));
		} else {
			res.status(201).json(new ResultModel('S', null, null));
		}
	});
}

// 관리자용 회원 정보 수정
function modifyMemberInfo_authentic(req, res) {
	var member = req.body;
	// 조건문
	var where = {where : {id : req.params.id}};


	// 비동기 waterfall
	async.waterfall([function(callback) {
		// 아이디 존재 유무 확인
		models.Member.findById(req.params.id).then(function(memInfo) {
			callback(null, memInfo)
		}, function() {
			callback('not exists member');
		});
	}, function(memInfo, callback) {
		// 수정 시간 갱신
		console.log(memInfo);
		console.log(member.ndPwd);
		member.updatedAt = models.Sequelize.fn('NOW');
		memInfo.setNewInfo(member);
		console.log(member);
		if(member.ndPwd != undefined) memInfo.setNdPwd(member.ndPwd);
		if(member.pwd != undefined) memInfo.changePassword(member.pwd);
		memInfo.setNewInfo(member);
		console.log(member);
		models.Member.update(memInfo.dataValues, where).then(function() {
			callback();
		}, function(err) {
			console.log("에러 ::::"+err);
			callback(err.message);
		});
	} ], function(err) {
		// 에러시 에러 출력
		if (err != null) {
			res.status(400).json(new ResultModel('F',err ,null));
		} else {
			res.status(201).json(new ResultModel('S', null, null));
		}
	});
}

// 내 정보 반환
function getMyInfo(req, res) {
	var userInfo = req.user.info;
	res.status(201).json(new ResultModel('S', userInfo));
	console.log(userInfo);
}


// 회원 정보 반환
function getMemberInfo(req, res) {
	// 쿼리스트링 type
	var type = req.query.type;
	if (typeof (type) != 'string')
		type = '';
	// 동적파라미터 id
	var id = req.params.id;

	models.Member.findById(id, {
		attributes : [ 'id', 'gender', 'phoneNum' ]
	}).then(function(member) {
		var result;
		// 조회 내역이 없으면
		if (member == null) {
			result = new ResultModel('F', 'no exists member', null);
		}else{
			result = new ResultModel('S', null, null);
		}
		// type이 id가 아니면 mebers객체 추가
		if (type != 'id') {
			result.resultData = {member : member};
		}
		res.status(200).json(result);
	}, function(err) {
		res.status(400).json(new ResultModel('F',err.errors[0]['message'], null));
	});
}
// 전체 회원 조회 
function getAllMemberInfo(req,res){
	var qOffset = parseInt(req.query.offset);
	models.Member.findAll({attributes : [ 'id', 'gender', 'phoneNum'], offset : qOffset, limit : 20}).then(
			function(ret){
				if(ret.length == 0){
					res.status(400).json(new ResultModel('F', 'not find member', null));
				}else{
					console.log(ret);
					var members = {
							lastOffest : qOffset + ret.length,
							members : ret
					}
					res.status(200).json(new ResultModel('S', "관리자 인증성공.", members));
				}
			}, function(err){
				console.log(err);
				res.status(400).json(new ResultModel('F', err.message, null));
			});
}

// 회원정보 삭제
function deleteMemberInfo(req,res){
	console.log('DELETE');
	var where = {where : {id : req.params.id}};
	models.Member.destroy(where).then(function(ret){
		if(ret == 1){
			res.status(200).json(new ResultModel('S', null, null));
		}
		else {
			res.status(400).json(new ResultModel('F', 'delete failed', null));
		}
	}, function(err){
		res.status(400).json(new ResultModel('F',err.message,null));
	});
}

module.exports = router;