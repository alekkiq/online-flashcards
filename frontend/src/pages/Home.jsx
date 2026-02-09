import { Button } from "../components/ui/Button";
import { useNavigate } from "react-router";
import { Label } from "../components/ui/Label";
import { Input } from "../components/ui/Input";

export default function Home() {
  const navigate = useNavigate();
  return (
    <div className="max-w-7xl mx-auto">
      <section className="relative flex flex-col items-center md:items-start gap-4 md:gap-6 px-4 md:px-0 py-12 mt-[10vh] h-[70vh]">
        <h1 className="font-serif text-center md:text-left font-bold text-5xl md:text-6xl lg:text-7xl text-main">
          Flashcard learning
          <br />
          made <span className="text-primary">easy</span>
        </h1>
        <Button
          onClick={() => navigate("/search")}
          className="py-5 px-10 text-lg md:py-6 md:px-12 md:text-2xl"
        >
          Search Quizzes
        </Button>
        <div className="flex flex-col font-serif items-center md:items-start font-bold text-center md:text-left max-w-md text-base md:text-lg">
          <p className="text-secondary">
            In vulputate cursus sem ac consectetur. Nam nec ex scelerisque, blandit neque sit amet.
          </p>
          <p className="text-gray-500">
            Morbi efficitur augue in odio posuere, vel lacinia purus auctor. Donec finibus non odio
            sed pellentesque.
          </p>
        </div>
        <hr className="border-secondary w-full absolute bottom-0" />
      </section>
      <section className="bg-grid">
        <div className="relative z-10 flex flex-col md:flex-row items-center justify-between text-center gap-4 md:gap-6 px-4 md:px-0 py-12 mt-[10vh]">
          <div className="flex flex-col font-serif gap-6 items-center md:items-start font-bold text-center md:text-left max-w-md text-base md:text-lg">
            <h2 className="font-serif text-center md:text-left font-bold text-5xl md:text-6xl lg:text-7xl text-main">
              Find <br /> <span className="text-secondary">community made</span> <br />{" "}
              <span className="text-primary">quizzes</span>
            </h2>
            <p className="text-secondary">
              In vulputate cursus sem ac consectetur. Nam nec ex scelerisque, blandit neque sit
              amet.
            </p>
          </div>
          <img
            src="/src/assets/images/hero_section_1.png"
            alt="Picture of searching quizzes"
            className="md:w-1/2 w-3/4 p-4 rounded-2xl bg-primary"
          />
        </div>
        <hr className="border-secondary mt-20" />
      </section>
      <section className="bg-grid">
        <div className="relative z-10 flex flex-col md:flex-row items-center justify-between text-center gap-4 md:gap-6 px-4 md:px-0 py-12 mt-[10vh]">
          <img
            src="/src/assets/images/hero_section_2.png"
            alt="Picture of quiz creation process"
            className="md:w-1/3 w-3/4 py-4 px-20 rounded-2xl bg-primary"
          />
          <div className="flex flex-col font-serif gap-6 items-center md:items-end font-bold text-center md:text-right max-w-md text-base md:text-lg">
            <h2 className="font-serif text-center md:text-right font-bold text-5xl md:text-6xl lg:text-7xl text-main">
              Create <br /> <span className="text-secondary">your own</span> <br />{" "}
              <span className="text-primary">quizzes</span>
            </h2>
            <Button
              onClick={() => navigate("/login?signup=true")}
              className="py-5 px-10 text-lg md:py-6 md:px-12 md:text-2xl"
            >
              Register Now
            </Button>
            <p className="text-secondary">
              Register now to create your own quizzes and share them to the community.
            </p>
          </div>
        </div>
      </section>
    </div>
  );
}
