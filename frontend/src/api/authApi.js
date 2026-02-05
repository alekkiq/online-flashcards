import { fetchData } from "/src/lib/fetchData";

const login = async (username, password) => {
    const response = await fetchData("/auth/login", {
        method: "POST",
        body: JSON.stringify({ username, password }),
    });
    return response;
};