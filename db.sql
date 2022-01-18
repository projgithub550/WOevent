create database event;

insert into user values(1,"admin","admin");
insert into user values(2,"user1","user1");

insert into role values(1,"admin");
insert into role values(2,"user");
insert into user_role values(1,1);
insert into user_role values(2,2);

insert into permission values(1,"set");
insert into role_permission values(1,1);