CREATE TABLE `lines`
(
  `id` INT PRIMARY KEY AUTO_INCREMENT,
  `name` VARCHAR(255) NOT NULL
);

CREATE TABLE `stations`
(
  `naptan` varchar(12) PRIMARY KEY,
  `name` varchar(255) NOT NULL,
  `wheelchair` boolean
);

CREATE TABLE `line_stations`
(
  `id` int PRIMARY KEY AUTO_INCREMENT,
  `line` int,
  `station` varchar(12),
  FOREIGN KEY (line) REFERENCES `lines`(id),
  FOREIGN KEY (station) REFERENCES `stations`(naptan)
);


CREATE TABLE `zones`
(
  `id` int PRIMARY KEY AUTO_INCREMENT,
  `name` VARCHAR(255)
);

CREATE TABLE `zones_stations`
(
    `zone` int,
    `station` VARCHAR(12),
    CONSTRAINT PRIMARY KEY (zone, station),
    CONSTRAINT FOREIGN KEY (zone) REFERENCES `zones`(id),
    CONSTRAINT FOREIGN KEY (station) REFERENCES `stations`(naptan)
);

CREATE TABLE `station_durations`
(
  `station_departing` VARCHAR(12) NOT NULL,
  `station_arriving` VARCHAR(12) NOT NULL,
  `duration` double NOT NULL,
  CONSTRAINT PRIMARY KEY (station_departing, station_arriving),
  CONSTRAINT FOREIGN KEY (station_departing) REFERENCES `stations`(naptan),
  CONSTRAINT FOREIGN KEY (station_arriving) REFERENCES `stations`(naptan)
);

CREATE TABLE `connections`
(
  `id` int PRIMARY KEY AUTO_INCREMENT,
  `station_one` VARCHAR(12) NOT NULL,
  `station_two` VARCHAR(12) NOT NULL,
  CONSTRAINT FOREIGN KEY (station_one) REFERENCES `stations`(naptan),
  CONSTRAINT FOREIGN KEY (station_two) REFERENCES `stations`(naptan)
);

CREATE TABLE `fares`
(
  `departing_zone` int,
  `arriving_zone` int,
  `price` decimal,
  CONSTRAINT PRIMARY KEY (departing_zone, arriving_zone),
  CONSTRAINT FOREIGN KEY (departing_zone) REFERENCES `zones`(id),
  CONSTRAINT FOREIGN KEY (arriving_zone) REFERENCES `zones`(id)
);

CREATE TABLE `user_addresses`
(
  `id` int PRIMARY KEY AUTO_INCREMENT,
  `street_number` varchar(255) NOT NULL,
  `street_name` varchar(255) NOT NULL,
  `postal_code` varchar(255) NOT NULL,
  `city` varchar(255) NOT NULL,
  `label` varchar(255) NOT NULL,
  `nearest_station` VARCHAR(12),
  CONSTRAINT FOREIGN KEY (nearest_station) REFERENCES `stations`(naptan)
);

CREATE TABLE `roles`
(
  `id` int PRIMARY KEY,
  `name` varchar(255) NOT NULL
);


CREATE TABLE `users`
(
  `id` int PRIMARY KEY AUTO_INCREMENT,
  `first_name` varchar(255) NOT NULL,
  `last_name` varchar(255) NOT NULL,
  `date_of_birth` date NOT NULL,
  `email` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `home` int,
  `work` int,
  `role` int,
  CONSTRAINT FOREIGN KEY (home) REFERENCES `user_addresses`(id),
  CONSTRAINT FOREIGN KEY (work) REFERENCES `user_addresses`(id),
  CONSTRAINT FOREIGN KEY (role) REFERENCES `roles`(id)
);


CREATE TABLE `travels`
(
  `id` int PRIMARY KEY AUTO_INCREMENT,
  `starting_station` VARCHAR(12) NOT NULL,
  `arriving_station` VARCHAR(12) NOT NULL,
  `user_id` int NOT NULL,
  CONSTRAINT FOREIGN KEY (starting_station) REFERENCES `stations`(naptan),
  CONSTRAINT FOREIGN KEY (arriving_station) REFERENCES `stations`(naptan),
  CONSTRAINT FOREIGN KEY (user_id) REFERENCES `users`(id)
);

CREATE TABLE `histories`
(
  `id` int PRIMARY KEY AUTO_INCREMENT,
  `travel_id` int NOT NULL,
  `station` varchar(12) NOT NULL,
  CONSTRAINT FOREIGN KEY (travel_id) REFERENCES `travels`(id),
  CONSTRAINT FOREIGN KEY (station) REFERENCES `stations`(naptan)
);