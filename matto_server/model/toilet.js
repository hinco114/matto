var Sequelize = require('sequelize');
var sequelize = require('../routes/dbConnection');
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

module.exports = Toilet;
