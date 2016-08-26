create table if not exists toiletProducts(
	toiletProductIdx int auto_increment primary key,
    stock int,  
    toiletIdx int,
    productIdx int,
    foreign key(toiletIdx) references toilets(toiletIdx), 
    foreign key(productIdx) references products(productIdx)
) ENGINE=InnoDB DEFAULT CHARSET=utf8