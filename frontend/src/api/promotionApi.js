import { fetchData } from "/src/lib/fetchData";

export const requestPromotion = async (token, message = "") => {
    const response = await fetchData("promotion-requests", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${token}`,
        },
        body: JSON.stringify({ message }),
    });
    return response;
};

export const fetchMyPromotionRequests = async (token) => {
    const response = await fetchData("promotion-requests/my-requests", {
        method: "GET",
        headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${token}`,
        },
    });
    return response;
}