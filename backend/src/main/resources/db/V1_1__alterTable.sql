ALTER TABLE `word_remember`
  DROP FOREIGN KEY `fk_word_id`;
ALTER TABLE `word_remember`
  ADD COLUMN `type_no` INT(2) NOT NULL AFTER `object_id`,
  CHANGE COLUMN `user_id` `user_id` INT(11) NOT NULL AFTER `type_no`,
  CHANGE COLUMN `word_id` `object_id` INT(11) NOT NULL ,
  DROP INDEX `fk_word_id_idx` ;
;

ALTER TABLE `word_remember`
  RENAME TO  `memory_detail` ;

UPDATE `memory_detail` SET type_no = 0;