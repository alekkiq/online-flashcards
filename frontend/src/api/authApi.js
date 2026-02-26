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

export const register = async (username, password, email) => {
    const response = await fetchData("auth/register", {
        method: "POST",
        body: JSON.stringify({
            username: username,
            password: password,
            email: email,
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

export const updateEmail = async (token, email) => {
    const response = await fetchData(`users/me/email`, {
        method: "PUT",
        headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${token}`,
        },
        body: JSON.stringify({ email }),
    });
    return response;
};

export const updatePassword = async (token, oldPassword, newPassword) => {
    const response = await fetchData(`users/me/password`, {
        method: "PUT",
        headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${token}`,
        },
        body: JSON.stringify({ oldPassword, newPassword }),
    });
    return response;
};
