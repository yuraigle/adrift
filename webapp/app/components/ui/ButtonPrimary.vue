<script setup lang="ts">
defineProps({
  type: {
    type: String as () => 'button' | 'submit' | 'reset' | undefined,
    default: 'button'
  },
  cls: {
    type: String,
    default: ''
  }
})
</script>

<template>
  <button
    :type="type"
    :class="`w-full justify-center rounded-md bg-indigo-600 px-3 py-1.5
              text-sm/6 font-semibold text-white shadow-xs hover:bg-indigo-500
              focus-visible:outline-2 focus-visible:outline-offset-2
              focus-visible:outline-indigo-600 cursor-pointer ${cls}`">
    <slot />
  </button>
</template>
