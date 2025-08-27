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

    <div
      v-if="page && page.pageable && page.totalPages > 1"
      class="my-4"
      :class="{'opacity-50 pointer-events-none': pending}"
    >
      <PaginationPrevNext
        :prev="`/category/${cat?.slug}?page=${page.pageable.pageNumber - 1}`"
        :next="`/category/${cat?.slug}?page=${page.pageable.pageNumber + 1}`"
        :page="page.pageable.pageNumber"
        :total="page.totalPages"
      />
    </div>

    <div v-if="page?.content?.length">
      <div v-for="a in page.content" :key="a.id" class="border px-3 py-2 mb-2 rounded border-gray-200 dark:border-gray-700">
        <div :class="{'opacity-10 pointer-events-none': pending}">
          <NuxtLink :to="`/a/${a.id}`" class="link-clr1">
            {{ a.title }}
          </NuxtLink>
        </div>
      </div>
    </div>
    <div v-else>
      <p>No content</p>
    </div>

  </div>
</template>
