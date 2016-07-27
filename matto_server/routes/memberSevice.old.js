var express = require('express');
var router = express.Router();
var pool = require('./dbConnection');
var passport  = reuqire('./passport');

router.get('/member/:id',searchByID);
router.post('/member',registMember);



function registMember(req,res){
	 var inputData = {
            id : req.body.id,
            pwd : req.body.pwd,
            sex : req.body.sex,
            phoneNum : req.body.phoneNum
    };
	registFunc(inputData).then(
		function (result){ res.end(JSON.stringify({status : 'success'}));},
		function (err) { console.log(err); res.end(JSON.stringify({status : 'fail'}));}
		); 

}


function registFunc(inputData){
	return new Promise(function (fullfill, reject) {
		console.log('회원가입');
		console.log(inputData);
		pool.getConnection(function (err, conn) {
			if (err) {
				console.log("실패");
				conn.release();
				reject(err);
				return;
			}
			var sql = 'INSERT INTO member SET ?;'
			console.log("쿼리실행");
			conn.query(sql, inputData, function (err, results) {
				if (err) {
					console.log("실패");
					conn.release();
					reject(err);
					return;
				}
				console.log(results);
				conn.release();
				fullfill();
				return;
			});
		});
	});
}

function searchByID(req,res) {
	var id = req.params.id;
	searchByIDFunc(id).then(
		function (result){ res.end(JSON.stringify({status : 'success'}));},
		function (err) { console.log(err); res.end(JSON.stringify({status : 'fail'}));}
		); 
}

function searchByIDFunc(id){
	return new Promise(function (fullfill, reject){
		console.log('searchByID');
		console.log(id);
		pool.getConnection(function (err,conn) {
			if(err){
				console.log('fail');
				conn.release();
				reject(err);
				return;
			}
			var sql = 'SELECT * FROM member WHERE id = ?;';
			conn.query(sql, id, function(err,result){
				if(err || result.length == 0){
					console.log('fail');
					conn.release();
					reject(err);
					return;
				}
				
				console.log(result);
				conn.release();
				fullfill();
				return;

			});
		});
	});
}


module.exports = router;
