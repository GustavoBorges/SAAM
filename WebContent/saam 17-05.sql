CREATE DATABASE  IF NOT EXISTS `saam` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `saam`;
-- MySQL dump 10.13  Distrib 5.5.16, for Win32 (x86)
--
-- Host: localhost    Database: saam
-- ------------------------------------------------------
-- Server version	5.5.30

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `areas`
--

DROP TABLE IF EXISTS `areas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `areas` (
  `COD_AREA` int(11) NOT NULL AUTO_INCREMENT,
  `NOME` varchar(45) NOT NULL,
  PRIMARY KEY (`COD_AREA`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `areas`
--

LOCK TABLES `areas` WRITE;
/*!40000 ALTER TABLE `areas` DISABLE KEYS */;
INSERT INTO `areas` (`COD_AREA`, `NOME`) VALUES (1,'JURÍDICA'),(2,'PSICOLÓGICA'),(3,'SOCIAL');
/*!40000 ALTER TABLE `areas` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `aten_mens`
--

DROP TABLE IF EXISTS `aten_mens`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `aten_mens` (
  `COD_ATENDIMENTO` int(11) NOT NULL,
  `COD_MENSAGEM` int(11) NOT NULL,
  PRIMARY KEY (`COD_ATENDIMENTO`,`COD_MENSAGEM`),
  KEY `IDX_ATEN_MENS_COD_MENSAGEM` (`COD_MENSAGEM`),
  KEY `IDX_ATEN_MENS_COD_ATENDIMENTO` (`COD_MENSAGEM`),
  CONSTRAINT `FRK_ATEND_MENS_COD_ATENDIMENTO` FOREIGN KEY (`COD_ATENDIMENTO`) REFERENCES `atendimentos` (`COD_ATENDIMENTO`),
  CONSTRAINT `FRK_ATEND_MENS_COD_MENSAGEM` FOREIGN KEY (`COD_MENSAGEM`) REFERENCES `mensagens` (`COD_MENSAGEM`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `aten_mens`
--

LOCK TABLES `aten_mens` WRITE;
/*!40000 ALTER TABLE `aten_mens` DISABLE KEYS */;
INSERT INTO `aten_mens` (`COD_ATENDIMENTO`, `COD_MENSAGEM`) VALUES (5,2),(5,3),(5,4),(5,5),(5,6),(5,7),(6,8),(6,9),(7,10),(7,11),(8,12),(8,13),(8,14),(8,15),(8,16),(9,17),(9,18),(9,19),(9,20),(9,21),(9,22),(9,23),(9,24),(9,25),(10,26),(10,27),(11,28),(11,29),(11,30),(11,31),(12,32),(13,33),(13,34),(14,35),(14,36),(14,37),(14,38),(14,39),(14,40),(14,41),(14,42),(14,43),(14,44),(14,45),(14,46),(14,47),(14,48),(14,49),(14,50),(14,51),(14,52),(22,53),(22,54),(24,55),(24,56),(24,57),(25,58),(25,59),(25,60),(25,61);
/*!40000 ALTER TABLE `aten_mens` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `atendimentos`
--

DROP TABLE IF EXISTS `atendimentos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `atendimentos` (
  `COD_ATENDIMENTO` int(11) NOT NULL AUTO_INCREMENT,
  `COD_ATENDENTE` int(11) NOT NULL,
  `COD_ATENDIDO` int(11) NOT NULL,
  `COD_TIPO_ATENDIMENTO` int(11) NOT NULL,
  `COD_AVALIACAO` int(11) DEFAULT NULL,
  `COD_AREA` int(11) NOT NULL,
  `DATA_INICIO` datetime NOT NULL,
  `DATA_FIM` datetime NOT NULL,
  PRIMARY KEY (`COD_ATENDIMENTO`),
  KEY `IDX_ATENDIMENTOS_COD_ATENDENTE` (`COD_ATENDENTE`),
  KEY `IDX_ATENDIMENTOS_COD_ATENDIDO` (`COD_ATENDIDO`),
  KEY `IDX_ATENDIMENTOS_COD_TIPO_ATENDIMENTO` (`COD_TIPO_ATENDIMENTO`),
  KEY `IDX_ATENDIMENTOS_COD_AVALIACAO` (`COD_AVALIACAO`),
  KEY `IDX_ATENDIMENTOS_COD_AREA` (`COD_AREA`),
  CONSTRAINT `FRK_ATENDIMENTOS_COD_AREA` FOREIGN KEY (`COD_AREA`) REFERENCES `areas` (`COD_AREA`),
  CONSTRAINT `FRK_ATENDIMENTOS_COD_ATENDENTE` FOREIGN KEY (`COD_ATENDENTE`) REFERENCES `usuarios` (`COD_USUARIO`),
  CONSTRAINT `FRK_ATENDIMENTOS_COD_ATENDIDO` FOREIGN KEY (`COD_ATENDIDO`) REFERENCES `usuarios` (`COD_USUARIO`),
  CONSTRAINT `FRK_ATENDIMENTOS_COD_AVALIACAO` FOREIGN KEY (`COD_AVALIACAO`) REFERENCES `avaliacoes` (`COD_AVALIACAO`),
  CONSTRAINT `FRK_ATENDIMENTOS_COD_TIPO_ATENDIMENTO` FOREIGN KEY (`COD_TIPO_ATENDIMENTO`) REFERENCES `tipo_atendimentos` (`COD_TIPO_ATENDIMENTO`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `atendimentos`
--

LOCK TABLES `atendimentos` WRITE;
/*!40000 ALTER TABLE `atendimentos` DISABLE KEYS */;
INSERT INTO `atendimentos` (`COD_ATENDIMENTO`, `COD_ATENDENTE`, `COD_ATENDIDO`, `COD_TIPO_ATENDIMENTO`, `COD_AVALIACAO`, `COD_AREA`, `DATA_INICIO`, `DATA_FIM`) VALUES (5,2,5,1,NULL,2,'2013-05-14 17:37:17','2013-05-14 17:37:42'),(6,2,5,1,NULL,2,'2013-05-14 17:59:08','2013-05-14 17:59:59'),(7,2,5,1,12,2,'2013-05-14 18:28:18','2013-05-14 18:28:34'),(8,2,5,1,3,2,'2013-05-14 19:58:19','2013-05-14 19:59:23'),(9,2,5,1,8,2,'2013-05-15 11:00:29','2013-05-15 11:01:01'),(10,2,5,1,NULL,2,'2013-05-15 11:04:15','2013-05-15 11:04:29'),(11,2,5,1,NULL,2,'2013-05-15 11:07:14','2013-05-15 11:08:14'),(12,2,5,1,NULL,2,'2013-05-15 11:25:19','2013-05-15 11:28:41'),(13,2,5,1,NULL,2,'2013-05-15 11:32:24','2013-05-15 11:33:35'),(14,2,5,1,NULL,2,'2013-05-15 12:05:41','2013-05-15 12:15:32'),(15,2,5,1,NULL,2,'2013-05-15 12:15:26','2013-05-15 12:17:01'),(16,2,5,1,NULL,2,'2013-05-15 12:50:59','2013-05-15 12:51:03'),(17,2,5,1,NULL,2,'2013-05-15 12:53:19','2013-05-15 12:53:28'),(18,2,5,1,NULL,2,'2013-05-15 13:01:30','2013-05-15 13:01:40'),(19,2,5,1,NULL,2,'2013-05-15 13:03:02','2013-05-15 13:03:11'),(20,2,5,1,NULL,2,'2013-05-15 13:05:53','2013-05-15 13:06:49'),(21,2,5,1,NULL,2,'2013-05-15 13:06:31','2013-05-15 13:07:45'),(22,2,5,1,NULL,2,'2013-05-15 14:31:40','2013-05-15 14:32:43'),(23,2,5,1,NULL,2,'2013-05-15 14:43:12','2013-05-15 14:45:20'),(24,2,5,1,NULL,2,'2013-05-15 18:35:16','2013-05-15 18:35:46'),(25,2,5,1,NULL,2,'2013-05-16 14:03:40','2013-05-16 14:03:53');
/*!40000 ALTER TABLE `atendimentos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `avaliacoes`
--

DROP TABLE IF EXISTS `avaliacoes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `avaliacoes` (
  `COD_AVALIACAO` int(11) NOT NULL AUTO_INCREMENT,
  `COD_CLASSIFICACAO` int(11) NOT NULL,
  `SUGESTAO` varchar(250) DEFAULT NULL,
  `DATA` datetime DEFAULT NULL,
  PRIMARY KEY (`COD_AVALIACAO`),
  KEY `IDX_AVALIACOES_COD_CLASSIFICACAO` (`COD_CLASSIFICACAO`),
  CONSTRAINT `FRK_AVALIACOES_COD_CLASSIFICACAO` FOREIGN KEY (`COD_CLASSIFICACAO`) REFERENCES `classificacoes` (`COD_CLASSIFICACAO`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `avaliacoes`
--

LOCK TABLES `avaliacoes` WRITE;
/*!40000 ALTER TABLE `avaliacoes` DISABLE KEYS */;
INSERT INTO `avaliacoes` (`COD_AVALIACAO`, `COD_CLASSIFICACAO`, `SUGESTAO`, `DATA`) VALUES (3,5,'balaafas','2013-05-15 10:33:06'),(4,2,'','2013-05-15 10:46:14'),(5,1,'aasdf','2013-05-15 10:50:36'),(6,1,'','2013-05-15 10:51:12'),(7,3,'','2013-05-15 10:51:24'),(8,4,'sagas','2013-05-15 11:01:48'),(9,4,'llllllllllllllll','2013-05-15 14:33:27'),(10,2,'','2013-05-15 14:46:55'),(11,5,'adfasdfsfsdfasdfasdfsdfasf','2013-05-15 18:36:38'),(12,2,'asdfsdf','2013-05-16 14:04:01');
/*!40000 ALTER TABLE `avaliacoes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `classificacoes`
--

DROP TABLE IF EXISTS `classificacoes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `classificacoes` (
  `COD_CLASSIFICACAO` int(11) NOT NULL AUTO_INCREMENT,
  `NOME` varchar(50) NOT NULL,
  PRIMARY KEY (`COD_CLASSIFICACAO`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `classificacoes`
--

LOCK TABLES `classificacoes` WRITE;
/*!40000 ALTER TABLE `classificacoes` DISABLE KEYS */;
INSERT INTO `classificacoes` (`COD_CLASSIFICACAO`, `NOME`) VALUES (1,'Ruim'),(2,'Regular'),(3,'Bom'),(4,'Muito Bom'),(5,'Excelente');
/*!40000 ALTER TABLE `classificacoes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `enderecos`
--

DROP TABLE IF EXISTS `enderecos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `enderecos` (
  `COD_ENDERECO` int(11) NOT NULL AUTO_INCREMENT,
  `LOGRADOURO` varchar(150) NOT NULL,
  `BAIRRO` varchar(50) NOT NULL,
  `CIDADE` varchar(50) NOT NULL,
  `ESTADO` varchar(2) NOT NULL,
  `CEP` varchar(8) NOT NULL,
  `PAIS` varchar(50) NOT NULL,
  PRIMARY KEY (`COD_ENDERECO`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `enderecos`
--

LOCK TABLES `enderecos` WRITE;
/*!40000 ALTER TABLE `enderecos` DISABLE KEYS */;
INSERT INTO `enderecos` (`COD_ENDERECO`, `LOGRADOURO`, `BAIRRO`, `CIDADE`, `ESTADO`, `CEP`, `PAIS`) VALUES (1,'QD 301 LT 01','BRASILIA','BRASILIA','DF','72000001','BRASIL'),(2,'QD 302 LT 02','BRASILIA','BRASILIA','DF','72000002','BRASIL'),(3,'QD 303 LT 03','BRASILIA','BRASILIA','DF','72000003','BRASIL'),(4,'QD 304 LT 04','BRASILIA','BRASILIA','DF','72000004','BRASIL'),(5,'QD 305 LT 05','BRASILIA','BRASILIA','DF','72000005','BRASIL'),(6,'QD 306 LT 06','BRASILIA','BRASILIA','DF','72000006','BRASIL'),(7,'Qd 309 Cj 14 Cs 16','Recanto','Recanto das Emas','DF','72622216','Brasil'),(8,'Qd 309 Cj 14 Cs 16','Recanto','Recanto das Emas','DF','72622216','Brasil'),(9,'Quadra 309 Conj 14 Casa 16','Rec das Emas','Rec das Emas','DF','72622216','Brasil'),(10,'QD 205','BLA','XD','DF','PE','BSB'),(11,'QD 205','BLA','XD','DF','PE','BSB'),(12,'Qd 309','Rec','Recanto das Emas','DF','11154178','bsb'),(13,'','','','','','');
/*!40000 ALTER TABLE `enderecos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `funcionalidades`
--

DROP TABLE IF EXISTS `funcionalidades`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `funcionalidades` (
  `COD_FUNCIONALIDADE` int(11) NOT NULL AUTO_INCREMENT,
  `NOME_TECNICO` varchar(50) NOT NULL,
  `NOME_USUARIO` varchar(20) DEFAULT NULL,
  `url` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`COD_FUNCIONALIDADE`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `funcionalidades`
--

LOCK TABLES `funcionalidades` WRITE;
/*!40000 ALTER TABLE `funcionalidades` DISABLE KEYS */;
INSERT INTO `funcionalidades` (`COD_FUNCIONALIDADE`, `NOME_TECNICO`, `NOME_USUARIO`, `url`) VALUES (1,'Solicitar um atendimento','',''),(2,'Atender Vítima','',''),(3,'Histórico de atendimentos','','#{atendimentoMB.historico}'),(4,'Atender Mensagens','',''),(5,'Manter Atendentes','',''),(6,'Manter Usuários','',''),(7,'Manter Perfis','','#{perfilMB.index}'),(8,'Avaliar Cadastro de Atendentes','','#{voluntarioMB.index}'),(9,'Consultar Logs','',''),(10,'Manter Perguntas Frequentes','','#{perguntaFrequenteMB.index}'),(11,'Relatório de Atendimentos','',''),(12,'Relatório de Acessos','',''),(13,'Relatório de Avaliações','',''),(14,'Relatório de Pessoas','',''),(15,'Acompanhar Atendimentos',NULL,'');
/*!40000 ALTER TABLE `funcionalidades` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `logs`
--

DROP TABLE IF EXISTS `logs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `logs` (
  `COD_LOG` int(11) NOT NULL AUTO_INCREMENT,
  `DATA` datetime NOT NULL,
  `MENSAGEM` varchar(150) NOT NULL,
  `COD_TIPO_LOG` int(11) NOT NULL,
  `COD_USUARIO` int(11) NOT NULL,
  PRIMARY KEY (`COD_LOG`),
  KEY `IDX_LOGS_COD_TIPO_LOG` (`COD_TIPO_LOG`),
  KEY `IDX_LOGS_COD_USUARIO` (`COD_USUARIO`),
  CONSTRAINT `FRK_LOGS_COD_TIPO_LOG` FOREIGN KEY (`COD_TIPO_LOG`) REFERENCES `tipo_logs` (`COD_TIPO_LOG`),
  CONSTRAINT `FRK_LOGS_COD_USUARIO` FOREIGN KEY (`COD_USUARIO`) REFERENCES `usuarios` (`COD_USUARIO`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `logs`
--

LOCK TABLES `logs` WRITE;
/*!40000 ALTER TABLE `logs` DISABLE KEYS */;
/*!40000 ALTER TABLE `logs` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `mensagens`
--

DROP TABLE IF EXISTS `mensagens`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `mensagens` (
  `COD_MENSAGEM` int(11) NOT NULL AUTO_INCREMENT,
  `DATA` datetime NOT NULL,
  `TEXTO` varchar(200) NOT NULL,
  `COD_USUARIO` int(11) NOT NULL,
  PRIMARY KEY (`COD_MENSAGEM`),
  KEY `IDX_MENSAGENS_COD_USUARIO` (`COD_USUARIO`),
  CONSTRAINT `FRK_MENSAGENS_COD_USUARIO` FOREIGN KEY (`COD_USUARIO`) REFERENCES `usuarios` (`COD_USUARIO`)
) ENGINE=InnoDB AUTO_INCREMENT=62 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `mensagens`
--

LOCK TABLES `mensagens` WRITE;
/*!40000 ALTER TABLE `mensagens` DISABLE KEYS */;
INSERT INTO `mensagens` (`COD_MENSAGEM`, `DATA`, `TEXTO`, `COD_USUARIO`) VALUES (1,'2013-05-14 17:31:08','',2),(2,'2013-05-14 17:37:22','afasf',5),(3,'2013-05-14 17:37:23','afasdf',5),(4,'2013-05-14 17:37:24','aasfasf',5),(5,'2013-05-14 17:37:26','fasfafdaf',5),(6,'2013-05-14 17:37:31','asaf',5),(7,'2013-05-14 17:37:40','asfasdf',2),(8,'2013-05-14 17:59:36','Olá tudo bem?',2),(9,'2013-05-14 17:59:51','boa noite xD',5),(10,'2013-05-14 18:28:24','geremias',5),(11,'2013-05-14 18:28:29','eu sou homi',2),(12,'2013-05-14 19:58:51','Olá',2),(13,'2013-05-14 19:58:54','',2),(14,'2013-05-14 19:58:57','',2),(15,'2013-05-14 19:59:10','fasfasf',5),(16,'2013-05-14 19:59:19','asfsf',2),(17,'2013-05-15 11:00:39','asfasf',5),(18,'2013-05-15 11:00:41','asfasf',5),(19,'2013-05-15 11:00:41','asfasf',5),(20,'2013-05-15 11:00:43','asdfasf',5),(21,'2013-05-15 11:00:44','asf',5),(22,'2013-05-15 11:00:48','asdfaf',2),(23,'2013-05-15 11:00:49','asfasf',2),(24,'2013-05-15 11:00:50','asfs',2),(25,'2013-05-15 11:01:00','kççklkçk',2),(26,'2013-05-15 11:04:18','fsdfasdf',2),(27,'2013-05-15 11:04:23','asfasdfasf',5),(28,'2013-05-15 11:07:17','fadfasdf',2),(29,'2013-05-15 11:07:20','asfasdf',5),(30,'2013-05-15 11:07:24','asfasf',2),(31,'2013-05-15 11:08:12','asdfa',2),(32,'2013-05-15 11:25:31','klb',2),(33,'2013-05-15 11:32:43','ola',5),(34,'2013-05-15 11:32:53','kkk',2),(35,'2013-05-15 12:05:53','\\',5),(36,'2013-05-15 12:05:55','a',5),(37,'2013-05-15 12:09:37','a',5),(38,'2013-05-15 12:09:42','b',5),(39,'2013-05-15 12:09:48','a',5),(40,'2013-05-15 12:09:51','sasfsdf',5),(41,'2013-05-15 12:10:33','',5),(42,'2013-05-15 12:11:17','A',5),(43,'2013-05-15 12:12:23','a',5),(44,'2013-05-15 12:12:26','b',5),(45,'2013-05-15 12:12:29','asdasf',5),(46,'2013-05-15 12:12:31','a',5),(47,'2013-05-15 12:13:46','z',5),(48,'2013-05-15 12:13:51','',5),(49,'2013-05-15 12:13:55','a',5),(50,'2013-05-15 12:14:27','a',5),(51,'2013-05-15 12:14:35','a',5),(52,'2013-05-15 12:14:38','a',5),(53,'2013-05-15 14:31:55','olá',2),(54,'2013-05-15 14:32:27','vc vem aki xD',5),(55,'2013-05-15 18:35:25','ola',5),(56,'2013-05-15 18:35:31','bla',2),(57,'2013-05-15 18:35:37','sdfg',5),(58,'2013-05-16 14:03:45','olá',5),(59,'2013-05-16 14:03:48','asdf',2),(60,'2013-05-16 14:03:50','asfasdf',2),(61,'2013-05-16 14:03:52','asdfsf',5);
/*!40000 ALTER TABLE `mensagens` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `perf_func`
--

DROP TABLE IF EXISTS `perf_func`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `perf_func` (
  `COD_PERFIL` int(11) NOT NULL,
  `COD_FUNCIONALIDADE` int(11) NOT NULL,
  PRIMARY KEY (`COD_PERFIL`,`COD_FUNCIONALIDADE`),
  KEY `IDX_PERF_FUNC_COD_PERFIL` (`COD_PERFIL`),
  KEY `IDX_PERF_FUNC_COD_FUNCIONALIDADE` (`COD_FUNCIONALIDADE`),
  CONSTRAINT `FRK_PERF_FUNC_COD_FUNCIONALIDADE` FOREIGN KEY (`COD_FUNCIONALIDADE`) REFERENCES `funcionalidades` (`COD_FUNCIONALIDADE`),
  CONSTRAINT `FRK_PERF_FUNC_COD_PERFIL` FOREIGN KEY (`COD_PERFIL`) REFERENCES `perfis` (`COD_PERFIL`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `perf_func`
--

LOCK TABLES `perf_func` WRITE;
/*!40000 ALTER TABLE `perf_func` DISABLE KEYS */;
INSERT INTO `perf_func` (`COD_PERFIL`, `COD_FUNCIONALIDADE`) VALUES (1,3),(1,5),(1,6),(1,7),(1,9),(2,2),(2,3),(2,4),(3,11),(3,12),(3,13),(3,14),(4,3),(4,8),(4,10),(4,11),(4,12),(4,13),(4,14),(4,15),(5,1),(5,3);
/*!40000 ALTER TABLE `perf_func` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `perfis`
--

DROP TABLE IF EXISTS `perfis`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `perfis` (
  `COD_PERFIL` int(11) NOT NULL AUTO_INCREMENT,
  `NOME` varchar(20) NOT NULL,
  PRIMARY KEY (`COD_PERFIL`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `perfis`
--

LOCK TABLES `perfis` WRITE;
/*!40000 ALTER TABLE `perfis` DISABLE KEYS */;
INSERT INTO `perfis` (`COD_PERFIL`, `NOME`) VALUES (1,'ADMINISTRADOR'),(2,'ATENDENTE'),(3,'DIRETORIA'),(4,'GERENTE'),(5,'MULHER');
/*!40000 ALTER TABLE `perfis` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `perguntas_frequentes`
--

DROP TABLE IF EXISTS `perguntas_frequentes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `perguntas_frequentes` (
  `COD_PERGUNTA_FREQUENTE` int(11) NOT NULL AUTO_INCREMENT,
  `PERGUNTA` varchar(100) NOT NULL,
  `RESPOSTA` varchar(100) NOT NULL,
  `DATA_CRIACAO` datetime NOT NULL,
  `COD_AREA` int(11) NOT NULL,
  PRIMARY KEY (`COD_PERGUNTA_FREQUENTE`),
  KEY `IDX_PERGUNTAS_FREQUENTES_COD_AREA` (`COD_AREA`),
  CONSTRAINT `FRK_PERGUNTAS_FREQUENTES_COD_AREA` FOREIGN KEY (`COD_AREA`) REFERENCES `areas` (`COD_AREA`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `perguntas_frequentes`
--

LOCK TABLES `perguntas_frequentes` WRITE;
/*!40000 ALTER TABLE `perguntas_frequentes` DISABLE KEYS */;
INSERT INTO `perguntas_frequentes` (`COD_PERGUNTA_FREQUENTE`, `PERGUNTA`, `RESPOSTA`, `DATA_CRIACAO`, `COD_AREA`) VALUES (1,'TESTANDO UMA PERGUNTA?','ESTA AI A RESPOSTA.','2013-04-13 00:00:00',1),(3,'VocÃª sabia que o sabia Ã© um passaro?','Sim porque?','2013-05-16 17:30:36',2);
/*!40000 ALTER TABLE `perguntas_frequentes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pessoas`
--

DROP TABLE IF EXISTS `pessoas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `pessoas` (
  `COD_PESSOA` int(11) NOT NULL AUTO_INCREMENT,
  `NOME` varchar(60) NOT NULL,
  `EMAIL` varchar(50) NOT NULL,
  `SEXO` varchar(1) NOT NULL,
  `ESTADO_CIVIL` varchar(20) NOT NULL,
  `TELEFONE_RESIDENCIAL` varchar(45) NOT NULL,
  `CELULAR` varchar(45) NOT NULL,
  `TELEFONE_COMERCIAL` varchar(45) NOT NULL,
  `COD_ENDERECO` int(11) NOT NULL,
  PRIMARY KEY (`COD_PESSOA`),
  UNIQUE KEY `UNK_PESSOAS_EMAIL` (`EMAIL`),
  KEY `IDX_PESSOAS_COD_ENDERECO` (`COD_ENDERECO`),
  CONSTRAINT `FRK_PESSOAS_COD_ENDERECO` FOREIGN KEY (`COD_ENDERECO`) REFERENCES `enderecos` (`COD_ENDERECO`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pessoas`
--

LOCK TABLES `pessoas` WRITE;
/*!40000 ALTER TABLE `pessoas` DISABLE KEYS */;
INSERT INTO `pessoas` (`COD_PESSOA`, `NOME`, `EMAIL`, `SEXO`, `ESTADO_CIVIL`, `TELEFONE_RESIDENCIAL`, `CELULAR`, `TELEFONE_COMERCIAL`, `COD_ENDERECO`) VALUES (1,'PESSOA 01 - ADM','adm@saam.com','M','SOLTEIRO','33333333','99999999','33333333',1),(2,'PESSOA 02 - ATD','atd@saam.com','F','SOLTEIRO','33333333','99999999','33333333',2),(3,'PESSOA 03 - DIR','dir@saam.com','F','SOLTEIRO','33333333','99999999','33333333',3),(4,'PESSOA 04 - GER','ger@saam.com','M','SOLTEIRO','33333333','99999999','33333333',4),(5,'PESSOA 05 - MLH','mlh@saam.com','F','SOLTEIRO','33333333','99999999','33333333',5),(7,'William Barreto','williamcpbarreto@yahoo.com.br','M','Solteiro','33312003','96127296','33033998',8),(9,'Nelson ','nelsongborges@gmail.com','F','Solteiro','479798789','479798789','479798789',10),(10,'William Barreto','bsi.william@gmail.com','M','Solteiro','33312003','96127296','33033998',12);
/*!40000 ALTER TABLE `pessoas` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tipo_atendimentos`
--

DROP TABLE IF EXISTS `tipo_atendimentos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tipo_atendimentos` (
  `COD_TIPO_ATENDIMENTO` int(11) NOT NULL AUTO_INCREMENT,
  `NOME` varchar(20) NOT NULL,
  PRIMARY KEY (`COD_TIPO_ATENDIMENTO`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tipo_atendimentos`
--

LOCK TABLES `tipo_atendimentos` WRITE;
/*!40000 ALTER TABLE `tipo_atendimentos` DISABLE KEYS */;
INSERT INTO `tipo_atendimentos` (`COD_TIPO_ATENDIMENTO`, `NOME`) VALUES (1,'Chat'),(2,'Mensagem');
/*!40000 ALTER TABLE `tipo_atendimentos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tipo_logs`
--

DROP TABLE IF EXISTS `tipo_logs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tipo_logs` (
  `COD_TIPO_LOG` int(11) NOT NULL AUTO_INCREMENT,
  `NOME` varchar(45) NOT NULL,
  PRIMARY KEY (`COD_TIPO_LOG`,`NOME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tipo_logs`
--

LOCK TABLES `tipo_logs` WRITE;
/*!40000 ALTER TABLE `tipo_logs` DISABLE KEYS */;
/*!40000 ALTER TABLE `tipo_logs` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usuarios`
--

DROP TABLE IF EXISTS `usuarios`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `usuarios` (
  `COD_USUARIO` int(11) NOT NULL AUTO_INCREMENT,
  `USUARIO` varchar(50) DEFAULT NULL,
  `SENHA` varchar(15) DEFAULT NULL,
  `COD_PERFIL` int(11) NOT NULL,
  `COD_PESSOA` int(11) NOT NULL,
  PRIMARY KEY (`COD_USUARIO`),
  UNIQUE KEY `UNK_USUARIOS_USUARIO` (`USUARIO`),
  KEY `IDX_USUARIOS_COD_PERFIL` (`COD_PERFIL`),
  KEY `IDX_USUARIOS_COD_PESSOA` (`COD_PESSOA`),
  CONSTRAINT `FRK_USUARIOS_COD_PERFIL` FOREIGN KEY (`COD_PERFIL`) REFERENCES `perfis` (`COD_PERFIL`),
  CONSTRAINT `FRK_USUARIOS_COD_PESSOA` FOREIGN KEY (`COD_PESSOA`) REFERENCES `pessoas` (`COD_PESSOA`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuarios`
--

LOCK TABLES `usuarios` WRITE;
/*!40000 ALTER TABLE `usuarios` DISABLE KEYS */;
INSERT INTO `usuarios` (`COD_USUARIO`, `USUARIO`, `SENHA`, `COD_PERFIL`, `COD_PESSOA`) VALUES (1,'admin','admin',1,1),(2,'atendente','atendente',2,2),(3,'diretoria','diretoria',3,3),(4,'gerente','gerente',4,4),(5,'mulher','mulher',5,5),(8,'bsi.william@gmail.com','william',2,10);
/*!40000 ALTER TABLE `usuarios` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `voluntarios`
--

DROP TABLE IF EXISTS `voluntarios`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `voluntarios` (
  `COD_VOLUNTARIO` int(11) NOT NULL AUTO_INCREMENT,
  `COD_AREA` int(11) NOT NULL,
  `SITUACAO` tinyint(4) DEFAULT '0',
  PRIMARY KEY (`COD_VOLUNTARIO`),
  KEY `IDX_VOLUNTARIOS_COD_AREA` (`COD_AREA`),
  KEY `IDX_VOLUNTARIOS_COD_VOLUNTARIO` (`COD_VOLUNTARIO`),
  CONSTRAINT `FRK_VOLUNTARIOS_COD_AREA` FOREIGN KEY (`COD_AREA`) REFERENCES `areas` (`COD_AREA`),
  CONSTRAINT `FRK_VOLUNTARIOS_COD_VOLUNTARIO` FOREIGN KEY (`COD_VOLUNTARIO`) REFERENCES `pessoas` (`COD_PESSOA`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `voluntarios`
--

LOCK TABLES `voluntarios` WRITE;
/*!40000 ALTER TABLE `voluntarios` DISABLE KEYS */;
INSERT INTO `voluntarios` (`COD_VOLUNTARIO`, `COD_AREA`, `SITUACAO`) VALUES (10,1,1);
/*!40000 ALTER TABLE `voluntarios` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2013-05-17 17:11:15
