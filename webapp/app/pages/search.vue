<script setup lang="ts">
const router = useRouter()
const q = computed(() => router.currentRoute.value.query.q)
</script>

<template>
  <div>
    <h1 class="text-2xl font-bold py-2">Search Results</h1>
    <p>{{ q }}</p>
  </div>
</template>
