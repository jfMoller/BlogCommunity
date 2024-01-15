<template>
  <header
    class="bg-gray-900 border border-gray-600 w-screen flex items-center justify-center space-x-6 p-5">
    <NavBarItems additionalClass="flex space-x-6 justify-center items-center" />
  </header>
</template>
  
<script lang="ts">
import { defineComponent } from 'vue';
import NavBarItems from './NavBarItems.vue';


export default defineComponent({
  name: "NavBar",

  setup() {


    return {}

  },
  components: {
    NavBarItems
  },
})
</script>