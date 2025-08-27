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
        :class="`
                block w-full rounded-md px-3 py-1.5 text-base
                text-gray-900 bg-gray-50 dark:text-white dark:bg-gray-700
                outline-1 -outline-offset-1 outline-gray-300
                placeholder:text-gray-400 focus:outline-2
                focus:-outline-offset-2 sm:text-sm/6
                `.replaceAll(/\s+/g, ' ')"
        @input="(e: Event) => $emit('update:val', e)">
    </div>
  </div>
</template>
