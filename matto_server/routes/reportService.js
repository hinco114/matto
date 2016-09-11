var express = require('express');
var router = express.Router();
var async = require('async');
var models = require('../models');
var passport = require('passport');

// 신고 등록
router.post('/reports', registReportInfo);
// 신고 수정
router.put('/reports/:idx', modifyReportInfo);
// 신고 개별조회
router.get('/reports/:idx', getReportInfo);
// 신고 전체조회
router.get('/reports', getAllReportInfo);
// 신고 삭제 
router.delete('/reports/:idx', deleteReportInfo);

// 신고 등록 함수 
function registReportInfo(req, res) {
   var reportInfo = req.body;
   var result = {
		   report_idx : null,
		   status : null,
		   reason : null
   }
   models.Report.create(reportInfo).then(function(ret) {
      console.log(ret);
      result.report_idx = ret.reportIdx;
      result.status = 'S';
      res.json(result);
   }, function(err) {
	   		result.status = 'F';
	   		res.json(result);
	   })
}

// 신고 수정 함수 
function modifyReportInfo(req, res) {
	var reportInfo = req.body;
	var result = {
			report_idx : req.params.idx,
			status : null,
			reason : null
	}
	var where = {
			where : {
				reportIdx : req.params.idx
			}
	};
	models.Report.update(reportInfo, where).then(function(ret) {
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

// 신고 개별조회 함수 
function getReportInfo(req, res) {
	var idx = req.params.idx;
	var result = {
			status : null,
			reason : null,
			report_info : null
	}
	models.Report.findById(idx).then(function(ret) {
		if(ret == null) {
			res.status(400);
			result.status = 'F';
			result.reason = 'not find report';
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
function getAllReportInfo(req, res) {
	// 쿼리스트링 offset
	var qOffset = parseInt(req.query.offset);
	var result = {
			status : null,
			reason : null,
			lastOffset : null,
			reports : null
	}
	models.Report.findAll({offset : qOffset, limit : 20}).then(
			function(ret){
				if(ret.length == 0){
					res.status(400);
					result.status = 'F';
					result.reason = 'not find report';
					
				}else{
					console.log(ret);
					result.status = 'S';
					result.lastOffset = qOffset + ret.length;
					result.reports = ret;
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
function deleteReportInfo(req, res) {
	var idx = req.params.idx;
	var result = {
			status : null,
			reason : null,
			report_idx : null
	}
	models.Report.destroy({where: {reportIdx : idx}}).then(function(ret) {
		if (ret == 1) {
			console.log(ret);
			result.status = 'S';
			result.report_idx = ret;
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