CREATE TABLE users(
	id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
	login TEXT NOT NULL UNIQUE,
	password TEXT NOT NULL,
	role TEXT NOT NULL
)	
	