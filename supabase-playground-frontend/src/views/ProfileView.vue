<template>
  <div class="profile-container">
    <h1>Profile</h1>
    
    <div v-if="loading" class="loading">
      Loading profile...
    </div>
    
    <div v-else-if="error" class="error-message">
      {{ error }}
    </div>
    
    <div v-else class="profile-info">
      <div class="user-info">
        <h2>User Information</h2>
        <p><strong>Email:</strong> {{ authStore.user?.email }}</p>
        <p><strong>ID:</strong> {{ authStore.user?.id }}</p>
        <p><strong>Last Sign In:</strong> {{ formatDate(authStore.user?.last_sign_in_at) }}</p>
      </div>
      
      <div class="secret-data" v-if="secretData">
        <h2>Secret Data from Backend</h2>
        <pre>{{ secretData }}</pre>
      </div>
      
      <button @click="fetchSecretData" class="btn-primary" :disabled="loadingSecret">
        {{ loadingSecret ? 'Loading...' : 'Fetch Secret Data' }}
      </button>
      
      <button @click="handleSignOut" class="btn-danger">
        Sign Out
      </button>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '../stores/auth'
import { apiService } from '../services/api'

const router = useRouter()
const authStore = useAuthStore()

const loading = ref(false)
const error = ref(null)
const secretData = ref(null)
const loadingSecret = ref(false)

onMounted(async () => {
  loading.value = true
  
  try {
    if (!authStore.isAuthenticated) {
      router.push('/login')
      return
    }
  } catch (err) {
    error.value = err.message
  } finally {
    loading.value = false
  }
})

async function fetchSecretData() {
  loadingSecret.value = true
  
  try {
    const data = await apiService.get('/secret')
    secretData.value = data
  } catch (err) {
    error.value = err.message
  } finally {
    loadingSecret.value = false
  }
}

async function handleSignOut() {
  await authStore.signOut()
  router.push('/login')
}

function formatDate(dateString) {
  if (!dateString) return 'Never'
  
  const date = new Date(dateString)
  return date.toLocaleString()
}
</script>

<style scoped>
.profile-container {
  max-width: 600px;
  margin: 0 auto;
  padding: 2rem;
}

.loading {
  text-align: center;
  margin: 2rem 0;
}

.profile-info {
  display: flex;
  flex-direction: column;
  gap: 2rem;
}

.user-info, .secret-data {
  background-color: #f5f5f5;
  padding: 1rem;
  border-radius: 4px;
}

.secret-data pre {
  background-color: #e0e0e0;
  padding: 1rem;
  border-radius: 4px;
  overflow-x: auto;
}

.btn-primary {
  background-color: #2196F3;
  color: white;
  border: none;
  padding: 0.5rem 1rem;
  border-radius: 4px;
  cursor: pointer;
  margin-bottom: 1rem;
}

.btn-primary:disabled {
  background-color: #cccccc;
  cursor: not-allowed;
}

.btn-danger {
  background-color: #f44336;
  color: white;
  border: none;
  padding: 0.5rem 1rem;
  border-radius: 4px;
  cursor: pointer;
}

.error-message {
  background-color: #ffebee;
  color: #c62828;
  padding: 0.5rem;
  border-radius: 4px;
  margin-bottom: 1rem;
}
</style>
