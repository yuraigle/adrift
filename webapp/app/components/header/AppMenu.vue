<template>
  <ThemeTogglerButton />
  <NuxtLink
    to="/auth/login"
    :class="`
            text-sm font-semibold flex w-18 ms-4
            gap-x-1 hover:gap-x-2
            hover:text-accent-600 dark:hover:text-accent-400
            `.replaceAll(/\s+/g, ' ')">
    Log in
    <span>&rarr;</span>
  </NuxtLink>
</template>
