import React, { Component } from "react";
import { withRouter } from "react-router";
import { Row, Col, Card, Form, InputGroup, FormControl, Button,Alert} from "react-bootstrap";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faLock, faUserPlus, faUndo, faUser } from "@fortawesome/free-solid-svg-icons"; 
import { connect } from "react-redux";
import { authenticateUser } from "../services/index";
import axios from "axios";
import { DOMAIN } from './domain';
class Signup extends Component {
    constructor(props){
        super(props);
        this.state = this.initialState;
    }
    initialState = {
        username:'', password:'', error:'', repeatPassword:'', success:''
    }
    resetForm=()=>{
        this.setState(()=>this.initialState);
    }
    credentialChange=event=>{
        this.setState({
            [event.target.name] : event.target.value
        })
    }
    submitForm = () =>{
        if(this.state.password !== this.state.repeatPassword){
            this.resetForm();
            this.setState({error:"Passwords don't match, please type again"})
        }else{
        setTimeout(()=> {
            axios.post(DOMAIN+"/signup", {
                username: this.state.username,
                password: this.state.password,
            })
            .then(response=> {
                this.resetForm();
                this.setState({success:response.data.message+' Please go to Login.', error:''});
            })
            .catch(error=>{
                this.resetForm();
                this.setState({error:error.error})
            });
        }, 200) }
    }
    render() {
        const {username, password, error, repeatPassword, success} = this.state;
        return (
            <Row className="justify-content-md-center">
                <Col xs={5}>
                    {success && (<Alert variant="success">{success}</Alert>)}
                    {error &&<Alert variant="danger">{error}</Alert>}
                    <Card>
                        <Card.Header>
                            Sign up
                        </Card.Header>
                        <Card.Body>
                        <Form>
                            <Form.Group className="mb-3" >
                                <Form.Label>Username</Form.Label>
                                <InputGroup>
                                    <FormControl required autoComplete="off" type="text" name="username" value={username} placeholder="Enter username" onChange={this.credentialChange}/>
                                </InputGroup>
                            </Form.Group>

                            <Form.Group className="mb-3" >
                                <Form.Label>Password</Form.Label>
                                <InputGroup>
                                    <FormControl required autoComplete="off" type="text" name="password" value={password} placeholder="Enter password" onChange={this.credentialChange}/>
                                </InputGroup>
                            </Form.Group>

                            <Form.Group className="mb-3" >
                                <Form.Label>Re-enter Password</Form.Label>
                                <InputGroup>
                                    <FormControl required autoComplete="off" type="text" name="repeatPassword" value={repeatPassword} placeholder="Enter password again" onChange={this.credentialChange}/>
                                </InputGroup>
                            </Form.Group>
                        </Form>
                          
                        </Card.Body>
                        <Card.Footer style={{"textAlign":"right"}}>
                            <Button size="sm" type="Button" onClick={this.submitForm}><FontAwesomeIcon icon={faUserPlus}/> Register</Button>{' '}{' '}
                            <Button size="sm" type="Button" onClick={this.resetForm}><FontAwesomeIcon icon={faUndo}/> Reset</Button>
                        </Card.Footer>
                    </Card>
                </Col>
            </Row>
        );
    }
}

export default withRouter(Signup);