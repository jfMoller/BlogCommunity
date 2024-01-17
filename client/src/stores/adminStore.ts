import { defineStore } from "pinia";
import { callDelete } from "./requests";

export const useAdminStore = defineStore("adminStore", () => {

  const API = {

    deleteAllBlogs: async () => await callDelete("/admin/blogs/delete/all"),

    deleteBlog: async (blogId: string) => await callDelete(`/admin/blogs/delete/${blogId}`),

  };

  return {
    API,
  };
});
