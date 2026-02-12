import { Link, redirect } from "react-router";
import { useAuth } from "/src/hooks/useAuth";
import { Avatar } from "/src/components/ui/Avatar";
import { Badge } from "/src/components/ui/Badge";
import { Button } from "/src/components/ui/Button";

export default function Profile() {
  const { user } = useAuth();

  const roleLabel = user?.role ? String(user.role) : "User";

  return (
    <div className="mx-auto max-w-5xl">
      <div className="mt-10">
        <h1 className="font-serif text-3xl md:text-5xl font-black text-main">Profile</h1>
        <p className="mt-2 text-secondary">Manage your account and jump back into studying.</p>
      </div>

      <div className="mt-8 rounded-2xl bg-white p-8">
        <div className="flex items-center justify-between gap-6 flex-wrap">
          <div className="flex items-center gap-4">
            <Avatar
              name={user.username}
              size="w-12 h-12"
              bgColor="bg-primary/10"
              textColor="text-primary"
              className="font-bold"
            />
            <div>
              <div className="flex items-center gap-2 flex-wrap">
                <p className="text-lg font-semibold text-main">{user.username}</p>
                <Badge>{roleLabel}</Badge>
              </div>
              <p className="text-sm text-secondary">User ID: {user.id}</p>
            </div>
          </div>
        </div>

        <div className="mt-8">
          <h2 className="text-sm font-semibold text-secondary uppercase tracking-wide">
            Quick Actions
          </h2>

          <div className="mt-3 flex flex-wrap gap-3">
            <Link to="/search">
              <Button>Search Quizzes</Button>
            </Link>

            <Link to="/my-quizzes">
              <Button variant="outline">My Quizzes</Button>
            </Link>

            <Link to="/my-quizzes/create">
              <Button variant="secondary">Create New Quiz</Button>
            </Link>
          </div>
        </div>

        <div className="mt-8 rounded-xl bg-primary/5 p-4 border border-primary/10">
          <p className="text-sm text-secondary">TODO IMPLEMENT HISTORY API</p>
        </div>
      </div>
    </div>
  );
}
