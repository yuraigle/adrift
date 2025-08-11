// https://nuxt.com/docs/api/configuration/nuxt-config
export default defineNuxtConfig({
  compatibilityDate: '2025-07-15',
  css: ['~/assets/css/main.css'],
  devtools: { enabled: true },
  modules: ['@nuxt/eslint'],
  typescript: {
    typeCheck: true,
    strict: true
  },
  imports: {
    dirs: [
      'types/*.ts', // Auto-import types from 'types/'
      'types/**/*.ts' // Auto-import types from nested 'types/' subdirectories
    ]
  }
})