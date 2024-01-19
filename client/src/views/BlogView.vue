<template>
  <section class="flex justify-center">
    <div v-if="blog" class="my-4 pb-4 border border-gray-700 bg-gray-900 text-white p-5 lg:min-w-[60%] lg:max-w-[60%]">
      <div class="flex justify-end items-center">
        <p>{{ blog.timeStamp.split('T')[0] + " - " + blog.timeStamp.split('T')[1].slice(0, 5) }}</p>
      </div>
      <h2 class="text-xl font-bold mb-5">{{ blog.title }}</h2>
      <p class="mb-5">{{ blog.text }}</p>
      <div class="flex justify-end items-center">
        <p>Written by <span class="font-bold">{{ blog.author }}</span></p>
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

});
</script>