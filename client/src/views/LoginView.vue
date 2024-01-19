<template>
  <section class="flex justify-center items-center">
    <div class="border border-gray-600 bg-gray-900 p-8 rounded shadow-lg w-[30rem]">
      <h2 class="text-2xl mb-4">Login</h2>
      <form @submit.prevent="handleLogin">
        <div class="mb-4">
          <label for="username" class="block text-sm font-bold mb-2">Username</label>
          <input v-model="username" type="text" id="username"
            class="w-full px-3 py-2 border rounded border-gray-700 bg-gray-800 focus:outline-none" required />
        </div>
        <div class="mb-6">
          <label for="password" class="block text-sm font-bold mb-2">Password</label>
          <input v-model="password" type="password" id="password"
            class="w-full px-3 py-2 border rounded border-gray-700 bg-gray-800 focus:outline-none" required />
        </div>
        <button type="submit"
          class="w-full bg-black border border-gray-700 hover:bg-gray-800 hover:border-gray-600 transition duration-9000 text-white font-bold py-2 px-4 rounded focus:outline-none mb-4">
          Login</button>
      </form>
      <button @click="handleGoogleLogin"
        class="w-full bg-blue-500 border border-gray-700 hover:bg-blue-600 hover:border-gray-600 transition duration-9000 text-white font-bold py-2 px-4 rounded focus:outline-none">
        Login with Google
      </button>
    </div>
  </section>
</template>

<script lang="ts">
import { defineComponent, ref } from 'vue';
import StyledRouterLink from '@/components/StyledRouterLink.vue';
import { useAuthorizationStore } from '@/stores/authorizationStore'

export default defineComponent({
  setup() {
    const authorizationStore = useAuthorizationStore();
    const username = ref('');
    const password = ref('');

    async function handleLogin() {
      await authorizationStore.API.submitLogin(username.value, password.value);
    }

    async function handleGoogleLogin() {
      await authorizationStore.API.getGoogleAuthUrl();
    }

    return { username, password, handleLogin, handleGoogleLogin };
  },

  components: { StyledRouterLink }
})
</script>