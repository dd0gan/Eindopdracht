insert into roles(rolename) values ('USER'), ('ADMIN');

--   username: karel, password: appel
insert into users(username, password) values ('karel', '$2a$12$v3hpM1z6mh.ITK9UdFeeiOHOaRzvrlLCCGQc9tyZi718XWXWmLub6');
-- gebruikersnaam admin: dd0gan / wachtwoord: appel
insert into users(username, password) values ('dd0gan', '$2a$12$v3hpM1z6mh.ITK9UdFeeiOHOaRzvrlLCCGQc9tyZi718XWXWmLub6');

insert into users_roles values('karel', 'USER');
insert into users_roles values('dd0gan', 'ADMIN');
