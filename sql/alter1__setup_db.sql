CREATE TABLE user_profile
(
  id            UUID,
  full_name     VARCHAR(100) NOT NULL,
  date_of_birth DATE,
  point           INT          NOT NULL DEFAULT 1000,
  avatar        TEXT,
  created_date  timestamp    NOT NULL DEFAULT NOW(),
  updated_date  timestamp    NOT NULL DEFAULT NOW(),
  CONSTRAINT user_profile_pk PRIMARY KEY (id)
);

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE TABLE account_level
(
  id             UUID               DEFAULT uuid_generate_v4(),
  level          VARCHAR(50) UNIQUE,
  max_of_matches SMALLINT,
  created_date   timestamp NOT NULL DEFAULT NOW(),
  updated_date   timestamp NOT NULL DEFAULT NOW(),
  CONSTRAINT account_level_pk PRIMARY KEY (id)
);

INSERT INTO account_level(level, max_of_matches)
VALUES ('NORMAL', 1),
       ('SILVER', 3),
       ('GOLD', 5),
       ('DIAMOND', 9);

CREATE TABLE account
(
  id               UUID,
  username     VARCHAR(50) NOT NULL UNIQUE,
  password         TEXT        NOT NULL,
  user_profile_id  UUID,
  account_level_id UUID,
  created_date     timestamp   NOT NULL DEFAULT NOW(),
  updated_date     timestamp   NOT NULL DEFAULT NOW(),
  CONSTRAINT account_pk PRIMARY KEY (id),
  CONSTRAINT account__user_profile_fk FOREIGN KEY (user_profile_id) REFERENCES user_profile (id),
  CONSTRAINT account__account_level_fk FOREIGN KEY (account_level_id) REFERENCES account_level
);


CREATE TABLE question_type
(
  id           UUID               DEFAULT uuid_generate_v4(),
  name         VARCHAR(50) UNIQUE,
  description  VARCHAR(100),
  created_date timestamp NOT NULL DEFAULT NOW(),
  updated_date timestamp NOT NULL DEFAULT NOW(),
  CONSTRAINT question_type_pk PRIMARY KEY (id)
);

INSERT INTO question_type(name, description)
VALUES ('MULTI_CHOICE', 'Câu hỏi trắc nghiệm'),
       ('FILL_IN', 'Câu hỏi điền kết quả');

CREATE TABLE question_level
(
  id           UUID               DEFAULT uuid_generate_v4(),
  level        VARCHAR(50) UNIQUE,
  description  VARCHAR(100),
  created_date timestamp NOT NULL DEFAULT NOW(),
  updated_date timestamp NOT NULL DEFAULT NOW(),
  CONSTRAINT question_level_pk PRIMARY KEY (id)
);
INSERT INTO question_level(level, description)
VALUES ('OPENING', 'Khai cuộc'),
       ('MIDDLE', 'Trung cuộc'),
       ('ENDGAME', 'Tàn cuộc');

CREATE TABLE question
(
  id                UUID               DEFAULT uuid_generate_v4(),
  question          TEXT      NOT NULL,
  question_type_id  UUID,
  question_level_id UUID,
  answer            TEXT,
  true_answer       TEXT      NOT NULL,
  created_date      timestamp NOT NULL DEFAULT NOW(),
  updated_date      timestamp NOT NULL DEFAULT NOW(),
  CONSTRAINT question_pk PRIMARY KEY (id),
  CONSTRAINT question__question_type_fk FOREIGN KEY (question_type_id) REFERENCES question_type (id),
  CONSTRAINT question__question_level_fk FOREIGN KEY (question_level_id) REFERENCES question_level (id)
);

CREATE INDEX question_index
  ON question (id);

CREATE TABLE match
(
  id               UUID,
  player_one       UUID,
  player_two       UUID,
  player_one_score SMALLINT,
  player_two_score SMALLINT,
  winner           UUID,
  created_date     timestamp NOT NULL DEFAULT NOW(),
  updated_date     timestamp NOT NULL DEFAULT NOW(),
  CONSTRAINT match_pk PRIMARY KEY (id),
  CONSTRAINT match__account_fk1 FOREIGN KEY (player_one) REFERENCES account (id),
  CONSTRAINT match__account_fk2 FOREIGN KEY (player_two) REFERENCES account (id)
);

CREATE TABLE app_versioning
(
  id           UUID                 DEFAULT uuid_generate_v4(),
  app_type     varchar(10) NOT NULL,
  versioning   VARCHAR(10) NOT NULL,
  created_date timestamp   NOT NULL DEFAULT NOW(),
  updated_date timestamp   NOT NULL DEFAULT NOW(),
  CONSTRAINT app_versioning_pk PRIMARY KEY (id)
);
INSERT INTO app_versioning(app_type, versioning)
VALUES ('ANDROID', '1.0.0'),
       ('IOS', '1.0.0');
