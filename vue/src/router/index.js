import Vue from 'vue'
import VueRouter from 'vue-router'

Vue.use(VueRouter)

const routes = [
    {
        path: '/',
        component: () => import(/* webpackChunkName: "about" */ '../components/Layout'),
        redirect: '/detail',
        children: [
            {
                path: 'detail',
                name: 'detail',
                component: () => import(/* webpackChunkName: "about" */ '../views/Detail'),

            },
            {
                path: 'statistics',
                name: 'statistics',
                component: () => import(/* webpackChunkName: "about" */ '../views/Statistics'),
            },
        ]
    },
    {
        path: "/login",
        name: "login",
        component: () => import('../views/Login')

    }

]

const router = new VueRouter({
    routes
})

export default router
