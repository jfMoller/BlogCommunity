<template>
  <section>
    <p class="font-bold">Welcome to Blog Community, a graded full stack project.</p>
    <p>This is the placeholder home page</p>
    <PrivacyPolicyPopup header="Privacy Policy" :isOpen='isPopupOpen == true' :onClose="closePrivacyPopup" />
  </section>
</template>

<script lang="ts">
import PrivacyPolicyPopup from '@/components/PrivacyPolicyPopup.vue';
import { defineComponent, ref } from 'vue';

export default defineComponent({
  name: "HomeView",
  setup() {
    const isPopupOpen = ref<Boolean>(true);

    function closePrivacyPopup() {
      isPopupOpen.value = false;
    }

    return { isPopupOpen, closePrivacyPopup }
  },
  components: { PrivacyPolicyPopup }
})
</script>