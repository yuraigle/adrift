<script setup lang="ts">
import { callApi } from '~/utils/api'

definePageMeta({
  layout: 'empty'
})

useHead({
  titleTemplate: (t: string | undefined) => 'Register - ' + t,
})

const form = reactive({
  email: '',
  username: '',
  password: ''
})

const loading = ref(false)

const onSubmit = () => {
  loading.value = true

  callApi('/auth/register', 'POST', JSON.stringify(form))
    .then(() => {
      useToastsStore().showMessage('Registration successful', 'success')
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
        Register a new account
      </h2>
    </div>
    <div class="mt-10 sm:mx-auto sm:w-full sm:max-w-sm">
      <form class="space-y-6" @submit.prevent="onSubmit">
        <UiFormInput
          id="email" label="Email:" :val="form.email"
          type="email" autocomplete="email"
          @update:val="form.email = $event.target.value" />

        <UiFormInput
          id="username" label="Username:" :val="form.username"
          autocomplete="username"
          @update:val="form.username = $event.target.value" />

        <UiFormInput
          id="password" label="Password:" :val="form.password"
          type="password" autocomplete="new-password"
          @update:val="form.password = $event.target.value" />

        <UiButtonPrimary type="submit" :loading="loading">
          Register
        </UiButtonPrimary>
      </form>

      <p class="mt-10 text-center text-sm/6 text-gray-500">
        Already have an account?
        <NuxtLink to="/auth/login" class="link-clr1 font-semibold ms-1">Log in</NuxtLink>
      </p>
    </div>
  </div>
</template>
