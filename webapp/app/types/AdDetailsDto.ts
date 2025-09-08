export interface AdDetailsDto {
  id: number
  title: string
  description: string
  price: number
  phone: string | null
  city: string | null
  zip: string | null
  address: string | null
  lat: number | null
  lon: number | null
  created: string,
  updated: string | null,
  user: UserDto,
  category: CategoryDto
  fields: FieldDto[],
  images: ImageDto[]
}
