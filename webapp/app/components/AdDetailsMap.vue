<script setup lang="ts">
import mapboxgl from 'mapbox-gl'
import 'mapbox-gl/dist/mapbox-gl.css'

const props = defineProps<{ lat: number, lon: number }>()

const zoom: number = 12
const w: number = 760
const h: number = Math.round(w / 16 * 9)
const token: string = useAppConfig().MAPBOX_TOKEN
const isInteractiveMapShown = ref(false)

const mapPic = computed((): string => {
  const { lat, lon } = props
  const x2 = window.devicePixelRatio >= 2 ? '@2x' : ''

  return `https://api.mapbox.com/styles/v1/mapbox/streets-v12/static` +
    `/pin-l+0092b8(${lon},${lat})/${lon},${lat},${zoom},0/${w}x${h}${x2}` +
    `?access_token=${token}`
})

const onClickMap = () => {
  if (isInteractiveMapShown.value) {
    return
  }

  const map: mapboxgl.Map = new mapboxgl.Map({
    accessToken: token,
    container: 'map1',
    style: 'mapbox://styles/mapbox/streets-v12',
    center: [props.lon, props.lat],
    zoom: zoom
  });

  map.on('load', () => {
    isInteractiveMapShown.value = true
  })

  new mapboxgl.Marker({ color: '#0092b8' })
    .setLngLat([props.lon, props.lat])
    .addTo(map);
}
</script>

<template>
  <div class="mt-2 w-full aspect-[16/9]">
    <div
      id="map1"
      class="w-full h-full bg-center bg-no-repeat bg-cover rounded-md cursor-pointer"
      :style="{ 'background-image': `url('${mapPic}')` }"
      @click="onClickMap"
    />
  </div>
</template>
