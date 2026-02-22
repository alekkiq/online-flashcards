import { useState, useEffect, useCallback } from "react";
import { useAuth } from "/src/hooks/useAuth";
import { fetchMyPromotionRequests, requestPromotion } from "/src/api/promotionApi";

export function usePromotionRequests() {
    const [requests, setRequests] = useState([]);
    const [isLoading, setIsLoading] = useState(true);
    const [error, setError] = useState(null);
    const [submitError, setSubmitError] = useState(null);
    const [submitSuccess, setSubmitSuccess] = useState(null);

    const { user } = useAuth();

    const fetchRequests = useCallback(async () => {
        setIsLoading(true);
        setError(null);

        const token = localStorage.getItem("token");
        const response = await fetchMyPromotionRequests(token);

        setIsLoading(false);

        if (!response.success) {
            setError(response.error ?? "Failed to load promotion requests");
            return;
        }

        setRequests(response.data?.data ?? []);
    }, []);

    const submitRequest = useCallback(async (message = "") => {
        setSubmitError(null);
        setSubmitSuccess(null);

        const token = localStorage.getItem("token");
        const response = await requestPromotion(token, message);

        if (!response.success) {
            setSubmitError(response.error ?? "Failed to submit promotion request");
            return { success: false, error: response.error };
        }

        setSubmitSuccess("Your promotion request has been submitted!");
        await fetchRequests();
        return { success: true };
    }, [fetchRequests]);

    useEffect(() => {
        if (user) {
            fetchRequests();
        }
    }, [user, fetchRequests]);

    return {
        requests,
        isLoading,
        error,
        submitError,
        submitSuccess,
        submitRequest,
        refetch: fetchRequests,
    };
}