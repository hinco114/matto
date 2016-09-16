var passport = require('passport');
var models = require('../models')
// Local Strategy
var LocalStrategy = require('passport-local').Strategy;

module.exports = function() {
	// Login Authenticate
	console.log('passport-config');

	var strategy = new LocalStrategy({
		usernameField : 'id',
		passwordField : 'pwd',
	}, function(id, pwd, done) {
		models.Member.findById(id).then(function(member) {
			// not find member
			if (!member) {
				console.log('not find');
				return done(null, false);
			}
			// unvalid password
			if (!member.matchPassword(pwd)) {
				console.log('no match');
				return done(null, false);
			}

			return done(null, member);
		}, function(err) {
			if (err) {
				console.log('err');
				return done(err);
			}
		})
	});

	passport.use(strategy);

	passport.serializeUser(function(member, done) {
		done(null, member.id);
	});
	passport.deserializeUser(function(id, done) {
		Member.findById(id).then(function(member) {
			done(null, member);
		});
	});
}
