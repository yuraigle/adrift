<script setup lang="ts">
defineProps({
  prev: {
    type: String,
    default: ''
  },
  next: {
    type: String,
    default: ''
  },
  page: {
    type: Number,
    default: 0
  },
  total: {
    type: Number,
    default: 0
  }
})
</script>

<template>
    <nav
      class="isolate inline-flex -space-x-px rounded-md shadow-xs"
      aria-label="Pagination">
      <NuxtLink
        :to="`${page === 0 ? null : prev}`"
        :class="`
                relative inline-flex items-center rounded-l-md px-2 py-2
                focus:z-20 focus:outline-offset-0
                text-gray-400 inset-ring
                inset-ring-gray-300 hover:bg-gray-100
                dark:inset-ring-gray-600 dark:hover:bg-gray-800
                ${page === 0 ? 'pointer-events-none opacity-50' : ''}
                `.replaceAll(/\s+/g, ' ')">
        <span class="sr-only">Previous</span>
        <IconChevronLeft :size=18 />
      </NuxtLink>
      <span
        :class="`
                relative inline-flex items-center px-4 py-2 text-sm
                font-semibold focus:outline-offset-0
                text-gray-700 dark:text-gray-300
                inset-ring inset-ring-gray-300 dark:inset-ring-gray-600
                `.replaceAll(/\s+/g, ' ')">
        {{ page + 1 }}
      </span>
      <NuxtLink
        :to="`${page >= total - 1 ? null : next}`"
        :class="`
                relative inline-flex items-center rounded-r-md px-2 py-2
                text-gray-400 inset-ring
                focus:z-20 focus:outline-offset-0
                inset-ring-gray-300 hover:bg-gray-100
                dark:inset-ring-gray-600 dark:hover:bg-gray-800
                ${page >= total - 1 ? 'pointer-events-none opacity-50' : ''}
                `.replaceAll(/\s+/g, ' ')">
        <span class="sr-only">Next</span>
        <IconChevronRight :size=18 />
      </NuxtLink>
    </nav>
</template>
