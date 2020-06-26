import axios from 'axios';
import {uri} from './config.js'
import {getToken} from './userAction';

axios.interceptors.request.use(
    config => {
        const token = getToken();
        if (token != null) {
            config.headers.token = token;
        }
        return config;
    },
)

axios.interceptors.response.use(
    response => response,
    error => {
        const status = error.response.status;
        if (status == 403) {
            error.message = '请登录';
            window.location.href = "/login";
            return Promise.reject(error);
        }
        if (error.message == 'Network Error') {
            error.message = '服务器故障'
            return Promise.reject(error);
        } else {
            alert(error.message);
            return Promise.reject(error);
        }
    }
)

export const list = () => {
    return axios.get(`${uri}/card/words`)
        .then(
            response => response.data, 
            error => {
                return {
                    dataSource: null, count:0,  countRemember: 0
                }
            }
        );

}

export const forget = (id) => {
    return axios.patch(`${uri}/card/word/forget/${id}`);
}

export const remeber = (id) => {
    return axios.patch(`${uri}/card/word/remeber/${id}`)
        .then(response => response.data);
}

export const finish = () => {
    return axios.post(`${uri}/card/word/finish`);
}
