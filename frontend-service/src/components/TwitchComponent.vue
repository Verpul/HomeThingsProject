<template>
  <v-app>
    <MainMenu/>
    <v-main>
      <v-container fluid>
        <v-card width="800" class="mx-auto" flat>
          <v-btn :href="getTwitchAuthLink()" small outlined color="purple" v-if="!authorized">Авторизация на Twitch
          </v-btn>
        </v-card>
      </v-container>
    </v-main>
  </v-app>
</template>

<script>
export default {
  name: "TwitchComponent",
  data: () => ({}),
  methods: {
    getTwitchAuthLink() {
      const twitchBaseLink = "https://id.twitch.tv/oauth2/authorize?";
      const twitchResponseType = "response_type=code";
      const twitchClientId = "&client_id=ebrxut4iey9tyh8ifxc86o4msf8t1p";
      const twitchRedirectUrl = "&redirect_uri=http://localhost:8080/twitch";
      const twitchScope = "&scope=user:read:follows";

      return twitchBaseLink + twitchResponseType + twitchClientId + twitchRedirectUrl + twitchScope;
    }
  },
  computed: {
    authorized() {
      return this.$store.getters.twitchAuthorized;
    }
  },
  created() {
    const action = "code" in this.$route.query ? "getNewToken" : "isAuthorized";

    this.$store.dispatch(action, this.$route.query.code);

    if (action === 'getNewToken') this.$router.push({query: {}});
  }
}
</script>
