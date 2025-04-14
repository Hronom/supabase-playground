# Supabase Configuration

## Project Setup
1. Create a new project at https://app.supabase.com/
2. Note down the following credentials:
   - Project URL
   - API Key (anon public)
   - API Key (service role) - Keep this secret!

## Authentication Settings
1. Enable Email Auth
2. Enable Google OAuth provider
   - Create OAuth credentials in Google Cloud Console
   - Configure redirect URL: `https://[YOUR_PROJECT_ID].supabase.co/auth/v1/callback`

## Database Schema
```sql
-- Create a public profiles table
create table public.profiles (
  id uuid references auth.users on delete cascade not null primary key,
  email text unique not null,
  name text,
  created_at timestamp with time zone default now() not null,
  updated_at timestamp with time zone default now() not null
);

-- Enable RLS (Row Level Security)
alter table public.profiles enable row level security;

-- Create policy to allow users to view their own profile
create policy "Users can view their own profile" on public.profiles
  for select using (auth.uid() = id);

-- Create policy to allow users to update their own profile
create policy "Users can update their own profile" on public.profiles
  for update using (auth.uid() = id);
```

## Security Rules
- Set up Row Level Security (RLS) for all tables
- Configure appropriate policies for data access
