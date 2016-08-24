create table if not exists purchase_history(
	sell_idx int auto_increment primary key,
    id varchar(15),
    toilet_product_idx int,
    amount int,	
    foreign key (id) references member(id),
    foreign key (toilet_product_idx) references toilet_product(toilet_product_idx)
) ENGINE=InnoDB DEFAULT CHARSET=utf8