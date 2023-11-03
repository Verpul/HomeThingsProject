<template>
  <v-app>
    <MainMenu/>
    <v-main>
      <v-container fluid>
        <v-card width="800" class="mx-auto" outlined>
          <div class="ma-2 text-center" v-if="!authorized">
            <v-btn :href="getTwitchAuthLink()" small outlined color="purple">Получить токен
            </v-btn>
          </div>
          <template>
            <v-simple-table>
              <thead>
              <tr>
                <th>Streamer</th>
                <th>Уведомление о начале трансляции</th>
                <th>Уведомление о смене категории</th>
                <th>
                  <v-menu bottom offset-y>
                    <template v-slot:activator="{ on, attrs }">
                      <v-btn density="compact" class="ms-3" icon
                             v-bind="attrs"
                             v-on="on">
                        <v-icon>mdi mdi-cog-outline</v-icon>
                      </v-btn>
                    </template>
                    <v-list>
                      <v-list-item
                          link
                          @click="loadFollowedChannels()"
                      >
                        <v-list-item-title class="text-body-2">Загрузить отслеживаемые каналы</v-list-item-title>
                      </v-list-item>
                    </v-list>
                  </v-menu>
                </th>
              </tr>
              </thead>
              <tbody>
              <tr v-for="channel in savedChannels" :key="channel.channelId">
                <td>{{ channel.channelName }}</td>
                <td>
                  <div class="d-flex justify-center">
                    <v-checkbox dense @click="setChannelPreferences(channel)" v-model="wentLiveNotificationCheckbox" :value="channel.channelId"></v-checkbox>
                  </div>
                </td>
                <td>
                  <div class="d-flex justify-center">
                    <v-checkbox dense @click="setChannelPreferences(channel)" v-model="changedCategoryNotificationCheckbox" :value="channel.channelId"></v-checkbox>
                  </div>
                </td>
                <td></td>
              </tr>
              <tr class="text-center">
                <td colspan="5" v-if="followedChannels.length > 0">Отслеживаемые каналы</td>
              </tr>
              <tr v-for="(channel, index) in followedChannels" :key="index">
                <td>{{ channel.channelName }}</td>
                <td>
                  <div class="d-flex justify-center">
                    <v-checkbox dense @click="setChannelPreferences(channel)" v-model="wentLiveNotificationCheckbox" :value="channel.channelId"></v-checkbox>
                  </div>
                </td>
                <td>
                  <div class="d-flex justify-center">
                    <v-checkbox dense @click="setChannelPreferences(channel)" v-model="changedCategoryNotificationCheckbox" :value="channel.channelId"></v-checkbox>
                  </div>
                </td>
                <td></td>
              </tr>
              </tbody>
            </v-simple-table>
          </template>
        </v-card>
      </v-container>
    </v-main>
  </v-app>
</template>

<script>
export default {
  name: "TwitchComponent",
  data: () => ({
    wentLiveNotificationCheckbox: [],
    changedCategoryNotificationCheckbox: []
  }),
  methods: {
    getTwitchAuthLink() {
      const twitchBaseLink = "https://id.twitch.tv/oauth2/authorize?";
      const twitchResponseType = "response_type=code";
      const twitchClientId = "&client_id=ebrxut4iey9tyh8ifxc86o4msf8t1p";
      const twitchRedirectUrl = "&redirect_uri=http://localhost:8080/twitch";
      const twitchScope = "&scope=user:read:follows";

      return twitchBaseLink + twitchResponseType + twitchClientId + twitchRedirectUrl + twitchScope;
    },
    loadFollowedChannels() {
      this.$store.dispatch('loadFollowedChannels');
    },
    setChannelPreferences(channel) {
      this.$store.dispatch("setChannelPreferences", {
        channelId: channel.channelId,
        channelName: channel.channelName,
        wentLiveNotification: !!this.wentLiveNotificationCheckbox.includes(channel.channelId),
        changedCategoryNotification: !!this.changedCategoryNotificationCheckbox.includes(channel.channelId)
      })
    },
    setChannelsPreferences(channels) {
      this.wentLiveNotificationCheckbox = channels.map(ch => {
        if (ch.wentLiveNotification) return ch.channelId;
      });

      this.changedCategoryNotificationCheckbox = channels.map(ch =>
      {
        if (ch.changedCategoryNotification) return ch.channelId;
      });
    }
  },
  computed: {
    authorized() {
      return this.$store.getters.twitchAuthorized;
    },
    followedChannels() {
      return this.$store.getters.followedChannels;
    },
    savedChannels() {
      let channels = this.$store.getters.savedChannels;
      this.setChannelsPreferences(channels);
      return channels;
    }
  },
  created() {
    this.$store.dispatch("loadSavedTwitchChannels");

    const action = "code" in this.$route.query ? "getNewToken" : "isAuthorized";
    this.$store.dispatch(action, this.$route.query.code);
    if (action === 'getNewToken') this.$router.push({query: {}});
  }
}
</script>
