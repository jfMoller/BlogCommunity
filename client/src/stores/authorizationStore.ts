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
      const code_verifier = window.crypto
        .getRandomValues(new Uint32Array(20))
        .join("");
      const code_challenge = await generateCodeChallenge(code_verifier);
      const code_challenge_method = "SHA-256";

      sessionStorage.setItem("codeVerifier", code_verifier);

      const url =
        "/google/auth/url" +
        "?code_challenge=" +
        code_challenge +
        "&code_challenge_method=" +
        code_challenge_method;

      const response = await callGet(url);

      if (response.url) {
        window.location.href = response.url;
      }
    },

    submitGoogleLogin: async (code: string): Promise<any> => {
      const response: LoginResponseSuccess | ResponseError = await callGet(
        "/google/auth/callback" +
          "?code=" +
          code +
          "&code_verifier=" +
          sessionStorage.getItem("codeVerifier")
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

  async function generateCodeChallenge(code_verifier: any) {
    const encoder = new TextEncoder();
    const data = encoder.encode(code_verifier);
    const digest = await window.crypto.subtle.digest("SHA-256", data);

    const digestArray = Array.from(new Uint8Array(digest));

    return btoa(String.fromCharCode.apply(null, digestArray))
      .replace(/\+/g, "-")
      .replace(/\//g, "_")
      .replace(/=+$/, "");
  }

  return {
    API,
  };
});
