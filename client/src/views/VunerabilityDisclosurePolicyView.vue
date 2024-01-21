<template>
  <section class="space-y-3 p-4 pb-[7rem]">
    <h2 class="font-bold">Vulnerability Disclosure Policy</h2>
    <p>
      Thank you for helping to keep our community secure. If you discover any
      security vulnerabilities, we appreciate your responsible disclosure to
      protect our users and systems.
    </p>
    <h3 class="font-semibold">Reporting a Vulnerability</h3>
    <ul class="space-y-2  bg-gray-900 border border-gray-700 p-5 rounded">
      <li>
        <p>
          <strong>Email:</strong> Send a detailed email to
          <a class="font-bold" href="mailto:security@blogcommunity.se">
            security@blogcommunity.se
          </a>
          with the subject "Security Vulnerability Disclosure."
        </p>
      </li>
      <li>
        <p>
          <strong>Include Information:</strong> Provide a detailed description of
          the vulnerability, including steps to reproduce it, potential impact,
          and any relevant technical details.
        </p>
      </li>
      <li>
        <p>
          <strong>Do Not Share Publicly:</strong> Please do not disclose the
          vulnerability publicly until we have had an opportunity to review and
          address it.
        </p>
      </li>
    </ul>
    <h3 class="font-semibold">What We Commit</h3>
    <ul class="space-y-2  bg-gray-900 border border-gray-700 p-5 rounded">
      <li>We commit to acknowledging receipt of your report within 3 business days.</li>
      <li>
        We will investigate and, if needed, work with you to understand and
        resolve the issue quickly.
      </li>
      <li>
        We will maintain open communication with you throughout the resolution
        process.
      </li>
      <li>
        We will credit you for your responsible disclosure unless you prefer to
        remain anonymous.
      </li>
    </ul>
    <h3 class="font-semibold">Guidelines for Researchers</h3>
    <ul class="space-y-2  bg-gray-900 border border-gray-700 p-5 rounded">
      <li>
        Make every effort to avoid privacy violations, disruption to our systems,
        or unauthorized access to sensitive data.
      </li>
      <li>
        Do not disclose the vulnerability publicly until we have had an
        opportunity to address it.
      </li>
      <li>
        Provide a reasonable amount of time for us to resolve the issue before
        public disclosure.
      </li>
    </ul>

    <h3 class="font-semibold">Scope</h3>
    <p>This policy applies to vulnerabilities found in our website. Testing on other systems without explicit
      authorization is not allowed.</p>


    <h3 class="font-semibold">Legal Safe Harbor</h3>
    <p>
      We will not take legal action against security researchers who act in good
      faith and adhere to this policy.
    </p>


    <h3 class="font-semibold">Contact Us</h3>
    <p>If you have any questions or concerns regarding this policy, please contact us at <a
        href="mailto:info@blogcommunity.se">info@blogcommunity.se</a>.</p>

    <p>Thank you for helping us maintain a secure environment for our users.</p>
  </section>
</template>

<script lang="ts">

import { defineComponent } from 'vue';

export default defineComponent({
  name: "VunerabilityDiscolosurePolicyView",
  components: {}
})
</script>