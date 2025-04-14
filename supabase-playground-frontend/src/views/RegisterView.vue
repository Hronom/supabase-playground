<template>
  <div class="register-container">
    <h1>Register</h1>
    
    <div v-if="authStore.error" class="error-message">
      {{ authStore.error }}
    </div>
    
    <form @submit.prevent="handleRegister" class="register-form">
      <div class="form-group">
        <label for="email">Email</label>
        <input 
          type="email" 
          id="email" 
          v-model="email" 
          required 
          placeholder="your@email.com"
        />
      </div>
      
      <div class="form-group">
        <label for="password">Password</label>
        <input 
          type="password" 
          id="password" 
          v-model="password" 
          required 
          placeholder="Your password"
          minlength="6"
        />
      </div>
      
      <div class="form-group">
        <label for="confirmPassword">Confirm Password</label>
        <input 
          type="password" 
          id="confirmPassword" 
          v-model="confirmPassword" 
          required 
          placeholder="Confirm your password"
          minlength="6"
        />
        <div v-if="passwordMismatch" class="error-text">
          Passwords do not match
        </div>
      </div>
      
      <div class="form-actions">
        <button 
          type="submit" 
          class="btn-primary" 
          :disabled="authStore.loading || passwordMismatch"
        >
          {{ authStore.loading ? 'Registering...' : 'Register' }}
        </button>
      </div>
    </form>
    
    <div class="social-login">
      <p>Or register with:</p>
      <button 
        @click="handleGoogleLogin" 
        class="btn-google" 
        :disabled="authStore.loading"
      >
        Google
      </button>
    </div>
    
    <div class="login-link">
      <p>Already have an account? <router-link to="/login">Login</router-link></p>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '../stores/auth'

const router = useRouter()
const authStore = useAuthStore()

const email = ref('')
const password = ref('')
const confirmPassword = ref('')

const passwordMismatch = computed(() => {
  return password.value && confirmPassword.value && password.value !== confirmPassword.value
})

async function handleRegister() {
  if (passwordMismatch.value) return
  
  await authStore.signUpWithEmail(email.value, password.value)
  if (!authStore.error) {
    router.push('/login')
  }
}

async function handleGoogleLogin() {
  await authStore.signInWithGoogle()
}
</script>

<style scoped>
.register-container {
  max-width: 400px;
  margin: 0 auto;
  padding: 2rem;
}

.register-form {
  display: flex;
  flex-direction: column;
  gap: 1rem;
  margin-bottom: 1.5rem;
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.form-group input {
  padding: 0.5rem;
  border: 1px solid #ccc;
  border-radius: 4px;
}

.form-actions {
  margin-top: 1rem;
}

.btn-primary {
  background-color: #4CAF50;
  color: white;
  border: none;
  padding: 0.5rem 1rem;
  border-radius: 4px;
  cursor: pointer;
}

.btn-primary:disabled {
  background-color: #cccccc;
  cursor: not-allowed;
}

.social-login {
  margin-top: 1.5rem;
  text-align: center;
}

.btn-google {
  background-color: #DB4437;
  color: white;
  border: none;
  padding: 0.5rem 1rem;
  border-radius: 4px;
  cursor: pointer;
}

.btn-google:disabled {
  background-color: #cccccc;
  cursor: not-allowed;
}

.login-link {
  margin-top: 1.5rem;
  text-align: center;
}

.error-message {
  background-color: #ffebee;
  color: #c62828;
  padding: 0.5rem;
  border-radius: 4px;
  margin-bottom: 1rem;
}

.error-text {
  color: #c62828;
  font-size: 0.8rem;
  margin-top: 0.25rem;
}
</style>
