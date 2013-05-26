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

Laden von Feiertagen

Die für die korrekte Berechnung des Urlaubszeitraums benötigten Daten lassen sich zur Zeit über einen Unit-Test
(CreateFeiertageTest) ausführen. Der Test schreibt die Feiertage der nächsten 10 Jahre in die Tabelle "feiertag".
am einfachsten geht das über das Kommando:

mvn test

## Build

Die Anwendung wird als Maven-Anwendung gebaut. 
Im Verzeichnis "anwesenheit": 

mvn install

Das War befindet sich dann im Verzeichnis anwesenheit-web/target/anwesenheit-web.war

## Integration in Eclipse
Um das Projekt in Eclipse zu bearbeiten muss Eclipse maven, WTP und git verstehen.
Die notwendigen Eclipse-Plugins können über den Eclipse Marketplace direkt installiert werden:

* Maven Integration for Eclipse WTP (a.k.a m2e-wtp)
* eGit


## Deployment

Die Anwendung wird als normales WAR auf dem Servlet-Container deployed.

## Verwenden JNDI auf JBoss

Auf JBoss verwendet die Anwendung eine JNDI-Datenquelle. D.h. auch, dass die Anwendung ohne konfigurierte
JNDI-Datenquelle nicht startet.

Der Name der Datenquelle ist: **AnwesenheitDS**. Eine mögliche Konfiguration der Datenquelle sieht in der Datei "standard.xml" folgendermaßen aus:

```xml
<datasource jndi-name="java:jboss/datasources/AnwesenheitDS" pool-name="AnwesenheitDS" enabled="true">
  <connection-url>jdbc:mysql://localhost/anwesenheit</connection-url>
  <driver>mysql</driver>
  <security>
      <user-name>anwesenheit</user-name>
      <password>anwesenheit</password>
  </security>
</datasource>
```

In den Treibern muss dann auch der My-SQL-Treiber stehen:

```xml
<drivers>
    <driver name="h2" module="com.h2database.h2">
        <xa-datasource-class>org.h2.jdbcx.JdbcDataSource</xa-datasource-class>
    </driver>
    <driver name="mysql" module="com.mysql"/>
</drivers>
```


Voraussetzung ist, dass der MySQL-Treiber auf dem JBoss installiert ist. Ein Beispiel für die Installation findet man beispielsweise hier:
http://javathreads.de/2011/09/jboss-as-7-mysql-datasource-konfigurieren/ .