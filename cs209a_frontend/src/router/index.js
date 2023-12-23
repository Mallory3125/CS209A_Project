import { createRouter, createWebHistory } from 'vue-router'
import BugView from '@/views/BugView.vue'
import RelatedTopicView from '@/views/RelatedTopicView.vue'
import TopicView from '@/views/TopicView.vue'
const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/bug',
      name: 'bug',
      component:BugView
    },
    {
        path: '/related',
        name: 'relatedTopic',
        component:RelatedTopicView
      },
      {
        path: '/tpoic',
        name: 'topic',
        component:TopicView
      },
    {
      path: '/about',
      name: 'about',
      // route level code-splitting
      // this generates a separate chunk (About.[hash].js) for this route
      // which is lazy-loaded when the route is visited.
      component: () => import('../views/AboutView.vue')
    }
  ]
})

export default router
