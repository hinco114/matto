create table if not exists report(
	report_idx int auto_increment primary key,
    id varchar(15),
    toilet_idx int,
    foreign key (id) references member(id),
    foreign key (toilet_idx) references toilet(toilet_idx)
) ENGINE=InnoDB DEFAULT CHARSET=utf8