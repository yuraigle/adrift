<script setup lang="ts">
import { callApi } from '~/utils/api'

definePageMeta({
  layout: 'empty'
})

useHead({
  titleTemplate: (t) => 'Reset your password - ' + t,
})

const form = reactive({
  email: ''
})

const message = ref('')
const message_type = ref('')

const onSubmit = () => {
  callApi('/auth/reset', 'POST', JSON.stringify(form))
    .then(() => {
      message.value = 'Instructions have been sent to your email'
      message_type.value = 'success'
    })
    .catch((error) => {
      message.value = error
      message_type.value = 'error'
    })
}
</script>

<template>
  <div class="flex min-h-full flex-col justify-center px-6 py-12 lg:px-8">
    <div class="sm:mx-auto sm:w-full sm:max-w-sm">
      <NuxtLink to="/">
        <IconLogoIcon class="mx-auto h-10 w-auto" />
      </NuxtLink>
      <h2 class="mt-10 text-center text-2xl/9 font-bold tracking-tight text-gray-900">
        Reset your password
      </h2>
      <p class="text-center">
        Enter your user account's verified email address and we will send you a password reset link.
      </p>
    </div>
    <div class="mt-10 sm:mx-auto sm:w-full sm:max-w-sm">
      <form class="space-y-6" @submit.prevent="onSubmit">
        <FormInput
          id="email" label="Email" :val="form.email" type="email" autocomplete="email"
          @update:val="form.email = $event.target.value" />

        <div>
          <button
            type="submit" :class="`flex w-full justify-center rounded-md bg-indigo-600 px-3
              py-1.5 text-sm/6 font-semibold text-white shadow-xs hover:bg-indigo-500
              focus-visible:outline-2 focus-visible:outline-offset-2
              focus-visible:outline-indigo-600`">
            Send a password reset instructions
          </button>
        </div>
      </form>

      <p v-if="message" :class="message_type">{{ message }}</p>
    </div>
  </div>
</template>
