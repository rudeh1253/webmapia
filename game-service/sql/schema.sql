DROP TABLE IF EXISTS activated_skill;
DROP TABLE IF EXISTS character_assignment;
DROP TABLE IF EXISTS participation;
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
    room_id          INTEGER                                                  NOT NULL,
    round            INTEGER                                                  NOT NULL CHECK (round > 0),
    start_time       DATETIME                                                 NOT NULL,
    end_time         DATETIME,
    game_phase       ENUM ('START', 'NIGHT', 'DAYTIME', 'DISCUSSION', 'VOTE') NOT NULL,
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
    character_code   INTEGER      NOT NULL,
    life             INTEGER DEFAULT 1,
    FOREIGN KEY (game_instance_id) REFERENCES game_instance (game_instance_id)
);

CREATE TABLE activated_skill
(
    activated_skill_id INTEGER AUTO_INCREMENT,
    skill_type         INTEGER,
    activator_id       INTEGER(255) NOT NULL,
    target_id          INTEGER(255),
    round              INTEGER      NOT NULL,
    PRIMARY KEY (activated_skill_id),
    FOREIGN KEY (activator_id) REFERENCES character_assignment (assignment_id),
    FOREIGN KEY (target_id) REFERENCES character_assignment (assignment_id)
);