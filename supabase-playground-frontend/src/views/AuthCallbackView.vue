<template>
  <div class="auth-callback">
    <h2>Processing authentication...</h2>
    <p>Please wait while we complete the authentication process.</p>
  </div>
</template>

<script setup>
import { onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '../stores/auth'

const router = useRouter()
const authStore = useAuthStore()

onMounted(async () => {
  try {
    // The Supabase client will automatically handle the OAuth callback
    // We just need to wait for the session to be established
    setTimeout(() => {
      if (authStore.isAuthenticated) {
        router.push('/profile')
      } else {
        router.push('/login')
      }
    }, 1000)
  } catch (error) {
    console.error('Auth callback error:', error)
    router.push('/login')
  }
})
</script>

<style scoped>
.auth-callback {
  max-width: 400px;
  margin: 4rem auto;
  text-align: center;
  padding: 2rem;
  background-color: #f5f5f5;
  border-radius: 8px;
}
</style>
