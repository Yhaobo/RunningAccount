import axios from "@/utils/axios.config";

export default {
    money(queryCondition) {
        return axios({
            url: 'statistics/money',
            method: 'get',
            params: queryCondition
        })
    },
}