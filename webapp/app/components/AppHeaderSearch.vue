<script setup lang="ts">
const q = ref('')
const router = useRouter()

const onSubmitSearch = () => {
  router.push({ path: '/search', query: { q: q.value } })
}
</script>

<template>
  <form class="flex-grow" @submit.prevent="onSubmitSearch">
    <label for="search-query" class="sr-only">Search</label>
    <div class="relative">
      <div
        :class="`
                absolute inset-y-0 start-0 flex items-center ps-3
                text-gray-500 dark:text-gray-400 pointer-events-none
                `.replaceAll(/\s+/g, ' ')">
        <IconSearchIcon :size=20 />
      </div>
      <input
        id="default-search"
        v-model="q"
        type="search"
        :class="`
                w-full py-2 ps-10 pe-4 text-sm rounded-xl border-2
                text-gray-900 bg-gray-50 dark:text-white dark:bg-gray-700
                border-siberia-600 dark:border-siberia-700
                focus-visible:outline-none
                `.replaceAll(/\s+/g, ' ')"
        placeholder="Search...">
      <button
        type="submit"
        :class="`
                text-white absolute px-4 py-2 end-0 bottom-0 border-2
                font-medium rounded-r-xl text-sm cursor-pointer
                bg-siberia-600 border-siberia-600 hover:bg-siberia-700
                dark:bg-siberia-700 dark:border-siberia-700
                dark:hover:bg-siberia-600 dark:hover:border-siberia-600
                `.replaceAll(/\s+/g, ' ')">
        Search
      </button>
    </div>
  </form>
</template>
