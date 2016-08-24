create table if not exists member(
	id varchar(15) primary key,
    pwd varchar(15) not null,
    ndpwd varchar(15) not null,
    gender char(1) not null,
    phoneNum varchar(12) unique not null
) ENGINE=InnoDB DEFAULT CHARSET=utf8