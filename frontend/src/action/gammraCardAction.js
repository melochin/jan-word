import axios from 'axios';
import {uri} from './config.js';

export const list = () => {
    return axios.get(`${uri}/card/grammars`)
        .then(response => response.data);
}

export const forget = (id) => {
    return axios.patch(`${uri}/card/grammar/forget/${id}`);
}

export const remeber = (id) => {
    return axios.patch(`${uri}/card/grammar/remeber/${id}`)
        .then(response => response.data);
}

export const finish = () => {
    return axios.post(`${uri}/card/grammar/finish`);
}
