var express = require('express');
var router = express.Router();

var async = require('async');
var models = require('../models');


// 해당 화장실 전체 상품 조회
router.get("/toilets/:toiletIdx/product", getAllToiletProduct);
// 화장실에 상품 등록
router.post("/toilets/:toiletIdx/product", registToiletProduct);

// 화장실에서 상품 삭제
router.delete("/toilets/:toiletIdx/product/:productIdx",deleteToiletProduct);

// 화장실 재고 수량 수정
router.put("/toilets/:toiletIdx/product/:productIdx",modifyStock);



function registToiletProduct(req,res){
	var toiletProductInfo = {
		toiletIdx : parseInt(req.params.toiletIdx),
		productIdx : req.body.productIdx,
		stock : req.body.stock
	};
	
	var result = {
			status : null,
			reason : null
	}
	console.log("tp");
	models.ToiletProduct.create(toiletProductInfo).then(function(ret){
		console.log(ret);
		result.status = 'S';
				
		res.json(result);
	}, function(err){
		res.status(400);
		result.status = 'F';
		result.reason = err.message;
		res.json(result);
	})
}

function deleteToiletProduct(req,res){
	var where = { where : {
		toiletIdx : req.params.toiletIdx,
		productIdx : req.params.productIdx		
	}};
	
	var result = {
			status : null,
			reason : null
	};
	
	models.ToiletProduct.destroy(where).then(function(ret){
		if(ret==1){
			result.status = 'S';
		}else{
			res.status(400);
			result.status = 'F';
			result.reason = 'delete failed';
		}
		res.json(result);
	}, function(err){
		res.status(400);
		result.status = 'F';
		result.reason = err.message;
		res.json(result);
	})
	
	
}

function modifyStock(req,res){
	var where = { where : {
		toiletIdx : req.params.toiletIdx,
		productIdx : req.params.productIdx		
	}};
	
	var modifyInfo = {
			stock :req.body.stock
	};
	
	var result = {
			status : null,
			reason : null
	};
	
	models.ToiletProduct.update(modifyInfo,where).then(function(ret){
		if(ret==1){
			result.status = 'S';
		}else{
			res.status(400);
			result.status = 'F';
			result.reason = 'update failed';
		}
		res.json(result);
	}, function(err){
		res.status(400);
		result.status = 'F';
		result.reason = err.message;
		res.json(result);
	});
	
	
}

function getAllToiletProduct(req,res){
	var where =  {
		toiletIdx : req.params.toiletIdx
	}
	var qOffset = parseInt(req.query.offset);
	var result ={
		status : null,
		reason : null,
		lastOffset : null,
		products : null
	}
	
	models.ToiletProduct.findAll({where : where, offset : qOffset, limit : 20}).then(
			function(ret){
				if(ret.length == 0){
					res.status(400);
					result.status = 'F';
					result.reason = 'not find product';
					
				}else{
					console.log(ret);
					result.status = 'S';
					result.lastOffset = qOffset + ret.length;
					result.products = ret;
				}
				res.json(result);
			}, function(err){
				console.log(err);
				result.status = 'F';
				result.reason = err.message;
				res.json(result);
			});
}


module.exports = router;