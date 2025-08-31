<script setup lang="ts">
onMounted(() => {
  const docEl: HTMLElement = document.documentElement;
  const browserPrefersDark = window.matchMedia('(prefers-color-scheme: dark)').matches;
  if (localStorage.theme === 'dark' || (!('theme' in localStorage) && browserPrefersDark)) {
    docEl.classList.add('dark');
  } else {
    docEl.classList.remove('dark');
  }
})
</script>

<template>
  <div class="h-screen">
    <slot />
    <ToastsContainer />
  </div>
</template>
