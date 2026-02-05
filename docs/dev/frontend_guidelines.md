# Frontend Development

The frontend is done with Vite and React. The structure of the project is as follows:
- `src/components`: React components
- `src/hooks`: custom hooks here (Remember to conduct tests for all custom hooks)
- `src/pages`: Page components
- `src/api`: API services, define here all the necessary api calls to the backend
- `src/test`: Test files (Test all context, custom hooks, and lib utils here)
- `src/lib`: Utility functions like fetchdata, dateformatting, tailwind merge, zod schemas, etc. (Remember to conduct tests for all lib utils)
- `src/context`: Contexts here (Remember to conduct tests for all contexts)
- `src/config`: configuration files, like navlinks, etc.
- `src/assets`: assets like images, icons, etc.
- `src/App.jsx`: Main application component
- `src/main.jsx`: Entry point of the appilcation

## UI guidelines

- use tailwind for styling
- use prebuilt buttons, formfields, labels etc found in `src/components/ui`
- use lucide react for icons
- use zod for schema validation
- use react hook form for form handling
- split components if they are too large or used in multiple place

## API guidelines

- use fetchdata.js for api calls, found in `src/lib/fetchdata.js`
- create own api services in `src/api` for each feature
- handle all false successes and errors and show them to the user


## Testing guidelines

- Test all context, custom hooks, and lib utils 

### Commands
- `npm test`:  Run tests in watch mode
- `npm run test:run`: Run tests once
- `npm run test:coverage`:  Run tests with coverage report


### Coverage

Coverage reports are generated in the `coverage/` directory after running `npm run test:coverage`. Open `coverage/index.html` in a browser to view the detailed report.

## Codebase guidelines

- use jsdoc for hooks and lib utils
- extract all "business logic" into custom hooks
- keep components small and focused on a single responsibiliy
- use descriptive variable and function names
- keep functions small and focused on a single responsibility
- use descriptive variable and function names

### Forms

- use zod for schema validation
- use react hook form for form handling
- use prebuilt components found in `src/components/ui` for building forms
- place schemas into a folder inside `src/lib/schemas/`

###How to use prebuilt components for building forms

```javascript
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
    username: z.string().min(1, "Username is required"),
    password: z.string().min(1, "Password is required"),
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
    };

    // THIS IS AN EXAMPLE OF HOW TO USE PREBUILT COMPONENTS FOR BUILDING FORMS, PLEASE USE IT THE SAME WAY FOR OTHER FORMS

    return (
        <div>
            <h1>Login</h1>
            <form onSubmit={handleSubmit(onSubmit)}>
                <FormField error={errors.username?.message} label="Username" > {/* add error messages do the field and also the label */}
                    <Input
                        id="username"
                        type="text"
                        error={!!errors.username}  {/* add for the input also so it turns red on error , remember to add the !! before the error */}
                        {...register("username")}
                    />
                </FormField>
                <FormField error={errors.password?.message} label="Password">
                    <Input
                        id="password"
                        type="password"
                        error={!!errors.password}
                        {...register("password")}
                    />
                </FormField>
                {serverError && <p className="text-red-500">{serverError}</p>} {/* add server errors here if any or wherever you want */}
                <Button type="submit" isLoading={isSubmitting}>
                    Login
                </Button>
            </form>
        </div>
    );
}
```
