import axios from "@/utils/axios.config";

export default {
    money(queryCondition) {
        return axios({
            url: 'statistics/money',
            method: 'get',
            params: queryCondition
        })
    },
    getDepartmentProportionData({beginDate,endDate}) {
        return axios({
            url: 'statistics/proportion/department',
            method: 'get',
            params: {beginDate,endDate}
        })
    },
    getCategoryProportionData({beginDate,endDate}) {
        return axios({
            url: 'statistics/proportion/category',
            method: 'get',
            params: {beginDate,endDate}
        })
    },

}