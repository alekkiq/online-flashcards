import { useParams } from "react-router";
import { useEffect, useState } from "react";
import { PageLoader } from "../components/ui/PageLoader";
import { Badge } from "../components/ui/Badge";
import { Avatar } from "../components/ui/Avatar";
import { Button } from "../components/ui/Button";
import { useAuth } from "../hooks/useAuth";
import { useNavigate } from "react-router";
import { useQuizContext } from "../hooks/useQuizContext";
import { ScoreBadge } from "../components/ui/ScoreBadge";
import { BackLink } from "../components/ui/BackLink";
import { History } from "lucide-react";
import { useTranslation } from "react-i18next";

export default function QuizDetails() {
  const { id } = useParams();
  const { user } = useAuth();
  const navigate = useNavigate();
  const { fetchQuiz, quizHistory, currentQuiz } = useQuizContext();
  const { t } = useTranslation();
  useEffect(() => {
    fetchQuiz(id);
  }, [id, fetchQuiz]);

  return (
    <>
      {currentQuiz ? (
        <div className="max-w-7xl mx-auto flex flex-col mt-8 mb-[10vh]">
          <BackLink label={t("quizDetails.backToQuizSearch")} />
          <div className="flex flex-col gap-4 p-8 bg-white rounded-lg">
            <div className="flex items-center gap-3 flex-wrap">
              <h1 className="font-serif text-3xl md:text-4xl lg:text-5xl font-bold text-main">
                {currentQuiz.title}
              </h1>
              <Badge size="md">{currentQuiz.subjectName}</Badge>
            </div>
            <p className="font-inter text-sm md:text-lg text-secondary max-w-2xl">
              {currentQuiz.description}
            </p>
            <div className="flex flex-row items-center gap-2 mt-1">
              <Avatar name={currentQuiz?.creatorUsername} className="w-9 h-9 md:w-11 md:h-11" />
              <div className="flex flex-col items-start">
                <p className="font-inter font-bold text-sm md:text-lg text-secondary">
                  <span className="text-main font-medium">{t("quizDetails.createdBy")}</span>
                  {currentQuiz?.creatorUsername}
                </p>
                {currentQuiz?.creatorRole && (
                  <Badge textColor="text-white" bgColor="bg-primary">
                    {t(`quizCard.roles.${currentQuiz.creatorRole}`, { defaultValue: currentQuiz.creatorRole })}
                  </Badge>
                )}
              </div>
            </div>
            <hr className="w-full border-secondary/50 my-2" />
            {user ? (
              <div className="flex flex-col gap-2">
                <div className="flex flex-row items-center gap-1">
                  <History className="w-5 h-5 text-secondary" strokeWidth={3} />
                  <p className="font-inter font-bold text-sm md:text-lg text-secondary">{t("quizDetails.history")}</p>
                </div>
                <div className="flex flex-col gap-5">
                  {quizHistory.map((h) => (
                    <div key={h.quizResultId} className="flex flex-col gap-2">
                      <ScoreBadge percentage={h.scorePercentage} date={h.completedAt} />
                    </div>
                  ))}
                </div>
              </div>
            ) : (
              <p className="font-inter font-medium text-sm md:text-lg text-secondary">
                {t("quizDetails.loginToSave")}
              </p>
            )}
          </div>
          <div className="flex flex-col md:flex-row gap-4 mt-6 sm:mt-8 justify-center md:justify-between items-center">
            <div className="flex flex-row gap-8 md:ml-8">
              <div className="flex flex-col items-center">
                <p className="font-inter font-bold text-sm md:text-lg text-main">
                  {quizHistory.length}
                </p>
                <p className="font-serif font-bold text-sm md:text-lg text-main">{t("quizDetails.tries")}</p>
              </div>
              <div className="w-px h-12 bg-secondary/20"></div>
              <div className="flex flex-col items-center">
                <p className="font-inter font-bold text-sm md:text-lg text-secondary">
                  {currentQuiz.cardCount}
                </p>
                <p className="font-serif font-bold text-sm md:text-lg text-main">{t("quizDetails.cardCount")}</p>
              </div>
            </div>
            <Button
              size="lg"
              className="md:w-1/3 w-full mt-2"
              onClick={() => navigate(`/quiz/${currentQuiz.quizId}`)}
            >
              {t("quizDetails.playQuiz")}
            </Button>
          </div>
        </div>
      ) : (
        <PageLoader />
      )}
    </>
  );
}
