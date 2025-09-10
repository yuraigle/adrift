export const useAuthStore = defineStore('auth', () => {
  const guest: AuthDetails = { id: 0, username: '', token: '' }
  const details = ref<AuthDetails>(guest)

  const saveDetails = (data: AuthDetails) => {
    details.value = data
    localStorage.setItem('user', JSON.stringify(data))
  }

  const saveDetailsJson = (json: string | null) => {
    if (json) {
      details.value = JSON.parse(json)
    }
  }

  return {
    details,
    saveDetails,
    saveDetailsJson
  }
})
