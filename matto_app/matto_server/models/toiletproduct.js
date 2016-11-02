'use strict';

var models = require('./index');

module.exports = function(sequelize, DataTypes) {
	  return sequelize.define("ToiletProduct", {
	    toiletIdx   : {type : DataTypes.INTEGER, primaryKey : true, references: {model: models.Toilet, key: 'toiletIdx'}},
	    productIdx   : {type : DataTypes.INTEGER, primaryKey : true, references: {model: models.Product, key: 'productIdx'}},
	    stock : {type : DataTypes.INTEGER}
	  }, {
		    classMethods: {},
		    tableName: 'toiletProducts',
		    freezeTableName: true,
		    underscored: true,
		    timestamps : false
		  });
	};

