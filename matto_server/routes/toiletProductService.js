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

// 화장실 상품 구매
router.get("/toilets/:toiletIdx/buy/:productIdx", buyProduct);

var ResultModel = function(status, reason, data) {
	this.status = status;
	this.reason = reason
	this.resultData = data;
};

function buyProduct(req, res){
	var toiletIdx = req.params.toiletIdx;
	var productIdx = req.params.productIdx;
	var where = {where : {toiletIdx : toiletIdx, 
		productIdx : productIdx}};
	
	
	var result = {};
	models.ToiletProduct.find(where).then(function(ret){
		if(ret.length == 0){
			res.status(400);
			result = new ResultModel('F', 'not find product', null);
			res.json(result);
		}else{
			var resultData ={};
			console.log(ret);
			if(ret.stock <= 0){
				result = new ResultModel('F', 'not enough stock', null);
				res.json(result);
			}else {
				models.ToiletProduct.update({stock : ret.stock-1}, where).then(function(ret){
					result = new ResultModel('S', null ,null);
					res.json(result);
				}, function(err){
					result = new ResultModel('F', err.message ,null);
					res.json(result);
				});
			}
		}
		
	},function(err){
		console.log(err);
		result = new ResultModel('F', err.message ,null);
		res.json(result);
	});
	
	
	/*models.sequelize.query('SELECT `Products`.`productIdx` AS productIdx, `Products`.`name` AS name, `Products`.`price` AS price, `Products`.`imgSrc` AS imgSrc, `Products`.`description` AS description, `Products.ToiletProduct`.`stock` AS stock'
			 +' FROM `toilets` AS `Toilet`' 
			 +'	JOIN (`toiletProducts` AS `Products.ToiletProduct` INNER JOIN `products` AS `Products` ON `Products`.`productIdx` = `Products.ToiletProduct`.`productIdx`) ON `Toilet`.`toiletIdx` = `Products.ToiletProduct`.`toiletIdx`'
			 +'	WHERE `Toilet`.`toiletIdx` = ? AND `Products.ToiletProduct`.`productIdx` = ?',
			{
				replacements : [toiletIdx, productIdx], type: models.Sequelize.QueryTypes.SELECT 
			}).then(function(ret){
					var result;
					
					if(ret.length == 0){
						res.status(400);
						result = new ResultModel('F', 'not find product', null);
						res.json(result);
					}else{
						var resultData ={};
						console.log(ret);
						resultData = ret[0].stock;
						if(ret[0].stock <= 0){
							result = new ResultModel('F', 'not enough stock', null);
							res.json(result);
						}else {
							//models.sequelize.query
							
							result = new ResultModel('S', null ,resultData);
						}
					}
					
				}, function(err){
					console.log(err);
					result.status = 'F';
					result.reason = err.message;
					res.json(result);
				})*/
}




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
	
	var	toiletIdx = req.params.toiletIdx

	var qOffset = parseInt(req.query.offset);
	var result ={
		status : null,
		reason : null,
		lastOffset : null,
		products : null
	}
	
	models.sequelize.query('SELECT `Products`.`productIdx` AS productIdx, `Products`.`name` AS name, `Products`.`price` AS price, `Products`.`imgSrc` AS imgSrc, `Products`.`description` AS description, `Products.ToiletProduct`.`stock` AS stock' 
			+' FROM `toilets` AS `Toilet`' 
			+' JOIN (`toiletProducts` AS `Products.ToiletProduct` INNER JOIN `products` AS `Products` ON `Products`.`productIdx` = `Products.ToiletProduct`.`productIdx`) ON `Toilet`.`toiletIdx` = `Products.ToiletProduct`.`toiletIdx`' 
			+' WHERE `Toilet`.`toiletIdx` = ?'
			+' LIMIT 20 OFFSET ?',
			{
				replacements : [toiletIdx, qOffset], type: models.Sequelize.QueryTypes.SELECT 
			}).then(function(ret){
					var result;
					if(ret.length == 0){
						res.status(400);
						result = new ResultModel('F', 'not find product', null);
						
						//result.status = 'F';
						//result.reason = 'not find product';
						
					}else{
						var resultData ={};
						console.log(ret);
						resultData.lastOffset = qOffset + ret.length;
						resultData.products = ret;
						result = new ResultModel('S', null ,resultData);
					}
					res.json(result);
				}, function(err){
					console.log(err);
					result.status = 'F';
					result.reason = err.message;
					res.json(result);
				})
	// models.ToiletProduct.findAll({where : where, offset : qOffset, limit :
	// 20}).then(
			
}




module.exports = router;