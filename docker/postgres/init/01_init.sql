CREATE EXTENSION IF NOT EXISTS pgcrypto;
create table users(
	id SERIAL PRIMARY KEY,
	username VARCHAR(50) COLLATE "C" UNIQUE NOT NULL,
	password VARCHAR(500) NOT NULL,
	enabled boolean NOT NULL
);
CREATE TABLE authorities (
	id SERIAL PRIMARY KEY,
	user_id INTEGER NOT NULL,
	authority VARCHAR(50) COLLATE "C" NOT NULL,
	CONSTRAINT fk_authorities_users FOREIGN KEY(user_id) REFERENCES users(id)
);
create unique index ix_auth_username on authorities (user_id, authority);
CREATE TABLE tasks (
	task_id SERIAL PRIMARY KEY,
	name VARCHAR(255) NOT NULL,
	detail TEXT,
	state VARCHAR(50) CHECK (state IN ('Todo', 'InProgress', 'Done')),
	user_id INTEGER REFERENCES users(id) ON DELETE CASCADE
);
