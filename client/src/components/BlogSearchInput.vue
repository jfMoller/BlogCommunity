<template>
    <div class="flex flex-col justify-center items-center relative text-white" @mouseover="showDropdown"
        @mouseleave="hideDropdown" @keyup.enter="handleSearch">
        <div class="flex justify-center items-center bg-gray-700 border border-gray-700 px-3 lg:w-[28rem]">
            <input type="text" v-model="search" class="bg-gray-800 px-4 py-2 w-full focus:aria-black focus:outline-none"
                placeholder="Search our blogs" />
        </div>
        <div v-if="isOpenDropdown"
            class=" bg-gray-800 w-full lg:w-[28rem] transition duration-400 
            rounded-sm min-h-max shadow-md border border-gray-700 absolute top-[2.6rem] 
            lg:top-[2.63rem] flex flex-col p-4 space-y-2">
            <h3 class="text-base font-semibold">SORT BY</h3>
            <div class="flex flex-col text-base items-start justify-center space-y-1">
                <label>
                    <input type="checkbox" :checked="filters.newest" @change="() => handleFilterChange('newest')"
                        class="form-checkbox h-4 w-4" />
                    Newest
                </label>
                <label>
                    <input type="checkbox" :checked="filters.oldest" @change="() => handleFilterChange('oldest')"
                        class="form-checkbox h-4 w-4" />
                    Oldest
                </label>
            </div>
        </div>
    </div>
</template>
  
<script lang="ts">
import { defineComponent, ref, reactive, onMounted, watch } from 'vue';
import { useRoute, useRouter } from 'vue-router';


export default defineComponent({
    name: 'BlogSearchInput',

    setup(props) {
        const search = ref<string>('');
        const isOpenDropdown = ref<boolean>(false);
        const filters = reactive({
            newest: false,
            oldest: false,
        });
        const router = useRouter();
        const route = useRoute();

        watch(
            () => ({
                activeSearch: route.query.search as string,
                activeFilter: route.query.filter as string,
            }),
            (newQuery) => {
                const { activeSearch, activeFilter } = newQuery;

                search.value = activeSearch;

                if (activeFilter == "newest") {
                    filters.newest = true;

                } else if (activeFilter == "oldest") {
                    filters.oldest = true;
                }
            },
        );

        function showDropdown() {
            isOpenDropdown.value = true;
        }

        function hideDropdown() {
            isOpenDropdown.value = false;
        }

        function handleSearch() {
            const queryParameters = {
                search: search.value,
                filter: filters.newest ? 'newest' : filters.oldest ? 'oldest' : '',
            };
            router.push({ name: 'BlogsView', query: queryParameters });
        }

        function handleFilterChange(targetFilter: string) {
            if (targetFilter === "newest") {
                filters.newest = !filters.newest;
                filters.oldest = false;
            }

            if (targetFilter === "oldest") {
                filters.oldest = !filters.oldest;
                filters.newest = false;
            }

        }

        return { props, showDropdown, hideDropdown, search, isOpenDropdown, filters, handleSearch, handleFilterChange };
    },
});
</script>
  