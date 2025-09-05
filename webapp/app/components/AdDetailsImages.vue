<script setup lang="ts">
import type { ImageDto } from '~/types/AdDetailsDto';

defineProps<{ images: ImageDto[] }>()
</script>

<template>
  <div>
    Images
  </div>
</template>
