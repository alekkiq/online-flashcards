import { defineConfig, devices } from "@playwright/test";

export default defineConfig({
  testDir: "./tests",
  timeout: 120_000,
  retries: 0,
  fullyParallel: false,

  use: {
    baseURL: "http://localhost:5173",
    locale: "en-US",

    // Video recorded for every test (required for submission)
    video: "on",
    // Slow down actions so the video is easy to follow
    slowMo: 500,

    screenshot: "only-on-failure",
    trace: "retain-on-failure",
  },

  projects: [
    {
      name: "chromium",
      use: { ...devices["Desktop Chrome"] },
    },
  ],

  reporter: [["html", { outputFolder: "playwright-report" }], ["list"]],
  outputDir: "test-results",
});
