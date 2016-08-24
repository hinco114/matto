create table if not exists toilet(
	toilet_idx int auto_increment primary key,
    name varchar(20) not null,
    longitude double,
    latitude double,
    address varchar(50),
    room_count int,
    img_src varchar(50),
    beacon_id varchar(50)
) ENGINE=InnoDB DEFAULT CHARSET=utf8