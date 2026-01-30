import { Outlet } from "react-router";
import NavBar from "./NavBar";

export default function MainLayout() {
    return (
        <div className="min-h-screen">
            <NavBar />
            <main className="w-full max-w-7xl mx-auto px-4">
                <Outlet />
            </main>
        </div>
    );
}
