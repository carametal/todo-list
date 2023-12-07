-- https://github.com/spring-projects/spring-security/blob/main/core/src/main/resources/org/springframework/security/core/userdetails/jdbc/users.ddl
-- デフォルトユーザースキーマ
create table users(
	id serial PRIMARY KEY,
	username VARCHAR(50) COLLATE "C" UNIQUE NOT NULL,
	password VARCHAR(500) NOT NULL,
	enabled boolean NOT NULL
);
CREATE TABLE authorities (
	id serial PRIMARY KEY,
	username VARCHAR(50) COLLATE "C" NOT NULL,
	authority VARCHAR(50) COLLATE "C" NOT NULL,
	CONSTRAINT fk_authorities_users FOREIGN KEY(username) REFERENCES users(username)
);
create unique index ix_auth_username on authorities (username, authority);
