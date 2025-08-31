<script setup lang="ts">
const toasts = useToastsStore()
</script>

<template>
  <div class="absolute bottom-5 right-5 z-50 flex flex-col gap-y-2">
    <template v-for="toast in toasts.messages" :key="toast.id">
      <ToastInfo v-if="toast.type == 'info'" :message="toast.message" @close="toasts.hide(toast.id)" />
      <ToastSuccess v-if="toast.type == 'success'" :message="toast.message" @close="toasts.hide(toast.id)" />
      <ToastError v-if="toast.type == 'error'" :message="toast.message" @close="toasts.hide(toast.id)" />
    </template>
  </div>
</template>
