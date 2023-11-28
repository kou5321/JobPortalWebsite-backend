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

CREATE TABLE subscription_preferences (
                                          id SERIAL PRIMARY KEY,
                                          preferred_year INT,
                                          preferred_location VARCHAR(255),
                                          user_id UUID REFERENCES users(id)
);

INSERT INTO users (email, username, password) VALUES ('ykou@ur.rochester.edu', 'root', '$2a$10$FhB4YizdMvDv2UOVniUEI.ARt.SOMqmvwvB0om8nyfD1WeIv0Mdmi');
INSERT INTO user_roles (user_id, role_id) VALUES ('24329298-0d77-4269-867a-8bda69d2d53e', 1); # this should be user_id

