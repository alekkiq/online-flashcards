import { useState, useEffect } from "react";

/**
 * Generalized search hook that can be extended to any list of items and filtering logic.
 * @param items - The list of items to filter
 * @param filterFn - A function that takes an item and a search query and returns true if the item matches the query
 * @param searchParams - The URLSearchParams object from the component using this hook
 * @param setSearchParams - The function to update the URLSearchParams object
 */
export function useSearch(items, filterFn, searchParams, setSearchParams) {
    const searchQuery = searchParams.get("q") || "";
    const [filteredItems, setFilteredItems] = useState(items);

    const setSearchQuery = (query) => {
        setSearchParams((prev) => {
            const newParams = new URLSearchParams(prev);
            if (query) {
                newParams.set("q", query);
            } else {
                newParams.delete("q");
            }
            return newParams;
        })
    };

    useEffect(() => {
        const query = searchQuery.toLowerCase();
        setFilteredItems(items.filter((item) => filterFn(item, query)));
    }, [searchQuery, items]);

    return {
        searchQuery,
        setSearchQuery,
        filteredItems,
    }
}