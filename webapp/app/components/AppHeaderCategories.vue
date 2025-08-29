<script setup lang="ts">
const isMenuShown = ref(false)

const handleClickOutsideMenu = (event: MouseEvent) => {
  const menuElements: Array<EventTarget> = [];
  ['categories-menu', 'categories-button'].forEach((id) => {
    const el: HTMLElement | null = document.getElementById(id);
    if (el) {
      menuElements.push(el);
    }
  });

  const isClickedOutside = event.composedPath()
    .every((e) => !menuElements.includes(e));

  if (isClickedOutside) {
    isMenuShown.value = false
  }
};

onMounted(() => {
  document.addEventListener('click', handleClickOutsideMenu);
});

onUnmounted(() => {
  document.removeEventListener('click', handleClickOutsideMenu);
});

</script>

<template>
  <div class="relative">
    <button
      id="categories-button"
      type="button"
      :class="`
                justify-center rounded-md px-3 py-2 rounded-xl
                text-sm/6 text-white shadow-xs
                cursor-pointer flex gap-x-2
                bg-siberia-600 dark:bg-siberia-700
                hover:bg-siberia-700 dark:hover:bg-siberia-600
                active:bg-siberia-700 dark:active:bg-siberia-600
              `.replaceAll(/\s+/g, ' ')"
      @click="isMenuShown = !isMenuShown"
    >
        <IconListSearchIcon v-if="!isMenuShown" :size=24 />
        <IconCloseIcon v-else :size=24 />
        <span>All Categories</span>
    </button>

    <div
      id="categories-menu"
      :class="`
              absolute top-12 rounded-3xl shadow-lg
              w-screen max-w-md overflow-hidden
              bg-gray-50 dark:bg-gray-700
              outline-1 outline-gray-900/5 backdrop:bg-transparent
              transition transition-discrete [--anchor-gap:--spacing(3)]
              ${isMenuShown ? 'opacity-100' : 'opacity-0 pointer-events-none'}
              `.replaceAll(/\s+/g, ' ')"
    >
      <div class="p-4">
        <div class="categories-menu-item">
          <div class="categories-menu-icon">
            <IconCategoryHousesSale :size=24 />
          </div>
          <div class="flex-auto">
            <NuxtLink
              to="/category/houses-for-sale"
              class="block font-semibold text-gray-900 dark:text-gray-100"
              @click="isMenuShown = false"
            >
              Houses for Sale
              <span class="absolute inset-0"/>
            </NuxtLink>
            <p class="mt-1 text-sm text-gray-600 dark:text-gray-300">
              Find the latest property for sale
            </p>
          </div>
        </div>

        <div class="categories-menu-item">
          <div class="categories-menu-icon">
            <IconCategoryApartmentsRent :size=24 />
          </div>
          <div class="flex-auto">
            <NuxtLink
              to="/category/apartments-for-rent"
              class="block font-semibold text-gray-900 dark:text-gray-100"
              @click="isMenuShown = false"
            >
              Apartments for Rent
              <span class="absolute inset-0"/>
            </NuxtLink>
            <p class="mt-1 text-sm text-gray-600 dark:text-gray-300">
              Find the latest property for rent
            </p>
          </div>
        </div>
      </div>

      <div
        :class="`
                grid grid-cols-2 divide-x
                divide-gray-300 border-t border-gray-300
                dark:divide-gray-600 dark:border-gray-600
                bg-gray-100 dark:bg-gray-800/50
                `.replaceAll(/\s+/g, ' ')">
        <NuxtLink
          to="/a/post"
          :class="`
                  flex items-center justify-center gap-x-2.5 p-3
                  text-sm font-semibold
                  text-gray-900 hover:bg-gray-200
                  dark:text-gray-100 dark:hover:bg-gray-700/50
                  `.replaceAll(/\s+/g, ' ')"
          @click="isMenuShown = false"
        >
          <IconCirclePlus :size=20 />
          Post an Ad
        </NuxtLink>
        <NuxtLink
          to="/category"
          :class="`
                  flex items-center justify-center gap-x-2.5 p-3
                  text-sm font-semibold
                  text-gray-900 hover:bg-gray-200
                  dark:text-gray-100 dark:hover:bg-gray-700/50
                  `.replaceAll(/\s+/g, ' ')"
          @click="isMenuShown = false"
        >
          <IconListSearchIcon :size=20 />
          Other Categories
        </NuxtLink>
      </div>
    </div>
  </div>
</template>

<style lang="css" scoped>
@reference "~/assets/css/main.css";

.categories-menu-item {
    @apply relative flex items-center gap-x-6 rounded-lg p-4
    hover:bg-gray-300/50 dark:hover:bg-gray-800/50;
}

.categories-menu-icon {
    @apply flex size-11 flex-none items-center justify-center
    rounded-lg text-gray-800 dark:text-gray-200;
}
</style>
