export interface AdSummary {
  id: number
  title: string
  description: string
  price: number
  category: CategorySummary
  images: ImageSummary[]
}
