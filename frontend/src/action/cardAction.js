import axios from 'axios';

export const list = () => {
    return axios.get('http://localhost:8080//card/words')
        .then(response => response.data);
}
