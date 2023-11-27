CREATE TABLE roles (
    id BIGSERIAL PRIMARY KEY,
                       name VARCHAR(20)
);

CREATE TABLE users (
                       id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                       email VARCHAR(30) NOT NULL UNIQUE,
                       password VARCHAR(200) NOT NULL,
                       username VARCHAR(30) NOT NULL UNIQUE
);

CREATE TABLE user_applied (
                              id UUID PRIMARY KEY,
                              user_id UUID REFERENCES users(id),
                              job_posting_id VARCHAR NOT NULL
);

CREATE TABLE user_applied_job_postings (
                                           user_id UUID REFERENCES users(id),
                                           job_posting_id VARCHAR NOT NULL,
                                           PRIMARY KEY (user_id, job_posting_id)
);


CREATE TABLE user_viewed_job_postings (
                                          user_id UUID REFERENCES users(id),
                                          job_posting_id VARCHAR NOT NULL,
                                          PRIMARY KEY (user_id, job_posting_id)
);


CREATE TABLE user_roles (
                            user_id UUID REFERENCES users(id),
                            role_id BIGINT REFERENCES roles(id),
                            PRIMARY KEY (user_id, role_id)
);

CREATE TABLE email_subscriptions (
                                     id SERIAL PRIMARY KEY,
                                     email VARCHAR(255) NOT NULL UNIQUE
);
