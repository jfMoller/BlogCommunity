<template>
  <div class="max-w-2xl mx-auto my-8">
    <p class="text-2xl font-bold mb-4">Blogs</p>
    <div v-if="loading" class="text-gray-600">Loading...</div>

    <div v-else>
      <div v-for="blog in blogs" :key="blog.id" class="my-4 border-b pb-4">
        <div class="cursor-pointer hover:bg-gray-900 p-5" @click="showBlogView(blog.id)">
          <h2 class="text-xl font-bold mb-2">{{ blog.title }}</h2>
          <p class="text-gray-700">{{ blog.text }}</p>
          <p class="text-gray-600 mt-2">Author: {{ blog.author }}</p>
          <p class="text-gray-600">Published on: {{ blog.timeStamp }}</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, ref, onMounted, watch } from 'vue';
import { useBlogsStore } from '@/stores/blogsStore';
import type { Blog } from '@/stores/blogsStore';
import { useRoute, useRouter } from 'vue-router';

export default defineComponent({
  name: 'BlogsView',

  setup() {
    const blogsStore = useBlogsStore();
    const blogs = ref<Blog[]>([]);
    const loading = ref(false);

    const router = useRouter();
    const route = useRoute();

    onMounted(async () => {
      const search = route.query.search as string;
      const filter = route.query.filter as string;
      const hasNoSearchQuery = search == '';
      const hasNoFilter = filter == '';

      if (hasNoSearchQuery && hasNoFilter) {
        getAllBlogs();
      } else {
        handleBlogSearch(search, filter);
      }
    });

    watch(
      () => ({
        search: route.query.search as string,
        filter: route.query.filter as string,
      }),
      (newQuery) => {
        const { search, filter } = newQuery;
        handleBlogSearch(search, filter);
      },
    );

    async function getAllBlogs() {
      try {
        blogs.value = await blogsStore.API.getAllBlogs();
      } catch (error) {
        console.error('Error fetching blogs:', error);
      } finally {
        loading.value = false;
      }
    }

    async function handleBlogSearch(search: string, filter: string) {
      if (isEmpty(search) && hasNoFilter(filter)) {
        getAllBlogs();
      } else {
        getSearchedBlogs(search, filter);
      }
    };

    function isEmpty(query: string): boolean {
      return query === '';
    }

    function hasNoFilter(filter: any): boolean {
      return filter === '';
    }

    async function getSearchedBlogs(query: string, filter: string) {
      blogs.value = await blogsStore.API.getSearchedBlogs(query, filter);
    }

    function showBlogView(blogId: string) {
      router.push({ name: 'BlogView', params: { blogId: blogId } });
    }

    return { blogs, loading, showBlogView };
  },
});
</script>
