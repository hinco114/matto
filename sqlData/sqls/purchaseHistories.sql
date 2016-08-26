create table if not exists purchaseHistories(
	sellIdx int auto_increment primary key,
    id varchar(15),
    toiletProductIdx int,
    amount int,	
    createdAt datetime not null,
    foreign key (id) references members(id),
    foreign key (toiletProductIdx) references toiletProducts(toiletProductIdx)
) ENGINE=InnoDB DEFAULT CHARSET=utf8