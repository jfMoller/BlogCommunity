<template>
    <section>
        <div class="max-w-2xl mx-auto my-3 bg-gray-900 p-5 border border-gray-700">
            <div class="flex justify-between items-center mb-5">
                <p class="text-2xl font-bold">Manage Blogs</p>
                <button class="bg-red-800 px-3 py-1 rounded-md font-emibold hover:bg-red-900"
                    @click="() => handleDeleteAllBlogs()">Delete All
                    Blogs</button>
            </div>
            <div v-if="loading" class="text-gray-600">Loading...</div>
            <div v-else>
                <table class="min-w-full">
                    <thead class="text-left border border-gray-700 bg-gray-900">
                        <tr>
                            <th class="p-2 border border-gray-700">Title</th>
                            <th class="p-2 border border-gray-700">Text</th>
                            <th class="p-2 border border-gray-700">Author</th>
                            <th class="p-2 border border-gray-700">Published</th>
                            <th class="p-2 border border-gray-700">Actions</th>
                        </tr>
                    </thead>
                    <tbody class="text-left border border-gray-700 whitespace-normal">
                        <tr v-for="blog in blogs" :key="blog.id" class="hover:bg-gray-800 cursor-pointer">
                            <td class="p-2 border border-gray-700">{{ blog.title }}</td>
                            <td class="p-2 border border-gray-700">{{ (blog.text.length > 10) ? blog.text.slice(0, 9) +
                                "..." : blog.text }}</td>
                            <td class="p-2 border border-gray-700">{{ blog.author }}</td>
                            <td class="p-2 border border-gray-700">{{ blog.timeStamp.split('T')[0] + " - " +
                                blog.timeStamp.split('T')[1].slice(0, 5) }}</td>
                            <td class="p-2 border border-gray-700">
                                <button class="text-red-500 hover:underline" @click="handleDeleteBlog(blog.id)">
                                    Delete
                                </button>
                            </td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </section>
</template>
  
<script lang="ts">
import { defineComponent, ref, onMounted } from 'vue';
import { useBlogsStore } from '@/stores/blogsStore';
import type { Blog } from '@/stores/blogsStore';
import { useAdminStore } from '@/stores/adminStore';

export default defineComponent({
    name: 'BlogsView',

    setup() {
        const blogsStore = useBlogsStore();
        const blogs = ref<Blog[]>([]);
        const loading = ref(true);
        const adminStore = useAdminStore();

        async function loadAllBlogs() {
            try {
                blogs.value = await blogsStore.API.getAllBlogs();
            } catch (error) {
                console.error('Error fetching blogs:', error);
            } finally {
                loading.value = false;
            }
        }

        onMounted(async () => {
            loadAllBlogs();
        });

        async function handleDeleteAllBlogs() {
            await adminStore.API.deleteAllBlogs();
            loadAllBlogs();
        }

        async function handleDeleteBlog(blogId: string) {
            await adminStore.API.deleteBlog(blogId);
            loadAllBlogs();
        }

        return { blogs, loading, handleDeleteAllBlogs, handleDeleteBlog };
    },
});
</script>
  