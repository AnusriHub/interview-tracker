# 🎯 Interview Tracker

> **Track applications. Monitor progress. Stay organized.**

Interview Tracker is a Spring Boot web application designed to help job and internship seekers manage their applications throughout the recruitment process.

Instead of maintaining application details across spreadsheets, notes, and bookmarks, the application provides a centralized dashboard to record applications, update their status, search and filter opportunities, store recruiter details, and monitor overall application progress.

---

## ✨ Features

### 📊 Application Dashboard

The dashboard provides an overview of the complete job application pipeline.

* Total applications
* Applied applications
* Online assessments
* Interviews
* Offers
* Rejected applications
* Withdrawn applications
* Success rate
* Interview rate
* Recently added applications

### 📝 Application Management

Users can create and manage job or internship applications with:

* Company name
* Job title
* Job URL
* Location
* Application status
* Applied date
* Recruiter / contact person
* Contact email
* Personal notes

Supported application statuses:

```text
APPLIED
   ↓
ONLINE ASSESSMENT
   ↓
INTERVIEW
   ↓
OFFER

Alternative outcomes:
   ├── REJECTED
   └── WITHDRAWN
```

### 🔍 Search & Filtering

Applications can be filtered by:

* Application status
* Company name
* Job title

The search field uses debouncing to avoid submitting the form on every keystroke.

### ✏️ Edit Applications

Existing applications can be updated whenever recruitment details change.

For example:

```text
Applied
   ↓
Online Assessment
   ↓
Interview
   ↓
Offer
```

### 🗑️ Delete Applications

Applications can be removed from the tracker when they are no longer needed.

### ⚡ Quick Status Updates

Application status can be updated directly from the application detail page without editing the complete application.

### 🌐 REST API

The application also exposes REST endpoints for retrieving applications, dashboard statistics, and updating application status programmatically.

---

## 🏗️ Architecture

The project follows a layered Spring MVC architecture:

```text
┌──────────────────────────────┐
│          Browser             │
│   HTML / CSS / Vanilla JS    │
└──────────────┬───────────────┘
               │
               ▼
┌──────────────────────────────┐
│       Spring MVC             │
│       Controllers            │
│                              │
│ ApplicationController        │
│ ApiController                │
└──────────────┬───────────────┘
               │
               ▼
┌──────────────────────────────┐
│          Service             │
│                              │
│     ApplicationService       │
│                              │
│  Business Logic + DTO        │
│  Conversion + Statistics     │
└──────────────┬───────────────┘
               │
               ▼
┌──────────────────────────────┐
│        Repository            │
│                              │
│   JobApplicationRepository   │
│       Spring Data JPA        │
└──────────────┬───────────────┘
               │
               ▼
┌──────────────────────────────┐
│        H2 Database           │
│      In-Memory Storage       │
└──────────────────────────────┘
```

---

## 🧩 Project Components

### Controller Layer

Handles HTTP requests and connects the web interface with the service layer.

```text
ApplicationController
├── Dashboard
├── Application List
├── Create Application
├── Edit Application
├── View Application
├── Delete Application
└── Update Status

ApiController
├── Get Applications
├── Get Dashboard Statistics
└── Quick Status Update
```

### Service Layer

`ApplicationService` contains the application's business logic.

Responsibilities include:

* Fetching applications
* Creating applications
* Updating applications
* Deleting applications
* Searching applications
* Filtering applications
* Calculating dashboard statistics
* Converting entities to DTOs

### Repository Layer

`JobApplicationRepository` extends Spring Data JPA's `JpaRepository`.

It provides:

* CRUD operations
* Status-based queries
* Search queries
* Application counting
* Combined status + search filtering

---

## 🗄️ Data Model

The application uses a single main entity: `JobApplication`.

```text
JobApplication
├── id
├── companyName
├── jobTitle
├── jobUrl
├── location
├── status
├── appliedDate
├── notes
├── contactPerson
├── contactEmail
├── createdAt
└── updatedAt
```

The corresponding database table is:

```text
job_applications
```

---

## 📈 Dashboard Metrics

The dashboard calculates two useful recruitment metrics.

### Success Rate

The success rate is calculated using offers:

```text
Success Rate =
(Offers / Total Applications) × 100
```

### Interview Rate

The interview rate includes applications that reached the interview stage or resulted in an offer:

```text
Interview Rate =
(Interviews + Offers) / Total Applications × 100
```

This gives a quick view of how applications are progressing through the recruitment pipeline.

---

## 🔎 Application Filtering

The application supports combined filtering by status and search term.

For example:

```text
Status: INTERVIEW
Search: Java
```

The backend retrieves applications where:

```text
status = INTERVIEW
AND
(companyName contains "Java"
 OR jobTitle contains "Java")
```

The filtering is implemented using a custom JPQL query in `JobApplicationRepository`.

---

## 🌐 REST API

Base URL:

```text
/api
```

### Get Applications

```http
GET /api/applications
```

Optional parameters:

```text
/api/applications?status=INTERVIEW
/api/applications?search=Java
/api/applications?status=INTERVIEW&search=Java
```

Returns the matching applications.

---

### Get Dashboard Statistics

```http
GET /api/stats
```

Returns statistics such as:

```json
{
  "totalApplications": 10,
  "applied": 4,
  "onlineAssessment": 2,
  "interview": 2,
  "offer": 1,
  "rejected": 1,
  "withdrawn": 0
}
```

---

### Update Application Status

```http
PATCH /api/applications/{id}/status
```

Example request:

```json
{
  "status": "INTERVIEW"
}
```

Example successful response:

```json
{
  "success": true,
  "status": "Interview",
  "cssClass": "status-interview"
}
```

---

## ⚙️ Tech Stack

| Layer                 | Technology                    |
| --------------------- | ----------------------------- |
| Language              | Java 11                       |
| Backend               | Spring Boot 2.7.18            |
| Web Framework         | Spring MVC                    |
| Template Engine       | Thymeleaf                     |
| ORM                   | Spring Data JPA               |
| Persistence           | Hibernate                     |
| Database              | H2 In-Memory Database         |
| Frontend              | HTML, CSS, Vanilla JavaScript |
| UI                    | Custom CSS                    |
| Validation            | Jakarta/Javax Validation      |
| API                   | Spring REST                   |
| Build Tool            | Maven                         |
| Boilerplate Reduction | Lombok                        |
| Development           | Spring Boot DevTools          |

---

## 📁 Project Structure

```text
interview-tracker/
│
├── pom.xml
│
├── src/
│   ├── main/
│   │   │
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── tracker/
│   │   │           │
│   │   │           ├── InterviewTrackerApplication.java
│   │   │           │
│   │   │           ├── controller/
│   │   │           │   ├── ApiController.java
│   │   │           │   └── ApplicationController.java
│   │   │           │
│   │   │           ├── dto/
│   │   │           │   ├── ApplicationDTO.java
│   │   │           │   └── DashboardStats.java
│   │   │           │
│   │   │           ├── model/
│   │   │           │   ├── ApplicationStatus.java
│   │   │           │   └── JobApplication.java
│   │   │           │
│   │   │           ├── repository/
│   │   │           │   └── JobApplicationRepository.java
│   │   │           │
│   │   │           └── service/
│   │   │               └── ApplicationService.java
│   │   │
│   │   └── resources/
│   │       │
│   │       ├── static/
│   │       │   ├── css/
│   │       │   │   └── main.css
│   │       │   └── js/
│   │       │       └── main.js
│   │       │
│   │       ├── templates/
│   │       │   ├── dashboard.html
│   │       │   ├── applications.html
│   │       │   ├── application-form.html
│   │       │   ├── application-detail.html
│   │       │   ├── layout.html
│   │       │   └── fragments/
│   │       │       └── layout.html
│   │       │
│   │       └── application.properties
│   │
│   └── test/
│
└── screenshots/
    ├── dashboard.png
    └── application-form.png
```

---

## 🚀 Getting Started

### Prerequisites

Make sure you have:

* Java 11 or later
* Maven 3.6+
* Git

No external database installation is required because the project uses an H2 in-memory database.

---

### 1. Clone the Repository

```bash
git clone <repository-url>
cd interview-tracker
```

---

### 2. Build the Project

```bash
mvn clean install
```

---

### 3. Run the Application

```bash
mvn spring-boot:run
```

Or run:

```text
InterviewTrackerApplication.java
```

directly from IntelliJ IDEA.

---

### 4. Open the Application

```text
http://localhost:8080
```

---

## 🗃️ H2 Database

The application uses an in-memory H2 database.

Current configuration:

```properties
spring.datasource.url=jdbc:h2:mem:interviewdb;DB_CLOSE_DELAY=-1
spring.datasource.username=root
spring.datasource.password=Anusri@123
```

The H2 console is enabled at:

```text
/h2-console
```

Database configuration:

```text
JDBC URL:
jdbc:h2:mem:interviewdb

Username:
root
```

> For a production deployment, database credentials should be moved to environment variables rather than stored directly in `application.properties`.

---

## 🧠 Key Backend Concepts Demonstrated

This project was built to practice and demonstrate practical Spring Boot concepts.

### 1. Spring MVC

```text
Request
   ↓
Controller
   ↓
Service
   ↓
Repository
   ↓
Database
```

### 2. Dependency Injection

Dependencies are injected through Lombok's:

```java
@RequiredArgsConstructor
```

instead of field injection.

### 3. Spring Data JPA

The repository extends:

```java
JpaRepository<JobApplication, Long>
```

which provides standard CRUD operations.

### 4. Custom JPQL Query

The application uses a custom query for combined filtering:

```java
@Query("""
    SELECT j FROM JobApplication j
    WHERE (:status IS NULL OR j.status = :status)
    AND (:search IS NULL OR
         LOWER(j.companyName) LIKE LOWER(CONCAT('%', :search, '%'))
         OR
         LOWER(j.jobTitle) LIKE LOWER(CONCAT('%', :search, '%')))
    ORDER BY j.createdAt DESC
""")
```

### 5. DTO Pattern

`ApplicationDTO` is used for form data instead of directly binding the web form to the entity.

```text
Form
 ↓
ApplicationDTO
 ↓
ApplicationService
 ↓
JobApplication
 ↓
Repository
```

### 6. Validation

Required fields are validated using annotations such as:

```java
@NotBlank
@NotNull
```

Invalid form submissions are returned to the form with validation errors.

### 7. REST API

The application exposes REST endpoints using:

```java
@RestController
@RequestMapping("/api")
```

### 8. Enum-Based Status Management

Application states are represented using:

```java
public enum ApplicationStatus {
    APPLIED,
    OA,
    INTERVIEW,
    OFFER,
    REJECTED,
    WITHDRAWN
}
```

This prevents arbitrary status values from being stored.

---

## ⚡ Frontend JavaScript

The application uses Vanilla JavaScript for lightweight client-side interactions.

### Auto-Dismissing Alerts

Success messages automatically disappear after four seconds.

### Debounced Search

Search requests are delayed by 400 ms after the user stops typing.

```text
User types
   ↓
400ms delay
   ↓
Submit search
```

### Animated Dashboard Statistics

Dashboard numbers animate from zero to their actual values when the page loads.

### Status Change Confirmation

Users are asked for confirmation before changing an application's status.

---

## 🔮 Future Improvements

* [ ] User authentication and authorization
* [ ] Multiple user accounts
* [ ] PostgreSQL / MySQL production database
* [ ] JWT-based authentication
* [ ] Application reminders
* [ ] Interview date and reminder tracking
* [ ] Company-wise analytics
* [ ] Monthly application analytics
* [ ] Pagination for large application lists
* [ ] Advanced filtering
* [ ] Resume attachment per application
* [ ] Interview notes and preparation checklist
* [ ] Email notifications
* [ ] Docker support
* [ ] Cloud deployment
* [ ] React frontend

---

## 💼 Interview Talking Points

### Why did you use a service layer?

The service layer separates business logic from HTTP request handling and database access.

```text
Controller
   ↓
Business Logic
   ↓
Repository
```

This makes the application easier to maintain and test.

### Why use DTO instead of directly using the entity?

`ApplicationDTO` separates web-layer input from the persistence entity.

It prevents the controller from being tightly coupled to the database model and gives the application a dedicated object for handling form data.

### How does filtering work?

The controller receives optional `status` and `search` parameters.

The service converts the status string into the `ApplicationStatus` enum and passes the filters to the repository, where a JPQL query performs the combined filtering.

### How are dashboard statistics calculated?

The service uses repository count queries for each application status:

```text
Total
Applied
Online Assessment
Interview
Offer
Rejected
Withdrawn
```

These values are assembled into `DashboardStats`, which also calculates the success and interview rates.

### How does the REST API fit into the application?

The web interface uses Spring MVC and Thymeleaf, while `ApiController` exposes JSON endpoints that can be consumed by external clients or a future frontend such as React.

---

## 🎯 Learning Outcomes

Through this project, I practiced:

* Building a Spring Boot MVC application
* Designing a layered backend architecture
* Working with Spring Data JPA
* Creating custom JPQL queries
* Using DTOs
* Implementing validation
* Designing REST APIs
* Working with enums
* Managing CRUD operations
* Building server-rendered Thymeleaf pages
* Adding client-side JavaScript interactions
* Calculating application analytics
* Using H2 for rapid development and testing

---

## 👩‍💻 Author

**Anusri R.**

Java | Spring Boot | SQL | Backend Development

---

## ⭐ Project Goal

> **Turn a scattered job-search process into one organized application pipeline.**

Interview Tracker was built as a practical Java backend project to understand how a real-world CRUD application can combine Spring Boot, JPA, database persistence, server-side rendering, REST APIs, validation, and frontend interactions.
