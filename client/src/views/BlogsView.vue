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
import { defineComponent, ref, onMounted } from 'vue';
import { useBlogsStore } from '@/stores/blogsStore';
import type { Blog } from '@/stores/blogsStore';
import { useRouter } from 'vue-router';

export default defineComponent({
  name: 'BlogsView',

  setup() {
    const router = useRouter();
    const blogsStore = useBlogsStore();
    const blogs = ref<Blog[]>([]);
    const loading = ref(true);

    onMounted(async () => {
      try {
        blogs.value = await blogsStore.API.getAllBlogs();
      } catch (error) {
        console.error('Error fetching blogs:', error);
      } finally {
        loading.value = false;
      }
    });

    function showBlogView(blogId: string) {
      router.push({ name: 'BlogView', params: { blogId: blogId } });
    }

    return { blogs, loading, showBlogView };
  },
});
</script>
