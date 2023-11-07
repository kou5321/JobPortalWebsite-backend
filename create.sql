-- Create the main users table
CREATE TABLE users (
                       id UUID PRIMARY KEY,
                       email VARCHAR(30) NOT NULL UNIQUE,
                       password VARCHAR(200) NOT NULL,
                       username VARCHAR(30) NOT NULL UNIQUE
);

-- Create a table for applied job postings (one-to-many relationship table)
CREATE TABLE user_applied_job_postings (
                                           user_id UUID NOT NULL,
                                           job_posting_id VARCHAR NOT NULL,
                                           CONSTRAINT fk_user
                                               FOREIGN KEY(user_id)
                                                   REFERENCES users(id)
                                                   ON DELETE CASCADE
);

-- Create a table for viewed job postings (one-to-many relationship table)
CREATE TABLE user_viewed_job_postings (
                                          user_id UUID NOT NULL,
                                          job_posting_id VARCHAR NOT NULL,
                                          CONSTRAINT fk_user_viewed
                                              FOREIGN KEY(user_id)
                                                  REFERENCES users(id)
                                                  ON DELETE CASCADE
);

-- Create a table for user_applied entity
CREATE TABLE user_applied (
                              id UUID PRIMARY KEY,
                              user_id UUID NOT NULL,
                              job_posting_id VARCHAR NOT NULL,
                              CONSTRAINT fk_user_applied
                                  FOREIGN KEY(user_id)
                                      REFERENCES users(id)
                                      ON DELETE CASCADE
);
