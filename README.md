# Pipeline Command Center (v1.0)

A full-stack, enterprise-grade job application tracking system engineered entirely from scratch without relying on heavy external frameworks like Spring Boot or React. This project demonstrates a deep, foundational understanding of web architecture, cloud deployment, manual database bridging, and RESTful API design.

## 🔗 Live Demo
[**Click here to view the Live Full-Stack Dashboard**](https://dharshansundarraj.github.io/JobTracker-CoreJava/)

> **⚠️ Performance Note (Cold Starts):**
> The backend Java Engine is deployed on a free-tier Render instance, which spins down after periods of inactivity. **If the dashboard takes ~50 seconds to load data initially, the server is simply "waking up."** Once active, subsequent requests and data mutations are near-instantaneous. 

## 🏗️ Technical Architecture

* **The Engine (Backend):** Core Java utilizing `com.sun.net.httpserver` to build a lightweight, custom HTTP server that manually parses incoming JSON streams, handles CORS pre-flight requests, and operates inside a custom **Docker** container hosted on **Render**.
* **The Bridge (Database):** Raw JDBC (Java Database Connectivity) executing custom SQL queries for robust data manipulation, using environment variables for secure credential injection.
* **The Vault (Storage):** Remote, cloud-hosted relational **MySQL database (Aiven)** designed to persist application metadata, timelines, locations, and statuses globally.
* **The Interface (Frontend):** Vanilla JavaScript, HTML5, and CSS3 hosted on **GitHub Pages**, featuring a custom DOM-manipulation engine, interactive glass-morphism modals, and real-time analytical stat tracking.

## ✨ Key Features

* **Zero-Framework REST API:** Fully functional CRUD (Create, Read, Update, Delete) endpoints built natively in Java.
* **Custom UI Components:** Native OS-styled `<select>` dropdowns were replaced with custom, animated HTML/CSS floating menus utilizing precise z-index stacking contexts for a premium UX.
* **Real-Time Analytics:** JavaScript engine dynamically calculates and renders active pipeline statistics (Total, In Progress, Rejected) upon every database state change.
* **Asynchronous Fetching:** Seamless, no-reload UI updates using modern JavaScript `fetch()` APIs to communicate directly with the live Java backend.

## 🚀 Why Core Java?

While enterprise frameworks abstract away complexity, this application was intentionally built using raw foundational technologies to demonstrate mastery over:
1.  Opening and securely managing manual database connections across cloud environments.
2.  Parsing raw JSON payloads from HTTP InputStreams without external libraries.
3.  Managing HTTP Headers, Status Codes, and REST methodologies natively.
4.  Writing multi-stage Dockerfiles to containerize and deploy Java applications.

## 🔮 Future Roadmap (v2.0)
This serves as the foundational V1 release. Future upgrades planned for the pipeline include:
* Migrating to a paid, always-on cloud instance to eliminate cold-start delays.
* Implementing user authentication (JWT) for multi-user support.
* Adding a Spring Boot wrapper as a microservice comparison.
* Exporting analytics to CSV/PDF formats.

## ⚙️ Local Setup Instructions (For Developers)

1.  Execute the provided SQL schema in your local MySQL instance to create the `applications` table.
2.  Set up local environment variables (e.g., `DB_PASSWORD`) on your machine.
3.  Ensure your `DatabaseConnection.java` points to your local database URL.
4.  Compile and run `JobTrackerAPI.java` (or use `mvn clean package`) to spin up the server locally.
5.  Open `index.html` in a local browser (ensure the `API_URL` in the JavaScript file points to `http://localhost:8080`).
