'use strict';
var crypto = require('crypto');

//Member 모델 정의
module.exports = function(sequelize, DataTypes) {
	  var member = sequelize.define("Member", {
	    id : {type : DataTypes.STRING(15), primaryKey : true, allowNull : false},
	    pwd : {type : DataTypes.STRING(64), allowNull : false},
	    salt : {type : DataTypes.STRING(64)},
	    ndPwd : {type : DataTypes.STRING(15), allowNull : true},
	    gender : {type : DataTypes.STRING(1), allowNull : false},
	    phoneNum : {type : DataTypes.STRING(12), allowNull : false}, // 유니크
	    createdAt : {type : DataTypes.DATE, defaultValue: DataTypes.NOW},
	    updatedAt : {type : DataTypes.DATE, defaultValue: DataTypes.NOW}
	  }, {
		    classMethods: {
		    	//salt 생성
		    	createSalt : function(){
		    		return crypto.randomBytes(32).toString('hex');
		    	},
		    	//비밀번호 해시 생성
		    	createHashPwd : function(pwd,salt){
		    		var key = crypto.pbkdf2Sync(pwd, salt, 100000, 32, 'sha256');
		    		return key.toString('hex');
		    	}
		    },
		    instanceMethods : {
		    	//비밀번호 확인
		    	matchPassword : function(password){
		    		return member.createHashPwd(password, this.salt) === this.pwd;
		    	} 
		    },
		    tableName: 'members',
		    freezeTableName: true,
		    underscored: true,
		    timestamps: false
		  });
	  return member;
	};


