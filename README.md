# Supabase Integration Example

This project demonstrates how to integrate Supabase authentication with a Vue.js frontend and Spring Boot backend.

## Features

- Frontend (Vue.js, Pinia)
  - User registration with email/password
  - Social login with Google
  - Protected routes
  - Authentication state management with Pinia

- Backend (Spring Boot, Spring Security)
  - JWT token validation
  - Protected endpoints
  - User registration handling

## Prerequisites

- Node.js and npm
- Java 17 or higher
- Maven
- Supabase account

## Setup

### Supabase Setup

1. Create a new project at [Supabase](https://app.supabase.co/)
2. Enable Email Auth in Authentication settings
3. Set up Google OAuth provider:
   - Create OAuth credentials in Google Cloud Console
   - Configure redirect URL: `https://[YOUR_PROJECT_ID].supabase.co/auth/v1/callback`
4. Note down your Supabase URL and anon key
5. Get your JWT secret from Project Settings > API > JWT Settings

### Frontend Setup

1. Install dependencies:
   ```shell
   cd supabase-playground-frontend
   npm install
   ```

2. Create a `.env` file based on `.env.example` and add your Supabase credentials:
   ```
   VITE_SUPABASE_URL=your-supabase-project-url
   VITE_SUPABASE_ANON_KEY=your-supabase-anon-key
   VITE_API_URL=http://localhost:8080/api
   ```

3. Start the development server:
   ```shell
   cd supabase-playground-frontend
   npm run dev
   ```

### Backend Setup

1. Update `application.properties` with your Supabase credentials:
   ```
   supabase.url=https://your-project-id.supabase.co
   supabase.key=your-supabase-anon-key
   supabase.jwt.secret=your-supabase-jwt-secret
   ```

2. Build and run the application:
   ```shell
   cd supabase-playground-backend
   mvn spring-boot:run
   ```

## Usage

1. Open your browser and navigate to `http://localhost:5173`
2. Register a new user or sign in with Google
3. Once authenticated, you can access the protected profile page
4. The profile page has a button to fetch secret data from the backend

## Architecture

### Authentication Flow

1. **Email/Password Registration**:
   - User registers on the frontend
   - Frontend calls Supabase Auth API
   - Backend handles user creation in the database

2. **Google Social Login**:
   - User clicks Google login button
   - Supabase handles OAuth flow
   - User is redirected back to the application

3. **API Authentication**:
   - Frontend includes Supabase JWT token in API requests
   - Backend validates the token
   - If valid, the request is processed
   - If invalid, a 401 Unauthorized response is returned

## License

MIT
