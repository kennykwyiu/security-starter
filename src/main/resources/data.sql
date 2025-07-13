-- insert into mooc_users(username, password, enabled) values
-- ('user', '{bcrypt}$2a$10$ROWbh4QyuvE.hr3xKWnW6uzrYWjqq.GX7c8YrlqcAYIweyCopnMN2', 1),
-- ('mary', '{SHA-1}{JHbzcU2gPQAmIrdOIj1bHTQ20jpYMzITRjrnDCnM6mo=}9349861ebe589ad9b330aeab2adbb9eed3e175bb', 1);
-- insert into mooc_authorities(username, authority) values
-- ('mary', 'ROLE_USER'),
-- ('user', 'ROLE_USER'),
-- ('user', 'ROLE_ADMIN');
--

insert into mooc_users(id, username, `name`, mobile, password_hash, enabled, account_non_expired, account_non_locked, credentials_non_expired, email)
values (1, 'user', 'user_peter', '13012341234', '{bcrypt}$2a$10$ROWbh4QyuvE.hr3xKWnW6uzrYWjqq.GX7c8YrlqcAYIweyCopnMN2', 1, 1, 1, 1, 'peter@local.dev'),
       (2, 'old_user', 'old_user_mary', '13812341234', '{SHA-1}{JHbzcU2gPQAmIrdOIj1bHTQ20jpYMzITRjrnDCnM6mo=}9349861ebe589ad9b330aeab2adbb9eed3e175bb', 1, 1, 1, 1, 'mary@local.dev');
insert into mooc_roles(id, role_name) values (1, 'ROLE_USER'), (2, 'ROLE_ADMIN');
insert into mooc_users_roles(user_id, role_id) values (1, 1), (1, 2), (2, 1);

--
-- insert into mooc_users(id, username, `name`, password_hash, enabled, account_non_expired, account_non_locked, credentials_non_expired, email)
-- values (1, 'user', 'user_peter', '{bcrypt}$2a$10$ROWbh4QyuvE.hr3xKWnW6uzrYWjqq.GX7c8YrlqcAYIweyCopnMN2', 1, 1, 1, 1, 'peter@local.dev'),
--        (2, 'old_user', 'old_user_mary',  '{SHA-1}{JHbzcU2gPQAmIrdOIj1bHTQ20jpYMzITRjrnDCnM6mo=}9349861ebe589ad9b330aeab2adbb9eed3e175bb', 1, 1, 1, 1, 'mary@local.dev');
-- insert into mooc_roles(id, role_name) values (1, 'ROLE_USER'), (2, 'ROLE_ADMIN');
-- insert into mooc_users_roles(user_id, role_id) values (1, 1), (1, 2), (2, 1);