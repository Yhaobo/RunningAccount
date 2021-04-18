import axios from "@/utils/axios.config";

export default {
    listAll() {
        return axios({
            url: `rbac/permission/listAll`,
            method: 'get',
        })
    },
}