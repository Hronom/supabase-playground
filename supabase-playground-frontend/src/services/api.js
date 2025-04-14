import { useAuthStore } from '../stores/auth'

const API_URL = import.meta.env.VITE_API_URL

/**
 * Base API service for making authenticated requests to the backend
 */
export const apiService = {
  /**
   * Make an authenticated GET request
   * @param {string} endpoint - API endpoint
   * @returns {Promise<any>} - Response data
   */
  async get(endpoint) {
    const authStore = useAuthStore()
    const token = authStore.getToken()
    
    if (!token) {
      throw new Error('No authentication token available')
    }
    
    const response = await fetch(`${API_URL}${endpoint}`, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`
      }
    })
    
    if (!response.ok) {
      const error = await response.json().catch(() => ({}))
      throw new Error(error.message || 'API request failed')
    }
    
    return response.json()
  },
  
  /**
   * Make an authenticated POST request
   * @param {string} endpoint - API endpoint
   * @param {object} data - Request payload
   * @returns {Promise<any>} - Response data
   */
  async post(endpoint, data) {
    const authStore = useAuthStore()
    const token = authStore.getToken()
    
    if (!token) {
      throw new Error('No authentication token available')
    }
    
    const response = await fetch(`${API_URL}${endpoint}`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`
      },
      body: JSON.stringify(data)
    })
    
    if (!response.ok) {
      const error = await response.json().catch(() => ({}))
      throw new Error(error.message || 'API request failed')
    }
    
    return response.json()
  }
}
