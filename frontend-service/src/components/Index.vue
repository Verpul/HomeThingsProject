<template>
  <div>
    <MainMenu/>
    <v-main>
      <v-container fluid>
        <router-view></router-view>
      </v-container>
    </v-main>
  </div>
</template>

<script>
import MainMenu from "@/components/MainMenu";

export default {
  name: 'IndexView',
  components: {MainMenu},
}
</script>