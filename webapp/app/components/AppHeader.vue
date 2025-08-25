<template>
  <header class="absolute inset-x-0 top-0 z-50">
    <nav class="flex items-center p-6 lg:px-8">
      <div class="flex lg:w-xs">
        <NuxtLink to="/">
          <div class="flex gap-x-2">
            <IconLogoIcon />
            <span class="font-bold">Adrift</span>
          </div>
        </NuxtLink>
      </div>

      <div class="flex-1 pe-4">
        <div class="flex max-w-xl mx-auto gap-x-2">
          <AppHeaderCategories />
          <div class="flex-1 ms-1">
            <AppHeaderSearch />
          </div>
        </div>
      </div>

      <div class="flex lg:w-xs justify-end">
        <NuxtLink to="/auth/login" :class="`text-sm/6 font-semibold text-gray-900 flex w-16 gap-x-1
              hover:gap-x-2 hover:text-indigo-600`">
          Log in
          <span>&rarr;</span>
        </NuxtLink>
      </div>
    </nav>
  </header>
</template>
