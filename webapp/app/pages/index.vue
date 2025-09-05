<script setup lang="ts">
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
      <PopularCategories />
    </div>
    <div class="grid grid-cols-6 gap-x-4">
      <div class="col-span-6 md:col-span-4">
        <ClientOnly>
          <h2 class="text-2xl font-bold py-1">Recommended for you</h2>
          <div
            :class="`
                    mt-6 grid gap-y-10 gap-x-6 xl:gap-x-8
                    grid-cols-2 xs:grid-cols-3 xl:grid-cols-4
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
        <FavoriteAds />
      </div>
    </div>
  </div>
</template>
