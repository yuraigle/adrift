<template>
  <header>
      <div class="max-w-[1200px] mx-auto px-4 md:px-6">
        <div class="md:flex relative">
          <div class="h-20 flex items-center pe-8">
            <HeaderAppLogo />
          </div>
          <div class="h-10 md:h-20 flex items-center gap-x-2 me-[160px] w-full max-w-[700px]">
            <HeaderAppCategories />
            <HeaderAppSearch />
          </div>
          <div
            :class="`
                    absolute top-0 right-0
                    h-20 flex items-center justify-end ps-4 w-[160px]
                    `.replaceAll(/\s+/g, ' ')">
            <HeaderAppMenu />
          </div>
        </div>
      </div>
  </header>
</template>
