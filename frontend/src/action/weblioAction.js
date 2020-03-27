import axios from 'axios';
import {uri} from './config.js'

const weblio = {

    getMeanings: (word) => {
        return axios.get(`${uri}/weblio?word=${word}`)
        .then(response => response.data);
    }

}

export default weblio;
