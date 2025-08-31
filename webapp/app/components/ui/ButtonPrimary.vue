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
            text-white bg-accent-600 hover:bg-accent-500
            focus-visible:outline-accent-600
            ${loading ? 'animate-pulse opacity-50 pointer-events-none' : ''}
            ${cls}
            `.replaceAll(/\s+/g, ' ')">
    <slot />
  </button>
</template>
