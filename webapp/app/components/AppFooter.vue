<script setup lang="ts">
const version = useAppConfig().APP_VERSION || '';
</script>

<template>
  <footer
    :class="`
            bottom-0 left-0 z-20 w-full shadow-sm
            mt-10 py-6 px-4 md:px-6
            border-t border-gray-200 dark:border-gray-700
            `.replaceAll(/\s+/g, ' ')">
    <div class="max-w-[1200px] mx-auto md:flex md:items-center md:justify-between">
      <div class="text-sm text-gray-500">
        &copy; {{ new Date().getFullYear() }}
        <a href="/" class="font-semibold hover:text-black dark:hover:text-white">
          Adrift
        </a>
        version {{ version }}.
        All Rights Reserved.
      </div>
      <ul class="flex text-sm font-medium text-gray-500 mt-3">
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
    </div>
  </footer>
</template>

<style scoped>
@reference "~/assets/css/main.css";

.link {
  @apply hover:text-siberia-700 dark:hover:text-siberia-600
}
</style>
