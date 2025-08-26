<script setup lang="ts">
const version = useAppConfig().APP_VERSION || '';
</script>

<template>
  <footer
    :class="`fixed bottom-0 left-0 z-20 w-full p-4 border-t border-gray-200
              shadow-sm md:flex md:items-center md:justify-between md:p-6
              dark:border-gray-600`">
    <div class="text-sm text-gray-500 sm:text-center">
      &copy; 2025
      <a href="/" class="font-semibold hover:text-black dark:hover:text-white">Adrift.</a>
      All Rights Reserved.
      v{{ version }}
    </div>
    <ul class="flex flex-wrap items-center mt-3 text-sm font-medium text-gray-500 sm:mt-0">
      <li>
        <NuxtLink to="/about" class="hover:underline me-4 md:me-6">About</NuxtLink>
      </li>
      <li>
        <NuxtLink to="/terms" class="hover:underline me-4 md:me-6">Terms of use</NuxtLink>
      </li>
      <li>
        <a href="#" class="hover:underline">Contact</a>
      </li>
    </ul>
  </footer>
</template>
