import Axios from 'axios'
import {Notification, MessageBox} from 'element-ui'
import router from "@/router";


// 创建axios实例
const axios = Axios.create({
    baseURL: process.env.VUE_APP_BaseURL, // api 的 base_url
    timeout: 30 * 1000, // 请求超时时间
    //跨域开启cookie, 注意: 开启的话,后端要设置Access-Control-Allow-Origin为你的源地址,不能是*，
    //而且还要设置header(‘Access-Control-Allow-Credentials: true’);
    //比如在SpringBoot: @CrossOrigin(value = "http://localhost:8081",allowCredentials="true")
    withCredentials: true,
})

axios.interceptors.request.use(
    function (config) {
        // Do something before request is sent
        return config;
    },
    function (error) {
        // Do something with request error
        return Promise.reject(error);
    }
);

// 登出提示信号量 (防止重复提示)
let logoutNoticeSemaphore = 1;

// Add a response interceptor
axios.interceptors.response.use(
    response => {
        /**
         * code为非200是抛错 可结合自己业务进行修改
         */
        const result = response.data
        if (result.code !== 200) {
            console.log('errorResult: ' + response.data);
            switch (result.code) {
                case 401:
                    if (logoutNoticeSemaphore > 0) {
                        logoutNoticeSemaphore = 0
                        MessageBox.confirm(
                            '你已被登出，可以取消继续留在该页面，或者重新登录',
                            '提示',
                            {
                                confirmButtonText: '重新登录',
                                cancelButtonText: '取消',
                                type: 'warning',
                            }
                        ).then(() => {
                            sessionStorage.clear()
                            router.replace({
                                path: "/login",
                                query: {redirect: router.currentRoute.fullPath}
                            })
                        }).finally(() => {
                            logoutNoticeSemaphore = 1
                        });
                    }
                    break
                case 403:
                    Notification({
                        title: '提示',
                        message: '权限受限',
                        type: 'warning',
                        showClose: true,
                        duration: 5 * 1000
                    });
                    break
                default:
                    Notification({
                        title: '提示',
                        message: result.message,
                        type: 'error',
                        showClose: true,
                        duration: 10 * 1000
                    })
            }
            return Promise.reject('error')
        } else {
            return result;
        }
    },
    error => {
        const errorMsg = 'error: ' + error;
        console.log(errorMsg) // for debug
        if (error.response) {
            switch (error.response.status) {
                case 401:
                    MessageBox.confirm(
                        '你已被登出，可以取消继续留在该页面，或者重新登录',
                        '确定登出',
                        {
                            confirmButtonText: '重新登录',
                            cancelButtonText: '取消',
                            type: 'warning'
                        }
                    ).then(() => {
                        sessionStorage.clear()
                        router.replace({
                            path: "/login",
                            query: {redirect: router.currentRoute.fullPath}
                        })
                    })
                    break
                case 403:
                    Notification({
                        title: '提示',
                        message: '权限受限',
                        type: 'warning',
                        showClose: true,
                        duration: 5 * 1000
                    });
                    break
                default:
                    Notification({
                        title: '提示',
                        message: error.message,
                        type: 'error',
                        showClose: true,
                        duration: 5 * 1000
                    });
            }
        } else if (errorMsg.includes('timeout')) {
            Notification({
                title: '提示',
                message: "连接超时",
                type: 'error',
                showClose: true,
                duration: 5 * 1000
            });
        } else if (errorMsg.includes('Network Error')) {
            Notification({
                title: '提示',
                message: "网络错误",
                type: 'error',
                showClose: true,
                duration: 5 * 1000
            });
        } else {
            Notification({
                title: '提示',
                message: error.message,
                type: 'error',
                showClose: true,
                duration: 5 * 1000
            });
        }
        return Promise.reject(error);
    }
);

export default axios;