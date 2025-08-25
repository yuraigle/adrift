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

const message = ref('')
const message_type = ref('')

const onSubmit = () => {
  callApi('/auth/login', 'POST', JSON.stringify(form))
    .then(() => {
      message.value = 'Login successful'
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
        Sign in to your account
      </h2>
    </div>
    <div class="mt-10 sm:mx-auto sm:w-full sm:max-w-sm">
      <form class="space-y-6" @submit.prevent="onSubmit">
        <FormInput
          id="email" label="Email" :val="form.email" type="email" autocomplete="email"
          @update:val="form.email = $event.target.value" />

        <div>
          <div class="flex items-center justify-between">
            <label for="password" class="block text-sm/6 font-medium text-gray-900">Password</label>
            <div class="text-sm">
              <NuxtLink to="/auth/reset" class="font-semibold text-indigo-600 hover:text-indigo-500">
                Forgot password?
              </NuxtLink>
            </div>
          </div>
          <FormInput
            id="password" :val="form.password" type="password" autocomplete="current-password"
            @update:val="form.password = $event.target.value" />
        </div>

        <div>
          <button
            type="submit" :class="`flex w-full justify-center rounded-md bg-indigo-600 px-3
              py-1.5 text-sm/6 font-semibold text-white shadow-xs hover:bg-indigo-500
              focus-visible:outline-2 focus-visible:outline-offset-2
              focus-visible:outline-indigo-600`">
            Sign in
          </button>
        </div>
      </form>

      <p class="mt-10 text-center text-sm/6 text-gray-500">
        Don't have an account?
        <NuxtLink to="/auth/register" class="font-semibold text-indigo-600 hover:text-indigo-500">
          Register
        </NuxtLink>
      </p>

      <p v-if="message" :class="message_type">{{ message }}</p>
    </div>
  </div>
</template>
