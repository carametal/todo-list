
CREATE EXTENSION IF NOT EXISTS pgcrypto;

create table users(
	id serial PRIMARY KEY,
	username VARCHAR(50) COLLATE "C" UNIQUE NOT NULL,
	password VARCHAR(500) NOT NULL,
	enabled boolean NOT NULL
);
CREATE TABLE authorities (
	id serial PRIMARY KEY,
	user_id integer NOT NULL,
	authority VARCHAR(50) COLLATE "C" NOT NULL,
	CONSTRAINT fk_authorities_users FOREIGN KEY(user_id) REFERENCES users(id)
);
create unique index ix_auth_username on authorities (user_id, authority);
