import Vue from 'vue'
import VueRouter from 'vue-router'
import Index from "@/components/Index";
import WeightComponent from "@/components/WeightComponent";

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    name: 'Index',
    component: Index,
    children: [
      {
        name: "Weight Component",
        path: "/weight",
        component: WeightComponent
      },
    ]
  },
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

export default router
