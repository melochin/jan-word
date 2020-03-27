import axios from 'axios';
import {uri} from './config.js'

export const login = (values) => {
    return axios.post(`${uri}/login`, values)
        .then(response => response.data);
}

export const getUserFromStorage = () => {
    return JSON.parse(sessionStorage.getItem('user'));
}

export const setUserInStorage = (user) => {
    sessionStorage.setItem('user', JSON.stringify(user));
}

export const removeUserInStorage = () => {
    sessionStorage.removeItem('user');
}

export const getToken = () => {

    const user  = JSON.parse(sessionStorage.getItem('user'));

    return user == null ? null : user.token;
}