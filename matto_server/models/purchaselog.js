'use strict';

var models = require('./index');

module.exports = function(sequelize, DataTypes) {
	return sequelize.define("PurchaseLog", {
		sellIdx : {
			type : DataTypes.INTEGER,
			primaryKey : true,
			autoIncrement : true,
			allowNull : false
		},
		id : {
			type : DataTypes.STRING(15),
			references : {
				model : models.Member,
				key : 'id'
			}
		},
		toiletIdx : {
			type : DataTypes.INTEGER,
			references : {
				model : models.ToiletProduct,
				key : 'toiletIdx'
			}
		},
		productIdx : {
			type : DataTypes.INTEGER,
			references : {
				model : models.ToiletProduct,
				key : 'productIdx'
			}
		},
		amount : {
			type : DataTypes.INTEGER
		}
		
	}, {
		classMethods : {},
		tableName : 'purchaseLogs',
	    freezeTableName: true,
	    underscored: true,
	    timestamps : false
	});
};

