/// <reference types="vitest" />
import { defineConfig, loadEnv } from "vite";
import path from "path";
import react from "@vitejs/plugin-react";
import tailwindcss from "@tailwindcss/vite";

export default defineConfig(({ mode }) => {
  const env = loadEnv(mode, path.resolve(__dirname, "../"), "");
  return {
    plugins: [react(), tailwindcss()],
    theme: {
      extend: {
        fontFamily: {
          sans: ["Montserrat", "sans-serif"],
        },
      },
    },

    server: {
      proxy: {
        "/api/v1": {
          target: env.BACKEND_URL || "http://localhost:8081",
          changeOrigin: true,
          secure: false,
        },
      },
    },

    // Vitest configuration
    test: {
      globals: true,
      environment: "jsdom",
      setupFiles: ["./src/test/setup.js"],
      css: true,
      coverage: {
        provider: "v8",
        reporter: ["text", "json", "html"],
        reportsDirectory: "./coverage",
        all: true,
        include: ["src/hooks/**/*.{js,jsx}", "src/context/**/*.{js,jsx}", "src/lib/**/*.{js,jsx}"],
        exclude: ["node_modules/", "src/test/", "**/*.d.ts", "**/*.config.{js,ts}", "**/main.jsx"],
      },
    },
  };
});
