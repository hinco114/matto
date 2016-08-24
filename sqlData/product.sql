create table if not exists product(
	product_idx int auto_increment primary key,
    name varchar(15) not null,
    price int,
	img_src varchar(50),
    description varchar(30)
) ENGINE=InnoDB DEFAULT CHARSET=utf8