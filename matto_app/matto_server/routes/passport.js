module.exports = function(){
//Login Authenticate
console.log('passport-config');
var passport = require('passport');
var Member = require('../model/member')
//Local Strategy
var LocalStrategy = require('passport-local').Strategy;

var strategy = new LocalStrategy({
        usernameField : 'id',
        passwordField : 'pwd',
    }
    , function (id,pwd,done) {
    Member.findById(id).then(function (member) {
        console.log(id,pwd);
        console.log(member);
		//not find member
        console.log(member.pwd)
		if(!member) {console.log('not find'); return done(null,false);}
		//unvalid password
		if(pwd != member.pwd){console.log('no match'); return done(null,false);}

		return done(null, member);
	}, function(err){ if(err) {console.log('err'); return done(err); }})
});

passport.use(strategy);

passport.serializeUser(function(member, done) {
  done(null, member.id);
});
passport.deserializeUser(function(id, done) {
    Member.findById(id).then(function(member){
         done(null,member);
    });
});
}
