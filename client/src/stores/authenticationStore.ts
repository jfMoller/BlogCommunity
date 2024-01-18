import { ref } from "vue";
import { defineStore } from "pinia";
import type { LoginResponseSuccess } from "./authorizationStore";
import { useRouter } from "vue-router";

export const useAuthenticationStore = defineStore("authenticationStore", () => {
  
  const router = useRouter();

  const states = {
    isAuthenticated: ref<boolean>(false),
    hasAdminRole: ref<boolean>(false),
  };

  const methods = {
    handleAuthentication: async (response: LoginResponseSuccess) => {
      if (response.isSuccessful && response.jwtToken) {
        storeJwtToken(response.jwtToken);
        states.isAuthenticated.value = true;

        if (response.userRole === "ADMIN") {
          states.hasAdminRole.value = true;
        }

        if (states.isAuthenticated.value) {
          router.push({ name: "BlogsView" });
        } else {
          router.push({ name: "LoginView" });
        }
      }
    },

    handleRevokeAuthentication: () => {
      clearJwtToken();
      revokeAuthentication();
    },

    getJwtToken: () => {
      return sessionStorage.getItem("jwtToken");
    },
  };

  function revokeAuthentication() {
    states.isAuthenticated.value = false;
  }

  function storeJwtToken(token: string) {
    sessionStorage.setItem("jwtToken", token);
  }

  function clearJwtToken() {
    sessionStorage.removeItem("jwtToken");
  }

  return {
    states,
    methods,
  };
});
