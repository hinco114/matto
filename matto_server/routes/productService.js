var express = require('express');
var router = express.Router();

var async = require('async');
var models = require('../models');
var passport = require('passport');

router.post('/products', registProductInfo); // 상품 등록 
router.put('/products/:idx', modifyProductInfo); // 상품 수정 
router.delete('/products/:idx', deleteProductInfo); // 상품 삭제 
router.get('/products/:idx',getProductInfo); // 상품 개별조회 
router.get('/products',getAllProductsInfo); // 상품 전체조회 

function registProductInfo(req, res) {
	var productInfo = req.body;
	var result = {
		product_idx : null,
		status : null,
		reason : null
	}

	models.Product.create(productInfo).then(function(ret) {
		console.log(ret);
		result.product_idx = ret.productIdx;
		result.status = 'S';
		res.json(result);
	}, function(err) {
		result.status = 'F';
		res.json(result);
	})
}

function modifyProductInfo(req, res) {
	var productInfo = req.body;
	var idx = req.params.idx;
	var result = {
		product_idx : idx,
		status : null,
		reason : null
	}
	var where = {
		where : {
			productIdx : idx
		}
	};

	models.Product.update(productInfo, where).then(function(ret) {
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
		res.json(result);
	});

}

function deleteProductInfo(req, res){
	var idx = req.params.idx;
	var result = {
			product_idx : idx,
			status : null,
			reason : null
		};	
	var where = {where : {productIdx : idx}};
	models.Product.destroy(where).then(function(ret){
		if(ret == 1){
			result.status = 'S';
		}else {
			res.status(400);
			result.status = 'F';
			result.reason = 'delete failed';
		}
		res.json(result);
	},function(err){
		console.log(err);
		result.status = 'F';
		result.reason = 'delete failed';
		res.json(result);
	});
	
}

function getProductInfo(req,res){
	var idx = req.params.idx;
	var result = {
			status : null,
			reason : null,
			product_info : null
	}
	models.Product.findById(idx).then(function(ret){
		console.log(ret);
		result.status = 'S';
		result.product_info = ret;
		res.json(result);
	},function(err){
		console.log(err);
		res.status(400);
		result.status = 'F';
		result.reason = err.message;
		res.json(result);
	})
	
}

function getAllProductsInfo(req,res){
	
	//쿼리스트링 offset
	var qOffset = parseInt(req.query.offset);
	
	var result = {
			status : null,
			resaon : null,
			length : null,
			lastOffset : null,
			products : null
	}
	models.Product.findAll({offset:qOffset, limit : 20}).then(function(ret){
		console.log(ret);
		result.status = 'S';
		result.length = ret.length;
		result.lastOffset = qOffset+ret.length;
		result.products = ret;
		res.json(result);		
	},function(err){
		console.log(err);
		result.status = 'F';
		result.reason = err.message;
		res.json(result);
	})
}




module.exports = router;