import './Profile.css'
import 'bootstrap/dist/css/bootstrap.min.css';
import React from 'react';
import { trackPromise } from 'react-promise-tracker';
import axios from 'axios';
import { withReactRouter } from "./util";

import {Alert, Container, Form, Row, Col, Button, Modal} from "react-bootstrap";

export async function loader({ params }) {
}

class Profile extends React.Component {

    constructor(props) {
        super(props);
        this.state = {selectedFile : null, selectedFileLabel : 'Curriculum Vitae' ,showMessagePopup : false, cvUniqueId : '', cvFilename : ''}
    }

    componentWillMount() {
        this.getUserDetail();
    }

    getUserDetail = () => {
        trackPromise (
            axios({
                method: 'get',
                url : 'api/users/me',
            }).then((response) => {
                console.log(response);
                this.setState({cvUniqueId : response.data.cvUniqueId, cvFilename : response.data.cvFilename})
            }, (error) => {
                console.log(error);
                let title = 'Error';
                let body = 'Exception occurred';
                this.setState({showMessagePopup : true, messagePopupTitle: title, messagePopupBody: body})
            })
        );
    }

    handleMessagePopupClose = (event) => {
        this.setState({showMessagePopup : false})
    }

    handleChangeFileUpload = (event) => {
        this.state.selectedFile = event.target.files[0];
        this.setState({selectedFileLabel : event.target.files[0].name})
    }

    handleFileSave = (event) => {
        event.preventDefault();
        const formData = new FormData();
        formData.append("file", this.state.selectedFile);

        trackPromise (
            axios({
                method: 'post',
                url : 'api/users/cv/upload',
                data : formData,
                headers: { "Content-Type": "multipart/form-data" }
            }).then((response) => {
                console.log(response);
                let title = 'Success';
                let body = 'Upload successful';
                this.setState({showMessagePopup : true, messagePopupTitle: title, messagePopupBody: body})
                this.getUserDetail();
            }, (error) => {
                console.log(error);
                let title = 'Error';
                let body = 'Exception occurred';
                this.setState({showMessagePopup : true, messagePopupTitle: title, messagePopupBody: body})
            })
        );
    }

    render() {
        return (

            <Container fluid>
                <Alert key="cv-current" variant="primary">
                    {
                        this.state.cvUniqueId ?
                        <a href={process.env.REACT_APP_API_URL + '/api/users/cv/download?uuid=' + this.state.cvUniqueId}>{ this.state.cvFilename}</a>
                            :
                            <div>Please upload CV</div>
                    }
                </Alert>

                <Row>
                    <Col>
                        <Form id="profile-form">
                            <Form.Group className="sm-3" controlId="profile-form.cv">
                                <Form.Label custom>Upload</Form.Label>
                                <Form.File
                                    type="file"
                                    label={this.state.selectedFileLabel}
                                    id = "cv"
                                    onChange={this.handleChangeFileUpload.bind(this)}
                                    custom
                                />
                            </Form.Group>

                            <Button variant="primary" onClick={this.handleFileSave}>
                                Save
                            </Button>
                        </Form>
                    </Col>
                    <Col></Col>
                </Row>

                <Modal show={this.state.showMessagePopup} onHide={this.handleMessagePopupClose}>
                    <Modal.Header closeButton>
                        <Modal.Title>{this.state.messagePopupTitle}</Modal.Title>
                    </Modal.Header>
                    <Modal.Body>
                        {this.state.messagePopupBody}
                    </Modal.Body>
                </Modal>
            </Container>
        )
    }
}

export default withReactRouter(Profile);