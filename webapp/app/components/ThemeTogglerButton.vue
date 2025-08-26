<script setup lang="ts">

const theme = ref('light');

onMounted(() => {
  const docEl: HTMLElement = document.documentElement;
  if (localStorage.theme === 'dark' || (!('theme' in localStorage) && window.matchMedia('(prefers-color-scheme: dark)').matches)) {
    docEl.classList.add('dark');
    theme.value = 'dark';
  } else {
    docEl.classList.remove('dark');
    theme.value = 'light';
  }
})

const toggleTheme = () => {
  const docEl: HTMLElement = document.documentElement;
  if (docEl.classList.contains('dark')) {
    docEl.classList.remove('dark');
    localStorage.theme = 'light';
    theme.value = 'light';
  } else {
    docEl.classList.add('dark');
    localStorage.theme = 'dark';
    theme.value = 'dark';
  }
}
</script>

<template>
  <button
    type="button" title="Toggle dark mode"
    :class="`cursor-pointer text-gray-500 dark:text-gray-400
              hover:text-gray-600 dark:hover:text-gray-300`"
    @click="toggleTheme"
  >
    <IconSunIcon v-if="theme === 'light'" />
    <IconMoonIcon v-else />
  </button>
</template>
