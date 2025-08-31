export const useToastsStore = defineStore('toasts', () => {
  const messages = ref<ToastMessage[]>([]);
  const nextId = ref(0);

  const showError = (msg: string) => {
    showMessage(msg, 'error');
  }

  const showMessage = (msg: string, t: 'error' | 'success' | 'info') => {
    const a = { id: nextId.value++, message: msg, type: t } as ToastMessage;
    messages.value.push(a);

    setTimeout(() => {
      messages.value.forEach((m) => {
        if (m.id === a.id) {
          messages.value.splice(messages.value.indexOf(m), 1);
        }
      });
    }, 4000);
  }

  const hide = (id: number) => {
    messages.value.forEach((m) => {
      if (m.id === id) {
        messages.value.splice(messages.value.indexOf(m), 1);
      }
    });
  }

  return {
    messages,
    showError,
    showMessage,
    hide,
  };
});
