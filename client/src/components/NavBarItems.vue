<template>
    <nav :class="[additionalClass]">
        <StyledRouterLink text="HOME" path="/" />
        <StyledRouterLink text="ADMIN" path="/admin" />
        <StyledRouterLink text="LOGIN" path="/login" />
        <StyledRouterLink text="BLOGS" path="/blogs" />
    </nav>
</template>
  
<script lang="ts">
import { defineComponent } from 'vue';
import StyledRouterLink from './StyledRouterLink.vue';

export default defineComponent({
    props: {
        additionalClass: {
            Type: String,
            required: false
        },
    },
    setup(props) {

        return {}
    },

    components: { StyledRouterLink }
    },
)
</script>