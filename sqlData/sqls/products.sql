create table if not exists products(
	productIdx int auto_increment primary key,
    name varchar(15) not null,
    price int,
	imgSrc varchar(50),
    description varchar(30)
) ENGINE=InnoDB DEFAULT CHARSET=utf8