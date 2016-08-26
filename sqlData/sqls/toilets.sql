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
) ENGINE=InnoDB DEFAULT CHARSET=utf8