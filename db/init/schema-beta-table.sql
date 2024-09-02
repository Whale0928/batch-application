drop table if exists console_history;

CREATE TABLE console_history
(
    console_id BIGINT       NOT NULL PRIMARY KEY auto_increment,
    var1      VARCHAR(255) NOT NULL,
    var2      VARCHAR(255)  NOT NULL,
    created_at datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP

) ENGINE = InnoDB;



