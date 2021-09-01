import React, { Component } from "react";
import { withRouter } from "react-router";
import { Row, Col, Card, Form, InputGroup, FormControl, Button,Alert} from "react-bootstrap";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faLock, faSignInAlt, faUndo, faUser } from "@fortawesome/free-solid-svg-icons"; 
import { connect } from "react-redux";
import { authenticateUser } from "../services/index";
import { DOMAIN } from './domain';
import axios from "axios";
class Login extends Component {
    constructor(props){
        super(props);
        this.state = this.initialState;
    }
    initialState = {
        username:'', password:'', error:'', showmessage:true
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
        if(localStorage.jwtToken){
            localStorage.clear();
        }
        delete axios.defaults.headers.common['Authorization'];
        setTimeout(()=> {
            axios.post(DOMAIN+"/login", 
            {
                username: this.state.username,
                password: this.state.password,
            })
            .then(response=> {
                localStorage.setItem("jwtToken", response.data.token);
                localStorage.setItem("role", response.data.role);
                localStorage.setItem("username", response.data.username);
                return this.props.history.push("/home");
            })
            .catch(error=>{
                this.resetForm();
                this.setState({error:"invalid username/password", showmessage:false})
            });
        }, 200) 
    }
    render() {
        const {username, password, error} = this.state;
        return (
            <Row className="justify-content-md-center">
                <Col xs={5}>
                    {(this.props.message && this.state.showmessage) && (<Alert variant="success">{this.props.message}</Alert>)}
                    {error &&<Alert variant="danger">{error}</Alert>}
                    <Card>
                        <Card.Header>
                            Login
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
                        </Form>
                          
                        </Card.Body>
                        <Card.Footer style={{"textAlign":"right"}}>
                            <Button size="sm" type="Button" onClick={this.submitForm}><FontAwesomeIcon icon={faSignInAlt}/> Login</Button>{' '}{' '}
                            <Button size="sm" type="Button" onClick={this.resetForm}><FontAwesomeIcon icon={faUndo}/> Reset</Button>
                        </Card.Footer>
                    </Card>
                </Col>
            </Row>
        );
    }
}
const mapStateToProps = state => {
    return {
        auth:state.auth
    }
}

const mapDispatchToProps = dispatch => {
    return {
        authenticateUser: (username, password) => dispatch(authenticateUser(username,password))
    };
}
export default withRouter(connect(mapStateToProps, mapDispatchToProps)(Login));


