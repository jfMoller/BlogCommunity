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
    submitLogin: async (
      username: string, 
      password: string
      ): Promise<any> => {
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

    getGoogleLogin: async (): Promise<any> => {
      const response: LoginResponseSuccess | ResponseError = await callGet(
        "/google/login"
      );

      console.log(response);
      useAuthenticationStore().methods.handleAuthentication(
        response as LoginResponseSuccess
      );

      return response;
    },

    submitLogout: async () =>
      useAuthenticationStore().methods.handleRevokeAuthentication(),
  };

  return {
    API,
  };
});
