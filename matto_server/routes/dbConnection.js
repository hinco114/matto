var Sequelize = require('sequelize');
var env       = process.env.NODE_ENV || "dbConfig";
var config = require('../config.json')[env];
var sequelize = new Sequelize(config.database,config.username,config.password,{
    dialect: config.dialect,
    host : config.host,
    port : config.port,
    pool: config.pool
});

module.exports = sequelize;