<script setup lang="ts">
const route = useRoute()
const slug = computed(() => route.params.slug)
const pageNum = computed(() => route.query.page)

const { data: categories } = await useAsyncData<Array<CategorySummary>>(
  'categories',
  () => $fetch(`${API_BASE}/categories`),
  {
    watch: [slug],
    server: true,
  }
);

const cat: Ref<CategorySummary | undefined> = computed(
  () => categories.value?.find((c) => c.slug === slug.value)
);

const { data: page, pending } = await useAsyncData<AdsPage>(
  'ads-by-category-' + cat.value?.id + '-page-' + pageNum.value,
  () => $fetch(`${API_BASE}/categories/${cat.value?.id}/a?page=${pageNum.value || 0}`),
  {
    watch: [cat, pageNum],
    server: false,
  }
);

</script>

<template>
  <div>
    <h1 class="text-2xl font-bold py-2">{{ cat?.name }}</h1>

    <ClientOnly>
      <div v-if="pending">
        <p>Loading...</p>
      </div>
      <div v-else-if="page?.content?.length">
        <div
          :class="`
                  mt-6 grid gap-y-10 gap-x-6 xl:gap-x-8
                  sm:grid-cols-2 md:grid-cols-4 lg:grid-cols-6
                  `">
          <AdCatalogPreview v-for="a in page.content" :key="a.id" :a="a" />
        </div>
      </div>
      <div v-else>
        <p>No content</p>
      </div>
    </ClientOnly>

    <div
      v-if="page && page.pageable && page.totalPages > 1"
      class="my-4"
      :class="{ 'opacity-50 pointer-events-none': pending }">
      <PaginationPrevNext
        :prev="`/category/${cat?.slug}?page=${page.pageable.pageNumber - 1}`"
        :next="`/category/${cat?.slug}?page=${page.pageable.pageNumber + 1}`"
        :page="page.pageable.pageNumber"
        :total="page.totalPages" />
    </div>
  </div>
</template>

<style lang="css" scoped>
.two-lines-only {
  display: -webkit-box;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 2;
  overflow: hidden;
  text-overflow: ellipsis;
}
</style>
