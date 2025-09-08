<script setup lang="ts">
defineProps<{ images: ImageDto[] }>()

const imageUrl = (filename: string, h: number) => {
  return useAppConfig().S3_BASE + filename + '_' + h + '.webp'
}

// todo: carousel
</script>

<template>
  <div v-if="images && images.length > 0 && images[0]">
    <div class="rounded-md bg-gray-100 dark:bg-gray-800">
      <img
        :src="imageUrl(images[0].filename, 600)"
        class="h-[400px] object-scale-down mx-auto"
        alt="">
    </div>
  </div>
</template>
