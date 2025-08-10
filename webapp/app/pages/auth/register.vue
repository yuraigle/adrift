<script setup lang="ts">
import { reactive, ref } from 'vue'
import { callApi } from '~/utils/api'

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
  <div>
    <h1>Register</h1>

    <form @submit.prevent="onSubmit">
      <div>
        <label for="login-username">Username:</label>
        <input id="login-username" v-model="form.username" type="text">
      </div>

      <div>
        <label for="login-email">Email:</label>
        <input id="login-email" v-model="form.email" type="email">
      </div>

      <div>
        <label for="login-password">Password:</label>
        <input id="login-password" v-model="form.password" type="password" autocomplete="new-password">
      </div>

      <div>
        <button type="submit">Create Account</button>
      </div>

      <p>
        Already have an account?
        <NuxtLink to="/auth/login">Sign in</NuxtLink>
      </p>
    </form>

    <p v-if="message" :class="message_type">{{ message }}</p>
  </div>
</template>

<style scoped>
form {
  width: 400px;
}

form > div {
  display: flex;
  margin-bottom: 8px;
}

form > div label {
  width: 100px;
  padding: 4px 0;
}

form > div input {
  flex-grow: 1;
  padding: 4px 8px;
}

button {
  width: 100%;
  padding: 4px 8px;
}
</style>