import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { z } from "zod";
import { Button } from "../ui/Button.jsx";
import { Input } from "../ui/Input.jsx";
import { FormField } from "../ui/FormField.jsx";
import { login } from "../../api";

// Zod validation schema
const loginSchema = z.object({
    username: z.string().min(1, "Username is required"),
    password: z.string().min(1, "Password is required"),
});

export default function LoginForm() {
    const {
        register,
        handleSubmit,
        formState: { errors, isSubmitting },
    } = useForm({
        resolver: zodResolver(loginSchema),
        defaultValues: {
            username: "",
            password: "",
        },
    });

    const onSubmit = async (data) => {
        console.log("Form data:", data);
       const response = await login(data.username, data.password);
       console.log("Login response:", response);
    };

    return (
        <div>
            <h1>Login</h1>
            <form onSubmit={handleSubmit(onSubmit)}>
                <FormField error={errors.username?.message} label="Username" >
                    <Input
                        id="username"
                        type="text"
                        error={errors.username?.message}
                        {...register("username")}
                    />
                </FormField>
                <FormField error={errors.password?.message} label="Password">
                    <Input
                        id="password"
                        type="password"
                        error={errors.password?.message}
                        {...register("password")}
                    />
                </FormField>
                <Button type="submit" disabled={isSubmitting}>
                    {isSubmitting ? "Logging in..." : "Login"}
                </Button>
            </form>
        </div>
    );
}