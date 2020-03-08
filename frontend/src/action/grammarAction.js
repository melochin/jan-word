import axios from 'axios';
import {uri} from './config';

export const add = (values) => {
    return axios.post('http://localhost:8080/grammar', values);
}

export const list = () => {
    return axios.get(`${uri}/grammars`)
        .then(response => response.data);
}

export const remove = (id) => {
    return axios.delete(`${uri}/grammar/${id}`);
}