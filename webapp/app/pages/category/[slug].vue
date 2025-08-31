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
    <div class="grid grid-cols-6 gap-x-4">
      <div class="col-span-6 md:col-span-2">
        <p>Filters...</p>
      </div>
      <div class="col-span-6 md:col-span-4">
        <h1 class="text-2xl font-bold py-2">{{ cat?.name }}</h1>
        <ClientOnly>
          <div
            :class="`
                    mt-6 grid gap-y-10 gap-x-6 xl:gap-x-8
                    grid-cols-2 md:grid-cols-3 lg:grid-cols-4
                    `">
            <template v-if="pending">
              <AdPreviewPlaceholder v-for="i in 9" :key="i" />
            </template>
            <template v-else-if="page?.content?.length">
              <AdPreviewThumbnail v-for="a in page.content" :key="a.id" :a="a" />
            </template>
          </div>

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
        </ClientOnly>
      </div>
    </div>
  </div>
</template>
