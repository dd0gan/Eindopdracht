import './App.css';
import React from 'react';
import {withReactRouter} from "./util";
import { Navbar, Nav, Link, NavDropdown } from 'react-bootstrap'
import Vacancy from "./Vacancy";

import {
  createBrowserRouter,
  RouterProvider,
  Navigate,
  BrowserRouter,
  Routes,
  Route,
  Outlet
} from "react-router-dom";

export async function loader({ params }) {
}

class App extends React.Component {
  constructor(props) {
    super(props);
  }

  componentWillMount() {
    this.setState({user : JSON.parse(localStorage.getItem ('user'))});
  }

  handleProfileClick = (event) => {
    event.preventDefault();
    const { navigate } = this.props;
    navigate('/dashboard/profile');
  }

  handleLogout = (event) => {
    event.preventDefault();
    localStorage.clear();
    const { navigate } = this.props;
    navigate('/auth');
  }

  render() {
    return (
        <div className="App">
          <Navbar bg="light" expand="lg">
            <NavDropdown title={ "Hi, " + this.state.user.username } id="basic-nav-dropdown">
              <NavDropdown.Item href="" onClick={this.handleProfileClick}>Profile</NavDropdown.Item>
              <NavDropdown.Divider />
              <NavDropdown.Item href="#" onClick={this.handleLogout}>Logout</NavDropdown.Item>
            </NavDropdown>

            <Navbar.Toggle aria-controls="basic-navbar-nav" />
            <Navbar.Collapse id="basic-navbar-nav">
              <Nav className="mr-auto">
                <Nav.Link href="/dashboard/vacancy">Vacancy</Nav.Link>
                <Nav.Link href="/dashboard/contact">Contact</Nav.Link>
              </Nav>
            </Navbar.Collapse>
          </Navbar>

          <Outlet/>
        </div>
    );
  }
}

export default withReactRouter(App);
