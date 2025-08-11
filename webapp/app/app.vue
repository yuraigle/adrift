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
          <NuxtLink to="/a/1">Ad</NuxtLink>
        </li>
        <li class="ms-auto">
          <NuxtLink to="/auth/login">Login</NuxtLink>
        </li>
      </ul>
    </nav>
  </header>
  <main>
    <NuxtPage />
  </main>
</template>

<style scoped>
ul {
  display: flex;
  list-style-type: none;
  gap: 10px;
  margin: 0;
  padding: 0;
}

ul li a {
  color: #666;
  text-decoration: none;
}

ul li a.router-link-active {
  color: #333;
  text-decoration: underline;
}

.ms-auto {
  margin-left: auto;
}
</style>