<script setup lang="ts">
const { a } = defineProps<{a: AdSummary}>()

const imageUrl = (filename: string, h: number) => {
  return useAppConfig().S3_BASE + filename + '_' + h + '.webp'
}

const formatPrice = (price: number) => {
  return new Intl.NumberFormat("en-US", {
    style: "currency", currency: "USD"
  }).format(price);
}

const adUrl = computed((): string => `/a/${a.id}`)

const hasImage = computed((): boolean => a.images && a.images.length > 0)

const adImage = computed((): string => a.images[0]?.filename || '')

</script>

<template>
  <div class="relative">
    <NuxtLink :to="adUrl" :title="a.title">
      <template v-if="hasImage">
        <img
          :class="`
                  rounded-md bg-gray-200 dark:bg-gray-700
                  aspect-square object-cover w-full
                  `.replaceAll(/\s+/g, ' ')"
          :src="imageUrl(adImage, 300)"
          :alt="a.title">
      </template>
      <template v-else>
        <span
          :class="`
                  aspect-square w-full rounded-md
                  text-gray-400 bg-gray-200
                  dark:text-gray-600 dark:bg-gray-700
                  flex items-center justify-center
                  `.replaceAll(/\s+/g, ' ')">
          <IconPictureOff :size=72 />
        </span>
      </template>
    </NuxtLink>
    <div class="mt-4 flex">
      <div class="flex-grow">
        <h3 class="text-sm text-gray-800 dark:text-gray-200">
          <NuxtLink :to="adUrl" :title="a.title">
            <span
              :class="`
                      two-lines-only
                      hover:text-accent-700 dark:hover:text-accent-200
                      `.replaceAll(/\s+/g, ' ')">
              {{ a.title }}
            </span>
          </NuxtLink>
        </h3>
        <p class="font-semibold text-gray-900 dark:text-white">
          {{ formatPrice(a.price) }}
        </p>
      </div>
      <div class="w-6 gap-y-1 flex flex-col">
        <button
          class="cursor-pointer hover:text-rose-500"
          title="Add to favorites">
          <IconHeartIcon :size=20 />
        </button>
      </div>
    </div>
  </div>
</template>

<style lang="css" scoped>
.two-lines-only {
  display: -webkit-box;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 2;
  overflow: hidden;
  text-overflow: ellipsis;
}
</style>
