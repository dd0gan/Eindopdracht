import {
    Navigate
} from "react-router-dom";

export const ProtectedRoute = ({
                            redirectPath = '/auth',
                            children,
                        }) => {
    let user = localStorage.getItem('user');
    // validate saved token
    if (!user) {
        return <Navigate to={redirectPath} replace />;
    }

    return children;
};

function parseJwt (token) {
    var base64Url = token.split('.')[1];
    var base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
    var jsonPayload = decodeURIComponent(window.atob(base64).split('').map(function(c) {
        return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
    }).join(''));

    return JSON.parse(jsonPayload);
}

export const getCurrentUser = () => {
    let user = localStorage.getItem('user');
    if (user) {
        let token = JSON.parse(user).token;
        let jwt = parseJwt(token);
        return jwt;
    }
    return null;

}

export const isAdminUser = () => {
    let user = getCurrentUser();
    return user.roles.includes('ADMIN');
}

export const isUser = () => {
    let user = getCurrentUser();
    return user.roles.includes('ADMIN') || user.roles.includes('USER');
}