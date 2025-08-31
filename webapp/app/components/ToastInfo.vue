<script setup lang="ts">
const { message } =defineProps<{ message: string }>();
defineEmits(['close'])
</script>

<template>
  <div
    :class="`
            flex items-center max-w-md p-4 text-gray-500
            rounded-lg shadow-sm dark:text-gray-100
            bg-sky-100 dark:bg-sky-800
            `.replaceAll(/\s+/g, ' ')"
    role="alert">
    <div
      :class="`
              inline-flex items-center justify-center shrink-0 w-8 h-8
              rounded-lg text-sky-500 bg-sky-100
              dark:text-sky-100 dark:bg-sky-800
              `.replaceAll(/\s+/g, ' ')">
        <svg class="w-5 h-5" aria-hidden="true" xmlns="http://www.w3.org/2000/svg" fill="currentColor" viewBox="0 0 20 20">
            <path d="M10 .5a9.5 9.5 0 1 0 9.5 9.5A9.51 9.51 0 0 0 10 .5ZM10 15a1 1 0 1 1 0-2 1 1 0 0 1 0 2Zm1-4a1 1 0 0 1-2 0V6a1 1 0 0 1 2 0v5Z"/>
        </svg>
      <span class="sr-only">Info icon</span>
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
