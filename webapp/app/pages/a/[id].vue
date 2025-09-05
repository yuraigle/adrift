<script setup lang="ts">
import type { AdDetailsDto } from '~/types/AdDetailsDto';
import type { TemplateDto } from '~/types/TemplateDto';
import { API_BASE } from '~/utils/api';

useHead({
  titleTemplate: (t: string | undefined) => (ad.value ? ad.value.title + ' - ' : '') + t,
})

const route = useRoute()
const id = computed(() => route.params.id)

const { data: ad } = await useAsyncData<AdDetailsDto>(
  'ad-details-' + id.value,
  () => {
    return $fetch(`${API_BASE}/ads/${id.value}`)
  },
  {
    watch: [id],
    server: true,
  }
)

const { data: template } = await useAsyncData<TemplateDto>(
  'category-template-' + ad.value?.category.id,
  () => {
    if (!ad.value) {
      return Promise.resolve({} as TemplateDto);
    }

    return $fetch(`${API_BASE}/categories/${ad.value.category.id}/template`)
  },
  {
    watch: [ad],
    server: true,
  }
)

</script>

<template>
  <div v-if="!ad">
    <p>No content</p>
  </div>
  <div v-else>
    <div class="flex justify-between items-center pb-2">
      <NuxtLink :to="`/category/${ad.category?.slug}`" class="text-lg link-clr0">
        <span class="flex items-center gap-x-1">
          <IconChevronLeft :size=18 />
          {{ ad.category?.name }}
        </span>
      </NuxtLink>
    </div>
    <h1 class="text-2xl font-bold py-2">{{ ad.title }}</h1>
    <p>{{ ad.price }}</p>
    <p>{{ ad.description }}</p>
    <p>{{ new Date(ad.created).getFullYear() }}</p>

    <p>{{ template?.id }}</p>
  </div>
</template>
