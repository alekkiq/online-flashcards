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

//TODO: Implement register