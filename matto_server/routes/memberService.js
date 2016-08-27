var express = require('express');
var router = express.Router();
var models = require('../models');
var passport = require('passport');

/*
 * router.post('/login',passport.authrenticate('local'), function(req, res) {
 * console.log('login ok'); res.end('login'); })
 */


//회원가입 작성중
router.post('/members', function(req, res) {
	var member = req.body;
	console.log(member);
	models.Member.create(member).then(function() {
		res.status(201);
		res.send('success');

	}, function(err) {
		res.status(400);
		console.log(err);
		res.send('fail');
	});

});


//회원정보 수정 작성중
router.put('/members/:id', function(req,res){
	var modifyInfo = req.body;
	var whereQ = {where :{id : req.params.id}};
	console.log(modifyInfo);
	models.Member.update(modifyInfo,whereQ).then(function() {
		res.status(201);
		res.send('success');

	}, function(err) {
		res.status(400);
		console.log(err);
		res.send('fail');
	});
})

router.get('/login', function(req, res) {
	res.end('login get');
})

router.get('/logout', function(req, res) {
	req.logout();
	res.end('LOGOUT');
})

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
 * res.end(JSON.stringify({status : 'fail'})); });
 *  }
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
