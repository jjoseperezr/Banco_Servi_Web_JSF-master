CREATE DATABASE user;

USE user;



CREATE TABLE users (
  id int(11) NOT NULL PRIMARY KEY AUTO_INCREMENT,
  name varchar(100) NOT NULL,
  email varchar(50) NOT NULL,
  password varchar(20) NOT NULL,
  gender varchar(1) NOT NULL,
  address text NOT NULL
);



INSERT INTO users (name, email, password, gender, address)
          VALUES ("admin", "admin@email.com", "admin", "M", "Administracion");


INSERT INTO users (name, email, password, gender, address)
          VALUES ("jhon", "jhon@email.com", "soyjhon", "M", "Urbano");

          
INSERT INTO users (name, email, password, gender, address)
          VALUES ("juan", "juan@email.com", "soyjuan", "M", "Urbano");



CREATE TABLE solicitudes (
  id int(11) NOT NULL PRIMARY KEY AUTO_INCREMENT,
  nombre varchar(50) NOT NULL,
  correo varchar(30) NOT NULL,
  fechanacimiento date NOT NULL,
  estadosolicitud varchar(15) NOT NULL,
  origentramite varchar(15) NOT NULL,
  cedula int(15) NOT NULL
);