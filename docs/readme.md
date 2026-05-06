# ONLINE FLASHCARDS

A web-based quiz and flashcard learning platform built as the second-year Software Engineering project at Metropolia UAS.

- **Project name:** Online Flashcards (OnlyCards)
- **Problem solved:** Fragmented tooling for flashcard study — content creation, group study, and progress tracking are normally split across multiple apps.
- **Target users:** Students, Teachers, Admins
- **Main technologies:** React 19, Spring Boot 4, MariaDB 11, Docker, Jenkins, Railway
- **Overall duration:** 8 sprints × 2 weeks (≈ 16 weeks)

---

## 1. Product Vision

Detailed vision document: [product_vision.pdf](./planning/product_vision.pdf)

**Vision statement:** Empower students and teachers to learn and teach more effectively through a single, multilingual platform where flashcard quizzes can be created, shared, and practiced inside virtual classrooms.

**Main goals:**
- Make flashcard study fast, collaborative, and language-inclusive.
- Give teachers lightweight tools to organize quizzes and classrooms without IT overhead.
- Track learner progress over time so users can identify weak topics.
- Stay deployable on commodity cloud (Docker / Railway) so it remains low-cost to operate.

**Key features:**
- **Authentication & roles** — Student / Teacher / Admin with JWT-based stateless auth and a Student → Teacher promotion request flow approved by Admins.
- **Quizzes & flashcards** — teachers create quizzes (title, description, subject) containing flashcards; quizzes are publicly searchable and playable.
- **Quiz game** — interactive flip-card UI with sliding transitions and self-evaluation; result saved as a score percentage.
- **Progress tracking** — every attempt stored as a quiz result with score and timestamp.
- **Search & discovery** — public search page with subject filtering, accessible to anonymous visitors too.
- **Classrooms** — virtual study groups with join code, attached quizzes, learning materials, and member management.
- **Profile management** — view/edit profile, submit promotion request, quick links to owned quizzes and classrooms.
- **Multilingual UI + content** — 4 languages (en, fi, fa, zh), including RTL layout for Farsi.

**Definition of success:** the full stack runs end-to-end via `docker compose up` and on Railway; backend + frontend test suites pass in CI with coverage published; SonarQube quality gate passes; and the UI is usable in all four supported languages (incl. RTL).

---

## 2. Technologies Used

### Frontend
| Technology | Purpose |
|---|---|
| **React 19** | UI library |
| **Vite 7** | Build tool & dev server |
| **i18next** + **react-i18next** | Frontend internationalization |
| **Tailwind CSS 4** | Utility-first CSS |
| **React Router 7** | Client-side routing |
| **React Hook Form** + **Zod** | Forms & validation |
| **Lucide React** | Icon library |
| **Nginx** | Production static serving & reverse proxy |

### Backend
| Technology | Purpose |
|---|---|
| **Java 17** | Programming language |
| **Spring Boot 4** | Application framework |
| **Spring Security** | Auth & authorization |
| **Spring Data JPA / Hibernate** | ORM & database access |
| **JWT (jjwt)** | Token-based authentication |
| **SpringDoc OpenAPI** | API documentation (Swagger UI) |
| **Maven** | Build & dependency management |

### Database, Testing, DevOps & Quality
| Technology | Purpose |
|---|---|
| **MariaDB 11** | Relational database |
| **Vitest** + **jsdom** + **React Testing Library** | Frontend unit/component tests |
| **JUnit 5** (Spring Boot Test) | Backend unit & integration tests |
| **JaCoCo** / **V8 Coverage** | Backend / frontend code coverage |
| **Playwright** | End-to-end testing |
| **Docker** + **Docker Compose** | Containerization & orchestration |
| **Jenkins** | CI/CD pipeline |
| **Docker Hub** | Container image registry |
| **Railway** | Cloud hosting & deployment |
| **ESLint 9** + **Prettier** | Frontend linting & formatting |
| **SonarQube** | Static code analysis & quality gate |

---

## 3. Project Plan & Sprint Structure

Full project plan: [project_planning.pdf](./planning/project_planning.pdf)

- **Methodology:** Agile / Scrum
- **Sprint length:** 2 weeks
- **Total sprints:** 8

| Sprint | Focus | Primary Deliverable |
|---|---|---|
| 1 | Project Planning & Vision | Backlog, vision, risk/scope |
| 2 | Requirements & Database | Use case + ER diagrams, DB schema, unit test setup |
| 3 | UI Implementation & CI | React SPA + Jenkins pipeline |
| 4 | Docker Containerization | Dockerfiles + `docker-compose.yaml` |
| 5 | UI Localization (+ K8s if applied) | i18next translations for 4 languages |
| 6 | Database Localization | Row-method localized content + schema migration |
| 7 | Quality Assurance | SonarQube, UAT, heuristic evaluation, e2e tests |
| 8 | Documentation & Finalization | Final docs, architecture diagrams, deployment |

---

## 4. Sprint 1 – Project Planning & Vision

Foundational planning artifacts produced in this sprint:

- **Project plan summary** — scope, timeline, and roles documented in [project_planning.pdf](./planning/project_planning.pdf).
- **Backlog creation** — initial product backlog drafted from the vision document.
- **Vision validation** — captured in [product_vision.pdf](./planning/product_vision.pdf).
- **Risk and scope definition** — covered inside the project plan (technical risk, schedule risk, scope boundaries).

Reports: [Sprint 1 Planning](./sprint_report/sprint_planning_report_1.pdf) · [Sprint 1 Review](./sprint_report/sprint_review_report_1.pdf)

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

Reports: [Sprint 2 Planning](./sprint_report/sprint_planning_report_2.pdf) · [Sprint 2 Review](./sprint_report/sprint_review_report_2.pdf)

---

## 6. Sprint 3 – UI Implementation & CI

User interface and CI automation.

- **UI framework & approach:** React 19 + Vite 7 + Tailwind CSS 4, with React Router 7 for client-side routing and React Hook Form + Zod for forms/validation. Component-based design, mobile-responsive.
- **Screens implemented:** Login, Register, Home, Search Quizzes, Quiz Play (flip-card), Quiz Results, Classroom List/Detail, Profile, Admin Dashboard.
- **Code coverage goals & tools:**
  - Frontend: V8 coverage via `vitest run --coverage` (HTML report at `frontend/coverage/index.html`).
  - Backend: JaCoCo (`backend/target/site/jacoco`).

### Jenkins Pipeline

Declarative pipeline (`Jenkinsfile`) running on a Windows Jenkins agent:

```
Checkout & Setup ➜ Build & Test (parallel) ➜ Build Docker Images ➜ Cleanup
```

| Stage | Description |
|---|---|
| **Setup & Checkout** | `checkout scm`, then injects the `flashcards-env` Jenkins file credential into the workspace as `.env` |
| **Backend** *(parallel)* | `docker compose up -d --wait db`, then `mvnw.cmd clean package` (compile, test, package) |
| **Frontend** *(parallel)* | `npm ci`, `npm run test:coverage` (Vitest + V8), `npm run build` |
| **Build Docker Images** | `docker compose build` produces production images |

**Jenkins tools** (configured under *Manage Jenkins → Tools*): `nodejs 'node-20'`, `jdk 'jdk-21'`, `maven 'Maven3'`.

**Test reporting published back to Jenkins:**
| Report | Tool | Location |
|---|---|---|
| Backend unit tests | JUnit Publisher | `backend/target/surefire-reports/*.xml` |
| Backend coverage | JaCoCo Publisher | `backend/target/jacoco.exec` |
| Frontend coverage | HTML Publisher | `frontend/coverage/index.html` |

**Required plugins:** NodeJS, JUnit, JaCoCo, HTML Publisher, Docker Pipeline, SonarQube Scanner.

Reports: [Sprint 3 Planning](./sprint_report/sprint_planning_report_3.pdf) · [Sprint 3 Review](./sprint_report/sprint_review_report_3.pdf)

---

## 7. Sprint 4 – Docker Containerization & Deployment

How the system was containerized and deployed.

- **Purpose:** reproducible local development and one-command deployment to Railway; isolates Java, Node, and MariaDB runtimes.
- **Services containerized:**
  | Service | Image | Notes |
  |---|---|---|
  | `backend` | `eclipse-temurin:21-jre-alpine` | Spring Boot JAR run with `java -jar` |
  | `frontend` | `nginx:stable-alpine` | Vite `dist/` served by Nginx; `/api/v1/` reverse-proxied to backend |
  | `db` | `mariadb:11` | Persistent `db_data` volume, healthcheck gates backend startup |
- **Compose orchestration:** [`docker-compose.yaml`](../docker-compose.yaml) wires all three services on a private bridge network and reads credentials from `.env`. The MariaDB healthcheck (`mariadb -e "SELECT 1"`, 10s interval, up to 20 retries) ensures the backend only starts once the DB is ready.
- **Ports:** frontend `3000:80`, backend `8080:8080`, db `3307:3306`.
- **Use in development/testing:** `docker compose up -d` brings up the full stack; CI uses `docker compose up -d --wait db` so backend tests hit a real MariaDB, then `docker compose build` for release images.

### Deployment (Railway)

Production images deploy to **[Railway](https://railway.app)** as three services (frontend, backend, MariaDB plugin). Services communicate over Railway's internal network — the frontend's Nginx `proxy_pass` resolves the backend via internal DNS (`http://online-flashcards-backend.railway.internal:8080`), configured through the `BACKEND_URL` env var. All other env vars (DB credentials, `JWT_SECRET`, `JWT_EXPIRATION`) are set per-service in the Railway dashboard.

Reports: [Sprint 4 Planning](./sprint_report/sprint_planning_report_4.pdf) · [Sprint 4 Review](./sprint_report/sprint_review_report_5.pdf)

---

## 8. Sprint 5 – UI Localization

Internationalization on the client.

- **Supported UI languages:**
  | Code | Language | RTL |
  |---|---|---|
  | `en` | English | No |
  | `fi` | Suomi | No |
  | `fa` | فارسی | Yes |
  | `zh` | 中文 | No |
- **Localization approach:** [i18next](https://www.i18next.com/) + `react-i18next`. Translation JSON lives at `frontend/public/locales/<lang>/translation.json` and is loaded at runtime via `i18next-http-backend`. The `LanguageSwitcher` component (`src/components/ui/LanguageSwitcher.jsx`) in the navbar lets users switch at runtime; initial language is detected from the browser, falling back to English. Languages flagged `isRtl: true` in `src/config/languages.js` (currently `fa`) trigger RTL layout handling.

Reports: [Sprint 5 Planning](./sprint_report/sprint_planning_report_5.pdf) · [Sprint 5 Review](./sprint_report/sprint_review_report_5.pdf)

---

## 9. Sprint 6 – Database Localization

Server-side and database-level localization of user-facing content.

- **Plan:** [database_localization_plan.pdf](./planning/database_localization_plan.pdf) · **Implementation report:** [database_localization_report.pdf](./reports/database_localization_report.pdf)

### Backend message localization

Spring's `AcceptHeaderLocaleResolver` parses the `Accept-Language` header. Supported languages are configured in `application.yaml`:

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

Localized strings live in `messages_<lang>.properties` files under `backend/src/main/resources/`. All custom exceptions inherit from `ApiException` and store a **message key** (not the message itself) plus optional parameters; the global exception handler resolves the localized message via Spring's `MessageSource` using the locale Spring injects from the header. The API returns localized JSON errors directly — no frontend translation needed for backend errors.

### Database content localization (Row Method)

Localized entities (e.g. `subjects`) store **one row per language**, keyed by a stable `code` plus a `language` column:

```sql
INSERT INTO subjects (code, name, language) VALUES ('math', 'Mathematics',  'en');
INSERT INTO subjects (code, name, language) VALUES ('math', 'Matematiikka', 'fi');
INSERT INTO subjects (code, name, language) VALUES ('math', 'ریاضیات',     'fa');
INSERT INTO subjects (code, name, language) VALUES ('math', '数学',         'zh');
```

- **Migration / schema changes:** added `language` column to localized tables and a composite uniqueness constraint on `(code, language)`. Updated seed data in `backend/src/main/resources/data.sql`.
- **Validation / fallback:** queries match the row for the resolved language; if no row exists for that language, the query falls back to English (`en`).
- **Schema reference:** [db-diagram-l10n.png](./diagrams/db-diagram-l10n.png), [er-diagram-l10n.png](./diagrams/er-diagram-l10n.png).

Reports: [Sprint 6 Planning](./sprint_report/sprint_planning_report_6.pdf) · [Sprint 6 Review](./sprint_report/sprint_review_report_6.pdf)

---

## 10. Sprint 7 – Quality Assurance

How quality was ensured.

- **SonarQube** — runs from the Jenkins pipeline against the backend (config: `backend/sonar-project.properties`). Tracked metrics: bugs, vulnerabilities, code smells, duplications, unit-test coverage. Latest report: [SonarQube_jenkins_report.pdf](./QA/SonarQube_jenkins_report.pdf). History: [sprint_6_stasticial_code_scan_report.docx.pdf](./reports/sprint_6_stasticial_code_scan_report.docx.pdf).
- **Code quality goals:** zero blockers/criticals on the backend quality gate, low code-smell density, ESLint clean on the frontend, Prettier-formatted source repo-wide.
- **Functional testing:**
  - Backend: JUnit 5 unit + integration tests (Spring Boot Test).
  - Frontend: Vitest + React Testing Library component tests.
  - End-to-end: Playwright user-journey tests in [`/e2e`](../e2e).
- **Non-functional testing:**
  - **UAT:** [acceptance_test_planning_04_2026.pdf](./QA/acceptance_test_planning_04_2026.pdf) · results in [ua_acceptance_test_online_flashcards_04_2026.pdf](./QA/ua_acceptance_test_online_flashcards_04_2026.pdf).
  - **Heuristic Evaluation** (Nielsen's 10): [heuristic_evaluation_online_flashcards_04_2026.pdf](./QA/heuristic_evaluation_online_flashcards_04_2026.pdf).
  - **Bug & issue tracking:** [bug\_&_issue_table.md](./QA/bug_&_issue_table.md).

Reports: [Sprint 7 Planning](./sprint_report/sprint_planning_report_7.pdf) · [Sprint 7 Review](./sprint_report/sprint_review_report_7.pdf)

---

## 11. Sprint 8 – Documentation & Finalization

How the project was finalized.

- **Final architecture diagrams:**
  - [Deployment diagram](./diagrams/deployment_diagram.pdf)
  - [Class diagram](./diagrams/class_diagram.pdf)
  - [Sequence diagram](./diagrams/sequence_diagram.pdf)
  - [Activity diagram](./diagrams/activity_diagram.pdf)
  - [Use-case diagram](./diagrams/use-case.pdf)
  - [ER diagram](./diagrams/er-diagram-l10n.png) and [DB schema](./diagrams/db-diagram-l10n.png)
  - [Figma reference](./diagrams/figma.md)
- **API documentation:** Swagger UI at `http://localhost:8080/swagger-ui.html` (generated by SpringDoc OpenAPI).
- **User documentation:** in-app help and the QA acceptance-test scenarios listed under Sprint 7.

---

## 12. How to Run the Project

**Prerequisites:** Docker + Docker Compose. For local non-Docker dev: JDK 21, Maven 3+, Node.js 20+.

1. Clone the repository.
2. Copy the env template: `cp .env.example .env` and fill in `MYSQL_*`, `JWT_SECRET`, `JWT_EXPIRATION`.
3. Start the stack: `docker compose up -d`.

**Access:**
- Frontend: http://localhost:3000
- Backend API: http://localhost:8080/api/v1
- Swagger UI: http://localhost:8080/swagger-ui.html
- Database: `localhost:3307`

---

## 13. Testing Instructions

- **Backend unit tests:** `cd backend && ./mvnw test`
- **Backend coverage (JaCoCo):** `cd backend && ./mvnw verify` → `backend/target/site/jacoco/index.html`
- **Frontend unit tests:** `cd frontend && npm run test:run`
- **Frontend coverage (V8):** `cd frontend && npm run test:coverage` → `frontend/coverage/index.html`
- **End-to-end (Playwright):** `cd e2e && npm install && npx playwright test` → `e2e/playwright-report/index.html`

CI publishes the backend and frontend coverage reports automatically on every Jenkins build.

---

## 14. Repository Structure

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

---

## 15. Authors

**Course:** Application Development — Ohjelmistotuotantoprojekti 1 & 2, Metropolia UAS, Spring 2026

**Team members:**
- Teemu Laasio
- Aleksi Putkonen
- Blendi Grajeqvci
- Alabass Alkhrsany
