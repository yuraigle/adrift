<script setup lang="ts">
</script>

<template>
  <div class="relative">
    <button
      type="button"
      :class="`w-full justify-center rounded-md bg-indigo-700 px-4 py-1.5
                text-sm/6 font-semibold text-white shadow-xs hover:bg-indigo-600
                focus-visible:outline-2 focus-visible:outline-offset-2 rounded-xl
                focus-visible:outline-indigo-600 cursor-pointer flex gap-x-2`">
        <IconListSearchIcon :size=28 />
        <span class="mt-0.5">All Categories</span>
    </button>
  </div>
</template>
