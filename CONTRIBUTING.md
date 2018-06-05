Prerequisites
==============
| **Tool** | **Required Version** | **How to check**  | **Comments** |
| --- | --- | --- | --- |
| Java | 1.8.x | java -version | |
| Maven | 3.2.3 or 3.2.5 | mvn -version | Newer versions should also work |
| Git | any (latest preferable) | git --version | |
| MySQL | 5.6.x (or newer) | mysql --version | |

Setup
======

Java
----------
Download and install latest JDK from `http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html`

Maven
----------
Download and install Maven from `https://maven.apache.org/download.cgi`

MySQL
-----------
Manual installation

  * Download and install latest MySQL community server from `http://dev.mysql.com/downloads/mysql/`
  * Login to mysql
  > `mysql -u root -p`
  * Create database: 
  > `betoshook_db`;
  * Set Default Collation:
  > `utf8_general_ci`;
 
Oauth 2 endpoints
----------- 
  Attempt to access resources [REST API] without any authorization [will fail of-course].
  GET http://localhost:8080/protected/user/
  
  Ask for tokens[access+refresh] using HTTP POST on /oauth/token, with grant_type=password,and resource owners credentials as req-params. Additionally, send client credentials in Authorization header.
  POST http://localhost:8080/oauth/token?grant_type=password&username=bill&password=abc123
  
  Ask for a new access token via valid refresh-token, using HTTP POST on /oauth/token, with grant_type=refresh_token,and sending actual refresh token. Additionally, send client credentials in Authorization header.
  POST http://localhost:8080/oauth/token?grant_type=refresh_token&refresh_token=094b7d23-973f-4cc1-83ad-8ffd43de1845
  
  Access the resource by providing an access token using access_token query param with request.
  GET http://localhost:8080/protected/user/?access_token=3525d0e4-d881-49e7-9f91-bcfd18259109
  
  "/oauth/token" (get, post)
  oauth/authorize
  
  Enable Lombok
  -----------