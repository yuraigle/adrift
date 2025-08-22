<script setup lang="ts">
defineProps({
  size: {
    type: Number,
    default: 24,
  }
})
</script>

<template>
  <svg xmlns="http://www.w3.org/2000/svg" :width="`${size}px`" :height="`${size}px`" viewBox="0 0 24 24" fill="none"
    stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"
    class="icon icon-tabler icons-tabler-outline icon-tabler-activity-heartbeat text-indigo-600">
    <path stroke="none" d="M0 0h24v24H0z" fill="none" />
    <path d="M3 12h4.5l1.5 -6l4 12l2 -9l1.5 3h4.5" />
  </svg>
</template>
