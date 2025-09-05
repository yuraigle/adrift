export interface AdDetailsDto {
  id: number
  title: string
  description: string
  price: number
  created: string,
  updated: string | null,
  user: UserDto,
  category: CategoryDto
  fields: FieldDto[],
  images: ImageDto[]
}

export interface UserDto {
  id: number
  username: string
}

export interface CategoryDto {
  id: number
  name: string
  slug: string
}

export interface FieldDto {
  qid: number
  value: string
}

export interface ImageDto {
  filename: string
  alt: string
}
