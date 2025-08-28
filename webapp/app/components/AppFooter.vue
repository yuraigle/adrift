<script setup lang="ts">
const version = useAppConfig().APP_VERSION || '';
</script>

<template>
  <footer
    :class="`
            bottom-0 left-0 z-20 w-full p-4 border-t shadow-sm
            md:flex md:items-center md:justify-between md:p-6
            border-gray-200 dark:border-gray-700 mt-10
            `.replaceAll(/\s+/g, ' ')">
    <div class="text-sm text-gray-500 sm:text-center">
      &copy; {{ new Date().getFullYear() }}
      <a href="/" class="font-semibold hover:text-black dark:hover:text-white">
        Adrift
      </a>
      version {{ version }}.
      All Rights Reserved.
    </div>
    <ul class="flex flex-wrap items-center mt-3 text-sm font-medium text-gray-500 sm:mt-0">
      <li>
        <NuxtLink to="/about" class="link me-4 md:me-6">About</NuxtLink>
      </li>
      <li>
        <NuxtLink to="/terms" class="link me-4 md:me-6">Terms of use</NuxtLink>
      </li>
      <li>
        <a href="#" class="link">Contact</a>
      </li>
    </ul>
  </footer>
</template>

<style scoped>
@reference "~/assets/css/main.css";

.link {
  @apply hover:text-siberia-700 dark:hover:text-siberia-600
}
</style>
