import { useTranslation } from "react-i18next";

export default function FlipCard({ front, back, isFlipped, handleFlip }) {
  const { t } = useTranslation();
  return (
    <div className={`flip-card-container ${isFlipped ? "flipped" : ""}`} onClick={handleFlip}>
      <div className="flip-card-inner">
        <div className="flip-card-front">
          <p>{front}</p>
          <div className="absolute bottom-4 left-4 right-4 text-center">
            <p className="font-inter text-sm opacity-70">{t("quizGame.clickToFlip")}</p>
          </div>
        </div>
        <div className="flip-card-back">
          <p>{back}</p>
        </div>
      </div>
    </div>
  );
}
