import axios from "@/utils/axios.config";

export default {
    assignPermissions(permissionIds, roleId) {
        return axios({
            url: `rbac/role/assignPermissions/${roleId}`,
            method: 'post',
            data: permissionIds
        })
    },
    delete(roleId) {
        return axios({
            url: 'rbac/role',
            method: 'delete',
            data: roleId
        })
    },
    listAll() {
        return axios({
            url: 'rbac/role/listAll',
            method: 'get',
        })
    },
    update(role) {
        return axios({
            url: 'rbac/role',
            method: 'put',
            data: role
        })
    },
    add(role) {
        return axios({
            url: 'rbac/role',
            method: 'post',
            data: role
        })
    },
    listPermission(roleId) {
        return axios({
            url: `rbac/role/listPermission/${roleId}`,
            method: 'get',
        })
    }
}