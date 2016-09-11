var express = require('express');
var router = express.Router();

var async = require('async');
var models = require('../models');
var passport = require('passport');
var auth = require('./auth');


var ResultModel = function(status, reason, data) {
	this.status = status;
	this.reason = reason;
	this.resultData = data;
};

//로그인
router.post('/login', getAccessToken);

router.get('/login_check',auth.isAuthenticated(),function(req,res){
	res.send(req.member);	
});


// 회원가입
router.post('/members', registMember);
// 회원정보 수정 waterfall 적용
router.put('/members/:id', modifyMemberInfo);
// 회원 정보 전체 조회
router.get('/members', getAllMemberInfo);
// 회원 정보 조회
router.get('/members/:id', getMemberInfo);
// 회원 정보 삭제
router.delete('/members/:id', deleteMemberInfo);


//로그인-토큰획득
function getAccessToken(req,res,next){
		// custom callback
		passport.authenticate('local', function(err, member, info){
			var error = err||info
			if(err){ 
				console.log(error);
				return res.status(400).json(new ResultModel('F', error, null));
			}
			if(!member) {
				return res.status(400).json(new ResultModel('F', 'not exists', null)); 
			}
			var token = auth.signToken(member.id);
			res.json(new ResultModel('S', null, {access_token : token}));
			
		})(req,res,next);
}


// 회원 가입 함수
function registMember(req, res) {
	var member = req.body;
	member.salt = models.Member.createSalt();
	member.pwd = models.Member.createHashPwd(member.pwd, member.salt);
	models.Member.create(member).then(function() {
		res.status(200).json(new ResultModel('S', null, null));
	}, function(err) {
		res.status(400).json(new ResultModel('F', err.message, null));
	});

}

// 회원 정보 수정
function modifyMemberInfo(req, res) {
	var member = req.body;
	// 조건문
	var where = {where : {id : req.params.id}};


	// 비동기 waterfall
	async.waterfall([ function(callback) {
		// 파라미터와 body의 id 불일치
		if (member.id != req.params.id) {
			callback('no match id');
		} else {
			callback();
		}
	}, function(callback) {
		// 아이디 존재 유무 확인
		models.Member.findById(member.id).then(function(memInfo) {
			callback(memInfo, callback)
		}, function() {
			callback('not exists member', member);
		});
	}, function(memInfo, callback) {
		// 비밀번호 존재 유무 확인
		if (memInfo.pwd != member.pwd) {
			callback('no match password');
		}
		callback();
	}, function(callback) {
		// 수정 시간 갱신
		member.updatedAt = models.Sequelize.fn('NOW');
		console.log(member);
		models.Member.update(member, where).then(function() {
			callback();
		}, function(err) {
			console.log(err);
			callback(err.errors[0]['message']);
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
			result.data = member;
		}
		res.status(200).json(result);
	}, function(err) {
		res.status(400).json(new ResultModel('F',err.errors[0]['message'], null));
	});
}

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
					res.status(200).json(new ResultModel('S', null, members));
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
			result.status = 'S';
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
