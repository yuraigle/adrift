<template>
  <header class="absolute inset-x-0 top-0 z-50">
    <nav class="flex items-center justify-between p-6 lg:px-8">
      <NuxtLink to="/" class="flex pt-1 gap-x-2">
        <span class="text-accent-600 dark:text-accent-400">
          <IconLogoIcon />
        </span>
        <span class="font-bold">Adrift</span>
      </NuxtLink>

      <div class="px-4 gap-x-2 flex hidden md:flex w-xl md:w-2xl">
        <AppHeaderCategories />
        <AppHeaderSearch />
      </div>

      <div class="flex justify-end">
        <div class="pt-1 me-4">
          <ThemeTogglerButton />
        </div>
        <NuxtLink
          to="/auth/login"
          :class="`
                  text-sm/6 font-semibold flex w-16 pt-1
                  gap-x-1 hover:gap-x-2
                  hover:text-accent-800 dark:hover:text-accent-200
                  `.replaceAll(/\s+/g, ' ')">
          Log in
          <span>&rarr;</span>
        </NuxtLink>
      </div>
    </nav>
  </header>
</template>
