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
            xs:flex gap-x-4
            hover:bg-gray-100 dark:hover:bg-gray-800
            `.replaceAll(/\s+/g, ' ')">
    <div class="flex-shrink-0">
      <NuxtLink :to="adUrl" :title="a.title" class="">
        <template v-if="hasImage">
          <img
            :class="`
                    rounded-md mx-auto
                    aspect-square object-cover
                    w-[10rem] xs:w-[7rem] md:w-[10rem] lg:w-[13rem]
                    bg-gray-200 dark:bg-gray-700
                    `.replaceAll(/\s+/g, ' ')"
            :src="imageUrl(adImage, 300)"
            :alt="a.title">
        </template>
        <template v-else>
          <span
            :class="`
                    rounded-md mx-auto
                    aspect-square object-cover
                    w-[10rem] xs:w-[7rem] md:w-[10rem] lg:w-[13rem]
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
        <h3 :title="a.title" class="text-lg">
          <NuxtLink :to="adUrl" class="link-clr0 one-line-only">
            {{ a.title }}
          </NuxtLink>
        </h3>
        <button
          class="cursor-pointer hover:text-rose-500"
          title="Add to favorites">
          <IconHeartIcon :size=20 />
        </button>
      </div>
      <NuxtLink :to="adUrl" class="block h-full">
        <p class="text-base md:text-xl font-semibold text-gray-900 dark:text-white">
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
