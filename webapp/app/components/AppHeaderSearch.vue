<template>
  <form class="flex-grow">
    <label for="default-search" class="sr-only">Search</label>
    <div class="relative">
      <div
        :class="`absolute inset-y-0 start-0 flex items-center ps-3 pointer-events-none
                  text-gray-500 dark:text-gray-400`">
        <IconSearchIcon :size=20 />
      </div>
      <input
        id="default-search" 
        type="search"
        :class="`w-full py-2 ps-10 pe-4 text-sm rounded-xl
                  text-gray-900 bg-gray-50 dark:text-white dark:bg-gray-700
                  border-2 border-indigo-700 dark:border-indigo-500
                  focus-visible:outline-none`"
        placeholder="Search...">
      <button
        type="submit"
        :class="`text-white absolute px-4 py-2 end-0 bottom-0
                  bg-indigo-700 hover:bg-indigo-600
                  dark:bg-indigo-500 dark:hover:bg-indigo-400
                  border-2 border-indigo-700 dark:border-indigo-500
                  font-medium rounded-r-xl text-sm cursor-pointer`">
        Search
      </button>
    </div>
  </form>
</template>
