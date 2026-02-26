import { Link } from "react-router";
import { Badge } from "/src/components/ui/Badge";
import { DropdownMenu } from "/src/components/ui/DropdownMenu";
import { LogOut, Users } from "lucide-react";

// TODO:
// - fetch classrooms from API
// - implement "Leave classroom" action in API and wire up to UI

const MOCK_CLASSROOMS = [
	{ id: 1, title: "Spring 2026 Biology", subject: "Biology", isOwner: true },
	{ id: 2, title: "Advanced Mathematics", subject: "Mathematics", isOwner: false },
	{ id: 3, title: "World History 101", subject: "History", isOwner: false },
	{ id: 4, title: "Introduction to Programming", subject: "Computer Science", isOwner: true },
];

function ClassroomRow({ classroom }) {
	const handleLeave = () => { // TODO
		console.log("Leave classroom", classroom.id);
	};

	const menuItems = [
		{ label: "Leave classroom", icon: <LogOut size={14} />, variant: "destructive", onClick: handleLeave },
	];

	return (
		<div className="flex items-center gap-4 rounded-lg py-2 hover:bg-secondary/5 transition-colors">
			<div className="flex h-9 w-9 shrink-0 items-center justify-center rounded-lg bg-primary/10 text-primary sm:ms-2">
				<Users size={22} />
			</div>

			<div className="min-w-0 flex-1">
				<div className="flex flex-nowrap items-center gap-2">
					<Link
						to={`/classrooms/${classroom.id}`}
						className="font-medium text-main truncate hover:text-primary hover:underline underline-offset-2 transition-colors"
					>
						{classroom.title}
					</Link>
					{classroom.isOwner && (
						<Badge bgColor="bg-green-100" textColor="text-green-700">Owner</Badge>
					)}
				</div>
				<p className="text-xs text-secondary">{classroom.subject}</p>
			</div>

			{!classroom.isOwner && (
				<DropdownMenu items={menuItems} triggerLabel="Classroom options" className="sm:me-2" />
			)}
		</div>
	);
}

export default function ClassroomList() {
	const classrooms = MOCK_CLASSROOMS;

	return (
		<div>
			<h2 className="text-sm font-semibold text-secondary uppercase tracking-wide">
				My Classrooms
			</h2>

			{classrooms.length === 0 ? (
				<p className="mt-3 text-sm text-secondary">You are not part of any classrooms yet.</p>
			) : (
				<div className="mt-2 flex flex-col divide-y divide-secondary/10">
					{classrooms.map((classroom) => (
						<ClassroomRow key={classroom.id} classroom={classroom} />
					))}
				</div>
			)}
		</div>
	);
}
