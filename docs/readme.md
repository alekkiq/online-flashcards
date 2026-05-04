# ONLINE FLASHCARDS

This is the second year Software Engineering project. The application is a quiz learning platform where the main learning method is flashcard learning.

- **Project name:** Online Flashcards (OnlyCards)
- **Problem solved:** Fragmented tooling for flashcard study — content creation, group study, and progress tracking are normally split across multiple apps.
- **Target users:** Students, Teachers, Admins
- **Main technologies:** React 19, Spring Boot 4, MariaDB 11, Docker, Jenkins, Railway
- **Overall duration:** 8 sprints × 2 weeks (≈ 16 weeks)

## 1. PRODUCT DESCRIPTION

**OnlyCards** is a web-based quiz and flashcard learning platform designed for students and educators. Users can create, share, discover, and study flashcard quizzes across a variety of subjects — all through a modern, responsive single-page application.

### Core Features

#### Authentication & User Roles

The platform supports three user roles with increasing privileges:

- **Student** — the default role. Can browse, search, and play quizzes, join classrooms, and view results.
- **Teacher** — a promoted role. Can do everything a student can, plus create and manage quizzes and classrooms.
- **Admin** — has full administrative privileges, including approving or rejecting promotion requests and managing all users and classrooms.

Users register with a username, email, and password. Authentication is handled via **JWT tokens**, so sessions are stateless and secure. Students who want to create content can submit a **promotion request** to be upgraded to the Teacher role, which an Admin can then approve or reject.

#### Quizzes & Flashcards

The central learning unit is the **quiz**, which is a named collection of **flashcards**. Each flashcard has a question side and an answer side. Quizzes are categorized by **subject** (e.g. Mathematics, History) for easy filtering.

Teachers create quizzes by giving them a title, description, subject, and then adding individual flashcards. Quizzes are publicly searchable and playable by all users.

#### Quiz Game

When a user plays a quiz, they are presented with flashcards one at a time in an interactive **flip-card** UI. The user reads the question, mentally answers, then flips the card to reveal the correct answer and self-evaluates. Navigation between cards is animated with sliding transitions. After completing all cards, the result (score percentage) is recorded and the user is taken to a **results page**.

#### Quiz Results & Progress Tracking

Each quiz attempt is saved as a **quiz result**, recording the score percentage and timestamp. This allows users to track their learning progress over time and revisit quizzes to improve.

#### Search & Discovery

The **Search Quizzes** page lets any visitor — authenticated or not — browse and search the full library of community-created quizzes. Quizzes can be filtered by subject. Each quiz card shows the title, creator, subject, and flashcard count.

#### Classrooms

Teachers and Admins can create **classrooms** — virtual study groups organized around a subject. A classroom has:

- A **title**, **description**, and optional **note**
- A **join code** that students use to enroll
- **Quizzes** assigned to the classroom for guided study
- **Learning materials** (text-based resources) uploaded by the owner
- A **members list** managed by the owner

Classroom owners can add or remove members, attach quizzes, and upload learning materials. Students can join using the code, view all classroom content, and leave at any time.

#### Profile Management

Authenticated users have a **profile page** where they can:

- View their username, email, and role
- Edit their profile (change username, email, or password)
- Submit a promotion request (Student > Teacher)
- See quick links to their quizzes and classrooms

## 2. PRODUCT VISION

Detailed vision document: [product_vision.pdf](./planning/product_vision.pdf)

**Vision statement:** Empower students and teachers to learn and teach more effectively through a single, multilingual platform where flashcard quizzes can be created, shared, and practiced inside virtual classrooms.

**Main goals:**
- Make flashcard study fast, collaborative, and language-inclusive.
- Give teachers lightweight tools to organize quizzes and classrooms without IT overhead.
- Track learner progress over time so users can identify weak topics.
- Stay deployable on commodity cloud (Docker / Railway) so it remains low-cost to operate.

**Definition of success:** the full stack runs end-to-end via `docker compose up` and on Railway; backend + frontend test suites pass in CI with coverage published; SonarQube quality gate passes; and the UI is usable in all four supported languages (incl. RTL).

## 3. TECHNOLOGIES USED

### Frontend
| Technology | Purpose |
|---|---|
| **React 19** | UI library |
| **Vite 7** | Build tool & dev server |
| **i18next** + **react-i18next** | Frontend internationalization and localization |
| **Tailwind CSS 4** | Utility-first CSS framework |
| **React Router 7** | Client-side routing |
| **React Hook Form** + **Zod** | Form handling & validation |
| **Lucide React** | Icon library |
| **Nginx** | Production static file serving & reverse proxy |

### Backend

| Technology                          | Purpose                        |
| ----------------------------------- | ------------------------------ |
| **Java 17**                         | Programming language           |
| **Spring Boot 4**                   | Application framework          |
| **Spring Security**                 | Authentication & authorization |
| **Spring Data JPA** / **Hibernate** | ORM & database access          |
| **JWT (jjwt)**                      | Token-based authentication     |
| **Jackson**                         | JSON serialization             |
| **SpringDoc OpenAPI**               | API documentation (Swagger UI) |
| **Maven**                           | Build & dependency management  |

### Database

| Technology     | Purpose             |
| -------------- | ------------------- |
| **MariaDB 11** | Relational database |

### Testing

| Technology                     | Purpose                            |
| ------------------------------ | ---------------------------------- |
| **Vitest** + **jsdom**         | Frontend unit testing              |
| **React Testing Library**      | Component testing                  |
| **V8 Coverage**                | Frontend code coverage             |
| **JUnit 5** (Spring Boot Test) | Backend unit & integration testing |
| **JaCoCo**                     | Backend code coverage              |
| **Playwright**                 | End-to-end testing                 |

### DevOps & Deployment

| Technology         | Purpose                       |
| ------------------ | ----------------------------- |
| **Docker**         | Containerization              |
| **Docker Compose** | Multi-container orchestration |
| **Jenkins**        | CI/CD pipeline                |
| **Docker Hub**     | Container image registry      |
| **Railway**        | Cloud hosting & deployment    |

### Code Quality

| Technology        | Purpose                             |
| ----------------- | ----------------------------------- |
| **ESLint 9**      | Frontend linting                    |
| **Prettier**      | Code formatting                     |
| **SonarQube**     | Static code analysis & quality gate |

## 4. Project Plan & Sprint Structure

Full project plan: [project_planning.pdf](./planning/project_planning.pdf)

- **Methodology:** Agile / Scrum
- **Sprint length:** 2 weeks
- **Total sprints:** 8

| Sprint | Focus                              | Primary Deliverable                                |
| ------ | ---------------------------------- | -------------------------------------------------- |
| 1      | Project Planning & Vision          | Backlog, vision, risk/scope                        |
| 2      | Requirements & Database            | Use case + ER diagrams, DB schema, unit test setup |
| 3      | UI Implementation & CI             | React SPA + Jenkins pipeline                       |
| 4      | Docker Containerization            | Dockerfiles + `docker-compose.yaml`                |
| 5      | UI Localization (+ K8s if applied) | i18next translations for 4 languages               |
| 6      | Database Localization              | Row-method localized content + schema migration    |
| 7      | Quality Assurance                  | SonarQube, UAT, heuristic evaluation, e2e tests    |
| 8      | Documentation & Finalization       | Final docs, architecture diagrams, deployment      |

---

## 5. Sprint 1 – Project Planning & Vision

Foundational planning artifacts produced in this sprint:

- **Project plan summary** — scope, timeline, and roles documented in [project_planning.pdf](./planning/project_planning.pdf).
- **Backlog creation** — initial product backlog drafted from the vision document.
- **Vision validation** — captured in [product_vision.pdf](./planning/product_vision.pdf).
- **Risk and scope definition** — covered inside the project plan (technical risk, schedule risk, scope boundaries).

Reports:

- [Sprint 1 Planning Report](./sprint_report/sprint_planning_report_1.pdf)
- [Sprint 1 Review Report](./sprint_report/sprint_review_report_1.pdf)

---

## 6. Sprint 2 – Requirements & Database

System requirements and data design.

- **Functional requirements summary** — auth, quiz CRUD, flashcard play, classroom management, results tracking, multilingual content.
- **Use Case Diagram:** [use-case.pdf](./diagrams/use-case.pdf)
- **ER Diagram:** [er-diagram-l10n.png](./diagrams/er-diagram-l10n.png)
- **Database schema:** [db-diagram-l10n.png](./diagrams/db-diagram-l10n.png)
- **Database technology:** MariaDB 11 (relational, run via Docker).
- **Database implementation:** Spring Data JPA / Hibernate manages the schema; `data.sql` seeds reference data (subjects, default admin).
- **Unit testing strategy:**
  - Backend: JUnit 5 via Spring Boot Test, with `@DataJpaTest` for repositories and `@WebMvcTest` for controllers.
  - Frontend: Vitest + jsdom + React Testing Library.
  - Coverage: JaCoCo (backend) and V8 coverage (frontend).

Reports:

- [Sprint 2 Planning Report](./sprint_report/sprint_planning_report_2.pdf)
- [Sprint 2 Review Report](./sprint_report/sprint_review_report_2.pdf)

---

## 7. Sprint 3 – UI Implementation & CI

User interface and CI automation.

- **UI framework & approach:** React 19 + Vite 7 + Tailwind CSS 4, with React Router 7 for client-side routing and React Hook Form + Zod for forms/validation. Component-based design, mobile-responsive.
- **Screens implemented:** Login, Register, Home, Search Quizzes, Quiz Play (flip-card), Quiz Results, Classroom List/Detail, Profile, Admin Dashboard.
- **Code coverage goals & tools:**
  - Frontend: V8 coverage via `vitest run --coverage` (HTML report at `frontend/coverage/index.html`).
  - Backend: JaCoCo (`backend/target/site/jacoco`).
- **Jenkins pipeline** (declarative, Windows agent — see [`/Jenkinsfile`](../Jenkinsfile)):
  - **Build:** `./mvnw clean package` (backend), `npm ci && npm run build` (frontend).
  - **Test:** Backend tests run during Maven `package`; frontend runs `vitest --coverage`.
  - **Coverage:** JaCoCo Publisher (backend) and HTML Publisher (frontend) push reports back to Jenkins.
  - **Containerization:** `docker compose build` produces the production images.

Reports:

- [Sprint 3 Planning Report](./sprint_report/sprint_planning_report_3.pdf)
- [Sprint 3 Review Report](./sprint_report/sprint_review_report_3.pdf)

---

## 8. Sprint 4 – Docker Containerization

How the system was containerized.

- **Purpose:** reproducible local development and one-command deployment to Railway; isolates Java, Node, and MariaDB runtimes.
- **Services containerized:**
  - `backend` — Spring Boot JAR on `eclipse-temurin:21-jre-alpine`.
  - `frontend` — Vite production build served by `nginx:stable-alpine` with `/api/v1/` reverse-proxied to the backend.
  - `db` — `mariadb:11` with persistent `db_data` volume and a healthcheck gating backend startup.
- **Dockerfile & compose overview:** [`backend/Dockerfile`](../backend/Dockerfile), [`frontend/Dockerfile`](../frontend/Dockerfile), and [`docker-compose.yaml`](../docker-compose.yaml). The compose file orchestrates all three services on a private bridge network and reads credentials from `.env`.
- **Use in development/testing:** developers run `docker compose up -d` to bring up the full stack; CI runs `docker compose up -d --wait db` so the backend tests hit a real MariaDB instance, then `docker compose build` to produce release images.

Report:

- [Sprint 4 Planning Report](./sprint_report/sprint_planning_report_4.pdf)

---

## 9. Sprint 5 – UI Localization & Kubernetes

Scalability and internationalization on the client.

- **Supported UI languages:**

  | Code | Language | RTL |
  | ---- | -------- | --- |
  | `en` | English  | No  |
  | `fi` | Suomi    | No  |
  | `fa` | فارسی    | Yes |
  | `zh` | 中文     | No  |

- **Localization approach:** [i18next](https://www.i18next.com/) + `react-i18next`, with translations stored as JSON files at `frontend/public/locales/<lang>/translation.json` and loaded at runtime via `i18next-http-backend`. A `LanguageSwitcher` component in the navbar lets users switch at runtime; the initial language is detected from the browser. Languages flagged `isRtl: true` (currently `fa`) trigger RTL layout handling.
- **Kubernetes:** **not applied.** The team chose Railway's managed container platform for production deployment instead of Kubernetes; horizontal scaling and routing are delegated to Railway. Docker Compose covers local orchestration.

Reports:

- [Sprint 5 Planning Report](./sprint_report/sprint_planning_report_5.pdf)
- [Sprint 5 Review Report](./sprint_report/sprint_review_report_5.pdf)

---

## 10. Sprint 6 – Database Localization

Database-level localization of user-facing reference data.

- **Plan:** [database_localization_plan.pdf](./planning/database_localization_plan.pdf)
- **Implementation report:** [database_localization_report.pdf](./reports/database_localization_report.pdf)
- **Approach — Row Method:** localized entities (e.g., `subjects`) store one row per language, keyed by a stable `code` plus a `language` column. Example:
  ```sql
  INSERT INTO subjects (code, name, language) VALUES ('math', 'Mathematics', 'en');
  INSERT INTO subjects (code, name, language) VALUES ('math', 'Matematiikka', 'fi');
  INSERT INTO subjects (code, name, language) VALUES ('math', 'ریاضیات',     'fa');
  INSERT INTO subjects (code, name, language) VALUES ('math', '数学',         'zh');
  ```
- **Migration / schema changes:** added `language` column to localized tables and a composite uniqueness constraint on `(code, language)`. Updated seed data in `backend/src/main/resources/data.sql`.
- **Validation approach:** the backend's `AcceptHeaderLocaleResolver` reads the `Accept-Language` header and queries the row matching the resolved language; if no row exists for that language, the query falls back to English (`en`). Backend exception messages also resolve via Spring's `MessageSource` against `messages_<lang>.properties`.
- **Schema reference:** [db-diagram-l10n.png](./diagrams/db-diagram-l10n.png), [er-diagram-l10n.png](./diagrams/er-diagram-l10n.png).

Reports:

- [Sprint 6 Planning Report](./sprint_report/sprint_planning_report_6.pdf)
- [Sprint 6 Review Report](./sprint_report/sprint_review_report_6.pdf)

---

## 11. Sprint 7 – Quality Assurance

How quality was ensured.

- **SonarQube usage and metrics:** SonarQube Scanner runs from the Jenkins pipeline against the backend (config at `backend/sonar-project.properties`). Tracked metrics: bugs, vulnerabilities, code smells, duplications, and unit-test coverage. Latest report: [SonarQube_jenkins_report.pdf](./QA/SonarQube_jenkins_report.pdf). Static-scan history: [sprint_6_stasticial_code_scan_report.docx.pdf](./reports/sprint_6_stasticial_code_scan_report.docx.pdf).
- **Code quality goals:** zero blockers and criticals on the backend quality gate, low code-smell density, ESLint clean on the frontend, and Prettier-formatted source across the repo.
- **JMeter test scenarios:** **not done** — performance was sanity-checked manually rather than via load testing.
- **Functional testing:**
  - Backend: JUnit 5 unit + integration tests (Spring Boot Test).
  - Frontend: Vitest + React Testing Library component tests.
  - End-to-end: Playwright user-journey tests in [`/e2e`](../e2e).
- **Non-functional testing:**
  - User Acceptance Testing (UAT): [acceptance_test_planning_04_2026.pdf](./QA/acceptance_test_planning_04_2026.pdf), results in [ua_acceptance_test_online_flashcards_04_2026.pdf](./QA/ua_acceptance_test_online_flashcards_04_2026.pdf).
  - Heuristic Evaluation (Nielsen's 10): [heuristic_evaluation_online_flashcards_04_2026.pdf](./QA/heuristic_evaluation_online_flashcards_04_2026.pdf).
  - Bug & issue tracking: [bug\_&_issue_table.md](./QA/bug_&_issue_table.md).

Reports:

- [Sprint 7 Planning Report](./sprint_report/sprint_planning_report_7.pdf)
- [Sprint 7 Review Report](./sprint_report/sprint_review_report_7.pdf)

---

## 12. Sprint 8 – Documentation & Finalization

How the project was finalized.
- **Final system architecture diagrams:**
  - [Deployment diagram](./diagrams/deployment_diagram.pdf)
  - [Class diagram](./diagrams/class_diagram.pdf)
  - [Sequence diagram](./diagrams/sequence_diagram.pdf)
  - [Activity diagram](./diagrams/activity_diagram.pdf)
  - [Use-case diagram](./diagrams/use-case.pdf)
  - [ER diagram](./diagrams/er-diagram-l10n.png) and [DB schema](./diagrams/db-diagram-l10n.png)
  - [Figma reference](./diagrams/figma.md)

---

## 13. CI/CD PIPELINE

The project uses a **Jenkins declarative pipeline** (`Jenkinsfile`) that automates building, testing, and containerizing the application. The pipeline runs on a **Windows** Jenkins agent and uses `bat` commands throughout.

### Pipeline Overview

```
Checkout & Setup ➜ Build & Test (parallel) ➜ Build Docker Images ➜ Cleanup
```

### Pipeline Stages

| Stage                     | Description                                                                                                                                                 |
| ------------------------- | ----------------------------------------------------------------------------------------------------------------------------------------------------------- |
| **Setup & Checkout**      | Clones the repository via `checkout scm` and injects the `.env` credentials file from Jenkins into the workspace root                                       |
| **Backend** _(parallel)_  | Spins up a MariaDB container (`docker compose up -d --wait db`), then runs `mvnw.cmd clean package` which compiles, tests, and packages the Spring Boot JAR |
| **Frontend** _(parallel)_ | Installs dependencies (`npm ci`), runs Vitest with V8 coverage (`npm run test:coverage`), and builds the production bundle (`npm run build`)                |
| **Build Docker Images**   | Runs `docker compose build` to create production-ready images for all services                                                                              |

### Jenkins Tools

The pipeline declares three managed tools in the `tools` block:

```groovy
tools {
    nodejs 'node-20'    // NodeJS plugin – Node.js 20.x
    jdk    'jdk-21'     // JDK 21
    maven  'Maven3'     // Maven 3.x
}
```

These must be configured in **Manage Jenkins → Tools** with the exact names shown above.

### Environment & Credentials

All environment variables (database credentials, JWT secret, etc.) are stored in a **single `.env` file** managed as a Jenkins `file` credential (`flashcards-env`). During the _Setup & Checkout_ stage, the file is copied into the workspace root:

```groovy
withCredentials([file(credentialsId: 'flashcards-env', variable: 'ENV_FILE')]) {
    bat 'copy "%ENV_FILE%" .env'
}
```

Docker Compose automatically reads the `.env` file to populate service environment variables.

### Test Reporting

| Report                 | Tool             | Location                                |
| ---------------------- | ---------------- | --------------------------------------- |
| Backend unit tests     | JUnit Publisher  | `backend/target/surefire-reports/*.xml` |
| Backend code coverage  | JaCoCo Publisher | `backend/target/jacoco.exec`            |
| Frontend code coverage | HTML Publisher   | `frontend/coverage/index.html`          |

### Required Jenkins Plugins

| Plugin                | Purpose                                |
| --------------------- | -------------------------------------- |
| **NodeJS**            | Provides the `nodejs` tool installer   |
| **JUnit**             | Publishes backend test results         |
| **JaCoCo**            | Publishes backend code coverage        |
| **HTML Publisher**    | Publishes the frontend coverage report |
| **Docker Pipeline**   | Docker integration for building images |
| **SonarQube Scanner** | Runs SonarQube analysis from the pipeline |

---

## 14. STATIC CODE ANALYSIS

The Jenkins pipeline runs **SonarQube** analysis on the backend after the build and test stages. It reports on code quality metrics such as bugs, vulnerabilities, code smells, and test coverage. Configuration is defined in `backend/sonar-project.properties`. Latest report: [SonarQube_jenkins_report.pdf](./QA/SonarQube_jenkins_report.pdf).

---

## 15. DOCKER SETUP

### Docker Images

| Image        | Base                            | Description                                                                               |
| ------------ | ------------------------------- | ----------------------------------------------------------------------------------------- |
| **Backend**  | `eclipse-temurin:21-jre-alpine` | Copies the pre-built `online-flashcards-api.jar` into `/app` and runs it with `java -jar` |
| **Frontend** | `nginx:stable-alpine`           | Copies the Vite `dist/` output and a custom `nginx.conf` into the container               |
| **Database** | `mariadb:11`                    | Official MariaDB image, configured entirely via environment variables                     |

### Nginx Reverse Proxy

The frontend container runs Nginx with two routes:

- **`/`** — serves the static React SPA with `try_files` fallback for client-side routing
- **`/api/v1/`** — proxies all API requests to the backend service on port `8080`

Backend resolution works the same locally (Docker network) and on Railway (internal DNS).

### Docker Compose Services

The `docker-compose.yaml` orchestrates three services:

| Service      | Container Name        | Ports       | Notes                                                                        |
| ------------ | --------------------- | ----------- | ---------------------------------------------------------------------------- |
| **db**       | `flashcards-db`       | `3307:3306` | MariaDB with health check, persistent `db_data` volume                       |
| **backend**  | `flashcards-backend`  | `8080:8080` | Starts only after `db` is healthy (`depends_on: condition: service_healthy`) |
| **frontend** | `flashcards-frontend` | `3000:80`   | Depends on `backend`; Nginx proxies `/api/v1/` to `http://backend:8080`      |

The MariaDB service uses a `mariadb -e "SELECT 1"` health check (10s interval, up to 20 retries) so the backend only starts once the database is ready.

---

## 16. DEPLOYMENT (RAILWAY)

Production images are deployed to **[Railway](https://railway.app)** as three separate services:

| Railway Service | Source                 | Description                                          |
| --------------- | ---------------------- | ---------------------------------------------------- |
| **Frontend**    | `frontend/Dockerfile`  | Nginx serving the SPA and reverse-proxying API calls |
| **Backend**     | `backend/Dockerfile`   | Spring Boot API                                      |
| **Database**    | Railway MariaDB plugin | Managed MariaDB instance                             |

### Internal Networking

Railway services communicate over a **private internal network**. The frontend Nginx `proxy_pass` resolves the backend via Railway's internal DNS (e.g., `http://online-flashcards-backend.railway.internal:8080`). The `BACKEND_URL` environment variable is set on the frontend Railway service to configure this.

### Required Railway Environment Variables

Each service must have its environment variables configured in the Railway dashboard — the same variables that appear in the `.env` file locally (database credentials, `JWT_SECRET`, `JWT_EXPIRATION`, `BACKEND_URL`, etc.).

## 17. FRONTEND LOCALIZATION (i18n)

The app uses [i18next](https://www.i18next.com/) with `react-i18next` for internationalization.

### Supported languages

| Code | Language | RTL |
| ---- | -------- | --- |
| `en` | English  | No  |
| `fi` | Suomi    | No  |
| `fa` | فارسی    | Yes |
| `zh` | 普通话   | No  |

### Translation files

Translations are stored as JSON files under `public/locales/<lang>/translation.json`. The app loads them at runtime via `i18next-http-backend`.

```
public/
  locales/
    en/translation.json
    fi/translation.json
    fa/translation.json
    zh/translation.json
```

The `LanguageSwitcher` component (`src/components/ui/LanguageSwitcher.jsx`) renders a dropdown in the navbar that lets users switch languages at runtime. The selected language is detected from the browser on first load and falls back to English if unsupported. Languages flagged `isRtl: true` in `src/config/languages.js` (currently Farsi) trigger RTL layout handling.

## 18. BACKEND LOCALIZATION (i18n)

The backend implements **server-side message localization** using Spring's built-in i18n framework, returning error and validation messages in the user's preferred language based on the **Accept-Language** HTTP header.

### LocaleResolver Configuration

The backend uses **Spring's `AcceptHeaderLocaleResolver`** to parse the **Accept-Language** header. **Supported languages** are configured in `application.yaml`:

```yaml
app:
  i18n:
    default-language: en
    supported-languages:
      - { code: en, tag: en-UK, label: English }
      - { code: fi, tag: fi-FI, label: Suomi }
      - { code: fa, tag: fa-IR, label: فارسی }
      - { code: zh, tag: zh-CN, label: 中文 }
```

If the user's language matches a supported one it is used; otherwise the default (English) is used.

### Message Properties Files

Localizable strings live in property files in `backend/src/main/resources/`:

| File                     | Language           |
| ------------------------ | ------------------ |
| `messages.properties`    | English (default)  |
| `messages_en.properties` | English (fallback) |
| `messages_fi.properties` | Finnish            |
| `messages_fa.properties` | Farsi              |
| `messages_zh.properties` | Chinese            |

Each entry is a key/value pair grouped by domain (auth, quiz, classroom, etc.) and supports `{0}`/`{1}` placeholders that are filled in at runtime.

### Locale-Aware Exception Handling

All custom exceptions inherit from `ApiException` and store a **message key** (not the message itself) plus optional parameters. The global exception handler resolves the localized message via Spring's `MessageSource`, using the `Locale` Spring injects from the `Accept-Language` header. The API then returns a localized JSON error — no frontend translation is needed for backend errors.

---

## 19. DATABASE CONTENT LOCALIZATION (Row Method)

User-generated content is localized using the **Row Method** where each entity stores **one row per language** with a `language` column.

```sql
INSERT INTO subjects (code, name, language) VALUES ('math', 'Mathematics', 'en');
INSERT INTO subjects (code, name, language) VALUES ('math', 'Matematiikka', 'fi');
INSERT INTO subjects (code, name, language) VALUES ('math', 'ریاضیات', 'fa');
INSERT INTO subjects (code, name, language) VALUES ('math', '数学', 'zh');
```

If a row for the requested language doesn't exist the application falls back to English. The schema and ER diagram below show how the Row Method is wired in.

## 20. ARCHITECTURE DESIGN

### ER Diagram

![Entity Relationship Diagram](./diagrams/er-diagram-l10n.png)

### Database schema

![Database Schema](./diagrams/db-diagram-l10n.png)

## 21. QUALITY ASSURANCE

The project includes two QA evaluations conducted during development:

- **User Acceptance Testing (UAT)** — real users tested the application against defined acceptance criteria to validate functionality and usability.
- **Heuristic Evaluation (Nielsen's 10 Usability Heuristics)** — the UI was evaluated against Nielsen's heuristics to identify usability issues.

The full reports are available in [`docs/QA/`](./QA).

---

## 22. HOW TO RUN THE PROJECT

**Prerequisites:** Docker + Docker Compose. For local non-Docker dev: JDK 21, Maven 3+, Node.js 20+.

1. Clone the repository.
2. Copy the env template: `cp .env.example .env` and fill in `MYSQL_*`, `JWT_SECRET`, `JWT_EXPIRATION`.
3. Start the stack: `docker compose up -d`.

**Access:**
- Frontend: http://localhost:3000
- Backend API: http://localhost:8080/api/v1
- Swagger UI: http://localhost:8080/swagger-ui.html
- Database: `localhost:3307`

## 23. TESTING INSTRUCTIONS

- **Backend unit tests:** `cd backend && ./mvnw test`
- **Backend coverage (JaCoCo):** `cd backend && ./mvnw verify` → `backend/target/site/jacoco/index.html`
- **Frontend unit tests:** `cd frontend && npm run test:run`
- **Frontend coverage (V8):** `cd frontend && npm run test:coverage` → `frontend/coverage/index.html`
- **End-to-end (Playwright):** `cd e2e && npm install && npx playwright test` → `e2e/playwright-report/index.html`
- **Performance testing:** not performed (no JMeter scenarios authored).

CI publishes the backend and frontend coverage reports automatically on every Jenkins build.

## 24. REPOSITORY STRUCTURE

```
/backend             Spring Boot REST API (Java 21, MariaDB)
/frontend            React 19 + Vite SPA
/e2e                 Playwright end-to-end tests
/docs                Documentation, planning, QA reports, diagrams
  /planning          Project plan, vision, localization plan
  /diagrams          Use-case, ER, DB schema, class, sequence, activity, deployment
  /sprint_report     Sprint planning + review PDFs
  /QA                UAT, heuristic evaluation, SonarQube reports, bug list
  /reports           Localization & static-scan reports
  /dev               Developer guidelines (backend/frontend)
/docker-compose.yaml Multi-service orchestration
/Jenkinsfile         CI/CD pipeline definition
/.env.example        Environment variable template
```

This project deploys to Railway, so there is no `/k8s` directory; Docker Compose covers local orchestration.

## 25. AUTHORS

**Course:** Application Development — Ohjelmistotuotantoprojekti 1 & 2, Metropolia UAS, Spring 2026

**Team members:**
- Teemu Laasio
- Aleksi Putkonen
- Blendi Grajeqvci
- Alabass Alkhrsany
