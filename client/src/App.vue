<template>
  <NavBar />
  <RouterView class="p-5" />
</template>

<script lang="ts">
import { defineComponent } from 'vue';
import NavBar from './components/NavBar.vue';

export default defineComponent({

  components: {
    NavBar
  },

})
</script>

<style>
html {
  background-color: black;
  font-family: system-ui;
  color: whitesmoke;
}
</style>