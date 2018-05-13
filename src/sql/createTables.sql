/* 
 * This learning ERP Database
 * 
 * 
 */
/**
 * Author:  Tikhonov Andrey
 * Created: 12.05.2018
 */

# use erpdb;
# IF NOT EXISTS
CREATE TABLE `erpdb`.`mfprinters` (
  `device_id` INT NOT NULL AUTO_INCREMENT,
  `device_name` VARCHAR(45) NOT NULL,
  `dealer` VARCHAR(45) NOT NULL,
  `location` VARCHAR(45) NOT NULL,
  `date` VARCHAR(20) NOT NULL,
  `state` VARCHAR(20) NOT NULL,
  `toner_cartridge` VARCHAR(45) NOT NULL,
  `drum_cartridge` VARCHAR(45) NOT NULL,
  `roller` VARCHAR(45) NOT NULL,
  `waste_toner_container` VARCHAR(45) NOT NULL,
  `notice` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`device_id`))
ENGINE=InnoDB DEFAULT CHARACTER SET = utf8 , COLLATE = utf8_general_ci;


CREATE TABLE `erpdb`.`mfprinters_log` (
  `action_id` INT(8) NOT NULL AUTO_INCREMENT,
  `action` VARCHAR(20) NOT NULL,
  `dealer` VARCHAR(45) NOT NULL,
  `date` VARCHAR(20) NOT NULL,
  `toner_cartridge` VARCHAR(45) NOT NULL,
  `drum_cartridge` VARCHAR(45) NOT NULL,
  `roller` VARCHAR(45) NOT NULL,
  `waste_toner_container` VARCHAR(45) NOT NULL,
  `count` INT(3) NOT NULL,
  `notice` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`action_id`))
ENGINE=InnoDB DEFAULT CHARACTER SET = utf8 , COLLATE = utf8_general_ci;

CREATE TABLE `erpdb`.`mf_spare_parts` (
  `device_id` INT(11) NOT NULL AUTO_INCREMENT,
  `device_name` VARCHAR(45) NOT NULL,
  `dealer` VARCHAR(45) NOT NULL,
  `location` VARCHAR(45) NOT NULL,
  `date` VARCHAR(20) NOT NULL,
  `state` VARCHAR(45) NOT NULL,
  `type` VARCHAR(45) NOT NULL,
  `notice` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`device_id`))
ENGINE=InnoDB DEFAULT CHARACTER SET = utf8 , COLLATE = utf8_general_ci;

