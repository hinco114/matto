create table if not exists members(
	id varchar(15) primary key,
    pwd varchar(15) not null,
    ndPwd varchar(15) not null,
    gender char(1) not null,
    phoneNum varchar(12) unique not null,
	createdAt datetime not null,
	updatedAt datetime not null
) ENGINE=InnoDB DEFAULT CHARSET=utf8