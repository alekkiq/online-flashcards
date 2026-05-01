import { test, expect } from "@playwright/test";

const PAUSE = 2500;

test("User Journey: Study German Vocabulary with Flashcards", async ({
  page,
}) => {
  // Step 1 user discovers the flashcard learning app
  await page.goto("/");
  await page.waitForTimeout(PAUSE);

  await expect(
    page.getByRole("heading", { name: /flashcard learning/i }),
  ).toBeVisible();
  await page.waitForTimeout(PAUSE);

  await expect(
    page.getByRole("button", { name: "Search Quizzes" }),
  ).toBeVisible();
  await expect(
    page.getByRole("button", { name: "Register Now" }),
  ).toBeVisible();
  await page.waitForTimeout(PAUSE);

  await page.getByRole("button", { name: "Search Quizzes" }).click();
  await expect(page).toHaveURL("/search");
  await page.waitForTimeout(PAUSE);

  // Step 2 user selects the free product
  const username = `studyuser_${Date.now()}`;
  await page.goto("/login?signup=true");
  await page.waitForTimeout(PAUSE);

  await expect(
    page.getByRole("main").getByRole("link", { name: "Sign Up" }),
  ).toBeVisible();

  await page.locator('input[name="username"]').fill(username);
  await page.waitForTimeout(PAUSE);
  await page.locator('input[name="email"]').fill(`${username}@example.com`);
  await page.waitForTimeout(PAUSE);
  await page.locator('input[name="password"]').fill("SecurePass123!");
  await page.waitForTimeout(PAUSE);
  await page.locator('input[name="repeatPassword"]').fill("SecurePass123!");
  await page.waitForTimeout(PAUSE);

  await page.getByRole("main").getByRole("button", { name: "Sign Up" }).click();
  await expect(page).toHaveURL("/", { timeout: 12_000 });
  await page.waitForTimeout(PAUSE);

  // Step 3 user searches for vocabulary quizzes
  await page.goto("/search");
  await page.waitForTimeout(PAUSE);

  await expect(
    page.getByRole("heading", { name: "Search Quizzes" }),
  ).toBeVisible();

  const searchInput = page.locator('input[type="text"]').first();
  await expect(searchInput).toBeVisible();

  await searchInput.fill("german");
  await page.waitForTimeout(2000);

  const cards = page.locator(".cursor-pointer.rounded-xl");
  const noResults = page.getByText(/no quizzes found/i);

  const hasCards = (await cards.count()) > 0;
  const hasNoResults = await noResults.isVisible().catch(() => false);

  expect(hasCards || hasNoResults).toBe(true);
  await page.waitForTimeout(PAUSE);

  // Step 4 user opens a quiz to review details
  await page.goto("/search");
  await page.waitForTimeout(PAUSE);

  const firstCard = page.locator(".cursor-pointer.rounded-xl").first();
  await expect(firstCard).toBeVisible({ timeout: 12_000 });
  await firstCard.click();
  await page.waitForTimeout(PAUSE);

  await expect(page).toHaveURL(/\/quiz-details\/\d+/);
  await expect(page.locator("h1").first()).toBeVisible();
  await expect(page.getByRole("button", { name: "Play Quiz" })).toBeVisible({
    timeout: 8_000,
  });
  await page.waitForTimeout(PAUSE);

  // Step 5 user studies all flashcards and sees results
  await page.goto("/search");
  await page.waitForTimeout(PAUSE);

  const quizCard = page.locator(".cursor-pointer.rounded-xl").first();
  await expect(quizCard).toBeVisible({ timeout: 12_000 });
  await quizCard.click();
  await page.waitForTimeout(PAUSE);

  await expect(page).toHaveURL(/\/quiz-details\/\d+/);

  await page.getByRole("button", { name: "Play Quiz" }).click();
  await expect(page).toHaveURL(/\/quiz\/\d+/);
  await page.waitForTimeout(PAUSE);

  for (let i = 0; i < 2; i++) {
    await expect(page.locator(".flip-card-container")).toBeVisible({
      timeout: 10_000,
    });
    await page.waitForTimeout(PAUSE);

    await page.locator(".flip-card-container").click();
    await page.waitForTimeout(PAUSE);

    await page.keyboard.press("1");
    await page.waitForTimeout(PAUSE);

    if (i < 1) {
      await page.keyboard.press("ArrowRight");
      await page.waitForTimeout(PAUSE);
    }
  }

  await expect(page).toHaveURL("/quiz/results", { timeout: 10_000 });
  await page.waitForTimeout(PAUSE);

  await expect(
    page.getByRole("heading", { name: /quiz complete/i }),
  ).toBeVisible();
  await page.waitForTimeout(PAUSE);
});
