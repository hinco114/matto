var Sequelize = require('sequelize');
var env = process.env.NODE_ENV || "dbConfig";
var config = require('../config.json')[env];
var sequelize = new Sequelize(config.database, config.username,
		config.password, {
			dialect : config.dialect,
			host : config.host,
			port : config.port,
			pool : config.pool,
			define : {
				charset : 'utf8',
				collate : 'utf8_general_ci'
			}
		});

module.exports = sequelize;