'use strict';

//Toilet 모델 정의
module.exports = function(sequelize, DataTypes) {
	  return sequelize.define("Toilet", {
	    toiletIdx : {type : DataTypes.INTEGER, primaryKey : true, autoIncrement: true, allowNull : false},
	    name : {type : DataTypes.STRING(20), allowNull : false},
	    longitude : {type : DataTypes.DOUBLE},
	    latitude : {type : DataTypes.DOUBLE},
	    address : {type : DataTypes.STRING(50)}, // 유니크
	    roomCount : {type : DataTypes.INTEGER},
	    beaconId : {type : DataTypes.STRING(50)},
	    ipAddr : {type : DataTypes.STRING(64)},
	    type : {type : DataTypes.STRING(1)},
	    createdAt : {type : DataTypes.DATE, defaultValue: DataTypes.NOW}
	  }, {
		    classMethods: {},
		    tableName: 'toilets',
		    freezeTableName: true,
		    underscored: true,
		    timestamps :false
		  });
	};
