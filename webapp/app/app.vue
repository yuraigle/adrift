<template>
  <header>
    <nav>
      <ul>
        <li>
          <NuxtLink to="/">Home</NuxtLink>
        </li>
        <li>
          <NuxtLink to="/about">About</NuxtLink>
        </li>
        <li>
          <NuxtLink to="/auth/login">Login</NuxtLink>
        </li>
      </ul>
    </nav>
  </header>
  <main>
    <NuxtPage />
  </main>
</template>
