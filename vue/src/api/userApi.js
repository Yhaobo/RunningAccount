import axios from "@/utils/axios.config";
import {Base64} from "js-base64";

export default {
    login(loginForm) {
        loginForm.password = Base64.encode(loginForm.password);
        return axios({
            url: 'user/login',
            method: 'post',
            data: loginForm
        })
    },

    logout() {
        return axios({
            url: 'user/logout',
            method: 'delete',
        })
    },

    listUsername(level) {
        return axios({
            url: `user/listUsername/${level}`,
            method: 'get',
        })
    },
    update(user) {
        user.password = Base64.encode(user.password);
        user.checkPassword = null;
        return axios({
            url: 'user',
            method: 'put',
            data: user
        })
    },
    add(user) {
        user.password = Base64.encode(user.password);
        user.checkPassword = null;
        return axios({
            url: 'user',
            method: 'post',
            data: user
        })
    },
    delete(user) {
        user.password = null;
        user.checkPassword = null;
        return axios({
            url: 'user',
            method: 'delete',
            data: user
        })
    },


}