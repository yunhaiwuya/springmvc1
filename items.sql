
DROP TABLE IF EXISTS items;
CREATE TABLE `items` (
	`id` INT (10),
	`name` VARCHAR (90),
	`price` FLOAT ,
	`pic` VARCHAR (300),
	`createtime` DATE ,
	`detail` BLOB 
); 
INSERT INTO `items` (`id`, `name`, `price`, `pic`, `createtime`, `detail`) VALUES('1','小米手机','1500',',','2019-01-01','');
INSERT INTO `items` (`id`, `name`, `price`, `pic`, `createtime`, `detail`) VALUES('2','手机','1000','f65f2ad8-49e5-4820-917b-b1df5afb8d8c.jpg',NULL,'');