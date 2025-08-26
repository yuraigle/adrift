<script setup lang="ts">
const route = useRoute()
const slug = computed(() => route.params.slug)
</script>

<template>
  <div>
    <h1 class="text-2xl font-bold py-2">Category {{ slug }}</h1>
  </div>
</template>
