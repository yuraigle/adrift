export interface ToastMessage {
  id: number
  message: string
  type: 'error' | 'success' | 'info'
}
