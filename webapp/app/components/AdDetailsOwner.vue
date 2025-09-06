<script setup lang="ts">
const props = defineProps<{
  user: UserDto
}>()
</script>

<template>
  <div>
    <div class="mb-4">
      <UiButtonPrimary cls="mb-3 py-3">
        <span class="block text-lg">Show contact</span>
        <span class="block text-xs font-light opacity-60">+7 (123) 456-XX-XX</span>
      </UiButtonPrimary>
      <UiButtonSuccess cls="mb-3 py-3">
        <span class="block text-lg">Send message</span>
        <span class="block text-xs font-light opacity-60">Answers in about 1 hour</span>
      </UiButtonSuccess>
    </div>

    <div class="flex gap-x-2">
      <div class="text-gray-400 dark:text-gray-600">
        <NuxtLink to="/">
          <IconUserScan :size=64 />
        </NuxtLink>
      </div>
      <div>
        <div>
          <NuxtLink to="/" class="text-xl link-clr0">
            {{ props.user.username }}
          </NuxtLink>
        </div>
      </div>
    </div>
  </div>
</template>
