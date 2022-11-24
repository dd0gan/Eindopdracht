import './Contact.css'
import 'bootstrap/dist/css/bootstrap.min.css';
import React from 'react';
import { trackPromise } from 'react-promise-tracker';
import axios from 'axios';
import { withReactRouter } from "./util";

import {Alert, Container} from "react-bootstrap";

export async function loader({ params }) {
}

class Contact extends React.Component {

    render() {
        return (

            <Container fluid>

            </Container>
        )
    }
}

export default withReactRouter(Contact);