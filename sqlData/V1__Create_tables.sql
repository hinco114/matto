create table if not exists members(
	id varchar(15) primary key,
    pwd varchar(15) not null,
    ndPwd varchar(15),
    gender char(1) not null,
    phoneNum varchar(12) unique not null,
	createdAt datetime not null,
	updatedAt datetime not null
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

create table if not exists toilets(
	toiletIdx int auto_increment primary key,
    name varchar(20) not null,
    longitude double,
    latitude double,
    address varchar(50),
    roomCount int,
    imgSrc varchar(50),
    beaconId varchar(50),
    createdAt datetime not null
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

create table if not exists products(
	productIdx int auto_increment primary key,
    name varchar(15) not null,
    price int,
	imgSrc varchar(50),
    description varchar(30)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

create table if not exists reports(
	reportIdx int auto_increment primary key,
    id varchar(15),
    toiletIdx int,
    createdAt datetime not null,
    foreign key (id) references members(id),
    foreign key (toiletIdx) references toilets(toiletIdx)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

create table if not exists toiletLogs(
	useIdx int auto_increment primary key,
	id varchar(15),
    toiletIdx int,
    usedCount int,
    createdAt datetime not null,
    foreign key (id) references members(id),
    foreign key (toiletIdx) references toilets(toiletIdx)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

create table if not exists toiletProducts(
	toiletProductIdx int auto_increment primary key,
    stock int,  
    toiletIdx int,
    productIdx int,
    foreign key(toiletIdx) references toilets(toiletIdx), 
    foreign key(productIdx) references products(productIdx)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

create table if not exists purchaseLogs(
	sellIdx int auto_increment primary key,
    id varchar(15),
    toiletProductIdx int,
    amount int,	
    createdAt datetime not null,
    foreign key (id) references members(id),
    foreign key (toiletProductIdx) references toiletProducts(toiletProductIdx)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;