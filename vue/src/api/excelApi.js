import axios from "@/utils/axios.config";

export default {
    generateExpenseClaimForm(details) {
        return axios({
            url: 'detail/excel/expenseClaimForm',
            method: 'post',
            data: details
        })
    },
}