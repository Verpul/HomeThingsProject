<template>
  <div>
    <v-navigation-drawer
        v-model="drawer"
        :mini-variant="miniVariant"
        app
        clipped
    >
      <v-list dense>
        <v-list-item to="/">
          <v-list-item-action>
            <v-icon>mdi-home</v-icon>
          </v-list-item-action>
          <v-list-item-content>
            <v-list-item-title>Главная</v-list-item-title>
          </v-list-item-content>
        </v-list-item>
        <v-list-item to="/reminder">
          <v-list-item-action>
            <v-icon>mdi-calendar-clock</v-icon>
          </v-list-item-action>
          <v-list-item-content>
            <v-list-item-title>Напоминания</v-list-item-title>
          </v-list-item-content>
        </v-list-item>
        <v-list-item to="/weight">
          <v-list-item-action>
            <v-icon>mdi-weight-kilogram</v-icon>
          </v-list-item-action>
          <v-list-item-content>
            <v-list-item-title>Контроль веса</v-list-item-title>
          </v-list-item-content>
        </v-list-item>
        <v-list-item to="/twitch">
          <v-list-item-action>
            <v-icon>mdi mdi-twitch</v-icon>
          </v-list-item-action>
          <v-list-item-content>
            <v-list-item-title>Twitch</v-list-item-title>
          </v-list-item-content>
        </v-list-item>
        <v-list-item to="/currency">
          <v-list-item-action>
            <v-icon>mdi mdi-currency-usd</v-icon>
          </v-list-item-action>
          <v-list-item-content>
            <v-list-item-title>Валюта</v-list-item-title>
          </v-list-item-content>
        </v-list-item>
        <v-list-item to="/vk">
          <v-list-item-action>
            <v-icon>mdi mdi-newspaper-variant-outline</v-icon>
          </v-list-item-action>
          <v-list-item-content>
            <v-list-item-title>VK</v-list-item-title>
          </v-list-item-content>
        </v-list-item>
        <v-list-item to="/notes">
          <v-list-item-action>
            <v-icon>mdi mdi-notebook-outline</v-icon>
          </v-list-item-action>
          <v-list-item-content>
            <v-list-item-title>Заметки</v-list-item-title>
          </v-list-item-content>
        </v-list-item>
        <v-list-item to="/youtube">
          <v-list-item-action>
            <v-icon>mdi mdi-youtube</v-icon>
          </v-list-item-action>
          <v-list-item-content>
            <v-list-item-title>Youtube</v-list-item-title>
          </v-list-item-content>
        </v-list-item>
      </v-list>

      <template v-slot:append>
        <v-divider></v-divider>
        <v-list-item @click="logout">
          <v-list-item-action>
            <v-icon>mdi-logout</v-icon>
          </v-list-item-action>
          <v-list-item-content>
            <v-list-item-title>Выйти</v-list-item-title>
          </v-list-item-content>
        </v-list-item>
      </template>
    </v-navigation-drawer>

    <v-app-bar app color="indigo" dark clipped-left>
      <v-app-bar-nav-icon
          @click.stop="miniVariant = !miniVariant"
      ></v-app-bar-nav-icon>
      <v-toolbar-title>HomeThings</v-toolbar-title>
    </v-app-bar>
  </div>
</template>

<script>
export default {
  name: 'MainMenu',

  data: () => ({
    drawer: null,
    miniVariant: false
  }),
  methods: {
    logout() {
      this.$keycloak.logout();
    }
  }
}
</script>