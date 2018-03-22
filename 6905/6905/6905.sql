/*
Navicat MySQL Data Transfer

Source Server         : 111
Source Server Version : 50715
Source Host           : localhost:3306
Source Database       : 6905

Target Server Type    : MYSQL
Target Server Version : 50715
File Encoding         : 65001

Date: 2017-10-25 14:51:28
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `companyinfo`
-- ----------------------------
DROP TABLE IF EXISTS `companyinfo`;
CREATE TABLE `companyinfo` (
  `companyId` bigint(20) NOT NULL AUTO_INCREMENT,
  `companyName` varchar(255) COLLATE utf8_bin NOT NULL,
  `ownedJdsName` varchar(255) COLLATE utf8_bin NOT NULL,
  `companyManager` varchar(255) COLLATE utf8_bin NOT NULL,
  `leader` varchar(255) COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`companyId`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of companyinfo
-- ----------------------------
INSERT INTO `companyinfo` VALUES ('1', '代储企业1', '军代室1', '负责人', '领导');
INSERT INTO `companyinfo` VALUES ('2', '代储企业2', '军代室1', '负责人', '领导');
INSERT INTO `companyinfo` VALUES ('3', '代储企业3', '军代室1', '负责人', '领导');
INSERT INTO `companyinfo` VALUES ('4', '修改的代储企业4', '军代室1', '负责人', '领导');
INSERT INTO `companyinfo` VALUES ('5', '修改代储企业7', '军代室2', '负责人', '领导');
INSERT INTO `companyinfo` VALUES ('6', '代储企业8', '军代室2', '负责人', '领导');
INSERT INTO `companyinfo` VALUES ('7', '代储企业5', '军代室3', '负责人', '领导');

-- ----------------------------
-- Table structure for `jdjinfo`
-- ----------------------------
DROP TABLE IF EXISTS `jdjinfo`;
CREATE TABLE `jdjinfo` (
  `jdjId` bigint(20) NOT NULL AUTO_INCREMENT,
  `jdjName` varchar(255) COLLATE utf8_bin NOT NULL,
  `ownedZhjName` varchar(255) COLLATE utf8_bin NOT NULL,
  `jdjManager` varchar(255) COLLATE utf8_bin NOT NULL,
  `leader` varchar(255) COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`jdjId`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of jdjinfo
-- ----------------------------
INSERT INTO `jdjinfo` VALUES ('1', '军代局1', '指挥局1', '负责人', '领导');
INSERT INTO `jdjinfo` VALUES ('2', '军代局2', '指挥局2', '负责人', '领导');

-- ----------------------------
-- Table structure for `jdsinfo`
-- ----------------------------
DROP TABLE IF EXISTS `jdsinfo`;
CREATE TABLE `jdsinfo` (
  `jdsId` bigint(20) NOT NULL AUTO_INCREMENT,
  `jdsName` varchar(255) COLLATE utf8_bin NOT NULL,
  `ownedJdjName` varchar(255) COLLATE utf8_bin NOT NULL,
  `jdsManager` varchar(255) COLLATE utf8_bin NOT NULL,
  `leader` varchar(255) COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`jdsId`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of jdsinfo
-- ----------------------------
INSERT INTO `jdsinfo` VALUES ('1', '军代室1', '军代局1', '负责人', '领导');
INSERT INTO `jdsinfo` VALUES ('2', '军代室2', '军代局1', '负责人', '领导');
INSERT INTO `jdsinfo` VALUES ('3', '军代室3', '军代局2', '负责人', '领导');

-- ----------------------------
-- Table structure for `qy_9831`
-- ----------------------------
DROP TABLE IF EXISTS `qy_9831`;
CREATE TABLE `qy_9831` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `PMNM` varchar(255) DEFAULT NULL,
  `PMBM` varchar(255) DEFAULT NULL,
  `QCBM` varchar(255) DEFAULT NULL,
  `PMCS` varchar(255) DEFAULT NULL,
  `XHTH` varchar(255) DEFAULT NULL,
  `XLDJ` varchar(255) DEFAULT NULL,
  `XHDE` varchar(255) DEFAULT NULL,
  `JLDW` varchar(255) DEFAULT NULL,
  `MJYL` varchar(255) DEFAULT NULL,
  `QCXS` varchar(255) DEFAULT NULL,
  `BZZL` varchar(255) DEFAULT NULL,
  `BZJS` varchar(255) DEFAULT NULL,
  `BZTJ` varchar(255) DEFAULT NULL,
  `CKDJ` varchar(255) DEFAULT NULL,
  `SCCJNM` varchar(255) DEFAULT NULL,
  `GHDWNM` varchar(255) DEFAULT NULL,
  `ZBSX` varchar(255) DEFAULT NULL,
  `LBQF` varchar(255) DEFAULT NULL,
  `ZBBDSJ` varchar(255) DEFAULT NULL,
  `SYBZ` varchar(255) DEFAULT NULL,
  `YJDBZ` varchar(255) DEFAULT NULL,
  `SCBZ` varchar(255) DEFAULT NULL,
  `SCDXNF` varchar(255) DEFAULT NULL,
  `ownedUnit` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of qy_9831
-- ----------------------------
INSERT INTO `qy_9831` VALUES ('1', '1100000000', '1100000000', '0', 'TCL-179型野战跳频数字接力机（装备）', 'TCL-179', 'I', '0', '套', '0', '0', '301', '7', '1.37', '298700', '60000058', '60000058', '（无）', '整机', '', '0', '1', '0', '1999', null);
INSERT INTO `qy_9831` VALUES ('2', '1100000000', '1100000000', '1', 'A型套', 'A-09', 'Ⅵ', '0', '套', '0', '0', '0', '0', '0', '298700', '60000058', '60000058', '（无）', '专用维修器材', '', '0', '1', '0', '', null);
INSERT INTO `qy_9831` VALUES ('3', '1100000000', '1100000000', '5', '维修套（套件）', 'W09', 'Ⅴ', '0', '套', '0', '0', '0', '0', '0', '243804', '60000058', '60000058', '（无）', '专用维修器材', '', '0', '1', '0', '', null);
INSERT INTO `qy_9831` VALUES ('4', '1100000000', '1100000000', '10', 'B型套', 'B-09', 'Ⅴ', '0', '套', '0', '0', '0', '0', '0', '347060', '60000058', '60000058', '（无）', '专用维修器材', '', '0', '1', '0', '', null);

-- ----------------------------
-- Table structure for `qy_account`
-- ----------------------------
DROP TABLE IF EXISTS `qy_account`;
CREATE TABLE `qy_account` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '流水账的ID',
  `productId` bigint(20) DEFAULT NULL,
  `operateType` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `operateTime` datetime DEFAULT NULL,
  `userName` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `remark` text COLLATE utf8_bin,
  `ownedUnit` varchar(255) CHARACTER SET utf8 NOT NULL DEFAULT '' COMMENT '企业名',
  PRIMARY KEY (`id`,`ownedUnit`),
  UNIQUE KEY `company-unique` (`ownedUnit`,`id`),
  UNIQUE KEY `type-unique` (`productId`,`operateType`) COMMENT '每个产品的每种申请方式只能有一个',
  KEY `qy_account_operatetime` (`operateTime`)
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of qy_account
-- ----------------------------
INSERT INTO `qy_account` VALUES ('7', '7', '新入库', '2016-01-19 22:16:31', null, 0xE69CAAE794B3E8AFB72D2D3EE5B7B2E585A5E5BA93, '代储企业1');
INSERT INTO `qy_account` VALUES ('8', '8', '新入库', '2016-01-19 22:16:31', null, 0xE69CAAE794B3E8AFB72D2D3EE5B7B2E585A5E5BA93, '代储企业1');
INSERT INTO `qy_account` VALUES ('9', '1', '新入库', '2016-01-19 22:19:38', null, 0xE8BF9BE5BA93E5BE85E5AEA1E6A0B82D2D3EE5B7B2E585A5E5BA93, '代储企业1');
INSERT INTO `qy_account` VALUES ('10', '2', '新入库', '2016-01-19 22:19:38', null, 0xE8BF9BE5BA93E5BE85E5AEA1E6A0B82D2D3EE5B7B2E585A5E5BA93, '代储企业1');
INSERT INTO `qy_account` VALUES ('11', '4', '新入库', '2016-01-19 22:19:38', null, 0xE8BF9BE5BA93E5BE85E5AEA1E6A0B82D2D3EE5B7B2E585A5E5BA93, '代储企业1');
INSERT INTO `qy_account` VALUES ('12', '5', '新入库', '2016-01-19 22:19:38', null, 0xE8BF9BE5BA93E5BE85E5AEA1E6A0B82D2D3EE5B7B2E585A5E5BA93, '代储企业1');
INSERT INTO `qy_account` VALUES ('13', '6', '新入库', '2016-01-19 22:19:38', null, 0xE8BF9BE5BA93E5BE85E5AEA1E6A0B82D2D3EE5B7B2E585A5E5BA93, '代储企业1');
INSERT INTO `qy_account` VALUES ('14', '1', '轮换出库', '2016-01-19 23:01:24', null, 0xE8BDAEE68DA2E587BAE5BA93E5BE85E5AEA1E6A0B82D2D3EE5B7B2E587BAE5BA93, '代储企业1');
INSERT INTO `qy_account` VALUES ('15', '2', '轮换出库', '2016-01-19 23:01:24', null, 0xE8BDAEE68DA2E587BAE5BA93E5BE85E5AEA1E6A0B82D2D3EE5B7B2E587BAE5BA93, '代储企业1');
INSERT INTO `qy_account` VALUES ('16', '12', '轮换入库', '2016-01-19 22:36:22', null, 0xE8BDAEE68DA2E585A5E5BA93E5BE85E5AEA1E6A0B82D2D3EE5B7B2E585A5E5BA93, '代储企业1');
INSERT INTO `qy_account` VALUES ('17', '4', '调拨出库', '2016-01-19 22:52:05', null, 0xE5B7B2E585A5E5BA932D2D3EE5B7B2E587BAE5BA93, '代储企业1');
INSERT INTO `qy_account` VALUES ('18', '5', '调拨出库', '2016-01-19 23:00:27', null, 0xE5B7B2E585A5E5BA932D2D3EE5B7B2E587BAE5BA93, '代储企业1');
INSERT INTO `qy_account` VALUES ('19', '6', '调拨出库', '2016-01-19 23:00:27', null, 0xE5B7B2E585A5E5BA932D2D3EE5B7B2E587BAE5BA93, '代储企业1');
INSERT INTO `qy_account` VALUES ('20', '12', '调拨出库', '2016-01-19 23:00:27', null, 0xE5B7B2E585A5E5BA932D2D3EE5B7B2E587BAE5BA93, '代储企业1');
INSERT INTO `qy_account` VALUES ('27', '13', '轮换入库', '2016-01-20 08:08:21', null, 0xE8BDAEE68DA2E585A5E5BA93E5BE85E5AEA1E6A0B82D2D3EE5B7B2E585A5E5BA93, '代储企业1');

-- ----------------------------
-- Table structure for `qy_attach`
-- ----------------------------
DROP TABLE IF EXISTS `qy_attach`;
CREATE TABLE `qy_attach` (
  `attachId` bigint(9) NOT NULL AUTO_INCREMENT,
  `attachTitle` varchar(255) DEFAULT NULL,
  `attachPath` varchar(255) DEFAULT NULL,
  `fareId` bigint(255) DEFAULT NULL,
  PRIMARY KEY (`attachId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of qy_attach
-- ----------------------------

-- ----------------------------
-- Table structure for `qy_basedata`
-- ----------------------------
DROP TABLE IF EXISTS `qy_basedata`;
CREATE TABLE `qy_basedata` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `PMNM` varchar(255) DEFAULT NULL,
  `PMBM` varchar(255) DEFAULT NULL,
  `QCBM` varchar(255) DEFAULT NULL,
  `PMCS` varchar(255) DEFAULT NULL,
  `XHTH` varchar(255) DEFAULT NULL,
  `XLDJ` varchar(255) DEFAULT NULL,
  `XHDE` varchar(255) DEFAULT NULL,
  `JLDW` varchar(255) DEFAULT NULL,
  `MJYL` varchar(255) DEFAULT NULL,
  `QCXS` varchar(255) DEFAULT NULL,
  `BZZL` varchar(255) DEFAULT NULL,
  `BZJS` varchar(255) DEFAULT NULL,
  `BZTJ` varchar(255) DEFAULT NULL,
  `CKDJ` varchar(255) DEFAULT NULL,
  `SCCJNM` varchar(255) DEFAULT NULL,
  `GHDWNM` varchar(255) DEFAULT NULL,
  `ZBSX` varchar(255) DEFAULT NULL,
  `LBQF` varchar(255) DEFAULT NULL,
  `ZBBDSJ` varchar(255) DEFAULT NULL,
  `SYBZ` varchar(255) DEFAULT NULL,
  `YJDBZ` varchar(255) DEFAULT NULL,
  `SCBZ` varchar(255) DEFAULT NULL,
  `SCDXNF` varchar(255) DEFAULT NULL,
  `ownedUnit` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of qy_basedata
-- ----------------------------
INSERT INTO `qy_basedata` VALUES ('1', '1100000000', '1100000000', '0', 'TCL-179型野战跳频数字接力机（装备）', 'TCL-179', 'I', '0', '套', '0', '0', '301', '7', '1.37', '298700', '60000058', '60000058', '（无）', '整机', '', '0', '1', '0', '1999', '代储企业1');
INSERT INTO `qy_basedata` VALUES ('2', '1100000000', '1100000000', '1', 'A型套', 'A-09', 'Ⅵ', '0', '套', '0', '0', '0', '0', '0', '298700', '60000058', '60000058', '（无）', '专用维修器材', '', '0', '1', '0', '', '代储企业1');
INSERT INTO `qy_basedata` VALUES ('3', '1100000000', '1100000000', '5', '维修套（套件）', 'W09', 'Ⅴ', '0', '套', '0', '0', '0', '0', '0', '243804', '60000058', '60000058', '（无）', '专用维修器材', '', '0', '1', '0', '', '代储企业1');
INSERT INTO `qy_basedata` VALUES ('5', '1100000000', '1100000000', '10', 'B型套', 'B-09', 'Ⅴ', '0', '套', '0', '0', '0', '0', '0', '347060', '60000058', '60000058', '（无）', '专用维修器材', '', '0', '1', '0', '', '指挥局1');

-- ----------------------------
-- Table structure for `qy_contract`
-- ----------------------------
DROP TABLE IF EXISTS `qy_contract`;
CREATE TABLE `qy_contract` (
  `contractId` varchar(255) NOT NULL,
  `totalNumber` int(11) DEFAULT '0',
  `contractPrice` double DEFAULT '0',
  `JDS` varchar(255) DEFAULT '暂无',
  `signDate` date DEFAULT NULL,
  `attachment` varchar(255) DEFAULT '暂无',
  `buyer` varchar(255) DEFAULT '暂无',
  `ownedUnit` varchar(255) NOT NULL DEFAULT '' COMMENT '企业名',
  PRIMARY KEY (`contractId`,`ownedUnit`),
  UNIQUE KEY `myunique` (`contractId`,`ownedUnit`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of qy_contract
-- ----------------------------
INSERT INTO `qy_contract` VALUES ('HS002', '10000', '100000', '军代室1', '2016-01-20', null, '6905', '代储企业1');
INSERT INTO `qy_contract` VALUES ('test2016', '50', '80000', '1', '2016-09-14', null, '???', '????1');

-- ----------------------------
-- Table structure for `qy_data`
-- ----------------------------
DROP TABLE IF EXISTS `qy_data`;
CREATE TABLE `qy_data` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `filename` varchar(225) COLLATE utf8_bin DEFAULT NULL,
  `time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of qy_data
-- ----------------------------

-- ----------------------------
-- Table structure for `qy_deviceinfo`
-- ----------------------------
DROP TABLE IF EXISTS `qy_deviceinfo`;
CREATE TABLE `qy_deviceinfo` (
  `deviceid` int(9) NOT NULL AUTO_INCREMENT,
  `devicename` varchar(255) DEFAULT NULL,
  `deviceno` varchar(50) DEFAULT NULL,
  `position` varchar(255) DEFAULT NULL,
  `deviceintime` varchar(255) DEFAULT NULL,
  `repairtime` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`deviceid`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of qy_deviceinfo
-- ----------------------------
INSERT INTO `qy_deviceinfo` VALUES ('1', '空调', 'KT001', 'A区', '2016-01-19 10:00:00', '', '使用');

-- ----------------------------
-- Table structure for `qy_equipmentdetail`
-- ----------------------------
DROP TABLE IF EXISTS `qy_equipmentdetail`;
CREATE TABLE `qy_equipmentdetail` (
  `instoreYear` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  `productModel` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  `packageNumber` int(11) DEFAULT NULL,
  `volume` double DEFAULT NULL,
  `weight` double DEFAULT NULL,
  `manufacturer` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  `productPrice` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  `packageDescription` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  `matchingInstruction` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  `PMNM` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  `year` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  `month` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  `day` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  `listId` bigint(20) DEFAULT NULL,
  `synopsis` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  `balanceQuantity` int(11) DEFAULT NULL,
  `income` double DEFAULT NULL,
  `out` double DEFAULT NULL,
  `balance` double DEFAULT NULL,
  `remark` text CHARACTER SET utf8,
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `ownedUnit` varchar(255) CHARACTER SET utf8 NOT NULL DEFAULT '' COMMENT '企业名',
  PRIMARY KEY (`id`,`ownedUnit`),
  UNIQUE KEY `equip-unique` (`id`,`ownedUnit`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of qy_equipmentdetail
-- ----------------------------

-- ----------------------------
-- Table structure for `qy_fare`
-- ----------------------------
DROP TABLE IF EXISTS `qy_fare`;
CREATE TABLE `qy_fare` (
  `fareId` bigint(20) NOT NULL AUTO_INCREMENT,
  `fareType` varchar(255) DEFAULT NULL,
  `fareAmount` double(9,2) DEFAULT NULL,
  `storeCompany` varchar(255) NOT NULL,
  `jdRoom` varchar(255) DEFAULT NULL,
  `operateDate` varchar(255) DEFAULT NULL,
  `remark` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`fareId`,`storeCompany`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of qy_fare
-- ----------------------------
INSERT INTO `qy_fare` VALUES ('1', '运杂费', '1000.00', '代储企业1', '军代室1', '2016-01-19', '备注');

-- ----------------------------
-- Table structure for `qy_faredetail`
-- ----------------------------
DROP TABLE IF EXISTS `qy_faredetail`;
CREATE TABLE `qy_faredetail` (
  `fareDetailId` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `detailName` varchar(255) DEFAULT NULL,
  `detailAmount` double(9,2) DEFAULT NULL,
  `fareId` bigint(20) DEFAULT NULL,
  `detailTime` varchar(255) DEFAULT NULL,
  `voucherNo` varchar(255) DEFAULT NULL,
  `abstract` varchar(255) DEFAULT NULL,
  `remark` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`fareDetailId`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of qy_faredetail
-- ----------------------------
INSERT INTO `qy_faredetail` VALUES ('1', '运输', '1000.00', '1', '2015-12-10', 'PZ001', '运输费摘要', '运输费备注');

-- ----------------------------
-- Table structure for `qy_humidity`
-- ----------------------------
DROP TABLE IF EXISTS `qy_humidity`;
CREATE TABLE `qy_humidity` (
  `humidityId` bigint(255) NOT NULL AUTO_INCREMENT,
  `humidity` double(20,2) DEFAULT NULL,
  `curRecordDate` varchar(255) DEFAULT NULL,
  `position` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`humidityId`)
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of qy_humidity
-- ----------------------------
INSERT INTO `qy_humidity` VALUES ('1', '36.00', '2016-01-19 20:56:19', 'A区');
INSERT INTO `qy_humidity` VALUES ('2', '36.00', '2016-01-19 21:02:59', 'A区');
INSERT INTO `qy_humidity` VALUES ('3', '36.00', '2016-01-19 21:03:40', 'A区');
INSERT INTO `qy_humidity` VALUES ('4', '36.00', '2016-01-19 21:07:13', 'A区');
INSERT INTO `qy_humidity` VALUES ('5', '36.00', '2016-01-19 21:07:35', 'A区');
INSERT INTO `qy_humidity` VALUES ('6', '37.00', '2016-01-19 22:01:01', 'A区');
INSERT INTO `qy_humidity` VALUES ('7', '37.00', '2016-01-19 22:15:07', 'A区');
INSERT INTO `qy_humidity` VALUES ('8', '37.00', '2016-01-19 22:18:28', 'A区');
INSERT INTO `qy_humidity` VALUES ('9', '37.00', '2016-01-19 22:20:20', 'A区');
INSERT INTO `qy_humidity` VALUES ('10', '37.00', '2016-01-19 22:21:22', 'A区');
INSERT INTO `qy_humidity` VALUES ('11', '37.00', '2016-01-19 22:31:57', 'A区');
INSERT INTO `qy_humidity` VALUES ('12', '37.00', '2016-01-19 22:32:49', 'A区');
INSERT INTO `qy_humidity` VALUES ('13', '37.00', '2016-01-19 22:34:25', 'A区');
INSERT INTO `qy_humidity` VALUES ('14', '37.00', '2016-01-19 22:37:29', 'A区');
INSERT INTO `qy_humidity` VALUES ('15', '37.00', '2016-01-19 22:38:10', 'A区');
INSERT INTO `qy_humidity` VALUES ('16', '37.00', '2016-01-19 22:40:02', 'A区');
INSERT INTO `qy_humidity` VALUES ('17', '37.00', '2016-01-19 22:41:24', 'A区');
INSERT INTO `qy_humidity` VALUES ('18', '37.00', '2016-01-19 22:41:42', 'A区');
INSERT INTO `qy_humidity` VALUES ('19', '37.00', '2016-01-19 22:46:49', 'A区');
INSERT INTO `qy_humidity` VALUES ('20', '37.00', '2016-01-19 22:51:29', 'A区');
INSERT INTO `qy_humidity` VALUES ('21', '37.00', '2016-01-19 22:51:55', 'A区');
INSERT INTO `qy_humidity` VALUES ('22', '38.00', '2016-01-19 22:59:48', 'A区');
INSERT INTO `qy_humidity` VALUES ('23', '37.00', '2016-01-19 23:02:37', 'A区');

-- ----------------------------
-- Table structure for `qy_inapply`
-- ----------------------------
DROP TABLE IF EXISTS `qy_inapply`;
CREATE TABLE `qy_inapply` (
  `inId` bigint(20) NOT NULL AUTO_INCREMENT,
  `contractId` varchar(255) DEFAULT NULL,
  `inMeans` varchar(255) DEFAULT NULL,
  `ProductType` varchar(255) DEFAULT NULL,
  `oldType` varchar(255) DEFAULT NULL,
  `wholeName` varchar(255) DEFAULT NULL,
  `unitName` varchar(255) DEFAULT NULL,
  `batch` varchar(255) DEFAULT NULL,
  `deviceNo` varchar(255) DEFAULT NULL,
  `unit` varchar(255) DEFAULT NULL,
  `newPrice` double(9,2) DEFAULT NULL,
  `oldPrice` double(9,2) DEFAULT NULL,
  `num` int(11) DEFAULT NULL,
  `oldNum` int(11) DEFAULT NULL,
  `measure` varchar(255) DEFAULT NULL,
  `manufacturer` varchar(255) DEFAULT NULL,
  `keeper` varchar(255) DEFAULT '',
  `productCode` varchar(255) DEFAULT NULL,
  `PMNM` varchar(255) DEFAULT NULL,
  `location` varchar(255) DEFAULT NULL,
  `storageTime` varchar(255) DEFAULT NULL,
  `maintainCycle` varchar(255) DEFAULT NULL,
  `producedDate` date DEFAULT NULL,
  `execDate` datetime DEFAULT NULL,
  `remark` text,
  `chStatus` varchar(255) DEFAULT NULL,
  `checkPerson` varchar(255) DEFAULT NULL COMMENT '审核人',
  `ownedUnit` varchar(255) NOT NULL DEFAULT '',
  PRIMARY KEY (`inId`,`ownedUnit`),
  UNIQUE KEY `inapply-unique` (`inId`,`ownedUnit`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of qy_inapply
-- ----------------------------
INSERT INTO `qy_inapply` VALUES ('1', 'HS002', '新入库', null, null, null, null, '2016新入库', null, null, null, null, '5', null, null, null, null, null, null, null, null, null, null, '2016-01-19 17:47:55', null, '入库审核已通过', '军代室管理员', '代储企业1');
INSERT INTO `qy_inapply` VALUES ('2', 'HS002', '新入库', null, null, null, null, 'PC01', null, null, null, null, '2', null, null, null, null, null, null, null, null, null, null, '2016-01-19 22:12:41', null, '入库审核已通过', '军代室管理员', '代储企业1');
INSERT INTO `qy_inapply` VALUES ('3', null, '轮换入库', null, null, null, null, 'RKPC001', null, null, null, null, '1', null, null, null, null, null, null, null, null, null, null, '2016-01-19 22:35:04', null, '轮换入库审核已通过', '军代室管理员', '代储企业1');
INSERT INTO `qy_inapply` VALUES ('4', null, '轮换入库', null, null, null, null, 'PC01', null, null, null, null, '1', null, null, null, null, null, null, null, null, null, null, '2016-01-20 08:07:07', null, '轮换入库审核已通过', '军代室管理员', '代储企业1');

-- ----------------------------
-- Table structure for `qy_inproductrelation`
-- ----------------------------
DROP TABLE IF EXISTS `qy_inproductrelation`;
CREATE TABLE `qy_inproductrelation` (
  `inId` bigint(20) NOT NULL DEFAULT '0',
  `productId` bigint(20) NOT NULL DEFAULT '0',
  `ownedUnit` varchar(255) NOT NULL DEFAULT '' COMMENT '代储单位',
  `insertTime` date DEFAULT NULL COMMENT '插入时间',
  `deviceNo` varchar(255) NOT NULL DEFAULT '' COMMENT '机号',
  `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  PRIMARY KEY (`id`,`ownedUnit`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of qy_inproductrelation
-- ----------------------------
INSERT INTO `qy_inproductrelation` VALUES ('1', '1', '代储企业1', '2016-01-19', 'aa004', '1');
INSERT INTO `qy_inproductrelation` VALUES ('1', '2', '代储企业1', '2016-01-19', 'aa005', '2');
INSERT INTO `qy_inproductrelation` VALUES ('1', '4', '代储企业1', '2016-01-19', 'aa001', '3');
INSERT INTO `qy_inproductrelation` VALUES ('1', '5', '代储企业1', '2016-01-19', 'aa002', '4');
INSERT INTO `qy_inproductrelation` VALUES ('1', '6', '代储企业1', '2016-01-19', 'aa003', '5');
INSERT INTO `qy_inproductrelation` VALUES ('2', '7', '代储企业1', '2016-01-19', 'ABC001', '6');
INSERT INTO `qy_inproductrelation` VALUES ('2', '8', '代储企业1', '2016-01-19', 'ABC002', '7');
INSERT INTO `qy_inproductrelation` VALUES ('3', '12', '代储企业1', '2016-01-19', 'Z001', '8');
INSERT INTO `qy_inproductrelation` VALUES ('4', '13', '代储企业1', '2016-01-20', 'bb005', '9');

-- ----------------------------
-- Table structure for `qy_inspectrecord`
-- ----------------------------
DROP TABLE IF EXISTS `qy_inspectrecord`;
CREATE TABLE `qy_inspectrecord` (
  `inspectId` bigint(255) NOT NULL AUTO_INCREMENT,
  `unit` varchar(255) DEFAULT NULL,
  `date` date DEFAULT NULL,
  `site` varchar(500) DEFAULT NULL,
  `item` varchar(1000) DEFAULT NULL,
  `suggest` varchar(500) DEFAULT NULL,
  `feedback` varchar(255) DEFAULT NULL,
  `remark` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`inspectId`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of qy_inspectrecord
-- ----------------------------
INSERT INTO `qy_inspectrecord` VALUES ('1', '检查单位', '2016-01-01', '会议室1', '讨论', '讨论', '讨论', '备注');

-- ----------------------------
-- Table structure for `qy_log`
-- ----------------------------
DROP TABLE IF EXISTS `qy_log`;
CREATE TABLE `qy_log` (
  `logId` bigint(20) NOT NULL AUTO_INCREMENT,
  `productId` bigint(20) NOT NULL,
  `operateType` varchar(255) DEFAULT NULL,
  `operateTime` datetime DEFAULT NULL,
  `userName` varchar(255) DEFAULT NULL,
  `maintainType` varchar(255) DEFAULT NULL,
  `inspectPerson` varchar(255) DEFAULT NULL,
  `remark` text,
  `ownedUnit` varchar(255) NOT NULL DEFAULT '' COMMENT '代储单位名',
  PRIMARY KEY (`logId`,`ownedUnit`),
  UNIQUE KEY `log-unique` (`logId`,`ownedUnit`)
) ENGINE=InnoDB AUTO_INCREMENT=222 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of qy_log
-- ----------------------------
INSERT INTO `qy_log` VALUES ('1', '0', '登录', '2016-01-19 16:33:17', '军代室管理员', null, null, null, '');
INSERT INTO `qy_log` VALUES ('2', '0', '导入审核文件', '2016-01-19 16:33:57', '军代室管理员', null, null, '文件类型为：更新出库', '');
INSERT INTO `qy_log` VALUES ('3', '0', '导入审核文件', '2016-01-19 16:34:44', '军代室管理员', null, null, '文件类型为：轮换出库', '');
INSERT INTO `qy_log` VALUES ('4', '0', '登录', '2016-01-19 16:47:07', '6905管理员', null, null, null, '');
INSERT INTO `qy_log` VALUES ('5', '0', '新增合同', '2016-01-19 16:51:01', '6905管理员', null, null, '合同编号为：HS001', '');
INSERT INTO `qy_log` VALUES ('6', '0', '上传合同附件', '2016-01-19 16:52:30', '6905管理员', null, null, '合同编号为：null', '');
INSERT INTO `qy_log` VALUES ('7', '0', '编辑合同附件', '2016-01-19 16:53:07', '6905管理员', null, null, '合同信息 [合同编号：HS001, 订货数量：1000, 合同金额：1000000.0, 军代室：军代室1, 签订日期：2016-01-19, 采购单位：6905]', '');
INSERT INTO `qy_log` VALUES ('8', '0', '从9831库批量导入基础数据库', '2016-01-19 16:57:04', '6905管理员', null, null, null, '');
INSERT INTO `qy_log` VALUES ('9', '0', '登录', '2016-01-19 16:59:41', '6905管理员', null, null, null, '');
INSERT INTO `qy_log` VALUES ('10', '0', '从9831库批量导入基础数据库', '2016-01-19 17:00:13', '6905管理员', null, null, null, '');
INSERT INTO `qy_log` VALUES ('11', '0', '新增合同', '2016-01-19 17:06:24', '6905管理员', null, null, '合同编号为：HS002', '');
INSERT INTO `qy_log` VALUES ('12', '0', '登录', '2016-01-19 17:07:48', '6905管理员', null, null, null, '');
INSERT INTO `qy_log` VALUES ('13', '0', '从9831库批量导入基础数据库', '2016-01-19 17:08:43', '6905管理员', null, null, null, '');
INSERT INTO `qy_log` VALUES ('14', '0', '登录', '2016-01-19 17:09:22', '6905管理员', null, null, null, '');
INSERT INTO `qy_log` VALUES ('15', '0', '上传合同附件', '2016-01-19 17:10:59', '6905管理员', null, null, '合同编号为：null', '');
INSERT INTO `qy_log` VALUES ('16', '0', '编辑合同附件', '2016-01-19 17:11:10', '6905管理员', null, null, '合同信息 [合同编号：HS001, 订货数量：1000, 合同金额：1000000.0, 军代室：军代室1, 签订日期：2016-01-19, 采购单位：6905]', '');
INSERT INTO `qy_log` VALUES ('17', '0', '登录', '2016-01-19 17:15:14', '6905管理员', null, null, null, '');
INSERT INTO `qy_log` VALUES ('18', '0', '登录', '2016-01-19 17:17:26', '6905管理员', null, null, null, '');
INSERT INTO `qy_log` VALUES ('19', '0', '登录', '2016-01-19 17:19:25', '6905管理员', null, null, null, '');
INSERT INTO `qy_log` VALUES ('20', '0', '编辑合同', '2016-01-19 17:19:34', '6905管理员', null, null, '合同信息 [合同编号：HS001, 订货数量：1000, 合同金额：1000000.0, 军代室：军代室1, 签订日期：2016-01-19, 采购单位：6905]', '');
INSERT INTO `qy_log` VALUES ('21', '0', '登录', '2016-01-19 17:20:31', '6905管理员', null, null, null, '');
INSERT INTO `qy_log` VALUES ('22', '0', '删除一条基础数据库纪录', '2016-01-19 17:20:46', '6905管理员', null, null, null, '');
INSERT INTO `qy_log` VALUES ('23', '0', '登录', '2016-01-19 17:23:42', '6905管理员', null, null, null, '');
INSERT INTO `qy_log` VALUES ('24', '0', '从9831库批量导入基础数据库', '2016-01-19 17:25:16', '6905管理员', null, null, null, '');
INSERT INTO `qy_log` VALUES ('25', '0', '从9831库批量导入基础数据库', '2016-01-19 17:30:32', '6905管理员', null, null, null, '');
INSERT INTO `qy_log` VALUES ('26', '0', '登录', '2016-01-19 17:33:23', '6905管理员', null, null, null, '');
INSERT INTO `qy_log` VALUES ('27', '0', '登录', '2016-01-19 17:35:01', '6905管理员', null, null, null, '');
INSERT INTO `qy_log` VALUES ('28', '0', '登录', '2016-01-19 17:38:31', '6905管理员', null, null, null, '');
INSERT INTO `qy_log` VALUES ('29', '0', '登录', '2016-01-19 17:43:11', '6905管理员', null, null, null, '');
INSERT INTO `qy_log` VALUES ('30', '0', '新增产品', '2016-01-19 17:44:27', '6905管理员', null, null, '产品型号：TCL-179,单元：A型套', '');
INSERT INTO `qy_log` VALUES ('31', '0', '新增产品', '2016-01-19 17:44:27', '6905管理员', null, null, '产品型号：TCL-179,单元：', '');
INSERT INTO `qy_log` VALUES ('32', '0', '登录', '2016-01-19 17:46:33', '6905管理员', null, null, null, '');
INSERT INTO `qy_log` VALUES ('33', '0', '登录', '2016-01-19 17:46:34', '6905管理员', null, null, null, '');
INSERT INTO `qy_log` VALUES ('34', '0', '新入库申请', '2016-01-19 17:47:58', '6905管理员', null, null, '入库批次：2016新入库', '');
INSERT INTO `qy_log` VALUES ('35', '0', '导出文件', '2016-01-19 17:48:17', '6905管理员', null, null, '操作方式为：newIn', '');
INSERT INTO `qy_log` VALUES ('36', '0', '下载文件', '2016-01-19 17:48:21', '6905管理员', null, null, '文件名为：%E4%BC%81%E4%B8%9A%E7%94%B3%E8%AF%B7_%E6%96%B0%E5%85%A5%E5%BA%93_20160119174821.whms', '');
INSERT INTO `qy_log` VALUES ('37', '0', '登录', '2016-01-19 17:49:53', '6905管理员', null, null, null, '');
INSERT INTO `qy_log` VALUES ('38', '0', '登录', '2016-01-19 17:53:25', '6905管理员', null, null, null, '');
INSERT INTO `qy_log` VALUES ('39', '0', '登录', '2016-01-19 17:56:00', '6905管理员', null, null, null, '');
INSERT INTO `qy_log` VALUES ('40', '0', '登录', '2016-01-19 18:11:36', '6905管理员', null, null, null, '');
INSERT INTO `qy_log` VALUES ('41', '0', '登录', '2016-01-19 18:11:48', '指挥局管理员', null, null, null, '');
INSERT INTO `qy_log` VALUES ('42', '0', '修改一条基础数据库纪录', '2016-01-19 18:12:08', '指挥局管理员', null, null, null, '');
INSERT INTO `qy_log` VALUES ('43', '0', '删除一条基础数据库纪录', '2016-01-19 18:12:12', '指挥局管理员', null, null, null, '');
INSERT INTO `qy_log` VALUES ('44', '0', '删除一条单元纪录', '2016-01-19 18:12:27', '指挥局管理员', null, null, null, '');
INSERT INTO `qy_log` VALUES ('45', '0', '从9831库批量导入基础数据库', '2016-01-19 18:12:37', '指挥局管理员', null, null, null, '');
INSERT INTO `qy_log` VALUES ('46', '0', '登录', '2016-01-19 18:22:24', '指挥局管理员', null, null, null, '');
INSERT INTO `qy_log` VALUES ('47', '0', '登录', '2016-01-19 18:36:18', '6905管理员', null, null, null, '');
INSERT INTO `qy_log` VALUES ('48', '0', '登录', '2016-01-19 18:36:32', '指挥局管理员', null, null, null, '');
INSERT INTO `qy_log` VALUES ('49', '0', '登录', '2016-01-19 18:43:15', '指挥局管理员', null, null, null, '');
INSERT INTO `qy_log` VALUES ('50', '0', '登录', '2016-01-19 18:46:23', '6905管理员', null, null, null, '');
INSERT INTO `qy_log` VALUES ('51', '0', '登录', '2016-01-19 18:55:31', '6905管理员', null, null, null, '');
INSERT INTO `qy_log` VALUES ('52', '0', '登录', '2016-01-19 19:17:15', '6905管理员', null, null, null, '');
INSERT INTO `qy_log` VALUES ('53', '0', '登录', '2016-01-19 19:59:45', '6905管理员', null, null, null, '');
INSERT INTO `qy_log` VALUES ('54', '0', '登录', '2016-01-19 20:00:53', '6905管理员', null, null, null, '');
INSERT INTO `qy_log` VALUES ('55', '0', '登录', '2016-01-19 20:37:57', '6905管理员', null, null, null, '');
INSERT INTO `qy_log` VALUES ('56', '0', '登录', '2016-01-19 20:56:28', '6905管理员', null, null, null, '');
INSERT INTO `qy_log` VALUES ('57', '0', '添加设备记录', '2016-01-19 20:57:43', '6905管理员', null, null, '设备名：空调，设备编号：KT001', '');
INSERT INTO `qy_log` VALUES ('58', '0', '添加一条经费记录', '2016-01-19 20:59:28', '6905管理员', null, null, '添加的经费信息概要：费用类型：运杂费；费用金额：1000.0；代储企业：代储企业1；军代室：军代室1；操作日期：2016-01-19；备注：备注', '');
INSERT INTO `qy_log` VALUES ('59', '0', '登录', '2016-01-19 21:02:17', '6905管理员', null, null, null, '');
INSERT INTO `qy_log` VALUES ('60', '0', '登录', '2016-01-19 21:04:34', '指挥局管理员', null, null, null, '');
INSERT INTO `qy_log` VALUES ('61', '0', '填写发料单', '2016-01-19 21:04:56', '指挥局管理员', null, null, '调拨出库发料单', '');
INSERT INTO `qy_log` VALUES ('62', '0', '填写发料单', '2016-01-19 21:05:18', '指挥局管理员', null, null, '调拨出库发料单', '');
INSERT INTO `qy_log` VALUES ('63', '0', '填写发料单', '2016-01-19 21:05:21', '指挥局管理员', null, null, '轮换出库发料单', '');
INSERT INTO `qy_log` VALUES ('64', '0', '填写发料单', '2016-01-19 21:05:27', '指挥局管理员', null, null, '更新出库发料单', '');
INSERT INTO `qy_log` VALUES ('65', '0', '登录', '2016-01-19 21:07:06', '6905管理员', null, null, null, '');
INSERT INTO `qy_log` VALUES ('66', '0', '登录', '2016-01-19 21:07:28', '6905管理员', null, null, null, '');
INSERT INTO `qy_log` VALUES ('67', '0', '登录', '2016-01-19 21:17:51', '6905管理员', null, null, null, '');
INSERT INTO `qy_log` VALUES ('68', '0', '登录', '2016-01-19 21:55:39', '6905管理员', null, null, null, '');
INSERT INTO `qy_log` VALUES ('69', '0', '登录', '2016-01-19 22:02:14', '6905管理员', null, null, null, '');
INSERT INTO `qy_log` VALUES ('70', '0', '新增产品', '2016-01-19 22:06:00', '6905管理员', null, null, '产品型号：TCL-179,单元：B型套', '');
INSERT INTO `qy_log` VALUES ('71', '0', '新入库申请', '2016-01-19 22:12:41', '6905管理员', null, null, '入库批次：PC01', '');
INSERT INTO `qy_log` VALUES ('72', '0', '导出文件', '2016-01-19 22:13:21', '6905管理员', null, null, '操作方式为：newIn', '');
INSERT INTO `qy_log` VALUES ('73', '0', '下载文件', '2016-01-19 22:13:23', '6905管理员', null, null, '文件名为：%E4%BC%81%E4%B8%9A%E7%94%B3%E8%AF%B7_%E6%96%B0%E5%85%A5%E5%BA%93_20160119221323.whms', '');
INSERT INTO `qy_log` VALUES ('74', '0', '登录', '2016-01-19 22:15:22', '军代室管理员', null, null, null, '');
INSERT INTO `qy_log` VALUES ('75', '0', '导入审核文件', '2016-01-19 22:15:39', '军代室管理员', null, null, '文件类型为：新入库', '');
INSERT INTO `qy_log` VALUES ('76', '0', '审核文件', '2016-01-19 22:16:45', '军代室管理员', null, null, '审核入库申请文件', '');
INSERT INTO `qy_log` VALUES ('77', '0', '审核文件', '2016-01-19 22:16:58', '军代室管理员', null, null, '审核入库申请文件', '');
INSERT INTO `qy_log` VALUES ('78', '0', '审核文件', '2016-01-19 22:17:07', '军代室管理员', null, null, '审核入库申请文件', '');
INSERT INTO `qy_log` VALUES ('79', '0', '审核文件', '2016-01-19 22:17:25', '军代室管理员', null, null, '审核入库申请文件', '');
INSERT INTO `qy_log` VALUES ('80', '0', '审核文件', '2016-01-19 22:17:30', '军代室管理员', null, null, '审核入库申请文件', '');
INSERT INTO `qy_log` VALUES ('81', '0', '导入审核文件', '2016-01-19 22:17:41', '军代室管理员', null, null, '文件类型为：新入库', '');
INSERT INTO `qy_log` VALUES ('82', '0', '审核文件', '2016-01-19 22:17:51', '军代室管理员', null, null, '审核入库申请文件', '');
INSERT INTO `qy_log` VALUES ('83', '0', '审核文件', '2016-01-19 22:17:58', '军代室管理员', null, null, '审核入库申请文件', '');
INSERT INTO `qy_log` VALUES ('84', '0', '导出文件', '2016-01-19 22:18:06', '军代室管理员', null, null, '操作方式为：newIn', '');
INSERT INTO `qy_log` VALUES ('85', '0', '下载文件', '2016-01-19 22:18:06', '军代室管理员', null, null, '文件名为：%E5%86%9B%E4%BB%A3%E5%AE%A4%E5%AE%A1%E6%A0%B8_%E6%96%B0%E5%85%A5%E5%BA%93_20160119221806.whms', '');
INSERT INTO `qy_log` VALUES ('86', '0', '登录', '2016-01-19 22:18:33', '6905管理员', null, null, null, '');
INSERT INTO `qy_log` VALUES ('87', '0', '入库申请', '2016-01-19 22:18:45', '6905管理员', null, null, '导入入库申请excel表', '');
INSERT INTO `qy_log` VALUES ('88', '0', '导出文件', '2016-01-19 22:19:59', '6905管理员', null, null, '操作方式为：newIn', '');
INSERT INTO `qy_log` VALUES ('89', '0', '下载文件', '2016-01-19 22:20:00', '6905管理员', null, null, '文件名为：%E4%BC%81%E4%B8%9A%E7%94%B3%E8%AF%B7_%E6%96%B0%E5%85%A5%E5%BA%93_20160119222000.whms', '');
INSERT INTO `qy_log` VALUES ('90', '0', '登录', '2016-01-19 22:20:44', '军代室管理员', null, null, null, '');
INSERT INTO `qy_log` VALUES ('91', '0', '导入审核文件', '2016-01-19 22:21:02', '军代室管理员', null, null, '文件类型为：新入库', '');
INSERT INTO `qy_log` VALUES ('92', '0', '审核文件', '2016-01-19 22:21:05', '军代室管理员', null, null, '审核入库申请文件', '');
INSERT INTO `qy_log` VALUES ('93', '0', '登录', '2016-01-19 22:21:22', '6905管理员', null, null, null, '');
INSERT INTO `qy_log` VALUES ('94', '0', '添加申请', '2016-01-19 22:29:14', '6905管理员', null, null, '轮换出库申请', '');
INSERT INTO `qy_log` VALUES ('95', '0', '登录', '2016-01-19 22:30:24', '6905管理员', null, null, null, '');
INSERT INTO `qy_log` VALUES ('96', '0', '导出文件', '2016-01-19 22:31:33', '6905管理员', null, null, '操作方式为：circleOut', '');
INSERT INTO `qy_log` VALUES ('97', '0', '下载文件', '2016-01-19 22:31:35', '6905管理员', null, null, '文件名为：%E4%BC%81%E4%B8%9A%E7%94%B3%E8%AF%B7_%E8%BD%AE%E6%8D%A2%E5%87%BA%E5%BA%93_20160119223135.whms', '');
INSERT INTO `qy_log` VALUES ('98', '0', '登录', '2016-01-19 22:31:57', '军代室管理员', null, null, null, '');
INSERT INTO `qy_log` VALUES ('99', '0', '导入审核文件', '2016-01-19 22:32:08', '军代室管理员', null, null, '文件类型为：轮换出库', '');
INSERT INTO `qy_log` VALUES ('100', '0', '审核文件', '2016-01-19 22:32:17', '军代室管理员', null, null, '审核出库申请文件', '');
INSERT INTO `qy_log` VALUES ('101', '0', '导出文件', '2016-01-19 22:32:18', '军代室管理员', null, null, '操作方式为：circleOut', '');
INSERT INTO `qy_log` VALUES ('102', '0', '下载文件', '2016-01-19 22:32:19', '军代室管理员', null, null, '文件名为：%E5%86%9B%E4%BB%A3%E5%AE%A4%E5%AE%A1%E6%A0%B8_%E8%BD%AE%E6%8D%A2%E5%87%BA%E5%BA%93_20160119223219.whms', '');
INSERT INTO `qy_log` VALUES ('103', '0', '登录', '2016-01-19 22:32:49', '6905管理员', null, null, null, '');
INSERT INTO `qy_log` VALUES ('104', '0', '登录', '2016-01-19 22:34:46', '6905管理员', null, null, null, '');
INSERT INTO `qy_log` VALUES ('105', '0', '导入', '2016-01-19 22:34:58', '6905管理员', null, null, '轮换审核文件导入', '');
INSERT INTO `qy_log` VALUES ('106', '0', '填写申请', '2016-01-19 22:36:31', '6905管理员', null, null, '填写轮换入库申请', '');
INSERT INTO `qy_log` VALUES ('107', '0', '导出文件', '2016-01-19 22:37:10', '6905管理员', null, null, '操作方式为：circleIn', '');
INSERT INTO `qy_log` VALUES ('108', '0', '下载文件', '2016-01-19 22:37:10', '6905管理员', null, null, '文件名为：%E4%BC%81%E4%B8%9A%E7%94%B3%E8%AF%B7_%E8%BD%AE%E6%8D%A2%E5%85%A5%E5%BA%93_20160119223710.whms', '');
INSERT INTO `qy_log` VALUES ('109', '0', '登录', '2016-01-19 22:37:32', '军代室管理员', null, null, null, '');
INSERT INTO `qy_log` VALUES ('110', '0', '导入审核文件', '2016-01-19 22:37:42', '军代室管理员', null, null, '文件类型为：轮换入库', '');
INSERT INTO `qy_log` VALUES ('111', '0', '审核文件', '2016-01-19 22:37:49', '军代室管理员', null, null, '审核入库申请文件', '');
INSERT INTO `qy_log` VALUES ('112', '0', '导出文件', '2016-01-19 22:37:51', '军代室管理员', null, null, '操作方式为：circleIn', '');
INSERT INTO `qy_log` VALUES ('113', '0', '下载文件', '2016-01-19 22:37:52', '军代室管理员', null, null, '文件名为：%E5%86%9B%E4%BB%A3%E5%AE%A4%E5%AE%A1%E6%A0%B8_%E8%BD%AE%E6%8D%A2%E5%85%A5%E5%BA%93_20160119223752.whms', '');
INSERT INTO `qy_log` VALUES ('114', '0', '登录', '2016-01-19 22:38:12', '6905管理员', null, null, null, '');
INSERT INTO `qy_log` VALUES ('115', '0', '导入', '2016-01-19 22:38:24', '6905管理员', null, null, '轮换审核文件导入', '');
INSERT INTO `qy_log` VALUES ('116', '0', '登录', '2016-01-19 22:40:17', '军代室管理员', null, null, null, '');
INSERT INTO `qy_log` VALUES ('117', '0', '登录', '2016-01-19 22:40:14', '6905管理员', null, null, null, '');
INSERT INTO `qy_log` VALUES ('118', '0', '填写发料单', '2016-01-19 22:40:30', '军代室管理员', null, null, '调拨出库发料单', '');
INSERT INTO `qy_log` VALUES ('119', '0', '登录', '2016-01-19 22:41:52', '军代室管理员', null, null, null, '');
INSERT INTO `qy_log` VALUES ('120', '0', '登录', '2016-01-19 22:41:55', '军代室管理员', null, null, null, '');
INSERT INTO `qy_log` VALUES ('121', '0', '填写发料单', '2016-01-19 22:41:58', '军代室管理员', null, null, '调拨出库发料单', '');
INSERT INTO `qy_log` VALUES ('122', '0', '填写发料单', '2016-01-19 22:42:01', '军代室管理员', null, null, '调拨出库发料单', '');
INSERT INTO `qy_log` VALUES ('123', '0', '登录', '2016-01-19 22:43:22', '军代室管理员', null, null, null, '');
INSERT INTO `qy_log` VALUES ('124', '0', '填写发料单', '2016-01-19 22:43:25', '军代室管理员', null, null, '调拨出库发料单', '');
INSERT INTO `qy_log` VALUES ('125', '0', '登录', '2016-01-19 22:47:02', '6905管理员', null, null, null, '');
INSERT INTO `qy_log` VALUES ('126', '0', '登录', '2016-01-19 22:47:08', '军代室管理员', null, null, null, '');
INSERT INTO `qy_log` VALUES ('127', '0', '填写发料单', '2016-01-19 22:47:14', '军代室管理员', null, null, '调拨出库发料单', '');
INSERT INTO `qy_log` VALUES ('128', '0', '填写发料单', '2016-01-19 22:47:54', '军代室管理员', null, null, '调拨出库发料单', '');
INSERT INTO `qy_log` VALUES ('129', '0', '填写发料单', '2016-01-19 22:48:35', '军代室管理员', null, null, '调拨出库发料单', '');
INSERT INTO `qy_log` VALUES ('130', '0', '填写发料单', '2016-01-19 22:49:22', '军代室管理员', null, null, '调拨出库发料单', '');
INSERT INTO `qy_log` VALUES ('131', '0', '导出', '2016-01-19 22:50:45', '军代室管理员', null, null, '导出出库发料单', '');
INSERT INTO `qy_log` VALUES ('132', '0', '登录', '2016-01-19 22:52:03', '6905管理员', null, null, null, '');
INSERT INTO `qy_log` VALUES ('133', '0', '登录', '2016-01-19 22:52:05', '6905管理员', null, null, null, '');
INSERT INTO `qy_log` VALUES ('134', '0', '导入发料单', '2016-01-19 22:52:19', '6905管理员', null, null, '增加或者更新发料单', '');
INSERT INTO `qy_log` VALUES ('135', '0', '导入发料单', '2016-01-19 22:52:19', '6905管理员', null, null, '', '');
INSERT INTO `qy_log` VALUES ('136', '0', '登录', '2016-01-19 22:54:05', '6905管理员', null, null, null, '');
INSERT INTO `qy_log` VALUES ('137', '0', '登录', '2016-01-19 22:59:55', '军代局管理员', null, null, null, '');
INSERT INTO `qy_log` VALUES ('138', '0', '上报备份数据', '2016-01-19 23:00:10', '军代局管理员', null, null, '', '');
INSERT INTO `qy_log` VALUES ('139', '0', '上报备份数据', '2016-01-19 23:00:31', '军代局管理员', null, null, '', '');
INSERT INTO `qy_log` VALUES ('140', '0', '上报备份数据', '2016-01-19 23:00:36', '军代局管理员', null, null, '', '');
INSERT INTO `qy_log` VALUES ('141', '0', '导入审核文件', '2016-01-19 23:01:31', '军代局管理员', null, null, '文件类型为：轮换出库', '');
INSERT INTO `qy_log` VALUES ('142', '0', '导入发料单', '2016-01-19 23:01:54', '军代局管理员', null, null, '', '');
INSERT INTO `qy_log` VALUES ('143', '0', '登录', '2016-01-19 23:02:43', '指挥局管理员', null, null, null, '');
INSERT INTO `qy_log` VALUES ('144', '0', '上报备份数据', '2016-01-19 23:02:50', '指挥局管理员', null, null, '', '');
INSERT INTO `qy_log` VALUES ('145', '0', '导入审核文件', '2016-01-19 23:02:50', '指挥局管理员', null, null, '文件类型为：轮换出库', '');
INSERT INTO `qy_log` VALUES ('146', '0', '填写发料单', '2016-01-19 23:03:37', '指挥局管理员', null, null, '调拨出库发料单', '');
INSERT INTO `qy_log` VALUES ('147', '0', '填写发料单', '2016-01-19 23:03:53', '指挥局管理员', null, null, '轮换出库发料单', '');
INSERT INTO `qy_log` VALUES ('148', '0', '填写发料单', '2016-01-19 23:05:18', '指挥局管理员', null, null, '轮换出库发料单', '');
INSERT INTO `qy_log` VALUES ('149', '0', '登录', '2016-01-20 08:00:18', '6905管理员', null, null, null, '');
INSERT INTO `qy_log` VALUES ('150', '0', '填写申请', '2016-01-20 08:08:30', '6905管理员', null, null, '填写轮换入库申请', '');
INSERT INTO `qy_log` VALUES ('151', '0', '导出文件', '2016-01-20 08:08:42', '6905管理员', null, null, '操作方式为：circleIn', '');
INSERT INTO `qy_log` VALUES ('152', '0', '下载文件', '2016-01-20 08:08:44', '6905管理员', null, null, '文件名为：%E4%BC%81%E4%B8%9A%E7%94%B3%E8%AF%B7_%E8%BD%AE%E6%8D%A2%E5%85%A5%E5%BA%93_20160120080844.whms', '');
INSERT INTO `qy_log` VALUES ('153', '0', '登录', '2016-01-20 08:09:25', '军代室管理员', null, null, null, '');
INSERT INTO `qy_log` VALUES ('154', '0', '导入审核文件', '2016-01-20 08:09:41', '军代室管理员', null, null, '文件类型为：轮换入库', '');
INSERT INTO `qy_log` VALUES ('155', '0', '审核文件', '2016-01-20 08:09:44', '军代室管理员', null, null, '审核入库申请文件', '');
INSERT INTO `qy_log` VALUES ('156', '0', '导出文件', '2016-01-20 08:09:48', '军代室管理员', null, null, '操作方式为：circleIn', '');
INSERT INTO `qy_log` VALUES ('157', '0', '下载文件', '2016-01-20 08:09:48', '军代室管理员', null, null, '文件名为：%E5%86%9B%E4%BB%A3%E5%AE%A4%E5%AE%A1%E6%A0%B8_%E8%BD%AE%E6%8D%A2%E5%85%A5%E5%BA%93_20160120080948.whms', '');
INSERT INTO `qy_log` VALUES ('158', '0', '登录', '2016-01-20 08:10:38', '6905管理员', null, null, null, '');
INSERT INTO `qy_log` VALUES ('159', '0', '登录', '2016-01-20 21:58:51', '军代室管理员', null, null, null, '');
INSERT INTO `qy_log` VALUES ('160', '0', '登录', '2016-01-20 22:00:37', '军代室管理员', null, null, null, '');
INSERT INTO `qy_log` VALUES ('161', '0', '登录', '2016-01-20 22:03:01', '6905管理员', null, null, null, '');
INSERT INTO `qy_log` VALUES ('162', '0', '登录', '2016-01-20 22:04:33', '6905管理员', null, null, null, '');
INSERT INTO `qy_log` VALUES ('163', '0', '登录', '2016-01-20 22:04:33', '', null, null, null, '');
INSERT INTO `qy_log` VALUES ('164', '0', '登录', '2016-01-20 22:04:33', '', null, null, null, '');
INSERT INTO `qy_log` VALUES ('165', '0', '登录', '2016-01-20 22:04:33', '6905管理员', null, null, null, '');
INSERT INTO `qy_log` VALUES ('166', '0', '登录', '2016-01-20 22:06:26', '6905管理员', null, null, null, '');
INSERT INTO `qy_log` VALUES ('167', '0', '登录', '2016-01-20 22:12:31', '6905管理员', null, null, null, '');
INSERT INTO `qy_log` VALUES ('168', '0', '登录', '2016-01-20 22:20:16', '6905管理员', null, null, null, '');
INSERT INTO `qy_log` VALUES ('169', '0', '登录', '2016-01-21 21:21:09', '6905管理员', null, null, null, '');
INSERT INTO `qy_log` VALUES ('170', '0', '登录', '2016-01-21 21:24:36', '6905管理员', null, null, null, '');
INSERT INTO `qy_log` VALUES ('171', '0', '登录', '2016-01-22 10:53:42', '6905管理员', null, null, null, '');
INSERT INTO `qy_log` VALUES ('172', '0', '登录', '2016-01-22 10:53:44', '6905管理员', null, null, null, '');
INSERT INTO `qy_log` VALUES ('173', '0', '登录', '2016-01-22 10:54:19', '6905管理员', null, null, null, '');
INSERT INTO `qy_log` VALUES ('174', '0', '登录', '2016-01-22 11:14:49', '6905管理员', null, null, null, '');
INSERT INTO `qy_log` VALUES ('175', '0', '登录', '2016-01-22 11:31:12', '6905管理员', null, null, null, '');
INSERT INTO `qy_log` VALUES ('176', '0', '新增产品', '2016-01-22 11:41:18', '6905管理员', null, null, '产品型号：TCL-179,单元：A型套', '');
INSERT INTO `qy_log` VALUES ('177', '0', '登录', '2016-01-22 11:44:18', '6905管理员', null, null, null, '');
INSERT INTO `qy_log` VALUES ('178', '0', '登录', '2016-01-22 13:52:39', '6905管理员', null, null, null, '');
INSERT INTO `qy_log` VALUES ('179', '0', '登录', '2016-01-22 14:10:58', '6905管理员', null, null, null, '');
INSERT INTO `qy_log` VALUES ('180', '0', '登录', '2016-01-22 14:21:33', '6905管理员', null, null, null, '');
INSERT INTO `qy_log` VALUES ('181', '0', '登录', '2016-01-22 14:22:41', '6905管理员', null, null, null, '');
INSERT INTO `qy_log` VALUES ('182', '0', '登录', '2016-09-09 10:54:35', '6905管理员', null, null, null, '');
INSERT INTO `qy_log` VALUES ('183', '0', '??', '2016-09-10 17:23:35', '6905???', null, null, null, '');
INSERT INTO `qy_log` VALUES ('184', '0', '??', '2016-09-10 17:26:13', '6905???', null, null, null, '');
INSERT INTO `qy_log` VALUES ('185', '0', '??', '2016-09-11 09:29:15', '6905???', null, null, null, '');
INSERT INTO `qy_log` VALUES ('186', '0', '??', '2016-09-11 09:29:29', '6905???', null, null, null, '');
INSERT INTO `qy_log` VALUES ('187', '0', '??', '2016-09-11 09:58:00', '6905???', null, null, null, '');
INSERT INTO `qy_log` VALUES ('188', '0', '??', '2016-09-11 10:05:46', '6905???', null, null, null, '');
INSERT INTO `qy_log` VALUES ('189', '0', '??', '2016-09-11 10:08:40', '6905???', null, null, null, '');
INSERT INTO `qy_log` VALUES ('190', '0', '??', '2016-09-11 10:17:37', '6905???', null, null, null, '');
INSERT INTO `qy_log` VALUES ('191', '0', '??', '2016-09-11 10:20:02', '6905???', null, null, null, '');
INSERT INTO `qy_log` VALUES ('192', '0', '??', '2016-09-11 10:23:08', '6905???', null, null, null, '');
INSERT INTO `qy_log` VALUES ('193', '0', '??', '2016-09-11 10:34:06', '6905???', null, null, null, '');
INSERT INTO `qy_log` VALUES ('194', '0', '??', '2016-09-11 11:06:57', '6905???', null, null, null, '');
INSERT INTO `qy_log` VALUES ('195', '0', '??', '2016-09-12 20:11:42', '6905???', null, null, null, '');
INSERT INTO `qy_log` VALUES ('196', '0', '??', '2016-09-12 20:13:06', '6905???', null, null, null, '');
INSERT INTO `qy_log` VALUES ('197', '0', '????', '2016-09-12 20:40:47', '6905???', null, null, '??????test2016', '');
INSERT INTO `qy_log` VALUES ('198', '0', '??', '2016-09-12 21:00:26', '6905???', null, null, null, '');
INSERT INTO `qy_log` VALUES ('199', '0', '??', '2016-09-12 21:29:16', '6905???', null, null, null, '');
INSERT INTO `qy_log` VALUES ('200', '0', '??', '2016-09-12 21:37:37', '6905???', null, null, null, '');
INSERT INTO `qy_log` VALUES ('201', '0', '??', '2016-09-12 21:44:19', '6905???', null, null, null, '');
INSERT INTO `qy_log` VALUES ('202', '0', '??', '2016-09-12 21:48:50', '6905???', null, null, null, '');
INSERT INTO `qy_log` VALUES ('203', '0', '??', '2016-09-13 10:36:11', '6905???', null, null, null, '');
INSERT INTO `qy_log` VALUES ('204', '0', '??', '2016-09-13 21:25:56', '6905???', null, null, null, '');
INSERT INTO `qy_log` VALUES ('205', '0', '登录', '2016-10-12 16:56:16', '6905管理员', null, null, null, '');
INSERT INTO `qy_log` VALUES ('206', '0', '登录', '2016-10-19 15:10:37', '6905管理员', null, null, null, '');
INSERT INTO `qy_log` VALUES ('207', '0', '登录', '2016-10-19 15:15:40', '6905管理员', null, null, null, '');
INSERT INTO `qy_log` VALUES ('208', '0', '登录', '2016-10-20 11:02:45', '6905管理员', null, null, null, '');
INSERT INTO `qy_log` VALUES ('209', '0', '登录', '2016-10-20 11:10:31', '6905管理员', null, null, null, '');
INSERT INTO `qy_log` VALUES ('210', '0', '登录', '2016-10-20 21:27:59', '6905管理员', null, null, null, '');
INSERT INTO `qy_log` VALUES ('211', '0', '登录', '2016-10-23 11:06:31', '6905管理员', null, null, null, '');
INSERT INTO `qy_log` VALUES ('212', '0', '登录', '2016-10-23 11:08:48', '6905管理员', null, null, null, '');
INSERT INTO `qy_log` VALUES ('213', '0', '登录', '2016-10-25 14:41:47', '6905管理员', null, null, null, '');
INSERT INTO `qy_log` VALUES ('214', '0', '登录', '2016-10-25 21:25:49', '6905管理员', null, null, null, '');
INSERT INTO `qy_log` VALUES ('215', '0', '登录', '2016-10-27 14:41:35', '6905管理员', null, null, null, '');
INSERT INTO `qy_log` VALUES ('216', '0', '添加用户,用户账号：0001', '2016-10-27 14:42:26', '6905管理员', null, null, null, '');
INSERT INTO `qy_log` VALUES ('217', '0', '登录', '2016-11-17 10:58:36', '00001', null, null, null, '');
INSERT INTO `qy_log` VALUES ('218', '0', '登录', '2016-11-17 15:22:04', '00001', null, null, null, '');
INSERT INTO `qy_log` VALUES ('219', '0', '登录', '2016-11-22 09:07:45', '00001', null, null, null, '');
INSERT INTO `qy_log` VALUES ('220', '0', '登录', '2016-11-23 14:52:28', '00001', null, null, null, '');
INSERT INTO `qy_log` VALUES ('221', '0', '登录', '2017-03-04 13:28:10', '00001', null, null, null, '');

-- ----------------------------
-- Table structure for `qy_outapply`
-- ----------------------------
DROP TABLE IF EXISTS `qy_outapply`;
CREATE TABLE `qy_outapply` (
  `outId` bigint(20) NOT NULL AUTO_INCREMENT,
  `contractId` varchar(255) DEFAULT NULL,
  `outMeans` varchar(255) DEFAULT NULL,
  `ProductType` varchar(255) DEFAULT NULL,
  `oldType` varchar(255) DEFAULT NULL,
  `wholeName` varchar(255) DEFAULT NULL,
  `unitName` varchar(255) DEFAULT NULL,
  `batch` varchar(255) DEFAULT NULL,
  `deviceNo` varchar(255) DEFAULT NULL,
  `unit` varchar(255) DEFAULT NULL,
  `newPrice` double(9,2) DEFAULT NULL,
  `oldPrice` double(9,2) DEFAULT NULL,
  `num` int(11) DEFAULT NULL,
  `oldNum` int(11) DEFAULT NULL,
  `measure` varchar(255) DEFAULT NULL,
  `manufacturer` varchar(255) DEFAULT NULL,
  `keeper` varchar(255) DEFAULT NULL,
  `productCode` varchar(255) DEFAULT NULL,
  `PMNM` varchar(255) DEFAULT NULL,
  `location` varchar(255) DEFAULT NULL,
  `storageTime` varchar(255) DEFAULT NULL,
  `maintainCycle` varchar(255) DEFAULT NULL,
  `producedDate` datetime DEFAULT NULL,
  `execDate` datetime DEFAULT NULL,
  `borrowLength` int(16) DEFAULT NULL,
  `remark` text,
  `chStatus` varchar(255) DEFAULT NULL,
  `insertTime` datetime DEFAULT NULL,
  `checkPerson` varchar(255) DEFAULT NULL COMMENT '审核人',
  `ownedUnit` varchar(255) NOT NULL DEFAULT '',
  `borrowReason` varchar(255) DEFAULT NULL COMMENT '轮换出库理由',
  PRIMARY KEY (`outId`,`ownedUnit`),
  UNIQUE KEY `out-unique` (`outId`,`ownedUnit`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of qy_outapply
-- ----------------------------
INSERT INTO `qy_outapply` VALUES ('16', null, '轮换出库', null, null, null, null, '2016轮换吧', null, null, null, null, '1', null, null, null, null, null, null, null, null, null, null, '2016-01-17 21:23:46', '3', '军代室可审核', '轮换出库待审核', null, null, '代储企业1', '训练');
INSERT INTO `qy_outapply` VALUES ('18', null, '更新出库', null, null, null, null, '2016更新出库', null, null, '298700.00', '298700.00', '1', '1', null, null, null, null, null, null, null, null, null, '2016-01-19 09:38:03', null, '0.0', '更新出库待审核', null, null, '代储企业1', null);
INSERT INTO `qy_outapply` VALUES ('19', null, '轮换出库', null, null, null, null, 'CKPC001', null, null, null, null, '2', null, null, null, null, null, null, null, null, null, null, '2016-01-19 22:27:47', '9', '军代室可审核', '轮换出库审核已通过', null, '军代室管理员', '代储企业1', '训练');

-- ----------------------------
-- Table structure for `qy_outlist`
-- ----------------------------
DROP TABLE IF EXISTS `qy_outlist`;
CREATE TABLE `qy_outlist` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `listId` varchar(255) NOT NULL,
  `fileNo` varchar(255) DEFAULT NULL,
  `deliverNo` varchar(255) DEFAULT NULL,
  `diliverMean` varchar(255) DEFAULT NULL,
  `PMNM` varchar(255) DEFAULT NULL,
  `productModel` varchar(255) DEFAULT NULL,
  `oldModel` varchar(255) DEFAULT NULL,
  `unit` varchar(255) DEFAULT NULL,
  `quanlity` varchar(255) DEFAULT NULL,
  `askCount` varchar(255) DEFAULT NULL,
  `realCount` varchar(255) DEFAULT NULL,
  `num` varchar(255) DEFAULT NULL,
  `oldNum` varchar(255) DEFAULT NULL,
  `outMeans` varchar(255) DEFAULT '',
  `money` double(9,2) DEFAULT NULL,
  `remark` text,
  `price` double(9,2) DEFAULT NULL,
  `date` datetime DEFAULT NULL,
  `orderId` int(11) DEFAULT NULL,
  `ownedUnit` varchar(255) DEFAULT '',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of qy_outlist
-- ----------------------------
INSERT INTO `qy_outlist` VALUES ('1', 'FLDH001', 'ZYWH', 'LDBH001', 'YSFS', '1100000000', 'TCL-179型野战跳频数字接力机（装备）+TCL-179', null, '套', '优', '3', '4', '3', null, '发料调拨出库', '1194800.00', '备注', '298700.00', '2016-01-19 22:50:45', '1', '代储企业1');

-- ----------------------------
-- Table structure for `qy_outlistproductrelation`
-- ----------------------------
DROP TABLE IF EXISTS `qy_outlistproductrelation`;
CREATE TABLE `qy_outlistproductrelation` (
  `listId` varchar(20) NOT NULL DEFAULT '0',
  `productId` bigint(20) NOT NULL,
  `keeper` varchar(255) DEFAULT NULL COMMENT '代储单位',
  `insertTime` date DEFAULT NULL COMMENT '插入时间',
  `ownedUnit` varchar(255) NOT NULL DEFAULT '' COMMENT '所属单位',
  PRIMARY KEY (`listId`,`productId`,`ownedUnit`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of qy_outlistproductrelation
-- ----------------------------
INSERT INTO `qy_outlistproductrelation` VALUES ('FLDH001', '4', '代储企业1', '2016-01-19', '代储企业1');
INSERT INTO `qy_outlistproductrelation` VALUES ('FLDH001', '5', '代储企业1', '2016-01-19', '代储企业1');
INSERT INTO `qy_outlistproductrelation` VALUES ('FLDH001', '6', '代储企业1', '2016-01-19', '代储企业1');
INSERT INTO `qy_outlistproductrelation` VALUES ('FLDH001', '12', '代储企业1', '2016-01-19', '代储企业1');

-- ----------------------------
-- Table structure for `qy_outproductrelation`
-- ----------------------------
DROP TABLE IF EXISTS `qy_outproductrelation`;
CREATE TABLE `qy_outproductrelation` (
  `outId` bigint(20) NOT NULL,
  `productId` bigint(20) NOT NULL,
  `ownedUnit` varchar(255) NOT NULL DEFAULT '',
  `deviceNo` varchar(255) NOT NULL COMMENT 'jihao',
  `insertTime` date DEFAULT NULL COMMENT '插入时间',
  `id` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`,`ownedUnit`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of qy_outproductrelation
-- ----------------------------
INSERT INTO `qy_outproductrelation` VALUES ('16', '96', '代储企业1', 'cc004', '2016-01-17', '21');
INSERT INTO `qy_outproductrelation` VALUES ('18', '98', '代储企业1', 'pp003', '2016-01-19', '23');
INSERT INTO `qy_outproductrelation` VALUES ('19', '1', '代储企业1', 'aa004', '2016-01-19', '24');
INSERT INTO `qy_outproductrelation` VALUES ('19', '2', '代储企业1', 'aa005', '2016-01-19', '25');

-- ----------------------------
-- Table structure for `qy_parameter_configuration`
-- ----------------------------
DROP TABLE IF EXISTS `qy_parameter_configuration`;
CREATE TABLE `qy_parameter_configuration` (
  `id` bigint(20) NOT NULL,
  `maintain_cycle` varchar(255) NOT NULL,
  `cycle_ahead_days` int(11) NOT NULL,
  `store_ahead_days` int(11) NOT NULL,
  `out_ahead_days` int(11) NOT NULL,
  `alarm_cycle` varchar(20) DEFAULT NULL,
  `alarm_ahead_days` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of qy_parameter_configuration
-- ----------------------------
INSERT INTO `qy_parameter_configuration` VALUES ('1', '3月', '7', '7', '7', '1月', '20');

-- ----------------------------
-- Table structure for `qy_product`
-- ----------------------------
DROP TABLE IF EXISTS `qy_product`;
CREATE TABLE `qy_product` (
  `contractId` varchar(255) DEFAULT NULL,
  `productId` bigint(20) NOT NULL AUTO_INCREMENT,
  `productCode` varchar(255) DEFAULT '暂无',
  `PMNM` varchar(255) DEFAULT '暂无',
  `productName` varchar(255) DEFAULT '暂无',
  `productType` varchar(255) DEFAULT '暂无',
  `productModel` varchar(255) DEFAULT '暂无',
  `productUnit` varchar(255) DEFAULT '暂无',
  `measureUnit` varchar(255) DEFAULT '暂无',
  `productPrice` varchar(24) DEFAULT '0',
  `deliveryTime` date DEFAULT NULL,
  `latestMaintainTime` date DEFAULT NULL,
  `manufacturer` varchar(255) DEFAULT '暂无',
  `keeper` varchar(255) DEFAULT '暂无',
  `buyer` varchar(255) DEFAULT '暂无',
  `signTime` date DEFAULT NULL,
  `proStatus` varchar(255) DEFAULT '未到库',
  `restKeepTime` int(11) DEFAULT '0',
  `restMaintainTime` int(11) DEFAULT '0',
  `ownedUnit` varchar(255) NOT NULL DEFAULT '',
  `deviceNo` varchar(255) NOT NULL DEFAULT '' COMMENT '机号',
  `location` varchar(255) DEFAULT NULL COMMENT '摆放位置',
  `producedDate` varchar(255) DEFAULT NULL COMMENT '生产时间',
  `storageTime` varchar(255) DEFAULT NULL COMMENT '存储期限',
  `borrowLength` int(11) DEFAULT NULL COMMENT '借出时间',
  `borrowReason` varchar(255) DEFAULT NULL COMMENT '借出原因',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `oldType` varchar(255) DEFAULT NULL COMMENT '原产品型号',
  `wholeName` varchar(255) DEFAULT NULL COMMENT '整机名称',
  `oldPrice` varchar(255) DEFAULT NULL,
  `maintainCycle` varchar(255) DEFAULT NULL COMMENT '维护周期',
  `flag` int(11) DEFAULT '0' COMMENT '0表示默认状态；1表示更新出库或者轮换出库；2表示更新入库或者轮换入库；',
  `otherProductId` varchar(255) DEFAULT NULL COMMENT '如果这个product是旧product，那么这个字段表示新product的id',
  `status` varchar(255) DEFAULT '' COMMENT '当前最新的：轮换入库|轮换出库||更新入库|更新出库',
  PRIMARY KEY (`productId`,`ownedUnit`),
  KEY `qy_product_model_unit` (`productModel`,`productUnit`)
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of qy_product
-- ----------------------------
INSERT INTO `qy_product` VALUES ('HS002', '1', 'code001', '1100000000', 'TCL-179型野战跳频数字接力机（装备）', null, 'TCL-179', 'A型套', '套', '298700.0', '2016-01-28', '2016-01-19', '6905', '6905', '6905', '2016-01-20', '已出库', '0', '30', '代储企业1', 'aa004', 'A区', '2016-01-02', '3年', '9', '训练', '备注', null, 'TCL-179型野战跳频数字接力机（装备）', null, '一个月', '1', null, '轮换出库');
INSERT INTO `qy_product` VALUES ('HS002', '2', 'code001', '1100000000', 'TCL-179型野战跳频数字接力机（装备）', null, 'TCL-179', 'A型套', '套', '298700.0', '2016-01-28', '2016-01-19', '6905', '6905', '6905', '2016-01-20', '已出库', '0', '30', '代储企业1', 'aa005', 'A区', '2016-01-02', '3年', '9', '训练', '备注', null, 'TCL-179型野战跳频数字接力机（装备）', null, '一个月', '2', '13', '轮换出库');
INSERT INTO `qy_product` VALUES ('HS002', '3', null, '1100000000', 'TCL-179型野战跳频数字接力机（装备）', null, 'TCL-179', 'A型套', '套', '298700.0', '2016-01-28', null, '6905', '6905', '6905', '2016-01-20', '未申请', '0', '0', '代储企业1', 'JH01', null, null, null, null, null, null, null, 'TCL-179型野战跳频数字接力机（装备）', null, null, '0', null, '');
INSERT INTO `qy_product` VALUES ('HS002', '4', 'code001', '1100000000', 'TCL-179型野战跳频数字接力机（装备）', null, 'TCL-179', null, '套', '298700.0', '2016-01-28', '2016-01-19', '6905', '6905', '6905', '2016-01-20', '已出库', '0', '0', '代储企业1', 'aa001', 'A区', '2016-01-02', '3年', null, null, '备注', null, 'TCL-179型野战跳频数字接力机（装备）', null, '一个月', '2', null, '发料调拨出库');
INSERT INTO `qy_product` VALUES ('HS002', '5', 'code001', '1100000000', 'TCL-179型野战跳频数字接力机（装备）', null, 'TCL-179', null, '套', '298700.0', '2016-01-28', '2016-01-19', '6905', '6905', '6905', '2016-01-20', '已出库', '0', '0', '代储企业1', 'aa002', 'A区', '2016-01-02', '3年', null, null, '备注', null, 'TCL-179型野战跳频数字接力机（装备）', null, '一个月', '2', null, '发料调拨出库');
INSERT INTO `qy_product` VALUES ('HS002', '6', 'code001', '1100000000', 'TCL-179型野战跳频数字接力机（装备）', null, 'TCL-179', null, '套', '298700.0', '2016-01-28', '2016-01-19', '6905', '6905', '6905', '2016-01-20', '已出库', '0', '0', '代储企业1', 'aa003', 'A区', '2016-01-02', '3年', null, null, '备注', null, 'TCL-179型野战跳频数字接力机（装备）', null, '一个月', '2', null, '发料调拨出库');
INSERT INTO `qy_product` VALUES ('HS002', '7', 'A001', '1100000000', 'TCL-179型野战跳频数字接力机（装备）', null, 'TCL-179', 'B型套', '套', '298700.0', '2016-01-31', '2016-01-19', '6905', '6905', '6905', '2016-01-20', '已入库', '0', '0', '代储企业1', 'ABC001', 'A区', '2016-01-04', '3年', null, null, '备注', null, 'TCL-179型野战跳频数字接力机（装备）', null, '一个月', null, null, null);
INSERT INTO `qy_product` VALUES ('HS002', '8', 'A001', '1100000000', 'TCL-179型野战跳频数字接力机（装备）', null, 'TCL-179', 'B型套', '套', '298700.0', '2016-01-31', '2016-01-19', '6905', '6905', '6905', '2016-01-20', '已入库', '0', '0', '代储企业1', 'ABC002', 'A区', '2016-01-04', '3年', null, null, '备注', null, 'TCL-179型野战跳频数字接力机（装备）', null, '一个月', null, null, null);
INSERT INTO `qy_product` VALUES ('HS002', '9', null, '1100000000', 'TCL-179型野战跳频数字接力机（装备）', null, 'TCL-179', 'B型套', '套', '298700.0', '2016-01-31', null, '6905', '6905', '6905', '2016-01-20', '未到库', '0', '0', '代储企业1', '', null, null, null, null, null, null, null, 'TCL-179型野战跳频数字接力机（装备）', null, null, '0', null, '');
INSERT INTO `qy_product` VALUES ('HS002', '10', null, '1100000000', 'TCL-179型野战跳频数字接力机（装备）', null, 'TCL-179', 'B型套', '套', '298700.0', '2016-01-31', null, '6905', '6905', '6905', '2016-01-20', '未到库', '0', '0', '代储企业1', '', null, null, null, null, null, null, null, 'TCL-179型野战跳频数字接力机（装备）', null, null, '0', null, '');
INSERT INTO `qy_product` VALUES ('HS002', '11', null, '1100000000', 'TCL-179型野战跳频数字接力机（装备）', null, 'TCL-179', 'B型套', '套', '298700.0', '2016-01-31', null, '6905', '6905', '6905', '2016-01-20', '未到库', '0', '0', '代储企业1', '', null, null, null, null, null, null, null, 'TCL-179型野战跳频数字接力机（装备）', null, null, '0', null, '');
INSERT INTO `qy_product` VALUES ('HS002', '12', 'code001', '1100000000', 'TCL-179型野战跳频数字接力机（装备）', null, 'TCL-179', 'A型套', '套', '298700.0', '2016-01-28', '2016-01-19', '6905', '6905', '6905', '2016-01-20', '已出库', '0', '30', '代储企业1', 'Z001', 'A区', '2016-01-02', '3年', '9', '训练', '备注', null, 'TCL-179型野战跳频数字接力机（装备）', null, '一个月', '2', '1', '轮换入库,发料调拨出库');
INSERT INTO `qy_product` VALUES ('HS002', '13', 'code001', '1100000000', 'TCL-179型野战跳频数字接力机（装备）', null, 'TCL-179', 'A型套', '套', '298700.0', '2016-01-28', '2016-01-19', '6905', '6905', '6905', '2016-01-20', '已入库', '0', '30', '代储企业1', 'bb005', 'A区', '2016-01-02', '3年', '9', '训练', '备注', null, 'TCL-179型野战跳频数字接力机（装备）', null, '一个月', '2', '2', '轮换入库');
INSERT INTO `qy_product` VALUES ('HS002', '14', null, '1100000000', 'TCL-179型野战跳频数字接力机（装备）', null, 'TCL-179', 'A型套', '套', '298700.0', '2016-01-05', null, '6905', '6905', '6905', '2016-01-20', '未申请', '0', '0', '代储企业1', 'JH02', null, null, null, null, null, null, null, 'TCL-179型野战跳频数字接力机（装备）', null, null, '0', null, '');
INSERT INTO `qy_product` VALUES ('HS002', '15', null, '1100000000', 'TCL-179型野战跳频数字接力机（装备）', null, 'TCL-179', 'A型套', '套', '298700.0', '2016-01-05', null, '6905', '6905', '6905', '2016-01-20', '未申请', '0', '0', '代储企业1', 'JH03', null, null, null, null, null, null, null, 'TCL-179型野战跳频数字接力机（装备）', null, null, '0', null, '');
INSERT INTO `qy_product` VALUES ('HS002', '16', null, '1100000000', 'TCL-179型野战跳频数字接力机（装备）', null, 'TCL-179', 'A型套', '套', '298700.0', '2016-01-05', null, '6905', '6905', '6905', '2016-01-20', '未申请', '0', '0', '代储企业1', 'JH04', null, null, null, null, null, null, null, 'TCL-179型野战跳频数字接力机（装备）', null, null, '0', null, '');
INSERT INTO `qy_product` VALUES ('HS002', '17', null, '1100000000', 'TCL-179型野战跳频数字接力机（装备）', null, 'TCL-179', 'A型套', '套', '298700.0', '2016-01-05', null, '6905', '6905', '6905', '2016-01-20', '未申请', '0', '0', '代储企业1', 'JH05', null, null, null, null, null, null, null, 'TCL-179型野战跳频数字接力机（装备）', null, null, '0', null, '');
INSERT INTO `qy_product` VALUES ('HS002', '18', null, '1100000000', 'TCL-179型野战跳频数字接力机（装备）', null, 'TCL-179', 'A型套', '套', '298700.0', '2016-01-05', null, '6905', '6905', '6905', '2016-01-20', '未申请', '0', '0', '代储企业1', 'JH06', null, null, null, null, null, null, null, 'TCL-179型野战跳频数字接力机（装备）', null, null, '0', null, '');
INSERT INTO `qy_product` VALUES ('HS002', '19', null, '1100000000', 'TCL-179型野战跳频数字接力机（装备）', null, 'TCL-179', 'A型套', '套', '298700.0', '2016-01-05', null, '6905', '6905', '6905', '2016-01-20', '未申请', '0', '0', '代储企业1', 'JH07', null, null, null, null, null, null, null, 'TCL-179型野战跳频数字接力机（装备）', null, null, '0', null, '');
INSERT INTO `qy_product` VALUES ('HS002', '20', null, '1100000000', 'TCL-179型野战跳频数字接力机（装备）', null, 'TCL-179', 'A型套', '套', '298700.0', '2016-01-05', null, '6905', '6905', '6905', '2016-01-20', '未申请', '0', '0', '代储企业1', 'JH08', null, null, null, null, null, null, null, 'TCL-179型野战跳频数字接力机（装备）', null, null, '0', null, '');
INSERT INTO `qy_product` VALUES ('HS002', '21', null, '1100000000', 'TCL-179型野战跳频数字接力机（装备）', null, 'TCL-179', 'A型套', '套', '298700.0', '2016-01-05', null, '6905', '6905', '6905', '2016-01-20', '未申请', '0', '0', '代储企业1', 'JH09', null, null, null, null, null, null, null, 'TCL-179型野战跳频数字接力机（装备）', null, null, '0', null, '');
INSERT INTO `qy_product` VALUES ('HS002', '22', null, '1100000000', 'TCL-179型野战跳频数字接力机（装备）', null, 'TCL-179', 'A型套', '套', '298700.0', '2016-01-05', null, '6905', '6905', '6905', '2016-01-20', '未申请', '0', '0', '代储企业1', 'JH10', null, null, null, null, null, null, null, 'TCL-179型野战跳频数字接力机（装备）', null, null, '0', null, '');
INSERT INTO `qy_product` VALUES ('HS002', '23', null, '1100000000', 'TCL-179型野战跳频数字接力机（装备）', null, 'TCL-179', 'A型套', '套', '298700.0', '2016-01-05', null, '6905', '6905', '6905', '2016-01-20', '未申请', '0', '0', '代储企业1', 'JH11', null, null, null, null, null, null, null, 'TCL-179型野战跳频数字接力机（装备）', null, null, '0', null, '');

-- ----------------------------
-- Table structure for `qy_qualify`
-- ----------------------------
DROP TABLE IF EXISTS `qy_qualify`;
CREATE TABLE `qy_qualify` (
  `qualifiyId` bigint(9) NOT NULL AUTO_INCREMENT,
  `qualifyType` varchar(255) NOT NULL DEFAULT '',
  `qualifyTitle` varchar(255) NOT NULL DEFAULT '',
  `qualifyPath` varchar(255) NOT NULL,
  `ownedUnit` varchar(255) NOT NULL DEFAULT '',
  `year` varchar(255) DEFAULT NULL,
  `qualifyAttr` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`qualifiyId`),
  UNIQUE KEY `qualifyUnique` (`qualifyType`,`qualifyTitle`,`ownedUnit`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of qy_qualify
-- ----------------------------

-- ----------------------------
-- Table structure for `qy_repairinfo`
-- ----------------------------
DROP TABLE IF EXISTS `qy_repairinfo`;
CREATE TABLE `qy_repairinfo` (
  `repairid` int(9) NOT NULL AUTO_INCREMENT,
  `did` int(9) NOT NULL,
  `devicename` varchar(255) DEFAULT NULL,
  `deviceno` varchar(255) DEFAULT NULL,
  `repairman` varchar(255) DEFAULT NULL,
  `repairtime` varchar(255) DEFAULT NULL,
  `repairreason` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`repairid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of qy_repairinfo
-- ----------------------------

-- ----------------------------
-- Table structure for `qy_role`
-- ----------------------------
DROP TABLE IF EXISTS `qy_role`;
CREATE TABLE `qy_role` (
  `roleId` bigint(20) NOT NULL AUTO_INCREMENT,
  `role` varchar(255) NOT NULL,
  `contractManage` int(9) NOT NULL,
  `queryBusiness` int(9) NOT NULL,
  `borrowUpdate` int(9) DEFAULT NULL,
  `storeMantain` int(9) NOT NULL,
  `warehouseManage` int(9) NOT NULL,
  `statistics` int(9) NOT NULL,
  `fareManage` int(9) NOT NULL,
  `qualificationManage` int(9) NOT NULL,
  `systemManage` int(9) NOT NULL,
  `userManage` int(9) NOT NULL,
  PRIMARY KEY (`roleId`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of qy_role
-- ----------------------------
INSERT INTO `qy_role` VALUES ('1', '超级管理员', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1');
INSERT INTO `qy_role` VALUES ('2', '不可用用户管理', '1', '1', '1', '1', '1', '1', '1', '1', '1', '0');
INSERT INTO `qy_role` VALUES ('3', '会计', '0', '0', '0', '0', '0', '0', '1', '0', '0', '0');
INSERT INTO `qy_role` VALUES ('4', '不可用业务办理', '0', '1', '1', '1', '1', '1', '1', '1', '1', '1');
INSERT INTO `qy_role` VALUES ('5', '不可用业务查询', '1', '0', '1', '1', '1', '1', '1', '1', '1', '1');
INSERT INTO `qy_role` VALUES ('6', '不可用存储维护', '1', '1', '1', '0', '1', '1', '1', '1', '1', '1');
INSERT INTO `qy_role` VALUES ('8', '不可用库房管理', '1', '1', '1', '1', '0', '1', '1', '1', '1', '1');
INSERT INTO `qy_role` VALUES ('9', '不可用实力统计', '1', '1', '1', '1', '1', '0', '1', '1', '1', '1');
INSERT INTO `qy_role` VALUES ('10', '不可用经费管理', '1', '1', '1', '1', '1', '1', '0', '1', '1', '1');
INSERT INTO `qy_role` VALUES ('11', '不可用系统管理', '1', '1', '1', '1', '1', '1', '1', '1', '0', '1');
INSERT INTO `qy_role` VALUES ('12', '不可用资质管理', '1', '1', '1', '1', '1', '1', '1', '0', '1', '1');
INSERT INTO `qy_role` VALUES ('13', '不可用轮换更新', '1', '1', '0', '1', '1', '1', '1', '1', '1', '1');

-- ----------------------------
-- Table structure for `qy_temperature`
-- ----------------------------
DROP TABLE IF EXISTS `qy_temperature`;
CREATE TABLE `qy_temperature` (
  `temperatureId` bigint(255) NOT NULL AUTO_INCREMENT,
  `temperature` double(20,2) DEFAULT NULL,
  `curRecordDate` varchar(255) DEFAULT NULL,
  `position` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`temperatureId`)
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of qy_temperature
-- ----------------------------
INSERT INTO `qy_temperature` VALUES ('1', '21.00', '2016-01-19 20:56:18', 'A区');
INSERT INTO `qy_temperature` VALUES ('2', '21.00', '2016-01-19 21:02:59', 'A区');
INSERT INTO `qy_temperature` VALUES ('3', '21.00', '2016-01-19 21:03:39', 'A区');
INSERT INTO `qy_temperature` VALUES ('4', '21.00', '2016-01-19 21:07:13', 'A区');
INSERT INTO `qy_temperature` VALUES ('5', '21.00', '2016-01-19 21:07:35', 'A区');
INSERT INTO `qy_temperature` VALUES ('6', '20.00', '2016-01-19 22:01:01', 'A区');
INSERT INTO `qy_temperature` VALUES ('7', '20.00', '2016-01-19 22:15:06', 'A区');
INSERT INTO `qy_temperature` VALUES ('8', '20.00', '2016-01-19 22:18:28', 'A区');
INSERT INTO `qy_temperature` VALUES ('9', '19.00', '2016-01-19 22:20:20', 'A区');
INSERT INTO `qy_temperature` VALUES ('10', '19.00', '2016-01-19 22:21:21', 'A区');
INSERT INTO `qy_temperature` VALUES ('11', '19.00', '2016-01-19 22:31:56', 'A区');
INSERT INTO `qy_temperature` VALUES ('12', '19.00', '2016-01-19 22:32:49', 'A区');
INSERT INTO `qy_temperature` VALUES ('13', '19.00', '2016-01-19 22:34:24', 'A区');
INSERT INTO `qy_temperature` VALUES ('14', '19.00', '2016-01-19 22:37:28', 'A区');
INSERT INTO `qy_temperature` VALUES ('15', '19.00', '2016-01-19 22:38:09', 'A区');
INSERT INTO `qy_temperature` VALUES ('16', '20.00', '2016-01-19 22:40:01', 'A区');
INSERT INTO `qy_temperature` VALUES ('17', '20.00', '2016-01-19 22:41:23', 'A区');
INSERT INTO `qy_temperature` VALUES ('18', '20.00', '2016-01-19 22:41:42', 'A区');
INSERT INTO `qy_temperature` VALUES ('19', '19.00', '2016-01-19 22:46:49', 'A区');
INSERT INTO `qy_temperature` VALUES ('20', '19.00', '2016-01-19 22:51:21', 'A区');
INSERT INTO `qy_temperature` VALUES ('21', '19.00', '2016-01-19 22:51:55', 'A区');
INSERT INTO `qy_temperature` VALUES ('22', '18.00', '2016-01-19 22:59:47', 'A区');
INSERT INTO `qy_temperature` VALUES ('23', '19.00', '2016-01-19 23:02:37', 'A区');

-- ----------------------------
-- Table structure for `qy_unit`
-- ----------------------------
DROP TABLE IF EXISTS `qy_unit`;
CREATE TABLE `qy_unit` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `PMNM` varchar(255) DEFAULT NULL,
  `PMBM` varchar(255) DEFAULT NULL,
  `QCBM` varchar(255) DEFAULT NULL,
  `PMCS` varchar(255) DEFAULT NULL,
  `XHTH` varchar(255) DEFAULT NULL,
  `XLDJ` varchar(255) DEFAULT NULL,
  `XHDE` varchar(255) DEFAULT NULL,
  `JLDW` varchar(255) DEFAULT NULL,
  `MJYL` varchar(255) DEFAULT NULL,
  `QCXS` varchar(255) DEFAULT NULL,
  `BZZL` varchar(255) DEFAULT NULL,
  `BZJS` varchar(255) DEFAULT NULL,
  `BZTJ` varchar(255) DEFAULT NULL,
  `CKDJ` varchar(255) DEFAULT NULL,
  `SCCJNM` varchar(255) DEFAULT NULL,
  `GHDWNM` varchar(255) DEFAULT NULL,
  `ZBSX` varchar(255) DEFAULT NULL,
  `LBQF` varchar(255) DEFAULT NULL,
  `ZBBDSJ` varchar(255) DEFAULT NULL,
  `SYBZ` varchar(255) DEFAULT NULL,
  `YJDBZ` varchar(255) DEFAULT NULL,
  `SCBZ` varchar(255) DEFAULT NULL,
  `SCDXNF` varchar(255) DEFAULT NULL,
  `FKPMNM` varchar(255) DEFAULT NULL,
  `ownedUnit` varchar(255) DEFAULT NULL,
  `flag` int(10) DEFAULT '0' COMMENT '是否添加单元的标志',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of qy_unit
-- ----------------------------
INSERT INTO `qy_unit` VALUES ('1', '1100000000', '1100000000', '1', 'A型套', 'A-09', 'Ⅵ', '0', '套', '0', '0', '0', '0', '0', '298700', '60000058', '60000058', '（无）', '专用维修器材', '', '0', '1', '0', '', '1100000000', null, '0');
INSERT INTO `qy_unit` VALUES ('2', '1100000000', '1100000000', '5', '维修套（套件）', 'W09', 'Ⅴ', '0', '套', '0', '0', '0', '0', '0', '243804', '60000058', '60000058', '（无）', '专用维修器材', '', '0', '1', '0', '', '1100000000', null, '0');
INSERT INTO `qy_unit` VALUES ('3', '1100000000', '1100000000', '10', 'B型套', 'B-09', 'Ⅴ', '0', '套', '0', '0', '0', '0', '0', '347060', '60000058', '60000058', '（无）', '专用维修器材', '', '0', '1', '0', '', '1100000000', null, '0');

-- ----------------------------
-- Table structure for `qy_user`
-- ----------------------------
DROP TABLE IF EXISTS `qy_user`;
CREATE TABLE `qy_user` (
  `userId` bigint(20) NOT NULL AUTO_INCREMENT,
  `identifyNum` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `role` varchar(255) DEFAULT NULL,
  `duty` varchar(255) DEFAULT NULL,
  `ownedUnit` varchar(255) DEFAULT NULL,
  `authorityUnit` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`userId`)
) ENGINE=InnoDB AUTO_INCREMENT=79 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of qy_user
-- ----------------------------
INSERT INTO `qy_user` VALUES ('3', '00003', '81dc9bdb52d04dc20036dbd8313ed055', '会计李', '会计', '只可用经费管理', '代储企业1', '军代室1');
INSERT INTO `qy_user` VALUES ('5', '00004', '81dc9bdb52d04dc20036dbd8313ed055', '不可用资质管理', '不可用资质管理', '不可用资质管理', '代储企业1', '军代室1');
INSERT INTO `qy_user` VALUES ('6', '00005', '81dc9bdb52d04dc20036dbd8313ed055', '不可用业务办理', '不可用业务办理', '不可用业务办理', '代储企业1', '军代室1');
INSERT INTO `qy_user` VALUES ('7', '00006', '81dc9bdb52d04dc20036dbd8313ed055', '不可用业务查询', '不可用业务查询', '不可用业务查询', '代储企业1', '军代室1');
INSERT INTO `qy_user` VALUES ('8', '00007', '81dc9bdb52d04dc20036dbd8313ed055', '不可用存储维护', '不可用存储维护', '不可用存储维护', '代储企业2', '军代室2');
INSERT INTO `qy_user` VALUES ('9', '00008', '81dc9bdb52d04dc20036dbd8313ed055', '不可用库房管理', '不可用库房管理', '不可用库房管理', '代储企业1', '军代室1');
INSERT INTO `qy_user` VALUES ('63', '00001', '81dc9bdb52d04dc20036dbd8313ed055', '6905管理员', '超级管理员', '管理', '代储企业1', '军代室1');
INSERT INTO `qy_user` VALUES ('64', '00002', '81dc9bdb52d04dc20036dbd8313ed055', '6905管理员2', '超级管理员', '管理测试', '代储企业2', '军代室1');
INSERT INTO `qy_user` VALUES ('65', '10001', '81dc9bdb52d04dc20036dbd8313ed055', '军代室管理员', '超级管理员', '管理', '军代室1', '总军代局');
INSERT INTO `qy_user` VALUES ('72', '30001', '81dc9bdb52d04dc20036dbd8313ed055', '军代局管理员', '超级管理员', '管理', '军代局1', '总指挥局');
INSERT INTO `qy_user` VALUES ('73', '40001', '81dc9bdb52d04dc20036dbd8313ed055', '指挥局管理员', '超级管理员', '管理', '指挥局1', '');
INSERT INTO `qy_user` VALUES ('74', '00009', '81dc9bdb52d04dc20036dbd8313ed055', '不可用用户管理', '不可用用户管理', '不可用用户管理', '代储企业1', '军代室1');
INSERT INTO `qy_user` VALUES ('75', '00010', '81dc9bdb52d04dc20036dbd8313ed055', '不可用实力统计', '不可用实力统计', '不可用实力统计', '代储企业1', '军代室1');
INSERT INTO `qy_user` VALUES ('76', '00011', '81dc9bdb52d04dc20036dbd8313ed055', '不可用经费管理', '不可用经费管理', '不可用经费管理', '代储企业1', '军代室1');
INSERT INTO `qy_user` VALUES ('77', '00012', '81dc9bdb52d04dc20036dbd8313ed055', '不可用系统管理', '不可用系统管理', '不可用系统管理', '代储企业1', '军代室1');
INSERT INTO `qy_user` VALUES ('78', '0001', '81dc9bdb52d04dc20036dbd8313ed055', '00001', '不可用系统管理', '', '', '');

-- ----------------------------
-- Table structure for `zhjinfo`
-- ----------------------------
DROP TABLE IF EXISTS `zhjinfo`;
CREATE TABLE `zhjinfo` (
  `zhjId` bigint(20) NOT NULL AUTO_INCREMENT,
  `zhjName` varchar(255) COLLATE utf8_bin NOT NULL,
  `zhjManager` varchar(255) COLLATE utf8_bin NOT NULL,
  `leader` varchar(255) COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`zhjId`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of zhjinfo
-- ----------------------------
INSERT INTO `zhjinfo` VALUES ('1', '指挥局1', '负责人', '领导');
INSERT INTO `zhjinfo` VALUES ('2', '指挥局2', '负责人', '领导');

-- ----------------------------
-- View structure for `outlist_prodetail`
-- ----------------------------
DROP VIEW IF EXISTS `outlist_prodetail`;
CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`%` SQL SECURITY DEFINER VIEW `outlist_prodetail` AS (select count(0) AS `totalNum`,`o`.`outMeans` AS `outMeans`,`o`.`listId` AS `listId`,`o`.`PMNM` AS `PMNM`,`o`.`productModel` AS `productModel`,`o`.`unit` AS `unit`,`o`.`quanlity` AS `quanlity`,`o`.`askCount` AS `askCount`,`o`.`price` AS `price`,`o`.`realCount` AS `realCount`,`o`.`num` AS `num`,`o`.`money` AS `money`,`o`.`remark` AS `remark`,`p`.`proStatus` AS `proStatus` from ((`qy_outlist` `o` join `qy_outlistproductrelation` `r` on((`o`.`listId` = `r`.`listId`))) join `qy_product` `p` on((`r`.`productId` = `p`.`productId`))) group by `o`.`listId`,`p`.`productModel`,`p`.`productUnit`) ;

-- ----------------------------
-- View structure for `outlist_prodetail_toout`
-- ----------------------------
DROP VIEW IF EXISTS `outlist_prodetail_toout`;
CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`%` SQL SECURITY DEFINER VIEW `outlist_prodetail_toout` AS (select count(0) AS `totalNum`,`o`.`outMeans` AS `outMeans`,`o`.`listId` AS `listId`,`o`.`PMNM` AS `PMNM`,`o`.`productModel` AS `productModel`,`o`.`unit` AS `unit`,`o`.`quanlity` AS `quanlity`,`o`.`askCount` AS `askCount`,`o`.`price` AS `price`,`o`.`realCount` AS `realCount`,`o`.`num` AS `num`,`o`.`money` AS `money`,`o`.`remark` AS `remark`,`p`.`proStatus` AS `proStatus` from ((`qy_outlist` `o` join `qy_outlistproductrelation` `r` on((`o`.`listId` = `r`.`listId`))) join `qy_product` `p` on((`r`.`productId` = `p`.`productId`))) where (`p`.`proStatus` regexp '待出库$') group by `p`.`productModel`,`p`.`productUnit`) ;

-- ----------------------------
-- View structure for `temp_all_product`
-- ----------------------------
DROP VIEW IF EXISTS `temp_all_product`;
CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `temp_all_product` AS (select `a`.`contractId` AS `contractId`,`a`.`productId` AS `productId`,`a`.`productCode` AS `productCode`,`a`.`PMNM` AS `PMNM`,`a`.`productName` AS `productName`,`a`.`productType` AS `productType`,`a`.`productModel` AS `productModel`,`a`.`productUnit` AS `productUnit`,`a`.`measureUnit` AS `measureUnit`,`a`.`productPrice` AS `productPrice`,`a`.`deliveryTime` AS `deliveryTime`,`a`.`latestMaintainTime` AS `latestMaintainTime`,`a`.`manufacturer` AS `manufacturer`,`a`.`keeper` AS `keeper`,`a`.`buyer` AS `buyer`,`a`.`signTime` AS `signTime`,`a`.`proStatus` AS `proStatus`,`a`.`restKeepTime` AS `restKeepTime`,`a`.`restMaintainTime` AS `restMaintainTime`,`a`.`ownedUnit` AS `ownedUnit`,`a`.`deviceNo` AS `deviceNo`,`a`.`location` AS `location`,`a`.`producedDate` AS `producedDate`,`a`.`storageTime` AS `storageTime`,`a`.`borrowLength` AS `borrowLength`,`a`.`borrowReason` AS `borrowReason`,`a`.`remark` AS `remark`,`a`.`oldType` AS `oldType`,`a`.`wholeName` AS `wholeName`,`a`.`oldPrice` AS `oldPrice`,`a`.`maintainCycle` AS `maintainCycle`,`a`.`flag` AS `flag`,`a`.`otherProductId` AS `otherProductId`,`a`.`status` AS `status`,`c`.`JDS` AS `JDS`,`b`.`operateTime` AS `operateTime`,count(0) AS `productNum` from ((`qy_product` `a` join `qy_account` `b` on((`a`.`productId` = `b`.`productId`))) join `qy_contract` `c` on((`a`.`contractId` = `c`.`contractId`))) where ((`b`.`operateType` in (_utf8'新入库',_utf8'轮换入库',_utf8'更新入库')) and (`a`.`proStatus` not in (_utf8'合同销毁',_utf8'未到库',_utf8'未申请'))) group by `a`.`productName`,`a`.`productModel`,`a`.`productUnit`,`a`.`proStatus`,`b`.`operateTime`) ;

-- ----------------------------
-- View structure for `temp_all_product_nostatus`
-- ----------------------------
DROP VIEW IF EXISTS `temp_all_product_nostatus`;
CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `temp_all_product_nostatus` AS (select `a`.`contractId` AS `contractId`,`a`.`productId` AS `productId`,`a`.`productCode` AS `productCode`,`a`.`PMNM` AS `PMNM`,`a`.`productName` AS `productName`,`a`.`productType` AS `productType`,`a`.`productModel` AS `productModel`,`a`.`productUnit` AS `productUnit`,`a`.`measureUnit` AS `measureUnit`,`a`.`productPrice` AS `productPrice`,`a`.`deliveryTime` AS `deliveryTime`,`a`.`latestMaintainTime` AS `latestMaintainTime`,`a`.`manufacturer` AS `manufacturer`,`a`.`keeper` AS `keeper`,`a`.`buyer` AS `buyer`,`a`.`signTime` AS `signTime`,`a`.`proStatus` AS `proStatus`,`a`.`restKeepTime` AS `restKeepTime`,`a`.`restMaintainTime` AS `restMaintainTime`,`a`.`ownedUnit` AS `ownedUnit`,`a`.`deviceNo` AS `deviceNo`,`a`.`location` AS `location`,`a`.`producedDate` AS `producedDate`,`a`.`storageTime` AS `storageTime`,`a`.`borrowLength` AS `borrowLength`,`a`.`borrowReason` AS `borrowReason`,`a`.`remark` AS `remark`,`a`.`oldType` AS `oldType`,`a`.`wholeName` AS `wholeName`,`a`.`oldPrice` AS `oldPrice`,`a`.`maintainCycle` AS `maintainCycle`,`a`.`flag` AS `flag`,`a`.`otherProductId` AS `otherProductId`,`a`.`status` AS `status`,`c`.`JDS` AS `JDS`,`b`.`operateTime` AS `operateTime`,count(0) AS `productNum` from ((`qy_product` `a` join `qy_account` `b` on((`a`.`productId` = `b`.`productId`))) join `qy_contract` `c` on((`a`.`contractId` = `c`.`contractId`))) where ((`b`.`operateType` in (_utf8'新入库',_utf8'轮换入库',_utf8'更新入库')) and (`a`.`proStatus` not in (_utf8'合同销毁',_utf8'未到库',_utf8'未申请'))) group by `a`.`productName`,`a`.`productModel`,`a`.`productUnit`,`b`.`operateTime`) ;

-- ----------------------------
-- View structure for `temp_in_product`
-- ----------------------------
DROP VIEW IF EXISTS `temp_in_product`;
CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `temp_in_product` AS (select `a`.`productName` AS `productName`,`a`.`productModel` AS `productModel`,`a`.`productUnit` AS `productUnit`,`a`.`productPrice` AS `productPrice`,sum(`a`.`productNum`) AS `productNum`,`a`.`operateTime` AS `operateTime`,`a`.`manufacturer` AS `manufacturer`,`a`.`keeper` AS `keeper`,`a`.`JDS` AS `JDS`,`a`.`signTime` AS `signTime` from `temp_all_product` `a` where ((`a`.`proStatus` regexp _utf8'^已.{0,}入库$') or (`a`.`proStatus` regexp _utf8'出库待审核$') or (`a`.`proStatus` regexp _utf8'待出库$')) group by `a`.`productName`,`a`.`operateTime`) ;

-- ----------------------------
-- View structure for `temp_out_product`
-- ----------------------------
DROP VIEW IF EXISTS `temp_out_product`;
CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `temp_out_product` AS (select `a`.`productName` AS `productName`,`a`.`productModel` AS `productModel`,`a`.`productUnit` AS `productUnit`,`a`.`productPrice` AS `productPrice`,sum(`a`.`productNum`) AS `productNum`,`a`.`operateTime` AS `operateTime`,`a`.`manufacturer` AS `manufacturer`,`a`.`keeper` AS `keeper`,`a`.`JDS` AS `JDS`,`a`.`signTime` AS `signTime` from `temp_all_product` `a` where ((`a`.`proStatus` regexp _utf8'出库$') or (`a`.`proStatus` regexp _utf8'入库待审核$')) group by `a`.`productName`,`a`.`operateTime`) ;
DROP TRIGGER IF EXISTS `delete_jdj_trigger`;
DELIMITER ;;
CREATE TRIGGER `delete_jdj_trigger` AFTER DELETE ON `jdjinfo` FOR EACH ROW begin
      DELETE FROM jdsinfo WHERE jdsinfo.ownedjdjname =old.jdjname;
end
;;
DELIMITER ;
DROP TRIGGER IF EXISTS `delete_jds_trigger`;
DELIMITER ;;
CREATE TRIGGER `delete_jds_trigger` AFTER DELETE ON `jdsinfo` FOR EACH ROW begin
      DELETE FROM companyinfo WHERE companyinfo.ownedjdsname =old.jdsname;
end
;;
DELIMITER ;
DROP TRIGGER IF EXISTS `delete_device_trigger`;
DELIMITER ;;
CREATE TRIGGER `delete_device_trigger` AFTER DELETE ON `qy_deviceinfo` FOR EACH ROW BEGIN
DELETE FROM qy_repairinfo WHERE qy_repairinfo.did=old.deviceid;
END
;;
DELIMITER ;
DROP TRIGGER IF EXISTS `del_faredetail_trigger`;
DELIMITER ;;
CREATE TRIGGER `del_faredetail_trigger` AFTER DELETE ON `qy_fare` FOR EACH ROW begin
      DELETE FROM qy_faredetail WHERE qy_faredetail.fareId =old.fareId;
end
;;
DELIMITER ;
DROP TRIGGER IF EXISTS `watchProStatus`;
DELIMITER ;;
CREATE TRIGGER `watchProStatus` AFTER UPDATE ON `qy_product` FOR EACH ROW begin
  DECLARE checkSta varchar(255);
  DECLARE aid bigint(20);
if new.proStatus <> old.proStatus then
            if NEW.proStatus regexp '^[已].{0,}入库$' then
            
               if OLD.proStatus = '进库待审核' then
                  INSERT INTO qy_account(productId,operateType,operateTime,ownedUnit,remark) VALUES(OLD.productId,'新入库',now(),OLD.ownedUnit,CONCAT(OLD.proStatus,'-->',NEW.proStatus))
                  ON DUPLICATE KEY UPDATE operateTime=VALUES(operateTime),productId=VALUES(productId),operateType=VALUES(operateType);
               elseif OLD.proStatus = '轮换入库待审核' then
                  INSERT INTO qy_account(productId,operateType,operateTime,ownedUnit,remark) VALUES(OLD.productId,'轮换入库',now(),OLD.ownedUnit,CONCAT(OLD.proStatus,'-->',NEW.proStatus))
                  ON DUPLICATE KEY UPDATE operateTime=VALUES(operateTime),productId=VALUES(productId),operateType=VALUES(operateType);
               elseif OLD.proStatus = '更新入库待审核' then
                  INSERT INTO qy_account(productId,operateType,operateTime,ownedUnit,remark) VALUES(OLD.productId,'更新入库',now(),OLD.ownedUnit,CONCAT(OLD.proStatus,'-->',NEW.proStatus))
                  ON DUPLICATE KEY UPDATE operateTime=VALUES(operateTime),productId=VALUES(productId),operateType=VALUES(operateType);
            
               elseif OLD.proStatus = '未申请' then
                  INSERT INTO qy_account(productId,operateType,operateTime,ownedUnit,remark) VALUES(OLD.productId,'新入库',now(),OLD.ownedUnit,CONCAT(OLD.proStatus,'-->',NEW.proStatus))
                  ON DUPLICATE KEY UPDATE operateTime=VALUES(operateTime),productId=VALUES(productId),operateType=VALUES(operateType);
               elseif OLD.proStatus = '合同销毁' then
                  SET checkSta = (SELECT status FROM qy_product WHERE productId=NEW.productId);
                 INSERT INTO qy_account(productId,operateType,operateTime,ownedUnit,remark) VALUES(OLD.productId,checkSta,now(),OLD.ownedUnit,CONCAT(OLD.proStatus,'-->',NEW.proStatus))
                  ON DUPLICATE KEY UPDATE operateTime=VALUES(operateTime),productId=VALUES(productId),operateType=VALUES(operateType);
            
               elseif OLD.proStatus = '出库待审核$' then
                  SET checkSta = (SELECT status FROM qy_product WHERE productId=NEW.productId);
                  SET aid = (SELECT id FROM qy_account WHERE productId = NEW.productId and operateType=checkSta);
                  DELETE FROM qy_account WHERE id = aid;
               elseif OLD.proStatus = '已出库' then
                  SET checkSta = (SELECT status FROM qy_product WHERE productId=NEW.productId);
                  SET aid = (SELECT id FROM qy_account WHERE productId = NEW.productId and operateType=checkSta);
                  DELETE FROM qy_account WHERE id = aid;
               end if;
            end if;
            if NEW.proStatus regexp '^[已].{0,}出库$' then
            
             if OLD.proStatus = '轮换出库待审核' or OLD.proStatus='轮换待出库' then
                INSERT INTO qy_account(productId,operateType,operateTime,ownedUnit,remark) VALUES(OLD.productId,'轮换出库',now(),OLD.ownedUnit,CONCAT(OLD.proStatus,'-->',NEW.proStatus))
                ON DUPLICATE KEY UPDATE operateTime=VALUES(operateTime),productId=VALUES(productId),operateType=VALUES(operateType);
             elseif OLD.proStatus = '更新出库待审核' or OLD.proStatus='更新待出库' then
                INSERT INTO qy_account(productId,operateType,operateTime,ownedUnit,remark) VALUES(OLD.productId,'更新出库',now(),OLD.ownedUnit,CONCAT(OLD.proStatus,'-->',NEW.proStatus))
                ON DUPLICATE KEY UPDATE operateTime=VALUES(operateTime),productId=VALUES(productId),operateType=VALUES(operateType);
             elseif OLD.proStatus regexp '调拨待出库$' then
                INSERT INTO qy_account(productId,operateType,operateTime,ownedUnit,remark) VALUES(OLD.productId,'调拨出库',now(),OLD.ownedUnit,CONCAT(OLD.proStatus,'-->',NEW.proStatus))
                ON DUPLICATE KEY UPDATE operateTime=VALUES(operateTime),productId=VALUES(productId),operateType=VALUES(operateType);
             
             
             elseif OLD.proStatus = '已入库' then
                  SET checkSta = (SELECT status FROM qy_product WHERE productId=NEW.productId);
                  if checkSta regexp '调拨出库$' then
                     INSERT INTO qy_account(productId,operateType,operateTime,ownedUnit,remark) VALUES(OLD.productId,'调拨出库',now(),OLD.ownedUnit,CONCAT(OLD.proStatus,'-->',NEW.proStatus))
                  ON DUPLICATE KEY UPDATE operateTime=VALUES(operateTime),productId=VALUES(productId),operateType=VALUES(operateType);
                  elseif checkSta regexp '轮换发料出库$' then
                     INSERT INTO qy_account(productId,operateType,operateTime,ownedUnit,remark) VALUES(OLD.productId,'轮换出库',now(),OLD.ownedUnit,CONCAT(OLD.proStatus,'-->',NEW.proStatus))
                  ON DUPLICATE KEY UPDATE operateTime=VALUES(operateTime),productId=VALUES(productId),operateType=VALUES(operateType);
                  elseif checkSta regexp '更新发料出库$' then
                     INSERT INTO qy_account(productId,operateType,operateTime,ownedUnit,remark) VALUES(OLD.productId,'更新出库',now(),OLD.ownedUnit,CONCAT(OLD.proStatus,'-->',NEW.proStatus))
                  ON DUPLICATE KEY UPDATE operateTime=VALUES(operateTime),productId=VALUES(productId),operateType=VALUES(operateType);
                  else
                      INSERT INTO qy_account(productId,operateType,operateTime,ownedUnit,remark) VALUES(OLD.productId,checkSta,now(),OLD.ownedUnit,CONCAT(OLD.proStatus,'-->',NEW.proStatus))
                  ON DUPLICATE KEY UPDATE operateTime=VALUES(operateTime),productId=VALUES(productId),operateType=VALUES(operateType);
                  end if;
              end if;
             end if;
             
             
             if NEW.proStatus regexp '合同销毁' then
             
             
             if OLD.proStatus = '轮换入库待审核' then
                SET aid = (SELECT id FROM qy_account WHERE productId = NEW.productId and operateType ='轮换入库');
                DELETE FROM qy_account WHERE id = aid;
             elseif OLD.proStatus = '更新入库待审核' then
                SET aid = (SELECT id FROM qy_account WHERE productId = NEW.productId and operateType ='更新入库');
                DELETE FROM qy_account WHERE id = aid;
             elseif OLD.proStatus = '已入库' then
                SET checkSta = (SELECT status FROM qy_product WHERE productId=NEW.productId);
                SET aid = (SELECT id FROM qy_account WHERE productId = NEW.productId and operateType=checkSta);
                DELETE FROM qy_account WHERE id = aid;
            end if;
        end if; 
         if NEW.proStatus regexp '未申请' then
            
            
             if OLD.proStatus = '进库待审核' then
                SET aid = (SELECT id FROM qy_account WHERE productId = NEW.productId and operateType ='新入库');
                DELETE FROM qy_account WHERE id = aid;
             elseif OLD.proStatus = '已入库' then
                SET aid = (SELECT id FROM qy_account WHERE productId = NEW.productId and operateType ='新入库');
                DELETE FROM qy_account WHERE id = aid;
             end if;
        end if; 
end if;
end
;;
DELIMITER ;
DROP TRIGGER IF EXISTS `delete_zhj_trigger`;
DELIMITER ;;
CREATE TRIGGER `delete_zhj_trigger` AFTER DELETE ON `zhjinfo` FOR EACH ROW begin
      DELETE FROM jdjinfo WHERE jdjinfo.ownedzhjname =old.zhjname;
end
;;
DELIMITER ;
