<template>
  <div class="max-w-2xl mx-auto my-8">
    <div class="flex justify-between items-center">
      <p class="text-2xl font-bold">Blogs</p>
      <button v-if="isCurrentUserAuthenticated" @click="openNewBlogFoldout"
        class="bg-blue-800 text-white px-4 py-2 rounded-md hover:bg-blue-900">
        Create New Blog
      </button>
      <NewBlogFoldout :isOpen="isNewBlogFoldoutOpen" :onClose="closeNewBlogFoldout" header="New Blog" />
    </div>

    <div v-if="loading" class="text-gray-600">Loading...</div>

    <div v-else>
      <div v-for="blog in blogs" :key="blog.id"
        class="my-4 pb-4 border border-gray-700 hover:border-gray-500 bg-gray-900 text-white">
        <div class="flex space-x-5 justify-end items-center border bg-gray-800 border-gray-700 py-1 px-5">
          <p class="font-bold">{{ blog.author }}</p>
          <p>{{ blog.timeStamp.split('T')[0] + " - " + blog.timeStamp.split('T')[1].slice(0, 5) }}</p>
        </div>
        <div class="cursor-pointer p-5" @click="showBlogView(blog.id)">
          <h2 class="text-xl font-bold mb-2">{{ blog.title }}</h2>
          <p>{{ (blog.text.length > 30) ? blog.text.slice(0, 30) + "..." : blog.text }}</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, ref, onMounted, watch, computed } from 'vue';
import { useBlogsStore } from '@/stores/blogsStore';
import type { Blog } from '@/stores/blogsStore';
import { useRoute, useRouter } from 'vue-router';
import { useAuthenticationStore } from '@/stores/authenticationStore';
import NewBlogFoldout from '@/components/NewBlogFoldout.vue';



export default defineComponent({
  name: 'BlogsView',

  setup() {
    const blogsStore = useBlogsStore();
    const blogs = ref<Blog[]>([]);
    const isCurrentUserAuthenticated = computed(() => useAuthenticationStore().states.isAuthenticated);
    const isNewBlogFoldoutOpen = computed(() => {
      return route.fullPath === "/blogs/new";
    });

    function openNewBlogFoldout() {
      router.push({ name: "ShowNewBlogFoldout" })
    }

    function closeNewBlogFoldout() {
      router.push({ name: "BlogsView" })
    }

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
      }
      else {
        handleBlogSearch(search, filter);
      }
    });

    watch(() => ({
      search: route.query.search as string,
      filter: route.query.filter as string,
    }), (newQuery) => {
      const { search, filter } = newQuery;
      handleBlogSearch(search, filter);
    });

    async function getAllBlogs() {
      try {
        blogs.value = await blogsStore.API.getAllBlogs();
      }
      catch (error) {
        console.error('Error fetching blogs:', error);
      }
      finally {
        loading.value = false;
      }
    }

    async function handleBlogSearch(search: string, filter: string) {
      if (isEmpty(search) && hasNoFilter(filter)) {
        getAllBlogs();
      }
      else {
        getSearchedBlogs(search, filter);
      }
    }


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

    return { blogs, isCurrentUserAuthenticated, isNewBlogFoldoutOpen, openNewBlogFoldout, closeNewBlogFoldout, loading, showBlogView };
  },
  components: { NewBlogFoldout }
});
</script>
