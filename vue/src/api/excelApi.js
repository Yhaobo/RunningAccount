import axios from "@/utils/axios.config";

export default {
    generateExpenseClaimForm(details) {
        return axios({
            url: 'excel/expenseClaimForm',
            method: 'post',
            data: details
        })
    },
}