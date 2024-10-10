DROP PROCEDURE IF EXISTS drop_all_tables;

SET FOREIGN_KEY_CHECKS = 0;

DELIMITER $$
CREATE PROCEDURE drop_all_tables()
BEGIN
    DECLARE done INT DEFAULT FALSE;
    DECLARE tname VARCHAR(64);

    DECLARE table_cur CURSOR FOR SELECT table_name FROM information_schema.tables WHERE table_schema = 'webmapia_auth';

DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;
OPEN table_cur;

table_loop
:
    LOOP
        FETCH table_cur INTO tname;
        IF done THEN LEAVE table_loop; END IF;
        SET @tname_schema = CONCAT('webmapia_auth.', tname);
        SET @drop_table_sql = CONCAT('DROP TABLE IF EXISTS ', @tname_schema);
PREPARE stmt FROM @drop_table_sql;
EXECUTE stmt;
END LOOP;

CLOSE table_cur;
END;
$$

DELIMITER ;

CALL drop_all_tables();

SET FOREIGN_KEY_CHECKS = 1;

CREATE TABLE members
(
    member_id     VARCHAR(255) PRIMARY KEY,
    password      VARCHAR(255),
    role          ENUM ('MEMBER', 'ADMIN') DEFAULT 'MEMBER',
    nickname      VARCHAR(255),
    creation_time DATETIME                 DEFAULT NOW()
);