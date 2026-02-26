import { describe, it, expect, beforeEach, vi } from "vitest";
import { render, act } from "@testing-library/react";
import { useContext } from "react";
import { AuthProvider, AuthContext } from "../context/AuthContext";
import { BrowserRouter } from "react-router";
import * as api from "../api";

vi.mock("../api", () => ({
  login: vi.fn(),
  autoLogin: vi.fn(),
}));

const TestConsumer = ({ onMount }) => {
  const context = useContext(AuthContext);
  onMount(context);
  return <div data-testid="test-consumer">Consumer</div>;
};

describe("AuthProvider", () => {
  let context;

  beforeEach(() => {
    vi.clearAllMocks();

    api.login.mockResolvedValue({
      success: true,
      data: {
        data: {
          userId: 1,
          username: "test",
          role: "USER",
          token: "fake-token",
        },
      },
    });

    render(
      <BrowserRouter>
        <AuthProvider>
          <TestConsumer onMount={(c) => (context = c)} />
        </AuthProvider>
      </BrowserRouter>
    );
  });

  it("provides initial state", () => {
    expect(context).not.toBe(null);
    expect(context.user).toBe(null);
  });

  it("login updates the user state on success", async () => {
    await act(async () => {
      await context.handleLogin({ username: "test", password: "password" });
    });

    expect(context.user).toEqual({
      id: 1,
      username: "test",
      role: "USER",
    });
  });

  it("autologin success", async () => {
    localStorage.setItem("token", "fake-token");
    api.autoLogin.mockResolvedValue({
      success: true,
      data: {
        data: {
          userId: 1,
          username: "test",
          role: "USER",
          token: "fake-token",
        },
      },
    });
    await act(async () => {
      await context.handleAutoLogin();
    });

    expect(context.user).toEqual({
      id: 1,
      username: "test",
      role: "USER",
    });
  });

  it("autologin failure", async () => {
    localStorage.setItem("token", "fake-token");
    api.autoLogin.mockResolvedValue({
      success: false,
      data: {
        data: {
          userId: 1,
          username: "test",
          role: "USER",
          token: "fake-token",
        },
      },
    });
    await act(async () => {
      await context.handleAutoLogin();
    });

    expect(context.user).toBeNull();
  });
});
