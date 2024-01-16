import { defineStore } from "pinia";
import { callGet, callPost } from "./requests";
import { useAuthenticationStore } from "./authenticationStore";

export interface LoginResponseSuccess {
  isSuccessful: boolean;
  userRole: string;
  jwtToken: string;
}

export interface ResponseError {
  error: boolean;
  message: string;
}

export const useConnectionStore = defineStore("connectionStore", () => {
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
      // Fetch the URL from the server
      const response = await callGet("/auth/url");

      if (response.url) {
        window.location.href = response.url;
      }
    },

    submitGoogleLogin: async (code: string) => {
      const response = await callGet(`/auth/callback?code=${code}`);

      console.log(response);
    },

    submitLogout: async () =>
      useAuthenticationStore().methods.handleRevokeAuthentication(),
  };

  return {
    API,
  };
});
