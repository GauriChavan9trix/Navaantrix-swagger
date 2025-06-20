
📁 Project Structure – Navaantrix Swagger
bash
Copy
Edit
NavaantrixSwagger/
├── src/
│   ├── main/
│   │   ├── java/com/example/demo/
│   │   │   ├── NavaantrixSwaggerApplication.java    # Main Spring Boot application
│   │   │   ├── config/                              # Configuration files
│   │   │   ├── controller/                          # REST controllers
│   │   │   ├── model/                               # Entity models
│   │   │   ├── repository/                          # JPA repositories
│   │   │   ├── service/                             # Service layer
│   │   │   ├── webConfig/                           # CORS and WebConfig
│   │   │   └── YamlGenerator/                       # YAML file generator logic
│   │   └── resources/
│   │       ├── static/                              # Static assets (CSS, JS)
│   │       ├── templates/                           # Thymeleaf or other templates (if used)
│   │       └── application.properties               # Spring Boot configuration
│   └── test/                                        # Test classes and resources
├── pom.xml                                          # Maven build configuration
🗃️ MySQL Database: swagger
This project uses a MySQL database named swagger. Below are the key tables used in the backend:

Table Name	Purpose
project	Stores project-level metadata and details
api_info	Contains API endpoint metadata (method, path, summary, etc.)
api_auth	Stores authentication and authorization data for APIs
api_field	Represents request/response fields for APIs
api_header	Defines custom headers required or returned by an API
api_param	Stores query/path/header parameters associated with APIs

Tech Stack
Backend: Spring Boot (Java 17)

Build Tool: Maven

Database: MySQL

ORM: Spring Data JPA

YAML Support: SnakeYAML

Version Control: Git & GitHub

