import { useState, useEffect } from "react";
import { useSearch } from "./useSearch";

const MOCK_CLASSROOMS = [
    {
        classroomId: 1,
        title: "Biology 101",
        description: "An introductory course to Biology.",
        note: "Next quiz on cell structure is on Friday!",
        subject: "Biology",
        joinCode: "BIO123",
        owner: {
            id: 1,
            username: "Aleksput",
            role: "teacher"
        },
        quizzes: [],
        users: [],
        learningMaterials: []
    }
];

const classroomFilterFn = (classroom, query) =>
    classroom.title.toLowerCase().includes(query) ||
    classroom.description.toLowerCase().includes(query) ||
    classroom.subject.toLowerCase().includes(query) ||
    classroom.owner.username.toLowerCase().includes(query);

export function useClassroomSearch(searchParams, setSearchParams) {
    const { searchQuery, setSearchQuery, filteredItems } = useSearch(
        MOCK_CLASSROOMS,
        classroomFilterFn,
        searchParams,
        setSearchParams
    );

    return {
        searchQuery,
        setSearchQuery,
        filteredClassrooms: filteredItems,
        isLoading: false,
    }
}