var Sequelize = require('sequelize');
var sequelize = require('../routes/dbConnection');
var Member = sequelize.define('member',{
    id : {type:Sequelize.STRING(15),primaryKey:true},
    pwd : {type:Sequelize.STRING(15)},
    sex : {type:Sequelize.STRING(1)},
    phoneNum : {type:Sequelize.INTEGER},
},{timestamps:true, tableName : 'member_info'});
Member.sync();
/*
Member.verifyPassword = function(){

}
*/
module.exports = Member;
