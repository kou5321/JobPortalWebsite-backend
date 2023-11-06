CREATE TABLE IF NOT EXISTS users (
    id UUID PRIMARY KEY,
    email VARCHAR(30) NOT NULL UNIQUE,
    password VARCHAR(200) NOT NULL,
    username VARCHAR(30) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS user_applied_job_postings (
    user_id UUID NOT NULL,
    job_posting_id TEXT NOT NULL,
    CONSTRAINT pk_user_applied_job_postings PRIMARY KEY (user_id, job_posting_id),
    CONSTRAINT fk_user_applied_job_postings_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS user_applied (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    job_posting_id TEXT NOT NULL,
    CONSTRAINT fk_user_applied_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);
