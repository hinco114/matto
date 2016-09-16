alter table members add column name char(20) not null after ndPwd;
alter table members modify ndPwd char(64);
alter table toilets add column ipAddr char(64) after beaconId;