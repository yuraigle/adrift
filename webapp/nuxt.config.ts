import tailwindcss from '@tailwindcss/vite'

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
  runtimeConfig: {
    APP_VERSION: JSON.stringify(process.env.npm_package_version)
  },
  vite: {
    plugins: [
      tailwindcss(),
    ],
  },
  imports: {
    dirs: [
      'types/*.ts', // Auto-import types from 'types/'
      'types/**/*.ts' // Auto-import types from nested 'types/' subdirectories
    ]
  },
  app: {
    head: {
      title: 'Adrift', // default fallback title
      htmlAttrs: {
        lang: 'en',
      },
      link: [
        { rel: 'icon', type: 'image/x-icon', href: '/favicon.ico' },
      ]
    }
  },
})
