'use strict';

var models = require('./index');

module.exports = function(sequelize, DataTypes) {
	  return sequelize.define("ToiletProduct", {
	    toiletProductIdx : {type : DataTypes.INTEGER, primaryKey : true,  autoIncrement: true, allowNull : false},
	    stock : {type : DataTypes.INTEGER},
	    toiletIdx   : {type : DataTypes.INTEGER,  references: {model: models.Toilet, key: 'toiletIdx'}},
	    productIdx   : {type : DataTypes.INTEGER,  references: {model: models.Product, key: 'productIdx'}}
	  }, {
		    classMethods: {},
		    tableName: 'toiletProducts',
		    freezeTableName: true,
		    underscored: true,
		    timestamps : false
		  });
	};

