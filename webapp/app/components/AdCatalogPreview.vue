<script setup lang="ts">
defineProps<{a: AdSummary}>()

const imageUrl = (filename: string, h: number) => {
  return useAppConfig().S3_BASE + filename + '_' + h + '.webp'
}

const formatPrice = (price: number) => {
  return new Intl.NumberFormat("en-US", { style: "currency", currency: "USD" }).format(price);
}

</script>

<template>
  <div class="group relative">
    <img
      :src="imageUrl('kysv0ztrvskldu8y', 300)"
      :class="`
              rounded-md bg-gray-200
              group-hover:opacity-75 aspect-square
              `.replaceAll(/\s+/g, ' ')"
      :alt="a.title">
    <div class="mt-4">
      <div>
        <h3 class="text-sm text-gray-800 dark:text-gray-200">
          <NuxtLink :to="`/a/${a.id}`" class="two-lines-only">
            <span aria-hidden="true" class="absolute inset-0" />
            {{ a.title }}
          </NuxtLink>
        </h3>
        <p class="font-semibold text-gray-900 dark:text-white">{{ formatPrice(a.price) }}</p>
      </div>
    </div>
  </div>
</template>
