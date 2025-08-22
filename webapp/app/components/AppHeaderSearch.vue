<template>
  <form>
    <label for="default-search" class="sr-only">Search</label>
    <div class="relative">
      <div :class="`absolute inset-y-0 start-0 flex items-center ps-3 pointer-events-none
                    text-gray-500 dark:text-gray-400`">
        <IconSearchIcon :size=20 />
      </div>
      <input id="default-search" type="search" :class="`block w-full py-2 ps-10 pe-4 text-sm
                    text-gray-900 rounded-xl bg-gray-50
                    border-2 border-blue-700 focus-visible:outline-none
                    dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white
                    dark:focus:ring-blue-500 dark:focus:border-blue-500`" placeholder="Search..." required>
      <button type="submit" :class="`text-white absolute px-4 py-2 end-0 bottom-0
                    border-2 border-blue-700 bg-blue-700 hover:bg-blue-800
                    font-medium rounded-r-xl text-sm cursor-pointer
                    dark:bg-blue-600 dark:hover:bg-blue-700 dark:focus:ring-blue-800`">
        Search
      </button>
    </div>
  </form>
</template>
