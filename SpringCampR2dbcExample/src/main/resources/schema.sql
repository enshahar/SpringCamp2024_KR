create table `users` (
                         `id` bigint(20) not null primary key auto_increment,
                         `user_name` varchar(50) not null,
                         `email` varchar(100) not null,
                         `created` timestamp not null
);

create table `groups` (
                          `id` bigint(20) not null primary key auto_increment,
                          `name` varchar(50) not null,
                          `created` timestamp not null
);

create table `user_group`(
                             `id` bigint(20) not null primary key auto_increment,
                             `user_id` bigint(20) not null,
                             `group_id` bigint(20) not null
);

alter table `user_group` add constraint foreign key(`user_id`) references `users`(`id`) on delete restrict;
alter table `user_group` add constraint foreign key(`group_id`) references `groups`(`id`) on delete restrict;
alter table `user_group` add constraint unique index (`user_id`,`group_id`);
alter table `users` add constraint unique index (`email`);
alter table `users` add constraint unique index (`user_name`);
alter table `groups` add constraint unique index (`name`);
