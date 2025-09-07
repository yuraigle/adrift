<template>
  <h1 class="text-2xl font-bold py-1">Browse popular categories</h1>
  <div class="grid grid-cols-2 md:grid-cols-3 lg:grid-cols-6 gap-4 my-4">
    <div class="col-span-2 cat-icon">
      <NuxtLink to="/category/houses-for-sale" class="cat-link">
        <IconChevronRight :size=18 />
        Property
      </NuxtLink>
    </div>
    <div class="col-span-1 lg:col-span-2 cat-icon">
      <NuxtLink to="/category/houses-for-rent" class="cat-link">
        <IconChevronRight :size=18 />
        Cars & Vehicles
      </NuxtLink>
    </div>
    <div class="col-span-1 lg:col-span-2 cat-icon">
      <NuxtLink to="/category/apartments-for-sale" class="cat-link">
        <IconChevronRight :size=18 />
        For Sale
      </NuxtLink>
    </div>
    <div class="col-span-2 hidden md:block cat-icon">
      <NuxtLink to="/category/apartments-for-rent" class="cat-link">
        <IconChevronRight :size=18 />
        Services
      </NuxtLink>
    </div>
    <div class="col-span-2 hidden lg:block cat-icon">
      <NuxtLink to="/category/villas-for-sale" class="cat-link">
        <IconChevronRight :size=18 />
        Jobs
      </NuxtLink>
    </div>
    <div class="col-span-2 hidden lg:block cat-icon">
      <NuxtLink to="/category/land-for-sale" class="cat-link">
        <IconChevronRight :size=18 />
        Pets
      </NuxtLink>
    </div>
  </div>
</template>

<style lang="css" scoped>
@reference "~/assets/css/main.css";

.cat-icon {
  @apply border px-6 py-4 border-gray-200 dark:border-gray-700;
}

.cat-link {
  @apply flex items-center gap-x-2 text-lg
         text-gray-900 dark:text-gray-100
         hover:text-accent-600 dark:hover:text-accent-400;
}
</style>
