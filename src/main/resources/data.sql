insert into mooc_users(username, password, enabled) values
('user', '{bcrypt}$2a$10$ROWbh4QyuvE.hr3xKWnW6uzrYWjqq.GX7c8YrlqcAYIweyCopnMN2', 1),
('mary', '{SHA-1}{JHbzcU2gPQAmIrdOIj1bHTQ20jpYMzITRjrnDCnM6mo=}9349861ebe589ad9b330aeab2adbb9eed3e175bb', 1);
insert into mooc_authorities(username, authority) values
('mary', 'ROLE_USER'),
('user', 'ROLE_USER'),
('user', 'ROLE_ADMIN');