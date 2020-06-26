ALTER TABLE `sentence`
DROP FOREIGN KEY `fk_grammar`;
ALTER TABLE `sentence`
ADD COLUMN `type_no` INT(2) NULL COMMENT '0: object_id is word_id\n1: object_id is grammar_id' AFTER `object_id`,
CHANGE COLUMN `grammar_id` `object_id` INT(11) NOT NULL ,
DROP INDEX `fk_grammar_idx` ;

update `sentence` set `type_no` = 0 where `type_no` is null;
