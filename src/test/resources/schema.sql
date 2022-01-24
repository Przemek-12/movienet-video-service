CREATE TABLE IF not exists hibernate_sequence (
  `next_val` bigint(20) DEFAULT NULL
);

insert into hibernate_sequence values (1);
