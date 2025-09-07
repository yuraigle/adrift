<script setup lang="ts">
import { API_BASE } from '~/utils/api';

useHead({
  titleTemplate: (t: string | undefined) =>
    (ad && ad.value ? ad.value.title + ' - ' : '') + (t || ''),
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

    const cid = ad.value.category.id;
    return $fetch(`${API_BASE}/categories/${cid}/template`)
  },
  {
    watch: [id],
    server: true,
  }
)

const formatPrice = (price: number) => {
  return new Intl.NumberFormat("en-US", {
    style: "currency", currency: "USD"
  }).format(price);
}

</script>

<template>
  <div v-if="ad && template">
    <div class="flex justify-between items-center pb-2">
      <NuxtLink :to="`/category/${ad.category?.slug}`" class="text-lg link-clr0">
        <span class="flex items-center gap-x-1">
          <IconChevronLeft :size=18 />
          {{ ad.category?.name }}
        </span>
      </NuxtLink>
    </div>

    <div class="grid grid-cols-12 gap-x-6 py-4">
      <div class="col-span-12 lg:col-span-8">
        <h1 class="text-3xl font-bold pb-4">
          {{ ad.title }}
        </h1>
      </div>
      <div class="col-span-12 lg:col-span-4 pb-4">
        <span class="text-3xl font-bold">
          {{ formatPrice(ad.price) }}
        </span>
      </div>
    </div>

    <div class="grid grid-cols-12 gap-x-6">
      <div class="col-span-12 lg:col-span-8">
        <div v-if="ad && ad.images && ad.images.length > 0">
          <AdDetailsImages :images="ad.images" />
        </div>

        <div class="my-4">
          <h3 class="text-xl font-semibold mb-2">Description</h3>
          <div class="text-justify">{{ ad.description }}</div>
        </div>

        <div v-if="template && ad && ad.fields && ad.fields.length > 0" class="my-4">
          <AdDetailsFields :fields="ad.fields" :template="template" />
        </div>
      </div>
      <div class="col-span-12 lg:col-span-4">
        <AdDetailsOwner v-if="ad.user" :user="ad.user" />
      </div>
    </div>

    <div class="grid grid-cols-12 gap-x-6">
      <div class="col-span-12 lg:col-span-8">
        <div class="mt-12 pt-2 border-t border-gray-200 dark:border-gray-700">
          Ad #{{ ad.id }} -
          {{ new Date(ad.created).toLocaleDateString("en-US", {}) }} -
          15 views
        </div>
      </div>
    </div>
  </div>
  <div v-else>
    <p>Loading...</p>
  </div>
</template>
