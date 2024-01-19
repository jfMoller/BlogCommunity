<template>
    <div v-if="props.isOpen"
        class="w-screen sm:w-max sm:min-w-[30rem] bg-gray-900 border-l border-gray-700 fixed h-screen top-[5.25rem] right-0 p-5 shadow-md">
        <div class="flex justify-between items-center">
            <h2 class="text-lg font-bold">{{ props.header }}</h2>
            <div @click="props.onClose" class="fas fa-close text-center font-semibold px-3 rounded-md cursor-pointer">X
            </div>
        </div>
        <div class="border border-t-1 border-gray-700 w-full my-2"></div>
        <div>
            <BlogPublisher />
        </div>
    </div>
</template>
  
<script lang="ts">
import { defineComponent } from 'vue';
import BlogPublisher from './BlogPublisher.vue';

export default defineComponent({
    name: 'NewBlogFoldout',
    props: {
        header: {
            type: String,
            required: true,
        },

        isOpen: {
            type: Boolean,
            required: true,
            default: true
        },

        onClose: {
            type: Function,
            required: true,
        },
    },
    setup(props) {

        return { props };
    },
    components: { BlogPublisher }
});
</script>
  