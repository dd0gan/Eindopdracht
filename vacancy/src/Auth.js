import './Auth.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import React from 'react';
import { trackPromise } from 'react-promise-tracker';
import axios from 'axios';
import { Modal } from 'react-bootstrap';
import { withReactRouter } from "./util";

export async function loader({ params }) {
}

class Auth extends React.Component {
    constructor(props) {
        super(props);
        this.state = { view : 'signin', loading : false, username : '', password :'', showPopup : false}

        this.handleChangeUsername = this.handleChangeUsername.bind(this);
        this.handleChangePassword = this.handleChangePassword.bind(this);
    }

    handleChangeView = (e) => {
        e.preventDefault();
        if (this.state.view === 'signup') {
            this.setState({view :'signin'});
        } else {
            this.setState({view :'signup'});
        }
    }

    isSignIn = () => {
        return this.state.view === 'signin';
    }

    handleSignIn = (event) => {
        event.preventDefault();

        trackPromise (
            axios.post('/api/auth/signIn' , {
                'username' : this.state.username,
                'password' : this.state.password
            }).then((response) => {
                console.log(response);
                let data = response.data;
                localStorage.setItem('user', JSON.stringify(data))
                const { navigate } = this.props;
                navigate('/dashboard');
            }, (error) => {
                console.log(error);
                let title = 'Error';
                let body = 'Username or password is incorrect';
                this.setState({showPopup : true, popupTitle: title, popupBody: body})
            })
        );
    }

    handleSignUp = (event) => {
        event.preventDefault();

        trackPromise (
            axios.post('/api/auth/signUp' , {
                'username' : this.state.username,
                'password' : this.state.password
            }).then((response) => {
                console.log(response);
                let title = 'Success';
                let body = 'User is created';
                this.setState({showPopup : true, popupTitle: title, popupBody: body})
                this.setState({view :'signin'});
            }, (error) => {
                console.log(error);
                let title = 'Error';
                let body = 'Username or password is incorrect';
                this.setState({showPopup : true, popupTitle: title, popupBody: body})
            })
        );
    }

    handleChangeUsername = (event) => {
        this.setState({username : event.target.value})
    }

    handleChangePassword = (event) => {
        this.setState({password : event.target.value})
    }

    handlePopupClose = (event) => {
        this.setState({showPopup : false})
    }

    render() {
        return (
            <div className="Auth-form-container">
                <form className="Auth-form">
                    <div className="Auth-form-content">
                        <h3 className="Auth-form-title"> {this.isSignIn() ? 'Sign In' : 'Sign Up'}</h3>
                        <div className="form-group mt-3">
                            <label>Username</label>
                            <input
                                type="text"
                                className="form-control mt-1"
                                placeholder="Enter username"
                                value={this.state.username}
                                onChange={ this.handleChangeUsername.bind(this) }
                            />
                        </div>
                        <div className="form-group mt-3">
                            <label>Password</label>
                            <input
                                type="password"
                                className="form-control mt-1"
                                placeholder="Enter password"
                                value={this.state.password}
                                onChange={ this.handleChangePassword.bind(this)}
                            />
                        </div>
                        <div className="d-grid gap-2 mt-3">
                            <button type="submit" className="btn btn-primary" onClick={this.isSignIn() ? this.handleSignIn : this.handleSignUp}>
                                Submit
                            </button>
                        </div>
                        <p className="sign-up text-right mt-2">
                            {this.isSignIn() ? 'Sign up?' : 'Sign in?'}  <a href="#" onClick={this.handleChangeView}>{this.isSignIn() ? 'Register here' : 'Login here'}</a>
                        </p>
                    </div>
                </form>

                <div>
                    <Modal show={this.state.showPopup} onHide={this.handlePopupClose}>
                        <Modal.Header closeButton>
                            <Modal.Title>{this.state.popupTitle}</Modal.Title>
                        </Modal.Header>
                        <Modal.Body>
                            {this.state.popupBody}
                        </Modal.Body>
                    </Modal>
                </div>
            </div>



        );
    }

}

export default withReactRouter(Auth);
