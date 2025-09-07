<script setup lang="ts">
import { callApi } from '~/utils/api'

definePageMeta({
  layout: 'empty'
})

useHead({
  titleTemplate: (t: string | undefined) => 'Login - ' + t,
})

const form = reactive({
  email: '',
  password: ''
})

const loading = ref(false)

const onSubmit = () => {
  loading.value = true

  callApi('/auth/login', 'POST', JSON.stringify(form))
    .then(() => {
      useToastsStore().showMessage('Welcome back!', 'success')
      useRouter().push({ path: '/' })
    })
    .catch((error) => useToastsStore().showError(error))
    .finally(() => loading.value = false)
}
</script>

<template>
  <div class="flex min-h-full flex-col justify-center px-6 py-12 lg:px-8">
    <div class="sm:mx-auto sm:w-full sm:max-w-sm">
      <NuxtLink to="/">
        <span class="text-accent-600 dark:text-accent-400">
          <IconLogoIcon class="mx-auto h-10 w-auto" />
        </span>
      </NuxtLink>
      <h2
        :class="`
                mt-10 text-center text-2xl/9 font-bold tracking-tight
                text-gray-900 dark:text-gray-100
                `.replaceAll(/\s+/g, ' ')">
        Sign in to your account
      </h2>
    </div>
    <div class="mt-10 sm:mx-auto sm:w-full sm:max-w-sm">
      <form class="space-y-6" @submit.prevent="onSubmit">
        <UiFormInput
          id="email" label="Email:" :val="form.email"
          type="email" autocomplete="email"
          @update:val="form.email = $event.target.value" />

        <div>
          <div class="flex items-center justify-between">
            <label
              for="password"
              :class="`
                      block text-sm/6 font-medium text-gray-700
                      dark:text-gray-300
                      `.replaceAll(/\s+/g, ' ')">Password:</label>
            <div class="text-sm">
              <NuxtLink to="/auth/reset" class="link-clr1 font-semibold">
                Forgot password?
              </NuxtLink>
            </div>
          </div>
          <UiFormInput
            id="password" :val="form.password"
            type="password" autocomplete="current-password"
            @update:val="form.password = $event.target.value" />
        </div>

        <UiButtonPrimary type="submit" :loading="loading">
          Sign in
        </UiButtonPrimary>
      </form>

      <p class="mt-10 text-center text-sm/6 text-gray-500">
        Don't have an account?
        <NuxtLink to="/auth/register" class="link-clr1 font-semibold ms-1">
          Register
        </NuxtLink>
      </p>
    </div>
  </div>
</template>
