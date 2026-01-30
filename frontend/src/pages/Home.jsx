import { Button } from "../components/ui/Button";
import { useNavigate } from "react-router";

export default function Home() {
    const navigate = useNavigate();
    return (
        <div className="flex flex-col items-center md:items-start gap-4 md:gap-6 px-4 md:px-0 py-12 mt-[10vh]">
            <h1 className="font-serif text-center md:text-left font-bold text-5xl md:text-6xl lg:text-7xl text-main">
                Flashcard learning<br />made <span className="text-primary">easy</span>
            </h1>
            <Button onClick={() => navigate("/search")} className="py-5 px-10 text-lg md:py-6 md:px-12 md:text-2xl">Search Quizzes</Button>
            <div className="flex flex-col font-serif items-center md:items-start font-bold text-center md:text-left max-w-md text-base md:text-lg">
                <p className="text-secondary">
                    In vulputate cursus sem ac consectetur. Nam nec ex scelerisque, blandit neque sit amet.
                </p>
                <p className="text-gray-500">
                    Morbi efficitur augue in odio posuere, vel lacinia purus auctor. Donec finibus non odio sed pellentesque.
                </p>
            </div>
        </div>
    );
}