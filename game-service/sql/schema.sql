DROP TABLE IF EXISTS character_assignment;
DROP TABLE IF EXISTS participation;
DROP TABLE IF EXISTS activated_skills;
DROP TABLE IF EXISTS vote;
DROP TABLE IF EXISTS game_instance;
DROP TABLE IF EXISTS game_room;

CREATE TABLE game_room
(
    room_id        INTEGER AUTO_INCREMENT,
    room_name      VARCHAR(255) NOT NULL,
    host_member_id VARCHAR(255) NOT NULL,
    creation_time  DATETIME     NOT NULL,
    PRIMARY KEY (room_id)
);

CREATE TABLE game_instance
(
    game_instance_id INTEGER AUTO_INCREMENT,
    room_id          INTEGER,
    round            INTEGER  NOT NULL CHECK (round > 0),
    start_time       DATETIME NOT NULL,
    end_time         DATETIME,
    game_phase       ENUM ('START', 'NIGHT', 'DAYTIME', 'DISCUSSION', 'VOTE'),
    PRIMARY KEY (game_instance_id)
);

ALTER TABLE game_instance
    ADD CONSTRAINT fk_game_instance_room_id FOREIGN KEY (room_id) REFERENCES game_room (room_id);

CREATE TABLE vote
(
    game_instance_id INTEGER      NOT NULL,
    round            INTEGER      NOT NULL,
    voter_id         VARCHAR(255) NOT NULL,
    target_id        VARCHAR(255) NOT NULL,
    vote_count       INTEGER      NOT NULL CHECK (vote_count > 0),
    PRIMARY KEY (game_instance_id, round, voter_id),
    FOREIGN KEY (game_instance_id) REFERENCES game_instance (game_instance_id)
);

CREATE TABLE activated_skills
(
    activated_skill_id INTEGER AUTO_INCREMENT,
    skill_type         VARCHAR(25),
    activator_id       VARCHAR(255),
    game_instance_id   INTEGER,
    round              INTEGER,
    PRIMARY KEY (activated_skill_id),
    FOREIGN KEY (game_instance_id) REFERENCES game_instance (game_instance_id)
);

CREATE TABLE participation
(
    participation_id INTEGER AUTO_INCREMENT,
    participant_id   VARCHAR(255),
    game_room_id     INTEGER NOT NULL,
    disconnected     BOOLEAN NOT NULL DEFAULT FALSE,
    PRIMARY KEY (participation_id),
    FOREIGN KEY (game_room_id) REFERENCES game_room (room_id)
);

CREATE TABLE character_assignment
(
    assignment_id    INTEGER PRIMARY KEY AUTO_INCREMENT,
    game_instance_id INTEGER      NOT NULL,
    member_id        VARCHAR(255) NOT NULL,
    character_code   ENUM ( 'WOLF', 'BETRAYER', 'FOLLOWER', 'PREDICTOR', 'GUARD', 'MEDIUMSHIP', 'DETECTIVE', 'SECRET_SOCIETY', 'NOBILITY', 'SOLDIER', 'TEMPLAR', 'CITIZEN', 'MURDERER', 'HUMAN_MOUSE' ),
    life             INTEGER      NOT NULL DEFAULT 1,
    FOREIGN KEY (game_instance_id) REFERENCES game_instance (game_instance_id)
);