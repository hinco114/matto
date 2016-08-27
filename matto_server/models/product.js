'use strict';


module.exports = function(sequelize, DataTypes) {
	  return sequelize.define("Product", {
	    productIdx : {type : DataTypes.INTEGER, primaryKey : true,  autoIncrement: true, allowNull : false},
	    name : {type : DataTypes.STRING(15), allowNull : false},
	    price : {type : DataTypes.INTEGER},
	    imgSrc : {type : DataTypes.STRING(50)}, 
	    description : {type : DataTypes.STRING(30)}
	  }, {
		    classMethods: {},
		    tableName: 'products',
		    freezeTableName: true,
		    underscored: true,
		    timestamps: false
		  });
	};
