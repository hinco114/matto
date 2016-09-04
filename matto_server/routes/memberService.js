var express = require('express');
var router = express.Router();

var async = require('async');
var models = require('../models');
var passport = require('passport');

/*
 * router.post('/login',passport.authrenticate('local'), function(req, res) {
 * console.log('login ok'); res.end('login'); })
 */

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




router.get('/login', function(req, res) {
	res.end('login get');
})

router.get('/logout', function(req, res) {
	req.logout();
	res.end('LOGOUT');
})

// 회원 가입 함수
function registMember(req, res) {
	var member = req.body;
	var result = {
		id : member.id,
		status : null,
		reason : null
	};

	models.Member.create(member).then(function() {
		res.status(201);
		result.status = 'S';
		res.json(result);
	}, function(err) {
		res.status(400);
		console.log(err);
		result.status = 'F';
		result.reason = err.errors[0]['message'];
		res.json(result);
	});

}



// 회원 정보 수정
function modifyMemberInfo(req, res) {
	var member = req.body;
	var result = {
		id : member.id,
		status : null,
		reason : null
	};
	// 조건문
	var where = {
		where : {
			id : req.params.id
		}
	};

	console.log(member);

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
			res.status(400);
			result.status = 'F';
			result.reason = err;
		} else {
			res.status(201);
			result.status = 'S';
		}
		res.json(result);
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
	var result = {
		id : req.params.id,
		status : null,
		reason : null
	};

	models.Member.findById(id, {
		attributes : [ 'id', 'gender', 'phoneNum' ]
	}).then(function(member) {
		// 조회 내역이 없으면
		if (member == null) {
			result.status = 'F';
			result.reason = 'no exists member';
		}else{
			result.status = 'S';
		}
		// type이 id가 아니면 mebers객체 추가
		if (type != 'id') {
			result.member = member;
		}
		res.json(result);
	}, function(err) {
		result.status = 'F';
		res.json(result)
	});
}

function getAllMemberInfo(req,res){
	var qOffset = parseInt(req.query.offset);
	var result ={
			status : null,
			reason : null,
			lastOffset : null,
			members : null
	}
	
	models.Member.findAll({attributes : [ 'id', 'gender', 'phoneNum'], offset : qOffset, limit : 20}).then(
			function(ret){
				if(ret.length == 0){
					res.status(400);
					result.status = 'F';
					result.reason = 'not find member';
					
				}else{
					console.log(ret);
					result.status = 'S';
					result.lastOffset = qOffset + ret.length;
					result.members = ret;
				}
				res.json(result);
			}, function(err){
				console.log(err);
				result.status = 'F';
				result.reason = err.message;
				res.json(result);
			});
}





// 회원정보 삭제
function deleteMemberInfo(req,res){
	console.log('DELETE');
	var where = {where : {id : req.params.id}};
	var result = {
			id : req.params.id,
			status : null,
			reason : null
		};	
	models.Member.destroy(where).then(function(ret){
		if(ret == 1){
			result.status = 'S';
		}
		else {
			result.status = 'F';
			result.reason = 'delete fail';
			res.status(400);			
		}
		res.json(result);
	}, function(errs){
		result.status = 'F';
		result.reason = 'delete fail';
		res.json(result);
	});
}




/*
 * router.get('/member/dup/:id',checkDuplicate);
 * router.get('/member/info/:id',showMemberInfo)
 * router.post('/member/signin',registMember);
 * 
 * router.get('/login_verify', isAuthenticated, verifyLogin);
 * 
 * function isAuthenticated(req,res,next) { if(req.isAuthenticated()){ return
 * next(); } res.end('NOT LOGIN'); } function verifyLogin(req,res) {
 * res.end('LOGIN OK'); }
 * 
 * function registMember(req,res){ var inputData = { id : req.body.id, pwd :
 * req.body.pwd, sex : req.body.sex, phoneNum : req.body.phoneNum };
 * Member.create(inputData).then(function(){ res.end(JSON.stringify({status :
 * 'success'})); },function (err) { console.log(err);
 * res.end(JSON.stringify({status : 'fail'})); }); }
 * 
 * function checkDuplicate(req,res) { var id = req.params.id;
 * Member.findById(id).then( function (result){ res.end(JSON.stringify({status :
 * 'success'}));}, function (err) { console.log(err);
 * res.end(JSON.stringify({status : 'fail'}));} ); }
 * 
 * function showMemberInfo(req,res) { var id = req.params.id;
 * Member.findById(id).then( function (result){
 * res.end(JSON.stringify(result));}, function (err) { console.log(err);
 * res.end(JSON.stringify({status : 'fail'}));} ); }
 */
module.exports = router;
