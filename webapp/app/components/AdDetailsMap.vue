<script setup lang="ts">
const props = defineProps<{ lat: number, lon: number }>()

const zoom = 14
const w: number = 760
const h: number = Math.round(w / 16 * 9)
const token: string = useAppConfig().MAPBOX_TOKEN

const mapPic = computed((): string => {
  const isDark: boolean = document.documentElement.classList.contains('dark')
  const style = isDark ? 'dark-v11' : 'streets-v12'
  const { lat, lon } = props
  const x2 = window.devicePixelRatio >= 2 ? '@2x' : ''

  return `https://api.mapbox.com/styles/v1/mapbox/${style}/static` +
    `/pin-l+0092b8(${lon},${lat})/${lon},${lat},${zoom},0/${w}x${h}${x2}` +
    `?access_token=${token}`
})
</script>

<template>
  <div class="mt-2 w-full aspect-[16/9]">
    <div
      class="w-full h-full bg-center bg-no-repeat"
      :style="{ 'background-image': `url('${mapPic}')` }"
    />
  </div>
</template>
