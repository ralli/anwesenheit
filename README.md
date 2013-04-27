# Prototyp für die Anlage von Urlaubsanträgen

Das hier ist ein experimentelles Projekt zur Anlage und Verwaltung von Urlaubsanträgen o.Ä.

## Systemvoraussetzungen

Das Projekt ist eine Java-Web-Anwendung. 

* Servlet-Container (Tomcat, Jetty, JBoss etc.)
* MySQL-Datenbank
* Java 1.6 (Höher geht auch)

## Datenbankeinstellungen

Die Datenbankzugangsdaten sind hartcodiert. Die Datenbank liegt auf localhost, hat den Namen 
"anwesenheit" mit Benutzername und Passwort "anwesenheit".

Anlage der Datenbank:

In mysql:

create database anwesenheit default character set utf8;
grant all on anwesenheit.* to 'anwesenheit'@'localhost' identified by 'anwesenheit';

Anlage des Datenbankschemas (im Unterprojekt "anwesenheit-core"):

mvn liquibase:update

Laden von Testdaten:

mysql -panwesenheit -uanwesenheit anwesenheit < src/main/db/testdata.sql

Es gibt vier Testbenutzer (alle mit Passwort "test"):

* boss
* chef
* demo
* ralli

Der Benutzer "demo" hat Sonderberechtigungen, d.h. dieser Benutzer kann sich die Urlaubsanträge aller anderen 
Benutzer anzeigen lassen.

## Build

Die Anwendung wird als Maven-Anwendung gebaut. 
Im Verzeichnis "anwesenheit": 

mvn install

Das War befindet sich dann im Verzeichnis anwesenheit-web/target/anwesenheit-web.war


## Deploy

Die Anwendug wird als normales WAR auf dem Servlet-Container deployed.
