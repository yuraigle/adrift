<script setup lang="ts">
import { callApi } from '~/utils/api'

const form = reactive({
  username: '',
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
  <div>
    <h1>Login</h1>

    <form @submit.prevent="onSubmit">
      <div>
        <label for="login-username">Username:</label>
        <input id="login-username" v-model="form.username" type="text">
      </div>

      <div>
        <label for="login-password">Password:</label>
        <input id="login-password" v-model="form.password" type="password">
      </div>

      <button type="submit">Login</button>

      <p>
        Don't have an account?
        <NuxtLink to="/auth/register">Register</NuxtLink>
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