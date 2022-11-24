import './Application.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import React from 'react';
import { trackPromise } from 'react-promise-tracker';
import axios from 'axios';
import { withReactRouter } from "./util";

import {Alert, Container, Form, Row, Col, Button, Modal, OverlayTrigger, Tooltip} from "react-bootstrap";
import DataTable from "react-data-table-component";
import {isAdminUser} from "./authUtil";

export async function loader({ params }) {
}

class Application extends React.Component {

    constructor(props) {
        super(props);

        const columns = [
            {
                name: 'ID',
                selector: row => row.id,
                sortable: true,
            },
            {
                name: 'Description',
                selector: row => row.description,
                sortable: false,
                cell: (row) => (
                    <div>
                        <span>{row.description}</span>
                    </div>
                )
            },
            {
                name: 'Hourly Rate',
                sortable: true,
                selector: row => row.hourlyRate,
            },
            {
                name: 'Working Hour',
                sortable: true,
                selector: row => row.workingHour,
            },
            {
                name: 'Type',
                sortable: true,
                selector: row => row.type,
            },
            {
                name: 'Location',
                sortable: true,
                selector: row => row.location,
            },
            {
                name: 'Status',
                sortable: true,
                selector: row => row.status,
            }
        ];

        const applicationColumns = [
            {
                name: 'ID',
                selector: row => row.id,
                sortable: true,
            },
            {
                name: 'User',
                sortable: true,
                selector: row => row.user['username'],
            },
            {
                name: 'Status',
                sortable: true,
                selector: row => row.status,
            },

            {
                name: 'CV',
                fixed: true,
                cell: (row) => (
                    <div>
                        {
                            row.user['cvUniqueId'] ?
                                <a href={process.env.REACT_APP_API_URL + '/api/users/cv/download?uuid=' + row.user['cvUniqueId']}>{ row.user['cvFilename']}</a>
                                :
                                <div>No CV</div>
                        }
                    </div>

                )
            },


            {
                name: 'Action',
                fixed: true,
                cell: (row) => (
                    <div>
                        {
                            isAdminUser() &&
                            <Button variant="success" onClick={(e) => {this.handleAcceptButton(e, row.id)}}>Accept</Button>
                        }
                        {
                            isAdminUser() &&
                            <Button variant="danger" onClick={(e) => {this.handleRejectButton(e, row.id)}} style={{marginLeft: 5}}>Reject</Button>
                        }
                    </div>
                )
            },
        ];
        this.state = {id : props.match.params.id, columns : columns, applicationColumns: applicationColumns};

    }

    componentWillMount() {
        this.getSelectedVacancies();
    }

    getSelectedVacancies() {
        trackPromise (
            axios.get('api/vacancies/' + this.state.id, ).then((response) => {
                console.log(response);
                this.setState({selectedVacancy : response.data, vacancies : [response.data], applications : response.data.applications})
            }, (error) => {
                console.log(error);
                let title = 'Error';
                let body = error.response.data.message;
                this.setState({showMessagePopup : true, messagePopupTitle: title, messagePopupBody: body})
            })
        );
    }

    handleMessagePopupClose = (event) => {
        this.setState({showMessagePopup : false})
    }

    handleAcceptButton = (event, id) => {
        this.completeAction(id, 'ACCEPT');
    }

    handleRejectButton = (event, id) => {
        this.completeAction(id, 'REJECT');
    }

    completeAction(applicationId, acceptReject) {
        trackPromise (
            axios.post('api/vacancies/complete?id=' + this.state.id + '&applicationId=' + applicationId + '&acceptReject=' + acceptReject, ).then((response) => {
                console.log(response);
                // refresh
                this.getSelectedVacancies();
            }, (error) => {
                console.log(error);
                let title = 'Error';
                let body = error.response.data.message;
                this.setState({showMessagePopup : true, messagePopupTitle: title, messagePopupBody: body})
            })
        );
    }


    render() {
        return (

            <Container fluid>
                <Alert variant='info'>
                    <Row>
                        <Col>
                            <div id="application-review-table-wrapper">
                                <DataTable
                                    title="Vacancy Review"
                                    columns={ this.state.columns }
                                    data={ this.state.vacancies }
                                />
                            </div>
                        </Col>
                    </Row>
                </Alert>
                <hr/>
                <Row>
                    <Col>
                        <div id="application-list-table-wrapper">
                            <DataTable
                                title="Applications"
                                columns={ this.state.applicationColumns }
                                data={ this.state.applications }
                            />
                        </div>
                    </Col>
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

export default withReactRouter(Application);