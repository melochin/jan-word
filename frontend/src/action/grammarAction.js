import axios from 'axios';
import {uri} from './config.js'

export const add = (values) => {
    return axios.post(`${uri}/grammar`, values);
}

export const list = (params) => {
    if (params != undefined && params != null ) {
        return axios.get(`${uri}/grammars`, {
            params: params
        })
        .then(response => response.data);    
    }

    return axios.get(`${uri}/grammars`)
        .then(response => response.data);
}

export const remove = (id) => {
    return axios.delete(`${uri}/grammar/${id}`);
}

export const modify = (values) => {
    return axios.put(`${uri}/grammar`, values);
}
