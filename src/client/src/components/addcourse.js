import react from 'react';
import axios from 'axios';
import addHeader from '../services/authHeader';
import { Row, Col, Card, Form, InputGroup, FormControl, Button,Alert} from "react-bootstrap";
import { faPlus, faUndo } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { DOMAIN } from './domain';
export default class AddCourse extends react.Component{
    constructor(props){
        super(props)
        this.state = this.initialState
    }
    initialState = {
        coursename:'',coursetitle:'',instructor:'',workload:'',added:'',error:''
    }

    resetForm = () => {
        this.setState(()=>this.initialState)
    }
    courseinfoChange = e =>{
        this.setState({
            [e.target.name] : e.target.value
        })
    }
    submitForm = () => {
        if(this.state.coursename && this.state.coursetitle && this.state.workload && this.state.instructor){
            if(isNaN(this.state.workload)){
                this.resetForm()
                this.setState({error:"Please enter number in workload!"})
            }
            else{
                addHeader(localStorage.jwtToken);
                setTimeout(()=>{
                    axios.post(DOMAIN+"/addCourse", 
                        {
                            coursename:this.state.coursename,
                            coursetitle:this.state.coursetitle,
                            workload:this.state.workload,
                            instructor:this.state.instructor
                        }).then(response=>{
                            this.resetForm();
                            this.setState({added:response.data.coursename, error:""})
                        }).catch(error=>{
                            this.resetForm();
                            this.setState({error:error.response.data.error+", Course exists!", added:""})
                        })
                },200);
            }
        }
        else{
            this.resetForm()
            this.setState({error:"Please complete all fields!"})
        }
    }
    render() {
        const {coursename, coursetitle, instructor, workload, added, error} = this.state;
        return (
            
            <Row className="justify-content-md-center">
                <Col xs={20}>
                    {added && (<Alert variant="success">Course {added} has been added</Alert>)}
                    {error &&<Alert variant="danger">{error}</Alert>}
                    <Card>
                        <Card.Header>
                            Add a new course
                        </Card.Header>
                        <Card.Body>
                            <Form>
                                <Row>
                                    <Col>
                                    <FormControl placeholder="course name" type="text" name="coursename" value={coursename} onChange={this.courseinfoChange}/>
                                    </Col>
                                    <Col  xs={5}>
                                    <FormControl placeholder="course title" type="text" name="coursetitle" value={coursetitle} onChange={this.courseinfoChange}/>
                                    </Col>
                                    <Col>
                                    <FormControl placeholder="instructor" type="text" name="instructor" value={instructor} onChange={this.courseinfoChange}/>
                                    </Col>
                                    <Col>
                                    <FormControl placeholder="workload" type="text" name="workload" value={workload} onChange={this.courseinfoChange}/>
                                    </Col>
                                </Row>
                            </Form>
                        </Card.Body>
                        <Card.Footer style={{"textAlign":"right"}}>
                            <Button size="sm" type="Button" onClick={this.submitForm}><FontAwesomeIcon icon={faPlus}/> Add</Button>{' '}{' '}
                            <Button size="sm" type="Button" onClick={this.resetForm}><FontAwesomeIcon icon={faUndo}/> Reset</Button>
                        </Card.Footer>
                    </Card>
                </Col>
            </Row>
        );
    }
}