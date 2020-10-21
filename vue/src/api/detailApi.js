import axios from "@/utils/axios.config";

export default {
    add(detail) {
        return axios({
            url: 'detail',
            method: 'post',
            data: detail
        })
    },
    update(detail) {
        return axios({
            url: 'detail',
            method: 'put',
            data: detail
        })
    },
    get(id) {
        return axios({
            url: `detail/${id}`,
            method: 'get'
        })
    },
    delete({id, date, expense, earning, account}) {
        return axios({
            url: 'detail',
            method: 'delete',
            data: {id, date, expense, earning, account}
        })
    },
    deletePicture(pictureId) {
        return axios({
            url: `detail/picture/${pictureId}`,
            method: 'delete'
        })
    },
    updateBalance() {
        return axios({
            url: 'detail/balance',
            method: 'put',
        })
    },

    list(queryCondition) {
        return axios({
            url: 'detail',
            method: 'get',
            params: queryCondition
        })
    },


    listPicture(detailId) {
        return axios({
            url: `detail/picture/${detailId}`,
            method: 'get',
        })
    }
}