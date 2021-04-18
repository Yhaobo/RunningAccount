import axios from "@/utils/axios.config";
import {Base64} from "js-base64";

export default {
    login(loginForm) {
        loginForm.password = Base64.encode(loginForm.password);
        return axios({
            url: 'rbac/user/login',
            method: 'post',
            data: loginForm
        })
    },

    logout() {
        return axios({
            url: 'rbac/user/logout',
            method: 'delete',
        })
    },

    update(user) {
        user.password = Base64.encode(user.password);
        user.checkPassword = null;
        return axios({
            url: 'rbac/user',
            method: 'put',
            data: user
        })
    },
    add(user) {
        user.password = Base64.encode(user.password);
        user.checkPassword = null;
        return axios({
            url: 'rbac/user',
            method: 'post',
            data: user
        })
    },
    delete(userId) {
        return axios({
            url: 'rbac/user',
            method: 'delete',
            data: userId
        })
    },
}