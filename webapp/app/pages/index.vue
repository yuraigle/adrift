<script setup lang="ts">
useHead({
  titleTemplate: (t: string | undefined) => 'Home - ' + t,
})
</script>

<template>
  <div>
    <h1 class="text-2xl font-bold py-2">Home</h1>

    <NuxtLink to="/a/1">Ad1</NuxtLink>
  </div>
</template>
