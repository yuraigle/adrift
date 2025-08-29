<template>
  <div class="h-screen flex flex-col">
    <AppHeader />

    <main class="flex-grow relative isolate pt-20">
      <div class="max-w-[1200px] mx-auto py-6 px-4 md:px-6">
        <slot />
      </div>
    </main>

    <AppFooter />
  </div>
</template>
