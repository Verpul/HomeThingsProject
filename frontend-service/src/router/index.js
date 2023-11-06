import Vue from 'vue'
import VueRouter from 'vue-router'
import Index from "@/components/Index";
import WeightComponent from "@/components/WeightComponent";
import ReminderComponent from "@/components/ReminderComponent";
import TwitchComponent from "@/components/TwitchComponent";

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    name: 'Index',
    component: Index,
  },
  {
    path: '/weight',
    name: 'Weight Component',
    component: WeightComponent
  },
  {
    path: '/reminder',
    name: 'Reminder Component',
    component: ReminderComponent
  },
  {
    path: '/twitch',
    name: 'Twitch Component',
    component: TwitchComponent
  }
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

export default router
