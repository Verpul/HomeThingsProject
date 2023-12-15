import Vue from 'vue'
import VueRouter from 'vue-router'
import Index from "@/components/Index";
import WeightComponent from "@/components/WeightComponent";
import ReminderComponent from "@/components/ReminderComponent";
import TwitchComponent from "@/components/TwitchComponent";
import CurrencyComponent from "@/components/CurrencyComponent";
import VKComponent from "@/components/VKComponent";
import NotesComponent from "@/components/NotesComponent";
import YoutubeComponent from "@/components/YoutubeComponent";

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
  },
  {
    path: '/currency',
    name: 'Currency Component',
    component: CurrencyComponent
  },
  {
    path: '/vk',
    name: 'VK Component',
    component: VKComponent
  },
  {
    path: '/notes',
    name: 'Notes Component',
    component: NotesComponent
  },
  {
    path: '/youtube',
    name: 'Youtube component',
    component: YoutubeComponent
  },
  {
    path: '/:catchAll(.*)',
    redirect: '/'
  }
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

export default router
