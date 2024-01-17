<template>
  <section>
    <div v-if="blog" class="flex justify-center">
      <div class="cursor-pointer p-5 border border-gray-700">
        <h2 class="text-xl font-bold mb-2">{{ blog.title }}</h2>
        <p class="text-gray-700">{{ blog.text }}</p>
        <p class="text-gray-600 mt-2">Author: {{ blog.author }}</p>
        <p class="text-gray-600">Published on: {{ blog.timeStamp }}</p>
      </div>
    </div>
  </section>
</template>
  
<script lang="ts">
import { defineComponent, onMounted, ref } from 'vue';
import { useRoute } from 'vue-router'
import { useBlogsStore } from '@/stores/blogsStore';
import type { Blog } from '@/stores/blogsStore';

export default defineComponent({
  name: "blogView",
  setup() {
    const route = useRoute();
    const blogsStore = useBlogsStore();
    const blog = ref<Blog | null>(null);

    onMounted(async () => {
      blog.value = await blogsStore.API.getBlog(route.params.blogId as string);
    })

    return {
      blog
    };
  },
  components: {
  }

});
</script>