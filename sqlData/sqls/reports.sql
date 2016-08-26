create table if not exists report(
	reportIdx int auto_increment primary key,
    id varchar(15),
    toiletIdx int,
    createdAt datetime not null,
    foreign key (id) references members(id),
    foreign key (toiletIdx) references toilets(toiletIdx)
) ENGINE=InnoDB DEFAULT CHARSET=utf8