create database feedback;

use feedback;

create table employee (employee_id varchar(50), first_name varchar(50), last_name varchar(50), 
designation varchar(50), manager_id varchar(50), primary key(employee_id));

create table login (user_id varchar(50), password varchar(100), primary key(user_id));

create table rating(employee_id varchar(50), manager_id varchar(50), employee_rating varchar(50), 
manager_rating varchar(50), primary key(employee_id));


insert into employee values ('001', 'CEO', 'CEO','manager','001');
