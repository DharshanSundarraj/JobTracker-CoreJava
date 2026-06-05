# Pipeline Command Center

A full-stack, enterprise-grade job application tracking system engineered entirely from scratch without relying on heavy external frameworks like Spring Boot or React. This project demonstrates a deep, foundational understanding of web architecture, manual database bridging, and RESTful API design.

## 🔗 Live Demo
[**Click here to view the Live Frontend UI**](https://dharshansundarraj.github.io/JobTracker-CoreJava/)

*(Note: This GitHub Pages demo showcases the responsive frontend UI, interactive components, and Vanilla JS filtering logic. The backend and database are currently configured for local execution).*

## 🏗️ Technical Architecture

* **The Engine (Backend):** Core Java utilizing `com.sun.net.httpserver` to build a lightweight, custom HTTP server that manually parses incoming JSON streams and handles CORS pre-flight requests.
* **The Bridge (Database):** Raw JDBC (Java Database Connectivity) executing custom SQL queries for robust data manipulation.
* **The Vault (Storage):** Relational MySQL database designed to persist application metadata, timelines, locations, and statuses.
* **The Interface (Frontend):** Vanilla JavaScript, HTML5, and CSS3 featuring a custom DOM-manipulation engine, interactive glass-morphism modals, and real-time analytical stat tracking.

## ✨ Key Features

* **Zero-Framework REST API:** Fully functional CRUD (Create, Read, Update, Delete) endpoints built natively in Java.
* **Custom UI Components:** Native OS-styled `<select>` dropdowns were replaced with custom, animated HTML/CSS floating menus utilizing precise z-index stacking contexts for a premium UX.
* **Real-Time Analytics:** JavaScript engine dynamically calculates and renders active pipeline statistics (Total, In Progress, Rejected) upon every database state change.
* **Asynchronous Fetching:** Seamless, no-reload UI updates using modern JavaScript `fetch()` APIs to communicate directly with the Java backend.

## 🚀 Why Core Java?

While enterprise frameworks abstract away complexity, this application was intentionally built using raw foundational technologies to demonstrate mastery over:
1.  Opening and securely managing manual database connections.
2.  Parsing raw JSON payloads from HTTP InputStreams.
3.  Managing HTTP Headers, Status Codes, and REST methodologies natively.

## ⚙️ Local Setup Instructions

1.  Execute the provided SQL schema in your local MySQL instance to create the `core_pipeline` database with the updated date and location columns.
2.  Update the `DatabaseConnection.java` file with your local MySQL credentials.
3.  Compile and run `JobTrackerAPI.java` to spin up the server on port `8080`.
4.  Open `index.html` in any modern web browser to access the dashboard.
