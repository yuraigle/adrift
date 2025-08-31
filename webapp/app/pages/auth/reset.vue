<script setup lang="ts">
import { callApi } from '~/utils/api'

definePageMeta({
  layout: 'empty'
})

useHead({
  titleTemplate: (t: string | undefined) => 'Reset your password - ' + t,
})

const form = reactive({
  email: ''
})

const loading = ref(false)

const onSubmit = () => {
  loading.value = true

  callApi('/auth/reset', 'POST', JSON.stringify(form))
    .then(() => {
      useToastsStore().showMessage('Instructions have been sent to your email', 'info')
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
        Reset your password
      </h2>
      <p class="text-center text-gray-900 dark:text-gray-100">
        Enter your user account's verified email address and we will send you
        a password reset link.
      </p>
    </div>
    <div class="mt-10 sm:mx-auto sm:w-full sm:max-w-sm">
      <form class="space-y-6" @submit.prevent="onSubmit">
        <FormInput
          id="email" label="Email:" :val="form.email"
          type="email" autocomplete="email"
          @update:val="form.email = $event.target.value" />

        <UiButtonPrimary type="submit">
          Send a password reset instructions
        </UiButtonPrimary>
      </form>
    </div>
  </div>
</template>
