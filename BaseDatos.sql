CREATE DATABASE `dbbank` /*!40100 DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci */;


-- dbbank.persona definition

CREATE TABLE `persona` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `direccion` varchar(255) DEFAULT NULL,
  `edad` int(11) NOT NULL,
  `genero` varchar(255) DEFAULT NULL,
  `identificacion` varchar(255) DEFAULT NULL,
  `nombre` varchar(255) DEFAULT NULL,
  `telefono` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;


-- dbbank.cliente definition

CREATE TABLE `cliente` (
  `estado` bit(1) NOT NULL,
  `password` varchar(255) DEFAULT NULL,
  `id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `FKkpvkbjg32bso6riqge70hwcel` FOREIGN KEY (`id`) REFERENCES `persona` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- dbbank.cuenta definition

CREATE TABLE `cuenta` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `estado` varchar(255) NOT NULL,
  `numero_cuenta` varchar(255) NOT NULL,
  `saldo_inicial` decimal(38,2) NOT NULL,
  `tipo_cuenta` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_pj7ncg765kt4klndu25bwbwe4` (`numero_cuenta`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- dbbank.movimiento definition

CREATE TABLE `movimiento` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `fecha` datetime(6) NOT NULL,
  `saldo` decimal(38,2) NOT NULL,
  `tipo_movimiento` varchar(255) NOT NULL,
  `valor` decimal(38,2) NOT NULL,
  `cuenta_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK4ea11fe7p3xa1kwwmdgi9f2fi` (`cuenta_id`),
  CONSTRAINT `FK4ea11fe7p3xa1kwwmdgi9f2fi` FOREIGN KEY (`cuenta_id`) REFERENCES `cuenta` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;