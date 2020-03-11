import axios from 'axios';
import {uri} from './config.js';

export const list = () => {
    return axios.get(`${uri}/card/words`)
        .then(response => response.data);
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
