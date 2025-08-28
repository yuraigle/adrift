<script setup lang="ts">
const route = useRoute()
const slug = computed(() => route.params.slug)
const pageNum = computed(() => route.query.page)

const imageUrl = (filename: string, h: number) => {
  return useAppConfig().S3_BASE + filename + '_' + h + '.webp'
}

const formatPrice = (price: number) => {
  return new Intl.NumberFormat("en-US", { style: "currency", currency: "USD" }).format(price);
}

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

    <div v-if="page?.content?.length">
      <div
        :class="`
                mt-6 grid gap-y-10 grid-cols-6 gap-x-6 sm:grid-cols-2
                lg:grid-cols-8 xl:gap-x-8
                `">
        <div v-for="a in page.content" :key="a.id" class="group relative aspect-4/3">
          <img
            :src="imageUrl('kysv0ztrvskldu8y', 300)"
            :class="`
                    rounded-md bg-gray-200
                    group-hover:opacity-75
                    `"
            alt="">
          <div class="mt-4">
            <div>
              <h3 class="text-sm text-gray-700">
                <NuxtLink :to="`/a/${a.id}`" class="two-lines-only">
                  <span aria-hidden="true" class="absolute inset-0" />
                  {{ a.title }}
                </NuxtLink>
              </h3>
              <p class="font-semibold text-gray-900">{{ formatPrice(a.price) }}</p>
              <p class="text-sm text-gray-500">Location line</p>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div v-else>
      <p>No content</p>
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
