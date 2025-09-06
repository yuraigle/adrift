<script setup lang="ts">
defineProps({
  type: {
    type: String as () => 'button' | 'submit' | 'reset' | undefined,
    default: 'button'
  },
  cls: {
    type: String,
    default: ''
  },
  loading: {
    type: Boolean,
    default: false
  }
})
</script>

<template>
  <button
    :type="type"
    :class="`
            w-full justify-center rounded-md px-3 py-1.5
            text-sm/6 font-semibold shadow-xs cursor-pointer
            focus-visible:outline-2 focus-visible:outline-offset-2
            text-white
            bg-emerald-600 hover:bg-emerald-700
            dark:bg-emerald-700 dark:hover:bg-emerald-800
            focus-visible:outline-emerald-600
            ${loading ? 'animate-pulse opacity-50 pointer-events-none' : ''}
            ${cls}
            `.replaceAll(/\s+/g, ' ')">
    <slot />
  </button>
</template>
