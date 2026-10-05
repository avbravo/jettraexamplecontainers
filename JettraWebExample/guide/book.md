# JettraWebExample - Comprehensive Guide & Architecture Manual

## 1. Overview & Architecture
`JettraWebExample` is a complete reference application demonstrating the practical integration of the Jettra technology stack: `JettraAppServer`, `JettraWUI`, `JettraStoreEngine` (with the `RECORDS` engine), `JettraJWT`, and `JettraRules`.

---

## 2. Key Features & Highlights
- **End-to-End Enterprise Workflows**: Demonstrates authentication, CRUD views, Kanban boards, and data tables.
- **Java 25 Records as Domain Models**: Uses immutable records for DTOs, domain events, and repository storage.
- **Dynamic Theming & Responsive Layouts**: Pre-configured dark mode and modern glassmorphism aesthetics.
- **Multi-Model Database Integration**: Shows real-time storage into `DOCUMENT`, `KEYVALUE`, and `RECORDS` engines.

---

## 3. Running the Example Application
```bash
mvn clean compile exec:java
```

Navigate to `http://localhost:8080` in your web browser.
