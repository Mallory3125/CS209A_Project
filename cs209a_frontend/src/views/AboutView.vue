<template>
  <div class="about">
    <div class="about-content">
      <h1>About Page</h1>
      <p class="description">This page is all about the Vue application and its features.</p>

      <div class="button-group">
        <router-link :to="{ name: 'bug' }">
          <button class="nav-button">Go to Bug View</button>
        </router-link>

        <router-link :to="{ name: 'relatedTopic' }">
          <button class="nav-button">Go to Related Topic View</button>
        </router-link>

        <router-link :to="{ name: 'topic' }">
          <button class="nav-button">Go to Topic View</button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script setup>
</script>

<style scoped>
.about {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 80vh;
  padding: 2rem;
}

.about-content {
  max-width: 800px;
  width: 100%;
  text-align: center;
}

h1 {
  margin-bottom: 1.5rem;
  font-size: 2.5rem;
  color: #2c3e50;
}

.description {
  margin-bottom: 2.5rem;
  font-size: 1.1rem;
  line-height: 1.6;
  color: #555;
}

.button-group {
  display: flex;
  flex-direction: column;
  gap: 1rem;
  max-width: 300px;
  margin: 0 auto;
}

.nav-button {
  width: 100%;
  padding: 0.8rem 1.5rem;
  font-size: 1rem;
  background-color: #42b983;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.nav-button:hover {
  background-color: #3aa876;
  transform: translateY(-2px);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}
</style>