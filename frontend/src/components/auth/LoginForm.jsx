import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { z } from "zod";
import { Button } from "../ui/Button.jsx";
import { Input } from "../ui/Input.jsx";
import { FormField } from "../ui/FormField.jsx";
import { login } from "../../api";
import { useState } from "react";

// Zod validation schema // Place scehmas into a folder inside src/lib/schemas/loginSchema please.
const loginSchema = z.object({
    username: z.string().min(10, "Username is required"),
    password: z.string().min(10, "Password is required"),
});

export default function LoginForm() {
    const [serverError, setServerError] = useState(null);
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
       if (!response.success) {
        setServerError(response.error);
       }
    };

    // THIS IS AN EXAMPLE OF HOW TO USE PREBUILT COMPONENTS FOR BUILDING FORMS, PLEASE USE IT THE SAME WAY FOR OTHER FORMS

    return (
        <div>
            <h1>Login</h1>
            <form onSubmit={handleSubmit(onSubmit)}>
                <FormField error={errors.username?.message } label="Username" >
                    <Input
                        id="username"
                        type="text"
                        hasError={!!errors.username}
                        {...register("username")}
                    />
                </FormField>
                <FormField error={errors.password?.message} label="Password">
                    <Input
                        id="password"
                        type="password"
                        hasError={!!errors.password}
                        {...register("password")}
                    />
                </FormField>
                {serverError && <p className="text-red-500">{serverError}</p>}
                <Button type="submit" loading={isSubmitting} loadingText="Logging in...">
                    Login
                </Button>
            </form>
        </div>
    );
}