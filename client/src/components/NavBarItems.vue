<template>
    <nav :class="[additionalClass]">
        <StyledRouterLink text="HOME" path="/" />
        <StyledRouterLink v-if="currentUserIsAuthenticated && currentUserHasAdminRole" text="ADMIN" path="/admin" />
        <StyledRouterLink v-if="currentUserIsAuthenticated" @click="submitLogout" text="LOGOUT" path="/" />
        <StyledRouterLink v-else text="LOGIN" path="/login" />
        <StyledRouterLink text="BLOGS" path="/blogs" />
    </nav>
</template>
  
<script lang="ts">
import { computed, defineComponent } from 'vue';
import StyledRouterLink from './StyledRouterLink.vue';
import { useAuthorizationStore } from '@/stores/authorizationStore';
import { useAuthenticationStore } from '@/stores/authenticationStore';

export default defineComponent({
    props: {
        additionalClass: {
            Type: String,
            required: false
        },
    },
    setup() {

        const authorizationStore = useAuthorizationStore();
        const authenticationStore = useAuthenticationStore();
        const currentUserIsAuthenticated = computed(() => authenticationStore.states.isAuthenticated);
        const currentUserHasAdminRole = computed(() => authenticationStore.states.hasAdminRole);

        function submitLogout() {
            authorizationStore.API.submitLogout()
        }

        return { currentUserIsAuthenticated, currentUserHasAdminRole, submitLogout }
    },

    components: { StyledRouterLink }
},
)
</script>