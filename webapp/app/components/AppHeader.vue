<template>
  <header class="w-full relative max-w-[1200px] mx-auto">
    <nav class="grid grid-cols-12 px-4 md:px-6">
      <div class="col-span-12 md:col-span-2 h-18">
        <NuxtLink to="/" class="flex items-center gap-x-2 px-4 py-4 h-full">
          <span class="text-accent-600 dark:text-accent-400">
            <IconLogoIcon />
          </span>
          <span class="font-bold">Adrift</span>
        </NuxtLink>
      </div>

      <div
        :class="`
                col-span-12 md:col-span-8 px-4 h-18
                flex items-center gap-x-2 w-full
                `.replaceAll(/\s+/g, ' ')">
        <AppHeaderCategories />
        <AppHeaderSearch />
      </div>

      <div
        :class="`
                absolute top-0 right-0 ps-4 h-18
                flex items-center justify-end
                `">
        <div class="me-4 py-4 flex items-center">
          <ThemeTogglerButton />
        </div>
        <NuxtLink
          to="/auth/login"
          :class="`
                  text-sm font-semibold flex w-18
                  gap-x-1 hover:gap-x-2
                  hover:text-accent-600 dark:hover:text-accent-400
                  `.replaceAll(/\s+/g, ' ')">
          Log in
          <span>&rarr;</span>
        </NuxtLink>
      </div>
    </nav>
  </header>
</template>
