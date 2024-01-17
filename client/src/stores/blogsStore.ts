import { defineStore } from "pinia";
import { callGet, callPost } from "./requests";

export interface Blog {
  id: string;
  title: string;
  text: string;
  timeStamp: string;
  author: string;
}

export interface PublishBlogDto {
  title: string;
  text: string;
}

export const useBlogsStore = defineStore("blogsStore", () => {
  
  const API = {

    getAllBlogs: async (): Promise<Blog[]> => await callGet("/blogs/all"),

    getSearchedBlogs: async (search: string, filter: string): Promise<Blog[]> =>
      await callGet(`/products/search?query=${search}&filter=${filter}`),

    getBlog: async (blogId: string): Promise<Blog> =>
      await callGet(`/blogs/${blogId}`),

    publishBlog: async (dto: PublishBlogDto) =>
      await callPost("/blogs/publish", dto),

  };

  return {
    API,
  };
});
