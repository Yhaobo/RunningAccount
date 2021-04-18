import axios from "@/utils/axios.config";

export default {
    assignRoles(roles,groupId) {
        return axios({
            url: `rbac/group/assignRoles/${groupId}`,
            method: 'post',
            data: roles
        })
    },
    delete(groupId) {
        return axios({
            url: 'rbac/group',
            method: 'delete',
            data: groupId
        })
    },
    listAll() {
        return axios({
            url: 'rbac/group/listAll',
            method: 'get',
        })
    },
    update(group) {
        return axios({
            url: 'rbac/group',
            method: 'put',
            data: group
        })
    },
    add(group) {
        return axios({
            url: 'rbac/group',
            method: 'post',
            data: group
        })
    },
    listGroupAndUser() {
        return axios({
            url: 'rbac/group/listGroupAndUser',
            method: 'get'
        })
    },
    listRole(groupId) {
        return axios({
            url: `rbac/group/listRole/${groupId}`,
            method: 'get',
        })
    }
}