<template>
  <p>Home</p>
</template>

<script lang="ts">

import { useConnectionStore } from '@/stores/connectionStore';
import { defineComponent, onMounted } from 'vue';


export default defineComponent({
  name: "HomeView",

  setup() {
    const connectionStore = useConnectionStore();


    return {}

  },
  components: {
  },
})
</script>