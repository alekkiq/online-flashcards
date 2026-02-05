/// <reference types="vitest" />
import { defineConfig } from "vite";
import path from "path";
import react from "@vitejs/plugin-react";
import tailwindcss from "@tailwindcss/vite";

// https://vite.dev/config/
export default defineConfig({
  plugins: [react(), tailwindcss()],
  build: {
    outDir: path.resolve(__dirname, "../server/public"),
    emptyOutDir: true,
  },
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
        target: "http://localhost:8080",
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
      include: [
        "src/hooks/**/*.{js,jsx}",
        "src/context/**/*.{js,jsx}",
        "src/lib/**/*.{js,jsx}",
      ],
      exclude: [
        "node_modules/",
        "src/test/",
        "**/*.d.ts",
        "**/*.config.{js,ts}",
        "**/main.jsx",
      ],
    },
  },
});