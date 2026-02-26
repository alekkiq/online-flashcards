import { Link, redirect } from "react-router";
import { useAuth } from "/src/hooks/useAuth";
import { Avatar } from "/src/components/ui/Avatar";
import { Badge } from "/src/components/ui/Badge";
import { Button } from "/src/components/ui/Button";
import EditProfileForms from "/src/components/auth/EditProfileForms";
import PromotionRequestForm from "/src/components/profile/PromotionRequestForm";
import ClassroomList from "/src/components/profile/ClassroomList";
import { usePromotionRequests } from "/src/hooks/usePromotionRequests";
import { useEffect, useState } from "react";

export default function Profile() {
  const { user } = useAuth();
  const [isEditingProfile, setIsEditingProfile] = useState(false);
  const [isRequestingPromotion, setIsRequestingPromotion] = useState(false);
  const { requests, isLoading: isLoadingRequests, submitRequest, submitError, submitSuccess } = usePromotionRequests();

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
    <div className="mx-auto max-w-5xl">
      <div className="mt-10">
        <h1 className="font-serif text-3xl md:text-5xl font-black text-main">Profile</h1>
        <p className="mt-2 text-secondary">Manage your account and jump back into studying.</p>
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
            {isEditingProfile ? "Cancel" : "Edit profile"}
          </Button>
        </div>

        {isEditingProfile && (
            <EditProfileForms user={user} onCancel={() => setIsEditingProfile(false)} />
        )}

        <hr className="border-secondary/20"/>

        <div>
          <h2 className="text-sm font-semibold text-secondary uppercase tracking-wide">
            Quick Actions
          </h2>

          <div className="mt-3 flex flex-wrap gap-3">
            <Link to="/search" className="w-full sm:w-auto">
              <Button>Search Quizzes</Button>
            </Link>

            <Link to="/my-quizzes" className="w-full sm:w-auto">
              <Button variant="outline">My Quizzes</Button>
            </Link>

            <Link to="/my-quizzes/create" className="w-full sm:w-auto">
              <Button variant="secondary">Create New Quiz</Button>
            </Link>
          </div>
        </div>

        <hr className="border-secondary/20"/>

        <ClassroomList />

        <hr className="border-secondary/20"/>

        <div className="flex items-center justify-between flex-wrap gap-y-2">
          <h2 className="text-sm font-semibold text-secondary uppercase tracking-wide">
            Request Promotion to Teacher
          </h2>
          {user?.role !== "ADMIN" && !isLoadingRequests && (
              hasPending ? (
                  <Badge bgColor="bg-green-100" textColor="text-green-700">Request pending</Badge>
              ) : (
                  <Button
                      className="w-full sm:w-auto"
                      variant={isRequestingPromotion ? "outline" : "secondary"}
                      onClick={handleRequestPromotion}
                  >
                    {isRequestingPromotion ? "Cancel" : "Request Promotion"}
                  </Button>
              )
          )}
        </div>

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
