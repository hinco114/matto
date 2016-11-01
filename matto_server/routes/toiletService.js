var express = require('express');
var router = express.Router();
var async = require('async');
var models = require('../models');
var passport = require('passport');
var auth = require('./auth');


/* 화장실에 대한 권한은 root, 화장실 관리자 */

// 화장실 등록
router.post('/toilets', auth.isAuthenticated(), checkAuth, registToiletInfo);
// 화장실정보 수정
router.put('/toilets/:idx', auth.isAuthenticated(), checkAuth, modifyToiletInfo);
// 화장실정보 개별조회
router.get('/toilets/:idx', auth.isAuthenticated(), getToiletInfo);
// 화장실정보 전체조회
router.get('/toilets', auth.isAuthenticated(), getAllToiletInfo); // .../toilets?offset=0
// 화장실정보 삭제 
router.delete('/toilets/:idx', auth.isAuthenticated(), checkAuth, deleteToiletInfo);
// 화장실 검색
router.get('/toiletFind', auth.isAuthenticated(), findToiletInfo); // .../toiletFind?latitude= &longitude=


var ResultModel = function(status, reason, data) {
	this.status = status;
	this.reason = reason;
	this.resultData = data;
};

//관리자 권한검사 ( 현재는 root 계정만 권한 획득 ) 
function checkAuth(req, res, next){
	var userAuth = req.user.info.auth;

	if(userAuth != "M"){
		res.status(400).json(new ResultModel('F', '접근권한이 없습니다.', null));
	} else {
		next();
	}
}
// 화장실 등록 함수 
function registToiletInfo(req, res) {
   var toiletInfo = req.body;
   var result = {
		   toilet_idx : null,
		   status : null,
		   reason : null
   }
   models.Toilet.create(toiletInfo).then(function(ret) {
      console.log(ret);
      result.toilet_idx = ret.toiletIdx;
      result.status = 'S';
      res.json(result);
   }, function(err) {
	   		result.status = 'F';
	   		res.json(result);
	   })
}

// 화장실정보 수정 함수 
function modifyToiletInfo(req, res) {
	var toiletInfo = req.body;
	var result = {
			toilet_idx : req.params.idx,
			status : null,
			reason : null
	}
	var where = {
			where : {
				toiletIdx : req.params.idx
			}
	};
	models.Toilet.update(toiletInfo, where).then(function(ret) {
		console.log(ret);
		if (ret == 1) {
			result.status = 'S';
		} else {
			res.status(400);
			result.status = 'F';
			result.reason = 'update failed';
		}
		res.json(result);
	}, function(err) {
			console.log(err);
			result.status = 'F';
			result.reason = 'update failed';
		})
}

// 화장실정보 개별조회 함수 
function getToiletInfo(req, res) {
	var idx = req.params.idx;
	var result = {
			status : null,
			reason : null,
			toilet_info : null
	}
	models.Toilet.findById(idx).then(function(ret) {
		if(ret == null) {
			res.status(400);
			result.status = 'F';
			result.reason = 'not find toilet';
			res.json(result);
		} else {
			console.log(ret);
			result.status = 'S';
			result.report_info = ret;
			res.json(result);
		}
	}, function(err) {
			console.log(err);
			res.status(400);
			result.status = 'F';
			result.reason = err.message;
			res.json(result);
		})
}

// 화장실정보 전체조회 함수
function getAllToiletInfo(req, res) {
	// 쿼리스트링 offset
	var qOffset = parseInt(req.query.offset);
	var result = {
			status : null,
			reason : null,
			lastOffset : null,
			toilets : null
	}
	models.Toilet.findAll({offset : qOffset, limit : 20}).then(
			function(ret){
				if(ret.length == 0){
					res.status(400);
					result.status = 'F';
					result.reason = 'not find toilet';
					
				}else{
					console.log(ret);
					result.status = 'S';
					result.lastOffset = qOffset + ret.length;
					result.toilets = ret;
				}
				res.json(result);
			}, function(err){
				console.log(err);
				result.status = 'F';
				result.reason = err.message;
				res.json(result);
			})
}

// 화장실정보 삭제 함수 
function deleteToiletInfo(req, res) {
	var idx = req.params.idx;
	var result = {
			status : null,
			reason : null,
			toilet_idx : null
	}
	models.Toilet.destroy({where: {toiletIdx : idx}}).then(function(ret) {
		if (ret == 1) {
			console.log(ret);
			result.status = 'S';
			result.toilet_idx = ret;
		} else {
			res.status(400);
			result.status = 'F';
			result.reason = 'delete failed';
		}
		res.json(result);
	}, function(err) {
			console.log(err);
			res.status(400);
			result.status = 'F';
			result.reason = err.message;
			res.json(result);
		})
}
// 근처 화장실 검색 함수
function findToiletInfo(req, res){
	var qLatitude = parseFloat(req.query.latitude);
	var qLongitude = parseFloat(req.query.longitude);
	var result = {
			status : null,
			reason : null,
			toilets : null
	}
	models.Toilet.sequelize.query('SELECT toiletIdx, name, ( 6171 * acos( cos( radians(:qLatitude)) * cos( radians( latitude ) ) * cos( radians( longitude ) - radians(:qLongitude) ) + sin( radians(:qLatitude) ) * sin( radians( latitude ) ) ) ) AS distance FROM toilets HAVING distance < 10 ORDER BY distance LIMIT 0, 5', {replacements : { qLatitude : qLatitude, qLongitude : qLongitude}})
	.then(function(ret){
		if(ret == null) {
			res.status(400);
			result.status = 'F';
			result.reason = 'not find toilet';
			res.json(result);
		} else {
			console.log(ret[0]);
			result.status = 'S';
			result.toilets = ret[0];
			res.json(result);
		}
	}, function(err) {
			console.log(err);
			res.status(400);
			result.status = 'F';
			result.reason = err.message;
			res.json(result);
		})
}
module.exports = router;