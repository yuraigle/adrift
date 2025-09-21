<script setup lang="ts">
import type { Filter } from "~/types/Filter";

defineEmits(['submit'])

const form = reactive<Filter>({
  price_from: '',
  price_to: '',
  keywords: '',
});

const onInputPriceFrom = (e: Event) => {
  form.price_from = (e.target as HTMLInputElement).value
  form.price_from = form.price_from.replace(/[^\d]+/g, '')
}

const onInputPriceTo = (e: Event) => {
  form.price_to = (e.target as HTMLInputElement).value
  form.price_to = form.price_to.replace(/[^\d]+/g, '')
}

</script>

<template>
  <form @submit.prevent="() => $emit('submit', form)">
    <div class="mb-4">
      <label class="font-semibold" for="price_to">Price</label>
      <div class="flex gap-x-2 mt-2">
        <input
          id="price_from"
          :value="form.price_from"
          class="input-field w-1/2"
          placeholder="From"
          @input="(e: Event) => onInputPriceFrom(e)">
        <input
          id="price_to"
          :value="form.price_to"
          class="input-field w-1/2"
          placeholder="To"
          @input="(e: Event) => onInputPriceTo(e)">
      </div>
    </div>

    <div class="mb-4">
      <label class="font-semibold" for="keywords">Keywords</label>
      <div class="flex gap-x-2 mt-2">
        <input
          id="keywords"
          v-model="form.keywords"
          class="input-field"
          placeholder="Search in title and description">
      </div>
    </div>

    <div class="mb-4 pt-2">
      <UiButtonPrimary cls="py-2.5" type="submit">
        <span class="font-light">Apply filters</span>
      </UiButtonPrimary>
    </div>
  </form>
</template>
