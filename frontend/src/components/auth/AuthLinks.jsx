import { Link } from "react-router";
import { Button } from "/src/components/ui/Button";
import { useAuth } from "/src/hooks/useAuth";
import { User } from "lucide-react";

export default function AuthLinks() {
  const { user, handleLogout } = useAuth();

  if (user) {
    return (
      <div className="flex items-center gap-3">
        <Button variant="outline" size="md" onClick={handleLogout}>
          Sign Out
        </Button>
        <Link
          to="/profile"
          aria-label="Profile"
          className="w-9 h-9 rounded-full bg-primary/10 flex items-center justify-center hover:bg-primary/20 transition-colors overflow-hidden"
        >
          <User className="w-5 h-5 text-primary" />
        </Link>
      </div>
    );
  }

  return (
    <div className="flex items-center gap-2">
      <Link to="/login">
        <Button variant="outline" size="md">
          Log In
        </Button>
      </Link>
      <Link to="/login?signup=true">
        <Button size="md">Sign Up</Button>
      </Link>
    </div>
  );
}
