create table `employees` (
	`id` int auto_increment primary key,
    `first_name` varchar(30),
    `last_name` varchar(30),
    `username` varchar(30) unique,
    `password` varchar(32),
    `office` varchar(30)
);

create table `clients` (
	`id` int auto_increment primary key,
    `first_name` varchar(30),
    `last_name` varchar(30)
);

create table `rides` (
	`id` int auto_increment primary key,
    `destination` varchar(30),
    `date` date,
    `hour` time
);

create table `bookings` (
	`id` int auto_increment primary key,
    `id_client` int,
    `id_ride` int,
    `seat_no` int,
    CONSTRAINT `seat_no_ck` CHECK (`seat_no` BETWEEN 1 AND 18),
    foreign key(`id_client`) references `clients`(`id`),
    foreign key(`id_ride`) references `rides`(`id`)
);


insert into clients values(1, "Tom", "Sawyer");
insert into clients values(2, "Jim", "Roberts");
insert into clients values(3, "Andrew", "Pence");
insert into clients values(4, "Jesse", "Jackson");

insert into employees values(1, "Vlad", "Toader", "tva_vlad", MD5("zxcvbnm"), "Cluj");
insert into employees values(2, "Miriam", "Toader", "miriamt", MD5("qwerty"), "Cluj");
insert into employees values(3, "Ion", "Popescu", "ionpopescu12", MD5("parola157"), "Arad");
insert into employees values(4, "Gheorghe", "Toma", "ghtoma", MD5("pass1"), "Bucuresti");

insert into rides values (1, "Cancun", "2019-10-27", "08:45");
insert into rides values (2, "San Jose", "2020-04-02", "11:20");
insert into rides values (3, "Reyjkavik", "2020-08-14", "14:30");
insert into rides values (4, "Phuket", "2021-12-11", "10:15");

insert into bookings values (1, 1, 1, 15);
insert into bookings values (2, 1, 2, 12);
insert into bookings values (3, 2, 3, 18);
insert into bookings values (4, 3, 1, 4);
insert into bookings values (5, 3, 2, 7);
insert into bookings values (6, 2, 1, 1);
insert into bookings values (7, 1, 3, 5);




