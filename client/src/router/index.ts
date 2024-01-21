import { createRouter, createWebHistory } from "vue-router";
import HomeView from "../views/HomeView.vue";
import AdminView from "../views/AdminView.vue";
import LoginView from "../views/LoginView.vue";
import BlogsView from "../views/BlogsView.vue";
import BlogView from "../views/BlogView.vue";
import VunerabilityDisclosurePolicyView from "@/views/VunerabilityDisclosurePolicyView.vue";
import { useAuthenticationStore } from "@/stores/authenticationStore";

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: "/",
      name: "HomeView",
      component: HomeView,
    },
    {
      path: "/admin",
      name: "AdminView",
      component: AdminView,
      beforeEnter: (to, from, next) => {
        if (useAuthenticationStore().states.isAuthenticated) {
          next();
        } else {
          next("/login");
        }
      },
    },
    {
      path: "/login",
      name: "LoginView",
      component: LoginView,
    },
    {
      path: "/blogs",
      name: "BlogsView",
      component: BlogsView,
      props: (route) => ({
        search: route.query.search || "",
        filter: route.query.filter || "",
      }),
      children: [
        { path: "new", name: "ShowNewBlogFoldout", component: BlogsView },
      ],
    },
    {
      path: "/blogs/:blogId",
      name: "BlogView",
      component: BlogView,
    },
    {
      path: "/vdp",
      name: "VunerabilityDisclosurePolicyView",
      component: VunerabilityDisclosurePolicyView,
    },
  ],
});

export default router;
