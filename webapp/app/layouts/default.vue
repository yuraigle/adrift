<template>
  <div>
    <header class="absolute inset-x-0 top-0 z-50">
      <nav class="flex items-center p-6 lg:px-8">
        <div class="flex pe-6">
          <NuxtLink to="/">
            <div class="flex gap-x-2">
              <IconLogoIcon />
              <span class="font-bold">Adrift</span>
            </div>
          </NuxtLink>
        </div>

        <div class="flex-1 pe-4">
          <div class="flex max-w-xl mx-auto">
            <div class="flex pe-2">
              <button type="button" :class="`text-white bg-blue-700 hover:bg-blue-800
                  font-medium rounded-lg text-sm px-5 py-2 cursor-pointer
                  dark:bg-blue-600 dark:hover:bg-blue-700 focus:outline-none dark:focus:ring-blue-800`">
                <span class="flex gap-x-2">
                  <IconListSearchIcon />
                  <span class="mt-0.5">All Categories</span>
                </span>
              </button>
            </div>

            <form class="flex-1">
              <label for="default-search" class="sr-only">Search</label>
              <div class="relative">
                <div :class="`absolute inset-y-0 start-0 flex items-center ps-3 pointer-events-none
                    text-gray-500 dark:text-gray-400`">
                  <IconSearchIcon :size=20 />
                </div>
                <input id="default-search" type="search" :class="`block w-full py-2 ps-10 pe-4 text-sm text-gray-900 rounded-lg bg-gray-50
                    border-2 border-blue-700 focus-visible:outline-none
                    dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white
                    dark:focus:ring-blue-500 dark:focus:border-blue-500`" placeholder="Search..." required>
                <button type="submit" :class="`text-white absolute px-4 py-2 end-0 bottom-0
                    border-2 border-blue-700 bg-blue-700 hover:bg-blue-800
                    font-medium rounded-r-lg text-sm cursor-pointer
                    dark:bg-blue-600 dark:hover:bg-blue-700 dark:focus:ring-blue-800`">
                  Search
                </button>
              </div>
            </form>
          </div>
        </div>
        <div class="flex lg:justify-end">
          <NuxtLink
            to="/auth/login"
            :class="`text-sm/6 font-semibold text-gray-900 flex w-16 gap-x-1
              hover:gap-x-2 hover:text-indigo-600`"
          >
            Log in
            <span>&rarr;</span>
          </NuxtLink>
        </div>
      </nav>
    </header>

    <main class="relative isolate px-6 pt-20 lg:px-8">
      <slot />
    </main>

    <AppFooter />
  </div>
</template>
