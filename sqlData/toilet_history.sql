create table if not exists toilet_history(
	id varchar(15),
    toilet_idx int,
    used_count int,
    foreign key (id) references member(id),
    foreign key (toilet_idx) references toilet(toilet_idx),
    primary key(id,toilet_idx)
) ENGINE=InnoDB DEFAULT CHARSET=utf8