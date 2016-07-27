var mysql = require('mysql');
var dbConfig = {
    host : '14.63.226.110',
    user : 'root',
    password : 'matto123',
    database : 'test',
    port : 3306,
    connectionLimit : 50
};
var dbPool = mysql.createPool(dbConfig);
module.exports = dbPool;