var express = require('express');
var router = express.Router();
var async = require('async');
var models = require('../models');
var passport = require('passport');

// 화장실 등록
router.post('/toilets', registToiletInfo);
// 화장실정보 수정
router.put('/toilets/:idx', modifyToiletInfo);
// 화장실정보 개별조회
router.get('/toilets/:idx', getToiletInfo);
// 화장실정보 전체조회
router.get('/toilets', getAllToiletInfo);
// 화장실정보 삭제 
router.delete('/toilets/:idx', deleteToiletInfo);

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
		console.log(ret);
		result.status = 'S';
		result.toilet_info = ret;
		res.json(result);
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
	var result = {
			status : null,
			reason : null,
			toilet_info : null
	}
	models.Toilet.findAll().then(function(toilets) {
		result.status = 'S';
		result.toilet_info = toilets;
		res.json(result);
	}, function(err) {
			console.log(err);
			res.status(400);
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

module.exports = router;