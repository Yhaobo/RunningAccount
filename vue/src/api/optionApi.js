import axios from "@/utils/axios.config";

export default {
    addProject(name) {
        return axios({
            url: 'option/project',
            method: 'post',
            data: {name}
        })
    },
    addAccount(name) {
        return axios({
            url: 'option/account',
            method: 'post',
            data: {name}
        })
    },
    addDepartment(name) {
        return axios({
            url: 'option/department',
            method: 'post',
            data: {name}
        })
    },
    addCategory(name) {
        return axios({
            url: 'option/category',
            method: 'post',
            data: {name}
        })
    },
    updateProject(option) {
        return axios({
            url: 'option/project',
            method: 'put',
            data: option
        })
    },
    updateAccount(option) {
        return axios({
            url: 'option/account',
            method: 'put',
            data: option
        })
    },
    updateDepartment(option) {
        return axios({
            url: 'option/department',
            method: 'put',
            data: option
        })
    },
    updateCategory(option) {
        return axios({
            url: 'option/category',
            method: 'put',
            data: option
        })
    },

    listAccount() {
        return axios({
            url: 'option/account',
            method: 'get'
        })
    },
    getAll() {
        return axios({
            url: 'option/all',
            method: 'get'
        })
    }
}