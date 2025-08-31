<template>
  <div class="relative animate-pulse">
    <div
      :class="`
              aspect-square w-full rounded-md
              text-gray-400 bg-gray-200
              dark:text-gray-600 dark:bg-gray-700
              flex items-center justify-center
              `">
      <IconPictureIcon :size=72 />
    </div>
    <div class="mt-5">
      <div class="h-2.5 bg-gray-200 rounded-full dark:bg-gray-700 w-36 mb-3">&nbsp;</div>
      <div class="h-2 bg-gray-200 rounded-full dark:bg-gray-700 w-25 mb-2.5">&nbsp;</div>
    </div>
  </div>
</template>
