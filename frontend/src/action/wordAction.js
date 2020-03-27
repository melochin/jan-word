import axios from 'axios';
import {uri} from './config.js'
let data = [
    {
        id: 1,
        word: 'ビイル' ,
        gana: null,
        chinese: '啤酒'
    }, {
        id: 2,
        word: '部屋',
        gana: 'へや',
        chinese: '房间'
    }
];

// export const add = (values) => {
//     return new Promise(resolve=> {
//         Object.assign(values, {id : Date.now()});
//         data.push(values)
//         resolve();
//     })
// }

// export const list = () => {
//     return new Promise(resolve => {
//         setTimeout(() => {
//             resolve(data.slice())
//         }, 1000)
//     }) 
// }

// export const remove = (id) =>{
//     data = data.filter(d => d.id != id);
//     return new Promise(resolve=> {
//         resolve();
//     })
// }

export const add = (values) => {
    return axios.post(`${uri}/word`, values);
}

export const list = () => {
    return axios.get(`${uri}/words`)
        .then(response => response.data);
}

export const modify = (values) => {
    return axios.put(`${uri}/word`, values);
}

export const remove = (id) =>{
    return axios.delete(`${uri}/word/${id}`);
}