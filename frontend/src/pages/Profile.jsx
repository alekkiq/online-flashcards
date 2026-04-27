import { Link, redirect } from "react-router";
import { useAuth } from "/src/hooks/useAuth";
import { Avatar } from "/src/components/ui/Avatar";
import { Badge } from "/src/components/ui/Badge";
import { Button } from "/src/components/ui/Button";
import EditProfileForms from "/src/components/auth/EditProfileForms";
import PromotionRequestForm from "/src/components/profile/PromotionRequestForm";
import ClassroomList from "/src/components/profile/ClassroomList";
import { usePromotionRequests } from "/src/hooks/usePromotionRequests";
import { useState } from "react";
import { useTranslation } from "react-i18next";

export default function Profile() {
  const { user } = useAuth();
  const [isEditingProfile, setIsEditingProfile] = useState(false);
  const [isRequestingPromotion, setIsRequestingPromotion] = useState(false);
  const { t } = useTranslation();
  const {
    requests,
    isLoading: isLoadingRequests,
    submitRequest,
    submitError,
    submitSuccess,
  } = usePromotionRequests();

  const roleLabel = user?.role ? String(user.role) : "User";
  const hasPending = requests.some((r) => r.status === "PENDING");

  const handleEditProfile = () => {
    setIsEditingProfile((prev) => !prev);
    setIsRequestingPromotion(false);
  };

  const handleRequestPromotion = () => {
    setIsRequestingPromotion((prev) => !prev);
    setIsEditingProfile(false);
  };

  return (
    <div className="mx-auto max-w-5xl py-8">
      <div className="mt-10">
        <h1 className="font-serif text-3xl md:text-5xl font-black text-main">{t("profile.title")}</h1>
        <p className="mt-2 text-secondary">{t("profile.subtitle")}</p>
      </div>

      <div className="mt-8 rounded-2xl bg-white p-3 sm:p-8 space-y-4">
        <div className="flex items-center justify-between gap-6 flex-wrap">
          <div className="flex items-center gap-4">
            <Avatar
              name={user.username}
              size="w-12 h-12"
              bgColor="bg-primary/10"
              textColor="text-primary"
              textSize="text-md"
              className="font-bold"
            />
            <div>
              <div className="flex items-center gap-2 flex-wrap">
                <p className="text-lg font-semibold text-main">{user.username}</p>
                <Badge>{roleLabel}</Badge>
              </div>
              <p className="text-sm text-secondary">{user.email}</p>
            </div>
          </div>
          <Button
            className="w-full sm:w-auto"
            variant={isEditingProfile ? "outline" : undefined}
            onClick={handleEditProfile}
          >
            {isEditingProfile ? t("profile.cancel") : t("profile.editProfile")}
          </Button>
        </div>

        {isEditingProfile && (
          <EditProfileForms user={user} onCancel={() => setIsEditingProfile(false)} />
        )}

        <hr className="border-secondary/20" />

        <div>
          <h2 className="text-sm font-semibold text-secondary uppercase tracking-wide">
            {t("profile.quickActions")}
          </h2>

          <div className="mt-3 flex flex-wrap gap-3">
            <Link to="/search" className="w-full sm:w-auto">
              <Button>{t("profile.searchQuizzes")}</Button>
            </Link>

            <Link to="/my-quizzes" className="w-full sm:w-auto">
              <Button variant="outline">{t("profile.myQuizzes")}</Button>
            </Link>

            <Link to="/my-quizzes/create" className="w-full sm:w-auto">
              <Button variant="secondary">{t("profile.createNewQuiz")}</Button>
            </Link>
          </div>
        </div>

        <hr className="border-secondary/20" />

        <ClassroomList />

        {user?.role !== "ADMIN" && user?.role !== "TEACHER" && (
          <>
            <hr className="border-secondary/20" />
            <div className="flex items-center justify-between flex-wrap gap-y-2">
              <h2 className="text-sm font-semibold text-secondary uppercase tracking-wide">
                {t("profile.requestPromotion")}
              </h2>
              {user?.role !== "ADMIN" &&
                !isLoadingRequests &&
                (hasPending ? (
                  <Badge bgColor="bg-green-100" textColor="text-green-700">
                    {t("profile.requestPending")}
                  </Badge>
                ) : (
                  <Button
                    className="w-full sm:w-auto"
                    variant={isRequestingPromotion ? "outline" : "secondary"}
                    onClick={handleRequestPromotion}
                  >
                    {isRequestingPromotion ? t("profile.cancel") : t("profile.requestPromotionBtn")}
                  </Button>
                ))}
            </div>
          </>
        )}

        <PromotionRequestForm
          isOpen={isRequestingPromotion}
          onClose={() => setIsRequestingPromotion(false)}
          submitRequest={submitRequest}
          submitError={submitError}
          submitSuccess={submitSuccess}
        />
      </div>
    </div>
  );
}
