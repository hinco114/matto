/* 구매 로그 외래키 삭제 */
alter table purchaseLogs drop foreign key purchaseLogs_ibfk_2;
alter table purchaseLogs drop column toiletProductIdx;


/* 화장실 상품 pk삭제후 복합 pk 추가*/
alter table toiletProducts modify toiletProductIdx int not null;
alter table toiletProducts drop primary key;
alter table toiletProducts drop column toiletProductIdx;
alter table toiletProducts add constraint primary key(toiletIdx,productIdx);

/* 복합 외래키toiletProductstoiletProducts 추가 */
alter table purchaseLogs add column toiletIdx int not null, add column productIdx int not null, add
 constraint purchaseLogs_ibfk_2 foreign key(toiletIdx,productIdx) references toiletProducts(toiletIdx,productIdx);
