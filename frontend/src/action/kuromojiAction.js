import axios from 'axios';
import {uri} from './config.js';

export const listReading = (word) => {
    return axios.get(`${uri}/kuromoji/reading/${word}`)
        .then(response => response.data);
}
