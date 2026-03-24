import {BookCopy} from "lucide-react";

export function Logo() {
    return (
        <div className="flex items-center gap-1.5">
            <BookCopy className="text-primary" strokeWidth={2.25} />
            <p className="font-serif text-2xl font-black">OnlyCards</p>
        </div>
    )
}