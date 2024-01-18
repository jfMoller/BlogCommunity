<template>
    <div class="max-w-md mx-auto mt-8 p-5 bg-gary-900 border border-gray-700">
        <form @submit.prevent="publishBlog">
            <div class="mb-4">
                <label for="title" class="block text-l font-semibold text-white">Title</label>
                <input v-model="title" placeholder="Enter your title" type="text" id="title"
                    class="mt-1 p-2 w-full border border-gray-700 bg-gray-800 focus:outline-none" required />
            </div>

            <div class="mb-4">
                <label for="title" class="block text-l font-semibold text-white">Text</label>
                <textarea v-model="text" placeholder="Enter your text" id="text"
                    class="mt-1 p-2 w-full border border-gray-700 bg-gray-800 focus:outline-none" required></textarea>
            </div>
            <div class="flex justify-end">
                <button type="submit" class="bg-green-800 text-white px-4 py-2 rounded-md hover:bg-green-900">
                    Publish
                </button>
            </div>
        </form>
    </div>
</template>
  
<script lang="ts">
import { defineComponent, ref } from 'vue';
import { useBlogsStore } from '@/stores/blogsStore';
import type { PublishBlogDto } from '@/stores/blogsStore';
import { useRouter } from 'vue-router';

export default defineComponent({
    name: 'BlogPublisher',

    setup() {
        const title = ref('Test Title');
        const text = ref('Lorem ipsum dolor sit amet, consectetur adipiscing elit. Maecenas feugiat varius tortor et ultricies. Nunc rutrum arcu nibh, ac euismod massa hendrerit ac. Ut ut urna tincidunt, scelerisque orci vel, convallis erat. Vivamus lacinia nulla in dolor tempus, eu sollicitudin libero euismod. Fusce pellentesque quam ac urna feugiat gravida ut in est. Donec varius sapien quis dignissim convallis. Praesent at turpis facilisis ligula aliquam tempor. Sed vel malesuada nisi, nec tempus neque. Aliquam non egestas purus. Morbi porttitor ante sagittis felis faucibus, vel convallis sapien sodales. Nunc at imperdiet urna. Vivamus id lacus quis diam semper vulputate. Ut vestibulum pretium metus, sed aliquet erat tempus id. Donec aliquet sit amet diam nec egestas. ');
        const blogsStore = useBlogsStore();
        const router = useRouter();

        const publishBlog = async () => {
            const dto: PublishBlogDto = {
                title: title.value,
                text: text.value,
            };

            await blogsStore.API.publishBlog(dto);

            router.push({ name: "BlogsView" })
        };

        return { title, text, publishBlog };
    },
});
</script>
  