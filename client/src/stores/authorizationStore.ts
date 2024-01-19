import { defineStore } from "pinia";
import { callGet, callPost } from "./requests";
import { useAuthenticationStore } from "./authenticationStore";
import { onMounted } from "vue";

export interface LoginResponseSuccess {
  isSuccessful: boolean;
  userRole: string;
  jwtToken: string;
}

export interface ResponseError {
  error: boolean;
  message: string;
}

export const useAuthorizationStore = defineStore("authorizationStore", () => {
  const API = {
    submitLogin: async (username: string, password: string): Promise<any> => {
      const response: LoginResponseSuccess | ResponseError = await callPost(
        "/login",
        {
          username: username,
          password: password,
        }
      );

      useAuthenticationStore().methods.handleAuthentication(
        response as LoginResponseSuccess
      );

      return response;
    },

    getGoogleAuthUrl: async () => {
      const response = await callGet("/google/auth/url");

      if (response.url) {
        window.location.href = response.url;
      }
    },

    submitGoogleLogin: async (code: string): Promise<any> => {
      const response: LoginResponseSuccess | ResponseError = await callGet(
        `/google/auth/callback?code=${code}`
      );

      useAuthenticationStore().methods.handleAuthentication(
        response as LoginResponseSuccess
      );

      return response;
    },

    submitLogout: async () =>
      useAuthenticationStore().methods.handleRevokeAuthentication(),
  };

  onMounted(() => {
    const urlSearchParams = new URLSearchParams(window.location.search);
    const code = urlSearchParams.get("code");

    if (code) {
      API.submitGoogleLogin(code as string);
    }
  });

  return {
    API,
  };
});
