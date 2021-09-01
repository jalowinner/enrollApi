import react from 'react';
import { Button,Card, Table, Row, Col, Alert} from 'react-bootstrap';
import axios from 'axios';
import addHeader from '../services/authHeader';
import { DOMAIN } from './domain';
export default class ManageCourses extends react.Component{
    constructor(props){
        super(props);
        this.state = this.initialState;
    }
    initialState = {
        courses:[],
        removed:"",
        error:""
    }
    componentDidMount(){
        addHeader(localStorage.jwtToken);

        setTimeout(() => {
            axios.get(DOMAIN+'/search')
            .then((response)=>{
                this.setState({courses:response.data});
            }).catch( (error)=>{
                console.log(error)
                localStorage.clear();
                this.props.history.push("/login")
            });
        }, 200);
    }
    removeAction = (coursename) =>{
        addHeader(localStorage.jwtToken);
        axios.delete(DOMAIN+"/removecourse", { params:{coursename: coursename}})
        .then(response =>{
            this.setState({courses: this.state.courses.filter(course => {
                return course.coursename !== response.data.coursename;
            }), removed:response.data.coursename, error:''});
        })
        .catch(error=>{
            this.setState({error:error.response.data.error})
        });
    } 

    render() {
        return (
            <Row className="justify-content-md-center">
                <Col xs={8}>
                    {this.state.removed && (<Alert variant="success">Course {this.state.removed} has been removed</Alert>)}
                    {this.state.error &&<Alert variant="danger">{this.state.error}</Alert>}
                    <Card>
                        <Card.Header>履修登録管理</Card.Header>
                        <Card.Body>
                            <Table striped bordered>
                                <thead>
                                    <tr>
                                    <th className="text-center">Course name</th>
                                    <th className="text-center">Course title</th>
                                    <th className="text-center">Instructor</th>
                                    <th className="text-center">Workload</th>
                                    <th className="text-center">Action</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    {
                                        this.state.courses.length === 0 ? 
                                        <tr align="center"><td colSpan="5">No course in System</td></tr>:
                                        this.state.courses.map((course)=>(
                                            <tr key={course.id}>
                                                <td className="text-center">{course.coursename}</td>
                                                <td className="text-center">{course.courseTitle}</td>
                                                <td className="text-center">{course.instructor}</td>
                                                <td className="text-center">{course.workload}</td>
                                                <td className="text-center">
                                                    <Button size="sm" variant="danger" onClick={()=>this.removeAction(course.coursename)}>Remove</Button>
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
        );
    }
}