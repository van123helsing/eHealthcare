DROP TABLE IF EXISTS group_members;
DROP TABLE IF EXISTS group_authorities;
DROP TABLE IF EXISTS groups;
DROP TABLE IF EXISTS authorities;
DROP TABLE IF EXISTS users;

create table users(
	username varchar(50) not null primary key,
	password varchar(100) not null,
	enabled boolean not null
);

create table authorities (
	username varchar(50) not null,
	authority varchar(50) not null,
	constraint fk_authorities_users foreign key(username) references users(username)
);
create unique index ix_auth_username on authorities (username,authority);

create table groups (
	id bigserial primary key,
	group_name varchar(50) not null
);

create table group_authorities (
	group_id bigint not null,
	authority varchar(50) not null,
	constraint fk_group_authorities_group foreign key(group_id) references groups(id)
);

create table group_members (
	id bigserial primary key,
	username varchar(50) not null,
	group_id bigint not null,
	constraint fk_group_members_group foreign key(group_id) references groups(id)
);

INSERT INTO users (username, password, enabled) VALUES
    ('patient1', '$2a$10$P2MzVxq4dIurHVDB1ztv3e62vGmue3uLUlMiY8M/VZZd6xeQdsXKG', TRUE), --password=password
    ('patient2', '$2a$10$P2MzVxq4dIurHVDB1ztv3e62vGmue3uLUlMiY8M/VZZd6xeQdsXKG', TRUE), --password=password
    ('doctor1', '$2a$10$P2MzVxq4dIurHVDB1ztv3e62vGmue3uLUlMiY8M/VZZd6xeQdsXKG', TRUE), --password=password
    ('doctor2', '$2a$10$P2MzVxq4dIurHVDB1ztv3e62vGmue3uLUlMiY8M/VZZd6xeQdsXKG', TRUE); --password=password


INSERT INTO authorities (username, authority) VALUES
    ('patient1', 'ROLE_PATIENT'),
    ('patient2', 'ROLE_PATIENT'),
    ('doctor1', 'ROLE_DOCTOR'),
    ('doctor2', 'ROLE_DOCTOR');