'use strict';

var initAssoci = {
  initAssociations: function(db) {
	//회원은 여러 신고를 갖는다.
	db.Member.hasMany(db.Report, {foreignKey : 'id'});
	//화장실은 여러 신고를 갖는다.
	db.Toilet.hasMany(db.Report, {foreignKey : 'toiletIdx'});
	
	//회원은 여러 사용내역을 갖는다.
	db.Member.hasMany(db.ToiletLog, {foreignKey : 'id'});
	//화장실은 여러 사용내역을 갖는다.
	db.Toilet.hasMany(db.ToiletLog, {foreignKey : 'toiletIdx'});
	
	//여러 화장실은은 여러 상품을 갖는다.
	db.Toilet.belongsToMany(db.Product, {through : db.ToiletProduct, foreignKey : 'toiletIdx'});
	//여러 상품은 여러 화장실에 속한다.(여러 화장실에 비치된다.)
	db.Product.belongsToMany(db.Toilet, {through : db.ToiletProduct, foreignKey : 'productIdx'});

	//상품화장실은 여러 구매내역을 갖는다.
	db.ToiletProduct.hasMany(db.PurchaseLog,{foreignKey : 'toiletProductIdx'});
	//db.PurchaseLog.belongsTo(db.ToiletProduct, {foreignKey : 'toiletProductIdx'});
	//회원은 여러 구매내역을 갖는다.
	
	db.Member.hasMany(db.PurchaseLog,{foreignKey : 'id'});
	//db.PurchaseLog.belongsTo(db.Member, {foreignKey : 'Toilet'});
	
  }
};

module.exports = initAssoci;