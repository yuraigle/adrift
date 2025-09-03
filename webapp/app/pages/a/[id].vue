<script setup lang="ts">
import { API_BASE } from '~/utils/api';

useHead({
  titleTemplate: (t: string | undefined) => (ad.value ? ad.value.title + ' - ' : '') + t,
})

const route = useRoute()
const id = computed(() => route.params.id)
const { data: ad } = await useFetch<AdSummary>(`${API_BASE}/ads/${id.value}`)

</script>

<template>
  <div v-if="!ad">
    <p>No content</p>
  </div>
  <div v-else>
    <p class="mt-1 mb-2">
      <NuxtLink
        :to="'/category/' + ad.category?.slug"
        class="text-lg hover:text-accent-600 dark:hover:text-accent-400">
        <span class="flex items-center gap-x-1">
          <IconChevronLeft :size=18 />
          {{ ad.category?.name }}
        </span>
      </NuxtLink>
    </p>
    <h1 class="text-2xl font-bold py-2">{{ ad.title }}</h1>
  </div>
</template>
