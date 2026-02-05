# Frontend Development

The frontend is done with Vite and React. The structure of the project is as follows:
- `src/components`: React components
- `src/hooks`: custom hooks here *** Remember to conduct tests for all custom hooks here ***
- `src/pages`: Page components
- `src/api`: API services, define here all the necessary api calls to the backend
- `src/test`: Test files *** Test all context, custom hooks, and lib utils here***
- `src/lib`: Utility functions like fetchdata, dateformatting, tailwind merge, zod schemas, etc. *** Remember to conduct tests for all lib utils here***
- `src/context`: Contexts here *** Remember to conduct tests for all contexts here***
- `src/config`: configuration files, like navlinks, etc.
- `src/assets`: assets like images, icons, etc.
- `src/App.jsx`: Main application component
- `src/main.jsx`: Entry point of the application

## UI guidelines

- use tailwind for styling
- use prebuilt buttons, formfields, labels etc found in `src/components/ui`
- use lucide react for icons
- use zod for schema validation
- use react hook form for form handling
- split components if they are too large or used in multiple places

## API guidelines

- use fetchdata.js for api calls, found in `src/lib/fetchdata.js`
- create own api services in `src/api` for each featuree
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
- keep components small and focused on a single responsibility
- use descriptive variable and function names
- keep functions small and focused on a single responsibility
- use descriptive variable and function names

