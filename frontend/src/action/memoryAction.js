import axios from 'axios';
import {uri} from './config.js'

export const list = () => {
    return axios.get(`${uri}/memory/record/count`)
        .then(response => response.data);
}