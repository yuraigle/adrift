<script setup lang="ts">
import type { Filter } from '~/types/Filter'

const route = useRoute()
const slug = computed(() => route.params.slug)
const pageNum = computed(() => route.query.page || 0)
const filter = reactive<Filter>({} as Filter)

const { data: cat } = await useAsyncData<CategorySummary>(
  'category-by-slug-' + slug.value,
  () => {
    if (!slug.value) {
      return Promise.resolve({} as CategorySummary);
    }

    return $fetch(useAppConfig().API_BASE + `/categories/slug/${slug.value}`);
  },
  {
    watch: [slug],
    server: true,
  }
)

const { data: page, pending } = await useAsyncData<AdsPage>(
  () => {
    if (!cat.value) {
      return Promise.resolve({} as AdsPage);
    }

    const cid = cat.value.id;
    const page = pageNum.value;
    const filterStr =encodeURIComponent(btoa(JSON.stringify(filter)));

    return $fetch(useAppConfig().API_BASE + `/categories/${cid}/a?` +
      `page=${page}&size=10&filter=${filterStr}`);
  },
  {
    watch: [slug, pageNum, filter],
    server: false
  }
);

const onFilterSubmit = (form: Filter) => {
  filter.keywords = form.keywords
  filter.price_from = form.price_from
  filter.price_to = form.price_to
}
</script>

<template>
  <div>
    <h1 class="text-2xl font-bold py-2 mb-4">{{ cat?.name }}</h1>
    <div class="grid grid-cols-12 gap-x-4">
      <div class="col-span-12 lg:col-span-3">
        <FilterDefaultCategory @submit="(f) => onFilterSubmit(f)" />
      </div>
      <div class="col-span-12 lg:col-span-9">
        <ClientOnly>
          <div class="mb-5">
            <template v-if="pending">
              <AdPreviewRowPlaceholder v-for="i in 9" :key="i" />
            </template>
            <template v-else-if="page?.content?.length">
              <AdPreviewRow v-for="a in page.content" :key="a.id" :a="a" />
            </template>
          </div>

          <div
            v-if="page && page.pageable && page.totalPages > 1"
            class="my-4"
            :class="{ 'opacity-50 pointer-events-none': pending }">
            <PaginationPrevNext
              :prev="`/category/${slug}?page=${page.pageable.pageNumber - 1}`"
              :next="`/category/${slug}?page=${page.pageable.pageNumber + 1}`"
              :page="page.pageable.pageNumber"
              :total="page.totalPages" />
          </div>
        </ClientOnly>
      </div>
    </div>
  </div>
</template>
