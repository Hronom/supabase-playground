<template>
  <div class="home">
    <h1>Supabase + Vue.js + Spring Boot Integration</h1>

    <div class="content">
      <p>Welcome to the Supabase integration example!</p>

      <div v-if="authStore.isAuthenticated" class="authenticated">
        <p>You are logged in as <strong>{{ authStore.user.email }}</strong></p>
        <router-link to="/profile" class="btn-primary">View Profile</router-link>
      </div>

      <div v-else class="unauthenticated">
        <p>Please log in to access the application features.</p>
        <div class="auth-buttons">
          <router-link to="/login" class="btn-primary">Login</router-link>
          <router-link to="/register" class="btn-secondary">Register</router-link>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { useAuthStore } from '../stores/auth'

const authStore = useAuthStore()
</script>

<style scoped>
.home {
  max-width: 800px;
  margin: 0 auto;
  padding: 2rem;
  text-align: center;
}

.content {
  margin-top: 2rem;
}

.authenticated, .unauthenticated {
  margin-top: 2rem;
  padding: 1.5rem;
  background-color: #f5f5f5;
  border-radius: 8px;
}

.auth-buttons {
  display: flex;
  justify-content: center;
  gap: 1rem;
  margin-top: 1rem;
}

.btn-primary, .btn-secondary {
  display: inline-block;
  padding: 0.5rem 1.5rem;
  border-radius: 4px;
  text-decoration: none;
  font-weight: bold;
}

.btn-primary {
  background-color: #4CAF50;
  color: white;
}

.btn-secondary {
  background-color: #2196F3;
  color: white;
}
</style>
