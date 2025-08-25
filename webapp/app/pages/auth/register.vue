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

const message = ref('')
const message_type = ref('')

const onSubmit = () => {
  callApi('/auth/register', 'POST', JSON.stringify(form))
    .then(() => {
      message.value = 'Registration successful'
      message_type.value = 'success'
    })
    .catch((error) => {
      message.value = 'Registration failed: ' + error
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
        Register a new account
      </h2>
    </div>
    <div class="mt-10 sm:mx-auto sm:w-full sm:max-w-sm">
      <form class="space-y-6" @submit.prevent="onSubmit">
        <FormInput id="email" label="Email" :val="form.email" type="email" autocomplete="email"
          @update:val="form.email = $event.target.value" />

        <FormInput id="username" label="Username" :val="form.username" autocomplete="username"
          @update:val="form.username = $event.target.value" />

        <FormInput id="password" label="Password" :val="form.password" type="password" autocomplete="new-password"
          @update:val="form.password = $event.target.value" />

        <div>
          <UiButtonPrimary type="submit">Register</UiButtonPrimary>
        </div>
      </form>

      <p class="mt-10 text-center text-sm/6 text-gray-500">
        Already have an account?
        <NuxtLink to="/auth/login" class="font-semibold text-indigo-600 hover:text-indigo-500">Log in</NuxtLink>
      </p>

      <p v-if="message" :class="message_type">{{ message }}</p>
    </div>
  </div>
</template>
