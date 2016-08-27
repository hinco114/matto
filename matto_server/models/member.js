'use strict';

//Member 모델 정의
module.exports = function(sequelize, DataTypes) {
	  return sequelize.define("Member", {
	    id : {type : DataTypes.STRING(15), primaryKey : true, allowNull : false},
	    pwd : {type : DataTypes.STRING(15), allowNull : false},
	    ndPwd : {type : DataTypes.STRING(15), allowNull : true},
	    gender : {type : DataTypes.STRING(1), allowNull : false},
	    phoneNum : {type : DataTypes.STRING(12), allowNull : false}, // 유니크
	    createdAt : {type : DataTypes.DATE, defaultValue: DataTypes.NOW},
	    updatedAt : {type : DataTypes.DATE, defaultValue: DataTypes.NOW}
	  }, {
		    classMethods: {},
		    tableName: 'members',
		    freezeTableName: true,
		    underscored: true,
		    timestamps: false
		  });
	};


