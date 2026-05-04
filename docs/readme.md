# Online Flashcards (OnlyCards)

## 1. Project Title & Overview

**OnlyCards** is a web-based quiz and flashcard learning platform aimed at students and educators. It addresses the lack of a single, accessible tool for creating, sharing, and studying flashcard-based quizzes by combining authoring, classroom management, and self-evaluation in one responsive single-page application. Quizzes are built from individual flashcards (question/answer pairs) organized by subject; classrooms group quizzes and learning materials around a common topic; and quiz attempts are tracked over time.

- **Project name:** Online Flashcards (OnlyCards)
- **Problem solved:** Fragmented tooling for flashcard study — content creation, group study, and progress tracking are normally split across multiple apps.
- **Target users:** Students (default role), Teachers (content creators), Admins (platform managers).
- **Main technologies:** React 19, Spring Boot 4, MariaDB 11, Docker, Jenkins, Railway.
- **Overall duration:** 8 sprints × 2 weeks (≈ 16 weeks).

---

## 2. Product Vision

Detailed vision document: [product_vision.pdf](./planning/product_vision.pdf)

### Vision Statement

Empower students and teachers to learn and teach more effectively by giving them a single, multilingual platform where flashcard quizzes can be created, shared, and practiced inside virtual classrooms.

### Main Goals

- Make flashcard study fast, collaborative, and language-inclusive.
- Provide teachers with lightweight tools to organize quizzes and classrooms without IT overhead.
- Track learner progress over time so users can identify weak topics.
- Keep the system deployable on commodity cloud (Docker / Railway) so it remains low-cost to operate.

### Key Features

- Role-based authentication (Student / Teacher / Admin) with JWT.
- Quiz authoring with subject categorization and multi-flashcard sets.
- Interactive flip-card quiz player with self-evaluation and result tracking.
- Classrooms with join codes, attached quizzes, and uploaded learning materials.
- Search and discovery across all public quizzes (no login required to browse).
- Full UI localization (English, Suomi, فارسی RTL, 中文) and server-side message localization.
- Database-level content localization for subjects (Row Method).
- User profile management and Student → Teacher promotion workflow.

### Definition of Success

The project is considered complete when:

1. All 15 README sections and 8 sprint goals are demonstrably delivered in the repo.
2. The full stack runs end-to-end via `docker compose up` and is reachable on Railway.
3. Backend and frontend test suites pass in CI with coverage published.
4. SonarQube quality gate passes on the backend.
5. UI is usable in all four supported languages (incl. RTL) and DB localization falls back gracefully.

---

## 3. Project Plan & Sprint Structure

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

## 4. Sprint 1 – Project Planning & Vision

Foundational planning artifacts produced in this sprint:

- **Project plan summary** — scope, timeline, and roles documented in [project_planning.pdf](./planning/project_planning.pdf).
- **Backlog creation** — initial product backlog drafted from the vision document.
- **Vision validation** — captured in [product_vision.pdf](./planning/product_vision.pdf).
- **Risk and scope definition** — covered inside the project plan (technical risk, schedule risk, scope boundaries).

Reports:

- [Sprint 1 Planning Report](./sprint_report/sprint_planning_report_1.pdf)
- [Sprint 1 Review Report](./sprint_report/sprint_review_report_1.pdf)

---

## 5. Sprint 2 – Requirements & Database

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

## 6. Sprint 3 – UI Implementation & CI

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

## 7. Sprint 4 – Docker Containerization

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

## 8. Sprint 5 – UI Localization & Kubernetes

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

## 9. Sprint 6 – Database Localization

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

## 10. Sprint 7 – Quality Assurance

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

## 11. Sprint 8 – Documentation & Finalization

How the project was finalized.

- **Technical documentation:** [docs/dev/backend_dev.md](./dev/backend_dev.md), [docs/dev/frontend_guidelines.md](./dev/frontend_guidelines.md).
- **User documentation:** this README plus the in-app onboarding and help text.
- **API documentation:** generated by SpringDoc OpenAPI; Swagger UI is served at `http://localhost:8080/swagger-ui.html` when the backend is running.
- **Final system architecture diagrams:**
  - [Deployment diagram](./diagrams/deployment_diagram.pdf)
  - [Class diagram](./diagrams/class_diagram.pdf)
  - [Sequence diagram](./diagrams/sequence_diagram.pdf)
  - [Activity diagram](./diagrams/activity_diagram.pdf)
  - [Use-case diagram](./diagrams/use-case.pdf)
  - [ER diagram](./diagrams/er-diagram-l10n.png) and [DB schema](./diagrams/db-diagram-l10n.png)
  - [Figma reference](./diagrams/figma.md)

---

## 12. How to Run the Project

### Prerequisites

- **Docker** and **Docker Compose** (recommended path).
- For local non-Docker dev: **JDK 21**, **Maven 3+**, **Node.js 20+**.

### Environment setup

1. Clone the repository.
2. Copy the env template: `cp .env.example .env`.
3. Fill in `MYSQL_*`, `JWT_SECRET`, and `JWT_EXPIRATION` in `.env`.

### Run with Docker

```
docker compose up -d
```

This starts MariaDB, the Spring Boot backend, and the Nginx-served frontend.

### Access the application

- Frontend (SPA): http://localhost:3000
- Backend API: http://localhost:8080/api/v1
- Swagger UI: http://localhost:8080/swagger-ui.html
- Database: `localhost:3307` (MariaDB)

### Production

Deployed to **Railway** as three services (frontend, backend, MariaDB plugin) communicating over Railway's internal network. Environment variables are set in the Railway dashboard.

---

## 13. Testing Instructions

### Unit & integration tests

- **Backend:** `cd backend && ./mvnw test`
- **Frontend:** `cd frontend && npm test` (watch mode) or `npm run test:run`

### Coverage

- **Backend (JaCoCo):** `cd backend && ./mvnw verify` — report at `backend/target/site/jacoco/index.html`.
- **Frontend (V8):** `cd frontend && npm run test:coverage` — report at `frontend/coverage/index.html`.
- **CI:** Jenkins publishes both reports automatically on every build.

### End-to-end tests

- `cd e2e && npm install && npx playwright test`
- HTML report: `e2e/playwright-report/index.html`.

### Performance testing

Not performed in this project (no JMeter scenarios authored).

---

## 14. Repository Structure

```
/backend            Spring Boot REST API (Java 21, MariaDB)
/frontend           React 19 + Vite SPA
/e2e                Playwright end-to-end tests
/docs               Documentation, planning, QA reports, diagrams
  /planning         Project plan, vision, localization plan
  /diagrams         Use-case, ER, DB schema, class, sequence, activity, deployment
  /sprint_report    Sprint planning + review PDFs (sprints 1–7)
  /QA               UAT, heuristic evaluation, SonarQube reports, bug list
  /reports          Localization & static-scan reports
  /dev              Developer guidelines (backend/frontend)
/docker-compose.yaml   Multi-service orchestration
/Jenkinsfile           CI/CD pipeline definition
/.env.example          Environment variable template
```

> Note: this project deploys to Railway and therefore has no `/k8s` directory; Docker Compose covers local orchestration.

---

## 15. Authors

**Course:** Application Development — Spring 2026

**Team members & roles:**

- _(To be filled in by the team — name and Scrum role for each member, e.g., Product Owner, Scrum Master, Developer, QA Lead.)_

---

## Appendix — Sprint Reports Index

All sprint planning and review reports are stored under [`docs/sprint_report/`](./sprint_report).
