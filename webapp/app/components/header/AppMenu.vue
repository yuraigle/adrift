<script setup lang="ts">
const auth = useAuthStore()
const username = computed(() => auth.details.username)
</script>

<template>
  <ThemeTogglerButton />
  <div class="w-18 ms-4 hover:text-accent-600 dark:hover:text-accent-400 text-sm font-semibold">
    <ClientOnly>
      <NuxtLink v-if="username" to="/profile">
        <span class="block overflow-hidden">{{ username }}</span>
      </NuxtLink>
      <NuxtLink v-else to="/auth/login" class="flex gap-x-1 hover:gap-x-2">
        Log in
        <span>&rarr;</span>
      </NuxtLink>
    </ClientOnly>
  </div>
</template>
