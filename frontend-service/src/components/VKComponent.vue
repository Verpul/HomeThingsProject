<template>
  <v-app>
    <MainMenu/>
    <v-main>
      <v-container fluid>
        <v-card width="1100" class="mx-auto" outlined>
          <div class="ma-2 text-center" v-if="!haveToken">
            <span>Необходимо перейти по следующей ссылке -
              <a :href="getVKAuthLink()" target="_blank">click</a>
              и скопировать acess_token и user_id.</span><br>
            <span>Затем вручную добавить их в таблицу vk_token вашей БД.</span>
          </div>
          <v-card-actions>
            <v-row class="mt-2 justify-center" v-if="haveToken">
              <v-col cols="3">
                <v-menu
                    ref="dateMenu"
                    v-model="visibility.startDateMenu"
                    :close-on-content-click="true"
                    transition="scale-transition"
                    offset-y
                    max-width="290px"
                    min-width="auto"
                >
                  <template v-slot:activator="{ on, attrs }">
                    <v-text-field
                        v-model="news.startDate"
                        persistent-hint
                        v-bind="attrs"
                        v-on="on"
                        dense
                        class="custom-text-field-font-size"
                        clearable
                    >
                      <template v-slot:label>
                    <span class="custom-text-field-font-size">
                      Дата последней загрузки
                    </span>
                      </template>
                    </v-text-field>
                  </template>
                  <v-date-picker
                      v-model="date"
                      no-title
                      @input="visibility.startDateMenu = false"
                      locale="ru-ru"
                  ></v-date-picker>
                </v-menu>
              </v-col>
              <v-col cols="3">
                <v-menu
                    ref="timeMenu"
                    v-model="visibility.startTimeMenu"
                    :close-on-content-click="false"
                    :nudge-right="40"
                    :return-value.sync="news.startTime"
                    transition="scale-transition"
                    offset-y
                    max-width="290px"
                    min-width="290px"
                >
                  <template v-slot:activator="{ on, attrs }">
                    <v-text-field
                        v-model="news.startTime"
                        readonly
                        v-bind="attrs"
                        v-on="on"
                        dense
                        class="custom-text-field-font-size"
                    >
                      <template v-slot:label>
                            <span class="custom-text-field-font-size">
                              Время последней загрузки
                            </span>
                      </template>
                    </v-text-field>
                  </template>
                  <v-time-picker
                      v-if="visibility.startTimeMenu"
                      format="24hr"
                      v-model="news.startTime"
                      full-width
                      @click:minute="$refs.timeMenu.save(news.startTime)"
                  ></v-time-picker>
                </v-menu>
              </v-col>
              <v-col cols="2">
                <v-btn outlined color="primary" @click="loadNewsfeed">Загрузить</v-btn>
              </v-col>
              <v-btn icon color="purple" class="v-btn--absolute" style="right: 15px"
                     @click="newsFilterDialog = true">
                <v-icon>mdi mdi-filter-outline</v-icon>
              </v-btn>
              <v-btn icon color="red" class="v-btn--absolute mt-7" style="right: 15px" @click="switchShowHiddenPosts()">
                <v-icon small>{{ numOfHiddenField }}</v-icon>
              </v-btn>
            </v-row>
          </v-card-actions>
          <v-divider v-if="newsfeed.length > 0"></v-divider>
          <v-list>
            <template v-for="(post, index) in newsfeed">
              <v-list-item :key="post.text">
                <v-list-item-content>
                  <v-list-item-title> {{ post.groupName }}</v-list-item-title>
                  <v-list-item-action-text class="text-pre-wrap mt-2 text-body-2">{{
                      post.text
                    }}
                  </v-list-item-action-text>
                  <v-list-item-subtitle class="mt-2">
                    <v-icon>mdi mdi-clock-outline</v-icon>
                    {{ post.postDate }}
                    <v-icon class="ms-1">mdi mdi-comment-account-outline</v-icon>
                    <span class="ms-1">{{ post.numberOfComments }}</span>
                    <v-icon class="ms-2">mdi mdi-open-in-new</v-icon>
                    <a :href="getPostLink(post)" class="ms-1" target="_blank">Открыть новость в VK</a>
                  </v-list-item-subtitle>
                </v-list-item-content>
              </v-list-item>
              <v-divider :key="index" v-if="index < newsfeed.length - 1"></v-divider>
            </template>
          </v-list>
        </v-card>
      </v-container>
    </v-main>

    <v-dialog v-model="newsFilterDialog" max-width="600">
      <v-card>
        <v-card-title>Фильтрация постов по ключевым словам</v-card-title>
        <v-card-actions>
          <v-textarea auto-grow outlined label="Каждый фильтр на новой строке" v-model="filter"></v-textarea>
        </v-card-actions>
        <v-card-actions class="justify-end">
          <v-btn small outlined color="success" @click="saveFilter()">Сохранить</v-btn>
          <v-btn small outlined color="grey" @click="newsFilterDialog = false">Отмена</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </v-app>
</template>

<script>
export default {
  name: "VKComponent",
  data: () => ({
    date: null,
    newsFilterDialog: false,
    visibility: {
      startDateMenu: false,
      startTimeMenu: false
    },
    news: {
      startDate: null,
      startTime: null
    },
    numOfHiddenField: 0
  }),
  methods: {
    formatDate(date) {
      if (!date) return null

      const [year, month, day] = date.split('-')
      return `${day}.${month}.${year}`
    },
    getVKAuthLink() {
      const vkBaseLink = "https://oauth.vk.com/authorize?";
      const vkClientId = "client_id=" + process.env.VUE_APP_VK_CLIENT_ID;
      const vkRedirectUrl = "&redirect_uri=https://oauth.vk.com/blank.html";
      const vkResponseType = "&response_type=token";
      const vkScope = "&scope=offline,wall,friends";

      return vkBaseLink + vkClientId + vkRedirectUrl + vkResponseType + vkScope;
    },
    loadNewsfeed() {
      this.$store.dispatch('loadNewsfeed', {
        beginDate: this.news.startDate,
        beginTime: this.news.startTime
      });
    },
    getPostLink(post) {
      const baseLink = "https://vk.com/";
      const group = post.groupScreenName;
      const params = "?w=wall" + post.sourceId + "_" + post.postId;

      return baseLink + group + params;
    },
    saveFilter() {
      this.$store.dispatch("saveVKFilter");
      this.newsFilterDialog = false;
    },
    setNumOfHiddenPosts(num) {
      this.numOfHiddenField = num;
    },
    switchShowHiddenPosts() {
      this.$store.commit("switchShowHiddenPosts");
    }
  },
  watch: {
    date() {
      this.news.startDate = this.formatDate(this.date)
    },
  },
  computed: {
    haveToken() {
      return this.$store.getters.vkHaveToken;
    },
    newsfeed() {
      const posts = this.$store.getters.vkNewsfeed;
      if (!this.$store.getters.showHiddenVKPosts) {
        const filteredPosts = posts.filter(post => !post.hidden);
        this.setNumOfHiddenPosts(posts.length - filteredPosts.length);
        return filteredPosts;
      }

      this.setNumOfHiddenPosts(0);
      return posts;
    },
    filter: {
      get() {
        return this.$store.getters.vkNewsFilter;
      },
      set(value) {
        this.$store.commit('setNewsFilter', {data: value})
      }
    }
  },
  created() {
    this.$store.dispatch('haveTokenVK');

    this.$store.dispatch('getLastNewsCheckDate').then(response => {
      this.news.startDate = response.data.beginDate;
      this.news.startTime = response.data.beginTime;
    });

    this.$store.dispatch('loadVKFilter');
  }
}
</script>

