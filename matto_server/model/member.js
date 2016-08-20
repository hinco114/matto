var Sequelize = require('sequelize');
var sequelize = require('../routes/dbConnection');
var Member = sequelize.define('member',{
    id : {type:Sequelize.STRING(15),primaryKey:true},
    pwd : {type:Sequelize.STRING(15)},
    secondpwd : {type:Sequelize.STRING(15)},
    sex : {type:Sequelize.STRING(1)},
    phoneNum : {type:Sequelize.INTEGER},
},{timestamps:true, tableName : 'member_info'});
Member.sync();

var Toilet = sequelize.define('toilet_info',{
    toilet_idx : {type:Sequelize.STRING(15),primaryKey:true},
    name : {type:Sequelize.STRING(15)},
    longitude : {type:Sequelize.DOUBLE},
    latitude : {type:Sequelize.DOUBLE},
    address : {type:Sequelize.STRING(30)},
    room_count : {type:Sequelize.INTEGER},
    img_src : {type:Sequelize.STRING(50)},
    beacon_id : {type:Sequelize.STRING(20)}
});
Toilet.sync();

var Report = sequelize.define('report_list',{
    report_idx : {type:Sequelize.STRING(15),primaryKey:true},
    id : {type:Sequelize.STRING(15)},
    toilet_idx : {type:Sequelize.STRING(15)}
});
Member.hasMany(Report,{foreignKey: 'id'});
Toilet.hasMany(Report,{foreignKey: 'toilet_idx'});
Report.sync();
/*
Member.verifyPassword = function(){

}
*/
module.exports = Member;



