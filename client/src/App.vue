<template>
  <NavBar />
  <RouterView class="w-screen max-h-screen text-white p-5" />
</template>

<script lang="ts">
import { defineComponent } from 'vue';
import NavBar from './components/NavBar.vue';

export default defineComponent({
  setup() {

    return {}
  },

  components: {
    NavBar
  },
})
</script>

<style>
html {
  background-color: black;
  font-family: system-ui;
}
</style>