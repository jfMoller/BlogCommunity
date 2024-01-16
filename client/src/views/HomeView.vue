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

    onMounted(() => {
      const urlSearchParams = new URLSearchParams(window.location.search);
      const code = urlSearchParams.get('code');

      if (code) {
        connectionStore.API.submitGoogleLogin(code as string);
      }
    })

    return {}

  },
  components: {
  },
})
</script>