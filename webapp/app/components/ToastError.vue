<script setup lang="ts">
const { message } =defineProps<{ message: string }>();
defineEmits(['close'])
</script>

<template>
  <div
    :class="`
            flex items-center max-w-md p-4 rounded-lg shadow-sm
            text-gray-500 dark:text-gray-100
            bg-red-100 dark:bg-red-800
            `.replaceAll(/\s+/g, ' ')"
    role="alert">
    <div
      :class="`
              inline-flex items-center justify-center shrink-0 w-8 h-8
              rounded-lg text-red-500 bg-red-100
              dark:text-red-100 dark:bg-red-800
              `.replaceAll(/\s+/g, ' ')">
      <svg class="w-5 h-5" aria-hidden="true" xmlns="http://www.w3.org/2000/svg" fill="currentColor" viewBox="0 0 20 20">
          <path d="M10 .5a9.5 9.5 0 1 0 9.5 9.5A9.51 9.51 0 0 0 10 .5Zm3.707 11.793a1 1 0 1 1-1.414 1.414L10 11.414l-2.293 2.293a1 1 0 0 1-1.414-1.414L8.586 10 6.293 7.707a1 1 0 0 1 1.414-1.414L10 8.586l2.293-2.293a1 1 0 0 1 1.414 1.414L11.414 10l2.293 2.293Z"/>
      </svg>
      <span class="sr-only">Error icon</span>
    </div>
    <div class="ms-1 me-4 text-sm font-normal">{{ message }}</div>
    <button
      type="button"
      :class="`
              ms-auto -my-1.5 p-1.5 cursor-pointer
              inline-flex items-center justify-center h-8 w-8 rounded-lg
              text-gray-400 hover:text-gray-900
              dark:text-gray-300 dark:hover:text-white
              `.replaceAll(/\s+/g, ' ')"
      aria-label="Close"
      @click="$emit('close')">
      <span class="sr-only">Close</span>
      <IconCloseIcon />
    </button>
  </div>
</template>
