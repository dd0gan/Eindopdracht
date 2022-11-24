import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import App, {
    loader as appLoader,
} from './App';

import Auth , {
    loader as authLoader,
} from './Auth';

import Vacancy , {
    loader as vacancyLoader,
} from './Vacancy';

import Contact , {
    loader as contactLoader,
} from './Contact';

import Profile , {
    loader as profileLoader,
} from './Profile';

import Application , {
    loader as applicationLoader,
} from './Application';

import reportWebVitals from './reportWebVitals';

import {
    createBrowserRouter,
    RouterProvider,
    Navigate,
    BrowserRouter,
    Routes,
    Route,
    Outlet
} from "react-router-dom";

import { usePromiseTracker } from "react-promise-tracker";
import Loader from 'react-promise-loader';
import { ProtectedRoute } from './authUtil';

import './axiosConfig';

const LoadingIndicator = props => {
    const { promiseInProgress } = usePromiseTracker();

    return (
        promiseInProgress &&
        <Loader promiseTracker={usePromiseTracker} />
    );
}

const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
    <div>
        <BrowserRouter>
            <Routes>
                <Route path="/dashboard" element={<ProtectedRoute><App /></ProtectedRoute>} loader={appLoader}>
                    <Route path="/dashboard/vacancy" element={<Vacancy/>} loader={vacancyLoader}/>
                    <Route path="/dashboard/contact" element={<Contact/>} loader={contactLoader}/>
                    <Route path="/dashboard/profile" element={<Profile/>} loader={profileLoader}/>
                    <Route path="/dashboard/application/:id" element={<Application/>} loader={applicationLoader}/>
                </Route>

                <Route path="/auth" element={<Auth />} loader={authLoader}>
                </Route>
                <Route path="*" element={<Navigate to="/dashboard" replace/>} />
            </Routes>
        </BrowserRouter>
        <LoadingIndicator/>
    </div>
);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();
