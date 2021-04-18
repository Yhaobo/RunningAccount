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
                path: '/detail',
                name: 'detail',
                component: () => import(/* webpackChunkName: "about" */ '../views/Detail'),

            },
            {
                path: '/statistics',
                name: 'statistics',
                component: () => import(/* webpackChunkName: "about" */ '../views/Statistics'),
            },
        ]
    },
    {
        path: '/rbac',
        component: () => import(/* webpackChunkName: "about" */ '../components/RbacLayout'),
        redirect: '/userAdmin',
        children: [
            {
                path: '/groupAdmin',
                name: 'groupAdmin',
                component: () => import(/* webpackChunkName: "about" */ '../views/rbac/admin/GroupAdmin'),
            },
            {
                path: '/userAdmin',
                name: 'userAdmin',
                component: () => import(/* webpackChunkName: "about" */ '../views/rbac/admin/UserAdmin'),
            },
            {
                path: '/roleAdmin',
                name: 'roleAdmin',
                component: () => import(/* webpackChunkName: "about" */ '../views/rbac/admin/RoleAdmin'),
            },
            {
                path: '/roleAssign',
                name: 'roleAssign',
                component: () => import(/* webpackChunkName: "about" */ '../views/rbac/assign/RoleAssign'),
            },
            {
                path: '/permissionAssign',
                name: 'permissionAssign',
                component: () => import(/* webpackChunkName: "about" */ '../views/rbac/assign/PermissionAssign'),
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
