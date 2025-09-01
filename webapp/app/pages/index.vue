<script setup lang="ts">
useHead({
  titleTemplate: (t: string | undefined) => 'Home - ' + t,
})

const { data: page, pending } = await useAsyncData<AdsPage>(
  'ads-home-rec',
  () => $fetch(`${API_BASE}/recommended?size=12`),
  {
    server: false,
  }
);

</script>

<template>
  <div>
    <div>
      <h1 class="text-2xl font-bold py-1">Browse popular categories</h1>
      <div class="grid grid-cols-2 md:grid-cols-3 lg:grid-cols-6 gap-4 my-4">
        <div class="col-span-2 border px-6 py-4 border-gray-200 dark:border-gray-700">
          ABC
        </div>
        <div class="col-span-1 lg:col-span-2 border px-6 py-4 border-gray-200 dark:border-gray-700">
          DEF
        </div>
        <div class="col-span-1 lg:col-span-2 border px-6 py-4 border-gray-200 dark:border-gray-700">
          GHI
        </div>
        <div class="col-span-2 hidden md:block border px-6 py-4 border-gray-200 dark:border-gray-700">
          JKL
        </div>
        <div class="col-span-2 hidden lg:block border px-6 py-4 border-gray-200 dark:border-gray-700">
          MNO
        </div>
        <div class="col-span-2 hidden lg:block border px-6 py-4 border-gray-200 dark:border-gray-700">
          PQR
        </div>
      </div>
    </div>
    <div class="grid grid-cols-6 gap-x-4">
      <div class="col-span-6 md:col-span-4">
        <ClientOnly>
          <h2 class="text-2xl font-bold py-1">Recommended for you</h2>
          <div
            :class="`
                    mt-6 grid gap-y-10 gap-x-6 xl:gap-x-8
                    grid-cols-2 md:grid-cols-3 lg:grid-cols-4
                    `">
            <template v-if="pending">
              <AdPreviewThumbPlaceholder v-for="i in 9" :key="i" />
            </template>
            <template v-else-if="page?.content?.length">
              <AdPreviewThumb v-for="a in page.content" :key="a.id" :a="a" />
            </template>
          </div>
        </ClientOnly>
      </div>
      <div class="col-span-6 md:col-span-2">
        <h2 class="text-xl font-bold py-1 mt-4 mb-3">Favorites</h2>
        <div class="border px-4 py-3 mb-4 border-gray-200 dark:border-gray-700">
          1
        </div>
        <div class="border px-4 py-3 mb-4 border-gray-200 dark:border-gray-700">
          2
        </div>
        <div class="border px-4 py-3 mb-4 border-gray-200 dark:border-gray-700">
          3
        </div>
      </div>
    </div>
  </div>
</template>
