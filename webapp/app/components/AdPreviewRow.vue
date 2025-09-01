<script setup lang="ts">
const { a } = defineProps<{a: AdSummary}>()

const adUrl = computed((): string => `/a/${a.id}`)

const hasImage = computed((): boolean => a.images && a.images.length > 0)

const adImage = computed((): string => a.images[0]?.filename || '')

const imageUrl = (filename: string, h: number) => {
  return useAppConfig().S3_BASE + filename + '_' + h + '.webp'
}

const formatPrice = (price: number) => {
  return new Intl.NumberFormat("en-US", {
    style: "currency", currency: "USD"
  }).format(price);
}
</script>

<template>
  <div
    :class="`
            mb-4 px-6 py-4 rounded-md
            flex gap-x-4
            hover:bg-gray-100 dark:hover:bg-gray-800
            `.replaceAll(/\s+/g, ' ')">
    <div class="flex-shrink-0">
      <NuxtLink :to="adUrl" :title="a.title">
        <template v-if="hasImage">
          <img
            :class="`
                    rounded-md
                    aspect-square object-cover w-[100px] lg:w-[200px]
                    bg-gray-200 dark:bg-gray-700
                    `.replaceAll(/\s+/g, ' ')"
            :src="imageUrl(adImage, 300)"
            :alt="a.title">
        </template>
        <template v-else>
          <span
            :class="`
                    rounded-md
                    aspect-square object-cover w-[100px] lg:w-[200px]
                    text-gray-400 bg-gray-200
                    dark:text-gray-600 dark:bg-gray-700
                    flex items-center justify-center
                    `.replaceAll(/\s+/g, ' ')">
            <IconPictureOff :size=72 />
          </span>
        </template>
      </NuxtLink>
    </div>
    <div class="flex-grow">
      <div class="flex justify-between items-center gap-x-4 pb-0 lg:pb-2">
        <NuxtLink :to="adUrl" class="w-full">
          <h3
            :title="a.title"
            :class="`
                    text-lg one-line-only
                    hover:text-accent-600 dark:hover:text-accent-400
                    `.replaceAll(/\s+/g, ' ')">
            {{ a.title }}
          </h3>
        </NuxtLink>
        <button
          class="cursor-pointer hover:text-rose-500"
          title="Add to favorites">
          <IconHeartIcon :size=20 />
        </button>
      </div>
      <NuxtLink :to="adUrl" class="block h-full">
        <p class="text-xl font-semibold text-gray-900 dark:text-white">
          {{ formatPrice(a.price) }}
        </p>
        <p
          :class="`
                  text-gray-600 dark:text-gray-300 three-lines-only
                  text-sm md:text-base
                  `.replaceAll(/\s+/g, ' ')">{{ a.description }}</p>
      </NuxtLink>
    </div>
  </div>
</template>
