<script setup lang="ts">
import { API_BASE } from '~/utils/api';

const route = useRoute()
const id = computed(() => route.params.id)
const { data: ad } = await useFetch<AdSummary>(`${API_BASE}/ads/${id.value}`)

</script>

<template>
  <div v-if="!ad">
    <p>No content</p>
  </div>
  <div v-else>
    <h1>{{ ad.title }}</h1>

    <ul class="pagination">
      <li v-if="ad.id > 1">
        <NuxtLink :to="`/a/${ad.id - 1}`">&laquo;</NuxtLink>
      </li>
      <li>{{ ad.id }}</li>
      <li>
        <NuxtLink :to="`/a/${ad.id + 1}`">&raquo;</NuxtLink>
      </li>
    </ul>
  </div>
</template>

<style scoped>
.pagination {
  list-style: none;
  display: flex;
  justify-content: center;
}

.pagination li {
  margin: 0 10px;
}
</style>
