var Sequelize = require('sequelize');
var sequelize = require('../routes/dbConnection');

var member = require('./member');
var toilet = require('./toilet');

var Report = sequelize.define('report_list',{
    report_idx : {type:Sequelize.STRING(15),primaryKey:true},
    id : {type:Sequelize.STRING(15)},
    toilet_idx : {type:Sequelize.STRING(15)}
});
member.hasMany(Report,{foreignKey: 'id'});
toilet.hasMany(Report,{foreignKey: 'toilet_idx'});
Report.sync();