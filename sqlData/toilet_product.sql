create table if not exists toilet_product(
	toilet_product_idx int auto_increment primary key,
    stock int,  
    toilet_idx int,
    product_idx int,
    foreign key(toilet_idx) references toilet(toilet_idx), 
    foreign key(product_idx) references product(product_idx)
) ENGINE=InnoDB DEFAULT CHARSET=utf8