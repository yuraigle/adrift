<script setup lang="ts">
const toasts = useToastsStore()
</script>

<template>
  <div class="absolute top-5 right-5 z-50 flex flex-col gap-y-2">
    <template v-for="toast in toasts.messages" :key="toast.id">
      <ToastInfo v-if="toast.type == 'info'" :message="toast.message" @close="toasts.hide(toast.id)" />
      <ToastSuccess v-if="toast.type == 'success'" :message="toast.message" @close="toasts.hide(toast.id)" />
      <ToastError v-if="toast.type == 'error'" :message="toast.message" @close="toasts.hide(toast.id)" />
    </template>
  </div>
</template>

<style>
.toast {
  position: fixed;
  right: 10px;
  z-index: 1100;
}
</style>
