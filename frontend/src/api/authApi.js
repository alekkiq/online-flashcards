import { fetchData } from "/src/lib/fetchData";

export const login = async (username, password) => {
    const response = await fetchData("auth/login", {
        method: "POST",
        body: JSON.stringify({
            username: username,
            password: password,
        }),
    });
    return response;
};

export const autoLogin = async (token) => {
    const response = await fetchData("users/me", {
        method: "GET",
        headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${token}`,
        },
    });
    return response;
};

//TODO: Implement register