<template>
  <v-app>
    <MainMenu/>
    <v-main>
      <v-container fluid>
        <v-card width="800" class="mx-auto" outlined>
          <template>
            <v-simple-table>
              <thead>
              <tr>
                <th class="text-center" colspan="2">Канал</th>
                <th class="text-center">Уведомление о новых видео</th>
                <th class="text-center">
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
                <td class="py-2">
                  <v-img :src="formatURL(channel.thumbnailUrl)" max-width="60px" max-height="60px"></v-img>
                </td>
                <td>{{ channel.channelName }}</td>
                <td>
                  <div class="d-flex justify-center">
                    <v-checkbox dense @click="setChannelPreferences(channel)" v-model="checkNewVideosNotification"
                                :value="channel.channelId"></v-checkbox>
                  </div>
                </td>
                <td></td>
              </tr>
              <tr class="text-center">
                <td colspan="5" v-if="followedChannels.length > 0">Отслеживаемые каналы</td>
              </tr>
              <tr v-for="channel in followedChannels" :key="channel.channelName">
                <td class="py-2">
                  <v-img :src="formatURL(channel.thumbnailUrl)" max-width="60px" max-height="60px"></v-img>
                </td>
                <td><b>{{ channel.channelName }}</b></td>
                <td>
                  <div class="d-flex justify-center">
                    <v-checkbox dense @click="setChannelPreferences(channel)" v-model="checkNewVideosNotification"
                                :value="channel.channelId"></v-checkbox>
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
  name: "YoutubeComponent",
  data: () => ({
    checkNewVideosNotification: [],
  }),
  methods: {
    loadFollowedChannels() {
      this.$store.dispatch("loadYoutubeFollowedChannels");
    },
    setChannelPreferences(channel) {
      this.$store.dispatch("setYoutubeChannelPreferences", channel)
    },
    formatURL(originalURL) {
      return originalURL.replace("ggpht", "googleusercontent");
    },
    setChannelsPreferences(channels) {
      this.checkNewVideosNotification = channels.map(ch => {
        if (ch.checkNewVideo) return ch.channelId;
      });
    }
  },
  computed: {
    followedChannels() {
      return this.$store.getters.followedYoutubeChannels;
    },
    savedChannels() {
      let channels = this.$store.getters.savedYoutubeChannels;
      this.setChannelsPreferences(channels);
      return channels;
    }
  },
  created() {
    this.$store.dispatch("loadSavedYoutubeChannels");
  }
}
</script>
