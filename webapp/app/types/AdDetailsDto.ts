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
