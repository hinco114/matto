'use strict';

var models = require('./index');

module.exports = function(sequelize, DataTypes) {
	return sequelize.define("ToiletLog", {
		useIdx : {
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
				model : models.Toilet,
				key : 'toiletIdx'
			}
		},
		usedCount : {
			type : DataTypes.INTEGER
		},
		createdAt : {
			type : DataTypes.DATE,
			defaultValue : DataTypes.NOW
		}
	}, {
		classMethods : {},
		tableName : 'toiletLogs',
		freezeTableName : true,
		underscored : true,
		timestamps : false
	});
};
