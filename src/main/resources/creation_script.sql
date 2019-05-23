CREATE TABLE `lines`
(
  `id` int PRIMARY KEY AUTO_INCREMENT,
  `name` varchar(255) NOT NULL
);

CREATE TABLE `stations`
(
  `id` int PRIMARY KEY AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `wheelchair` boolean,
  `zone` int
);

CREATE TABLE `line_stations`
(
  `id` int PRIMARY KEY AUTO_INCREMENT,
  `line` int,
  `station` int
);


CREATE TABLE `zones`
(
  `id` int PRIMARY KEY AUTO_INCREMENT,
  `name` varchar(255)
);

CREATE TABLE `zones_stations`
(
    `zone` int,
    `station` int,
    CONSTRAINT PRIMARY KEY (zone, station),
    CONSTRAINT FOREIGN KEY (zone) REFERENCES zones(id),
    CONSTRAINT FOREIGN KEY (station) REFERENCES stations(id)
);

CREATE TABLE `station_durations`
(
  `station_departing` int NOT NULL,
  `station_arriving` int NOT NULL,
  `duration` time NOT NULL,
  CONSTRAINT PRIMARY KEY (station_departing, station_arriving)
);

CREATE TABLE `connections`
(
  `id` int PRIMARY KEY AUTO_INCREMENT,
  `station_one` int NOT NULL,
  `station_two` int NOT NULL
);

CREATE TABLE `fares`
(
  `departing_zone` int,
  `arriving_zone` int,
  `price` decimal,
  CONSTRAINT PRIMARY KEY (departing_zone, arriving_zone)
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
  `role` int
);

CREATE TABLE `roles`
(
  `id` int PRIMARY KEY,
  `name` varchar(255) NOT NULL
);

CREATE TABLE `user_addresses`
(
  `id` int PRIMARY KEY AUTO_INCREMENT,
  `street_number` varchar(255) NOT NULL,
  `street_name` varchar(255) NOT NULL,
  `postal_code` varchar(255) NOT NULL,
  `city` varchar(255) NOT NULL,
  `label` varchar(255) NOT NULL,
  `nearest_station` int
);

CREATE TABLE `travels`
(
  `id` int PRIMARY KEY AUTO_INCREMENT,
  `starting_station` int NOT NULL,
  `arriving_station` int NOT NULL,
  `user_id` int NOT NULL
);

CREATE TABLE `histories`
(
  `id` int PRIMARY KEY AUTO_INCREMENT,
  `travel_id` int NOT NULL,
  `station` int NOT NULL
);

ALTER TABLE `stations` ADD FOREIGN KEY (`zone`) REFERENCES `zones` (`id`);

ALTER TABLE `line_stations` ADD FOREIGN KEY (`line`) REFERENCES `lines` (`id`);

ALTER TABLE `line_stations` ADD FOREIGN KEY (`station`) REFERENCES `stations` (`id`);

ALTER TABLE `station_durations` ADD FOREIGN KEY (`station_departing`) REFERENCES `stations` (`id`);

ALTER TABLE `station_durations` ADD FOREIGN KEY (`station_arriving`) REFERENCES `stations` (`id`);

ALTER TABLE `connections` ADD FOREIGN KEY (`station_one`) REFERENCES `stations` (`id`);

ALTER TABLE `connections` ADD FOREIGN KEY (`station_two`) REFERENCES `stations` (`id`);

ALTER TABLE `fares` ADD FOREIGN KEY (`departing_zone`) REFERENCES `zones` (`id`);

ALTER TABLE `fares` ADD FOREIGN KEY (`arriving_zone`) REFERENCES `zones` (`id`);

ALTER TABLE `users` ADD FOREIGN KEY (`home`) REFERENCES `user_addresses` (`id`);

ALTER TABLE `users` ADD FOREIGN KEY (`work`) REFERENCES `user_addresses` (`id`);

ALTER TABLE `users` ADD FOREIGN KEY (`role`) REFERENCES `roles` (`id`);

ALTER TABLE `user_addresses` ADD FOREIGN KEY (`nearest_station`) REFERENCES `stations` (`id`);

ALTER TABLE `travels` ADD FOREIGN KEY (`starting_station`) REFERENCES `stations` (`id`);

ALTER TABLE `travels` ADD FOREIGN KEY (`arriving_station`) REFERENCES `stations` (`id`);

ALTER TABLE `travels` ADD FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);

ALTER TABLE `histories` ADD FOREIGN KEY (`travel_id`) REFERENCES `travels` (`id`);

ALTER TABLE `histories` ADD FOREIGN KEY (`station`) REFERENCES `stations` (`id`);