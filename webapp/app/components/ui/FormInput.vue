<script setup lang="ts">
defineProps({
  id: {
    type: String,
    required: true,
  },
  val: {
    type: undefined,
    required: true
  },
  label: {
    type: String,
    default: null
  },
  error: {
    type: String,
    default: null
  },
  type: {
    type: String,
    default: 'text'
  },
  autocomplete: {
    type: String,
    default: null
  },
  placeholder: {
    type: String,
    default: null
  }
})

defineEmits(['update:val'])
</script>

<template>
  <div>
    <label
      v-if="label"
      :for="id"
      class="text-sm/6 font-medium text-gray-700 dark:text-gray-300"
    >{{ label }}</label>
    <div class="mt-2">
      <input
        :id="id" :value="val" :type="type" :autocomplete="autocomplete"
        :placeholder="placeholder" class="input-field"
        @input="(e: Event) => $emit('update:val', e)">
    </div>
  </div>
</template>
