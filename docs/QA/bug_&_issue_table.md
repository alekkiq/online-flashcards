# Bug & Issue Tracking — Online Flashcards

This document is the live record of all bugs and issues found during testing of the Online Flashcards application. Every entry here represents something that was identified during QA and either needs to be resolved or has already been fixed.

**Purpose:** Keep all testers and developers aligned on what is broken, what is in progress, and what has been resolved — without waiting for a meeting or digging through chat history.

**Status values:** `Open` · `In Progress` · `Fixed`

---

## How to mark an issue as fixed

1. Fill in the **How It Was Fixed** column with a short description of what was changed and where (e.g. file name, function, translation key).
2. Fill in the **Date Fixed** column with today's date (`DD.MM.YYYY`).
3. Change **Status** to `Fixed`.

## How to add a new bug or issue

Add a new row at the bottom of the table with the next sequential `#`. Fill in:

| Field | What to write |
|---|---|
| **Issue / Bug** | A clear one-sentence description of the problem. |
| **Date Found** | Today's date (`DD.MM.YYYY`). |
| **How It Was Fixed** | Leave blank until resolved. |
| **Date Fixed** | Leave blank until resolved. |
| **Status** | `Open` |

---

## Issue & Bug Table

| # | Issue / Bug | Date Found | How It Was Fixed | Date Fixed | Status |
|---|---|---|---|---|---|
| 1 | A lot of lorem ipsum text found in the project, and some unnecessary links. | 26.04.2026 | Replaced all 3 placeholder lorem ipsum strings (`heroDescription1`, `heroDescription2`, `findDescription`) in all 4 locale files (en, fi, fa, zh) with real descriptive content. | 27.04.2026 | Fixed |
| 2 | When searching for quizzes and there are no results, there isn't any text saying "No search results found." | 26.04.2026 | Added "No quizzes found for '...'" message in `SearchQuizzes.jsx` when a search returns empty results; added the `noQuizzesFound` translation key to all 4 locales. | 27.04.2026 | Fixed |
| 3 | Found a piece of untranslated text in the app at the Flashcard game stage. | 26.04.2026 | | | Open |
| 4 | Forgot to localize text in quiz result "Correct". | 26.04.2026 | `ScoreBadge.jsx`: replaced hardcoded "Correct" with `t("results.correct")` (translation key already existed in all locales). | 27.04.2026 | Fixed |
| 5 | Classroom tag is not localized if the user switches app languages (e.g. user in a Finnish class). | 26.04.2026 | | | Open |
| 6 | When login token is expired, the user is not notified — it just logs out. | 26.04.2026 | | | Open |
| 7 | If the user forgets to click "I know" or "Don't know", the game doesn't count it toward progress, blocking the user from moving forward. | 26.04.2026 | | | Open |
| 8 | Margin top on the profile page is lower than other pages (inconsistent layout). | 26.04.2026 | Added `py-8` to the outer wrapper in `Profile.jsx` to match the vertical spacing of other pages. | 27.04.2026 | Fixed |
| 9 | When creating a new quiz, no success message is shown. | 26.04.2026 | | | Open |
| 10 | When creating a new classroom, no success message is shown. | 26.04.2026 | | | Open |
| 11 | No keyboard shortcuts (spacebar, enter, +/−) to navigate in the quiz game. | 26.04.2026 | Implemented keyboard navigation in the quiz game: Space/Enter flips card, ←/→ navigates, 1 = I know, 2 = I don't know. | 27.04.2026 | Fixed |
| 12 | No way to bookmark/favorite quizzes or see recently played — users must always search. | 26.04.2026 | | | Open |
| 13 | If a teacher application is rejected, no message is shown to the user. | 26.04.2026 | | | Open |
| 14 | Once a learning material is published, there is no way to edit or delete it. | 26.04.2026 | | | Open |
| 15 | Destructive actions (leaving a classroom, removing a user, etc.) do not ask for confirmation. | 26.04.2026 | | | Open |
| 16 | Pressing submit on quiz creation immediately publishes the quiz with no "Publish quiz?" confirmation. | 26.04.2026 | | | Open |
| 17 | No About or FAQ section for new users. | 26.04.2026 | | | Open |
| 18 | No tooltips anywhere (e.g. classroom creation, quiz creation, learning material creation). | 26.04.2026 | | | Open |
| 19 | On the profile page, the "request promotion to teacher" form never explains what a teacher can do or why a user would want it. | 26.04.2026 | | | Open |
| 20 | Hardcoded "Click to flip" text in flashcard game (not localized). | 26.04.2026 | `FlipCard.jsx`: replaced hardcoded "Click to flip" with `t("quizGame.clickToFlip")`; added `clickToFlip` translation key to all 4 locales. | 27.04.2026 | Fixed |
