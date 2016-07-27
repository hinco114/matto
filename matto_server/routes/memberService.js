var express = require('express');
var router = express.Router();
var Member = require('../model/member');
var passport  = require('passport');

router.post('/login',passport.authenticate('local'), function(req, res) {
	console.log('login ok');
	res.end('login');
})

router.get('/login',function (req,res) {
	res.end('login get');
})

router.get('/logout',function (req,res) {
	req.logout();
	res.end('LOGOUT');
})
router.get('/member/dup/:id',checkDuplicate);
router.get('/member/info/:id',showMemberInfo)
router.post('/member/signin',registMember);

router.get('/login_verify', isAuthenticated, verifyLogin);

function isAuthenticated(req,res,next) {
	if(req.isAuthenticated()){
		return next();
	}
	res.end('NOT LOGIN');
}
function verifyLogin(req,res) {
	res.end('LOGIN OK');
}

function registMember(req,res){
	 var inputData = {
            id : req.body.id,
            pwd : req.body.pwd,
            sex : req.body.sex,
            phoneNum : req.body.phoneNum
    };
	Member.create(inputData).then(function(){
		res.end(JSON.stringify({status : 'success'}));
	},function (err) {
		console.log(err); 
		res.end(JSON.stringify({status : 'fail'}));
	});

}

function checkDuplicate(req,res) {
	var id = req.params.id;
	Member.findById(id).then(
		function (result){ res.end(JSON.stringify({status : 'success'}));},
		function (err) { console.log(err); res.end(JSON.stringify({status : 'fail'}));}
		); 
}

function showMemberInfo(req,res) {
	var id = req.params.id;
	Member.findById(id).then(
		function (result){ res.end(JSON.stringify(result));},
		function (err) { console.log(err); res.end(JSON.stringify({status : 'fail'}));}
		); 
}

module.exports = router;
