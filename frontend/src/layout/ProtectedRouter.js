import React from 'react';
import {Route,Redirect} from "react-router-dom";  
import {getUserFromStorage} from '../action/userAction';

const ProtectedRoute = ({children, role, ...rest}) => {


    const render = ({location}) => {

        const user = getUserFromStorage();

        if (user != null && user.token != null && (role == null || user.role === role)) {
            return (children);
        } 
        return (
            <Redirect to={{
                pathname: "/login",
                state: {from: location}
            }}>
            </Redirect>
        )
    }

    return (
        <Route
            {...rest}
            render ={render} 
        />
    )
}

export default ProtectedRoute;