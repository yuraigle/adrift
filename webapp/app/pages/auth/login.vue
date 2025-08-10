<script setup lang="ts">
import { reactive, ref } from 'vue'
import { callApi } from '~/utils/api'

const form = reactive({
  username: '',
  password: ''
})

const message = ref('')

const onSubmit = () => {
  callApi('/auth/login', 'POST', JSON.stringify(form))
    .then(() => {
      message.value = 'Login successful'
    })
    .catch((error) => {
      message.value = 'Login failed: ' + error
    })
}
</script>

<template>
  <div>
    <h1>Login</h1>

    <form @submit.prevent="onSubmit">
      <div>
        <label for="login-username">Email:</label>
        <input id="login-username" v-model="form.username" type="text" placeholder="Email">
      </div>

      <div>
        <label for="login-password">Password:</label>
        <input id="login-password" v-model="form.password" type="password" placeholder="Password">
      </div>

      <button type="submit">Login</button>
    </form>

    <div v-if="message">{{ message }}</div>
  </div>
</template>
