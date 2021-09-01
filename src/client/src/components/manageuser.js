import react from 'react';
import { Button,Card, Table, Row, Col, Alert, Modal} from 'react-bootstrap';
import axios from 'axios';
import addHeader from '../services/authHeader';
import '../App.css';
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faClipboardCheck, faUserSlash } from "@fortawesome/free-solid-svg-icons"; 
import { DOMAIN } from './domain';
export default class ManageUsers extends react.Component{
    constructor(props){
        super(props);
        this.state = this.initialState;
    }
    initialState = {
        users:[],
        removed:"",
        error:"",
        courselist:[],
        show:false,
        editing:"",
        courseSuccess:"",
        couresErr:""
    }
    componentDidMount(){
        addHeader(localStorage.jwtToken);
        setTimeout(() => {
            axios.get(DOMAIN+'/listusers')
            .then((response)=>{
                this.setState({users:response.data});
            }).catch( (error)=>{
                console.log(error)
                localStorage.clear();
                this.props.history.push("/login")
            });
        }, 200);
    }
    removeAction = (e, username) =>{
        e.stopPropagation();
        addHeader(localStorage.jwtToken);
        axios.delete(DOMAIN+"/removeuser", { params:{username: username}})
        .then(response =>{
            
            this.setState({users: this.state.users.filter(user => {
                return user.username !== response.data.username;
            }), removed:response.data.username, error:''});
        })
        .catch(error=>{
            this.setState({error:error.response.data.error})
        });
    } 
    hide = () =>{
        this.setState({show:false, courseSuccess:"", courseErr:""})
    }
    editCourse = (username) => {
            addHeader(localStorage.jwtToken)
            axios.get(DOMAIN+'/getUserCourse', { params: { username: username } })
            .then((response)=>{
                this.setState({courselist:response.data, show:true, editing:username});
            }).catch( (error)=>{
                console.log(error);
                this.setState({error:error.response.data.error});
            });
    }
    passAction = (coursename) => {
        addHeader(localStorage.jwtToken)
        axios.post(DOMAIN+'/passAction', {username: this.state.editing, coursename: coursename})
        .then((response)=>{
            this.setState({courseSuccess:"User "+this.state.editing+" has passed "+ response.data.coursename, courselist:this.state.courselist.filter(course=>{return course.coursename!==response.data.coursename}), courseErr:""})
        }).catch((error)=>{
            this.setState({courseErr:error.response.data.error})
        });
    }
    dischargeFromCourseAction = (coursename) => {
        addHeader(localStorage.jwtToken)
        axios.delete(DOMAIN+'/adminWithdraw', { params:{username: this.state.editing, coursename:coursename}})
        .then((response)=>{
            this.setState({courseSuccess:"User "+this.state.editing+" has been discharged from "+ response.data.coursename, courselist:this.state.courselist.filter(course=>{return course.coursename!==response.data.coursename}), courseErr:""})
        }).catch((error)=>{
            this.setState({courseErr:error.response.data.error})
        });
    }
    render() {
        return (
            <>
            <Row className="justify-content-md-center">
                <Col xs={6}>
                    {this.state.removed && (<Alert variant="success">User {this.state.removed} has been removed</Alert>)}
                    {this.state.error &&<Alert variant="danger">{this.state.error}</Alert>}
                    <Card>
                        <Card.Header>ユーザー管理</Card.Header>
                        <Card.Body>
                            <Table striped bordered hover="true">
                                <thead>
                                    <tr>
                                    <th className="text-center">Username</th>
                                    <th className="text-center">Role</th>
                                    <th className="text-center">Action</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    {
                                        this.state.users.length === 0 ? 
                                        <tr align="center" ><td colSpan="3">No user in System</td></tr>:
                                        this.state.users.map((user) => (
                                            
                                            <tr key={user.id} onClick={()=>this.editCourse(user.username) } style={{cursor:"pointer"}}>
                                                <td className="text-center">{user.username}</td>
                                                <td className="text-center">{user.role}</td>
                                                <td size = "sm" className="text-center">
                                                    <Button size="sm" variant="danger" onClick={(e)=>this.removeAction(e, user.username)}>Remove</Button>{' '}{' '}
                                                </td>
                                                
                                            </tr>
                                        ))
                                    }   
                                </tbody>
                            </Table>
                        </Card.Body>
                    </Card>
                </Col>
            </Row>
            
            <Modal dialogClassName="my-modal" show={this.state.show} onHide={this.hide}>
            <Modal.Header>
              <Modal.Title>{"Enrolled Courses of "+this.state.editing}</Modal.Title>
            </Modal.Header>
          
            <Modal.Body>
                {this.state.courseSuccess && (<Alert variant="success">{this.state.courseSuccess}</Alert>)}
                {this.state.courseErr &&<Alert variant="danger">{this.state.courseErr}</Alert>}
                <Table bordered>
                    <thead>
                        <tr>
                            <th className="text-center">Course name</th>
                            <th className="text-center">Title</th>
                            <th className="text-center">Instructor</th>
                            <th className="text-center">Workload</th>
                            <th className="text-center">Action</th>
                        </tr>
                    </thead>
                        <tbody>
                            {
                                this.state.courselist.length === 0 ? <tr align="center" ><td colSpan="5">No course enrolled yet</td></tr>:
                                this.state.courselist.map((course)=>(
                                    <tr key={course.id}>
                                        <td className="text-center">{course.coursename}</td>
                                        <td className="text-center">{course.courseTitle}</td>
                                        <td className="text-center">{course.instructor}</td>
                                        <td className="text-center">{course.workload}</td>
                                        
                                        <td size = "sm" className="text-center"><FontAwesomeIcon icon={faClipboardCheck} onClick={()=>this.passAction(course.coursename)} style={{cursor:"pointer"}}/>&nbsp;&nbsp;&nbsp;<FontAwesomeIcon icon={faUserSlash} onClick={()=>this.dischargeFromCourseAction(course.coursename)} style={{cursor:"pointer"}}/></td>
                                    </tr>
                                ))
                            }
                        </tbody>
                </Table>
            </Modal.Body>
          
            <Modal.Footer>
              <Button variant="secondary" onClick={this.hide}>Close</Button>
            </Modal.Footer>
          </Modal>
          </>
        );
    }
}
