<script setup lang="ts">
defineProps<{ images: ImageDto[] }>()

const imageUrl = (filename: string, h: number) => {
  return useAppConfig().S3_BASE + filename + '_' + h + '.webp'
}

// todo: carousel
</script>

<template>
  <div v-if="images && images.length > 0 && images[0]">
    <div class="rounded bg-gray-200 dark:bg-gray-800 my-4">
      <img
        :src="imageUrl(images[0].filename, 600)"
        class="object-fit mx-auto h-[400px]"
        alt="">
    </div>
  </div>
</template>
