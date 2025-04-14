import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { supabase } from '../supabase'

export const useAuthStore = defineStore('auth', () => {
  const user = ref(null)
  const session = ref(null)
  const loading = ref(false)
  const error = ref(null)

  const isAuthenticated = computed(() => !!user.value)

  // Initialize the store with the current session
  async function init() {
    loading.value = true
    
    try {
      // Get the current session
      const { data } = await supabase.auth.getSession()
      
      if (data?.session) {
        session.value = data.session
        user.value = data.session.user
      }
      
      // Listen for auth changes
      supabase.auth.onAuthStateChange((event, newSession) => {
        session.value = newSession
        user.value = newSession?.user || null
      })
    } catch (err) {
      error.value = err.message
      console.error('Auth init error:', err)
    } finally {
      loading.value = false
    }
  }

  // Sign in with Google
  async function signInWithGoogle() {
    loading.value = true
    error.value = null
    
    try {
      const { error: signInError } = await supabase.auth.signInWithOAuth({
        provider: 'google',
        options: {
          redirectTo: `${window.location.origin}/auth/callback`
        }
      })
      
      if (signInError) throw signInError
    } catch (err) {
      error.value = err.message
      console.error('Google sign in error:', err)
    } finally {
      loading.value = false
    }
  }

  // Sign in with email and password
  async function signInWithEmail(email, password) {
    loading.value = true
    error.value = null
    
    try {
      const { error: signInError } = await supabase.auth.signInWithPassword({
        email,
        password
      })
      
      if (signInError) throw signInError
    } catch (err) {
      error.value = err.message
      console.error('Email sign in error:', err)
    } finally {
      loading.value = false
    }
  }

  // Sign up with email and password
  async function signUpWithEmail(email, password) {
    loading.value = true
    error.value = null
    
    try {
      // This will trigger the backend registration endpoint
      const { error: signUpError } = await supabase.auth.signUp({
        email,
        password,
        options: {
          emailRedirectTo: `${window.location.origin}/auth/callback`
        }
      })
      
      if (signUpError) throw signUpError
    } catch (err) {
      error.value = err.message
      console.error('Email sign up error:', err)
    } finally {
      loading.value = false
    }
  }

  // Sign out
  async function signOut() {
    loading.value = true
    error.value = null
    
    try {
      const { error: signOutError } = await supabase.auth.signOut()
      if (signOutError) throw signOutError
      
      user.value = null
      session.value = null
    } catch (err) {
      error.value = err.message
      console.error('Sign out error:', err)
    } finally {
      loading.value = false
    }
  }

  // Get the current session token for API calls
  function getToken() {
    return session.value?.access_token
  }

  return {
    user,
    session,
    loading,
    error,
    isAuthenticated,
    init,
    signInWithGoogle,
    signInWithEmail,
    signUpWithEmail,
    signOut,
    getToken
  }
})
