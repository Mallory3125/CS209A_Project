<template>
    <div class="about">
      <h1>About Page</h1>
      <p>This page is all about the Vue application and its features.</p>
  
      <!-- Navigation buttons using router-link -->
      <router-link :to="{ name: 'bug' }">
        <button>Go to Bug View</button>
      </router-link>
  
      <router-link :to="{ name: 'relatedTopic' }">
        <button>Go to Related Topic View</button>
      </router-link>
  
      <router-link :to="{ name: 'topic' }">
        <button>Go to Topic View</button>
      </router-link>
    </div>
  </template>
  
  <script setup>
  </script>
  
  <!-- Add "scoped" attribute to limit CSS to this component only -->
  <style scoped>
  .about {
    display: flex;
    flex-direction: column;
    align-items: center;
  }
  
  .about h1 {
    margin-bottom: 1rem;
  }
  
  button {
    margin: 0.5rem;
    padding: 0.5rem 1rem;
    cursor: pointer;
  }
  </style>
