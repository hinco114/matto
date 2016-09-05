var express = require('express');
var router = express.Router();

var async = require('async');
var models = require('../models');
var passport = require('passport');

// 화장실 등록
router.post('/toilets', registToilet);
// 화장실정보 수정
router.put('/toilets/:idx', modifytoiletInfo);

// 화장실 등록 함수 
function registToilet(req, res) {
   var toiletInfo = req.body;
   var result = {
      toilet_idx : null,
      status : null,
      reason : null
   }
   console.log(toiletInfo);

   models.Toilet.create(toiletInfo).then(function(ret) {
      //console.log(ret);
      result.toilet_idx = ret.toiletIdx;
      result.status = 'S';
      res.json(result);
   }, function(err) {
      result.status = 'F';
      res.json(result);
   })
};

// 화장실정보 수정 함수 
function modifytoiletInfo(req, res) {
	var toiletInfo = req.body;
	var result = {
			toilet_idx : req.param.idx,
			status : null,
			reason : null
	}
	var where = {
			where : {
				toiletIdx : req.param.idx
			}
	};
	
	models.Toilet.update(toiletInfo, where).then(function(ret) {
		console.log(ret);
		if (ret === 1) {
			result.status = 'S';
		} else {
			res.status(400);
			result.status = 'F';
			result.reason = 'update failed';
		}
		res.json(result);
	});
}

module.exports = router;