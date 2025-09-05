<script setup lang="ts">
import type { AdDetailsDto } from '~/types/AdDetailsDto';
import type { TemplateDto } from '~/types/TemplateDto';
import { API_BASE } from '~/utils/api';

useHead({
  titleTemplate: (t: string | undefined) =>
    (ad && ad.value ? ad.value.title + ' - ' : '') + t,
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

    <div class="grid grid-cols-12 gap-x-4">
      <div class="col-span-12 lg:col-span-8">
        <h1 class="text-2xl font-bold py-4">{{ ad.title }}</h1>

        <div v-if="ad && ad.images && ad.images.length > 0">
          <AdDetailsImages :images="ad.images" />
        </div>

        <p>Price: {{ formatPrice(ad.price) }}</p>
        <p>Created: {{ new Date(ad.created).toLocaleString('en-US') }}</p>

        <div class="my-4">
          <h3 class="text-xl font-semibold mb-2">Description</h3>
          <div class="text-justify">{{ ad.description }}</div>
        </div>

        <div v-if="template && ad && ad.fields.length > 0" class="my-4">
          <AdDetailsFields :fields="ad.fields" :template="template" />
        </div>
      </div>
      <div class="col-span-12 lg:col-span-4">
        User
      </div>
    </div>

  </div>
</template>
