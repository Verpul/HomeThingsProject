<template>
  <v-app>
    <MainMenu/>
    <v-main>
      <v-container fluid>
        <WeatherComponent/>
      </v-container>
    </v-main>
  </v-app>
</template>

<script>
import WeatherComponent from "@/components/WeatherComponent";

export default {
  name: 'IndexView',
  components: {WeatherComponent},
}
</script>