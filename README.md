
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
 * Git  

### Clone, build and run
Clone repository:  
```sh
$ git clone https://github.com/monaboiste/url-shortener.git
```  

Then cd into ``url-shortener`` directory and execute:  
```sh
$ ./gradlew :bootRun
```  
Gradle should build project and start Tomcat Server on your localhost. Base URL of Web Api: [http://localhost:8080/api](http://localhost:8080/api).
  
#### Tests
Run unit tests:
```sh
$ ./gradlew test
```  
Run integration tests:
```sh
$ ./gradlew integrationTest
```  
### Available Endpoints
🚧 WIP 🚧