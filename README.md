
# Custom URL Shortener  
URL Shortener is simple, early-stage Spring Boot Application used for reducing long links. Currently I focus on Backend module implementation.  
Tech stack (at the moment):  
 * Spring Boot
 * MySQL
 * H2
 * Gradle  

## Getting started
### Prerequisites
These of following must be installed on your local machine:  
 * JDK 11  
 * MySQL 8  
 * Git  

### Set up database
Connect to MySQL and create new database:
```sh
mysql -u yourusername -p
create database url_shortener;
```

### Set up environment variables
Windows (requires administrator):
```sh
setx MYSQL_USER "yourusername" /M
setx MYSQL_USER "yourpassword" /M
```
or, on Linux:
```sh
echo "export MYSQL_USER=yourusername" >> ~/.profile
echo "export MYSQL_PASSWORD=yourpassword" >> ~/.profile
```
### First run
Clone repository:  
```sh
git clone https://github.com/monaboiste/url-shortener.git
```  
Run MySQL Server:
```sh
mysqld
```
or, on Linux:
```sh
sudo /etc/init.d/mysql start
```

Then cd into ``url-shortener`` directory and execute:  
```sh
./gradlew :bootRun -Dspring.config.location="startup.yaml"
```
**COMMAND ABOVE WILL DROP url_shortener DATABASE**, so execute it only if you run the application first time!

Gradle should build project and start Tomcat Server on your localhost. Base URL of Web Api: [http://localhost:8080/api](http://localhost:8080/api).
  
From now on you can build and run application with:
```sh
./gradlew :bootRun
```  

#### Tests
Run unit tests:
```sh
./gradlew test
```  
Run integration tests:
```sh
./gradlew integrationTest
```  
### Available Endpoints
🚧 WIP 🚧