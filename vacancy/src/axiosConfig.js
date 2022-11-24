import axios from 'axios';
axios.defaults.baseURL = process.env.REACT_APP_API_URL;

axios.interceptors.request.use(
    config => {
        if (!config.headers.Authorization) {
            const token = localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")).token : null;

            if (token) {
                config.headers.Authorization = `Bearer ${token}`;
            }
        }

        return config;
    },
);

axios.interceptors.response.use(response => {
    if (response.status === 401 || response.status === 403) {
        localStorage.removeItem("user");
        window.location.href = '/auth';
    }
    return response;
}, function (error) {
    return Promise.reject(error);
});
