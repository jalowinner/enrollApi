import react from 'react';
import { Card, Table } from 'react-bootstrap';
import axios from 'axios';
import addHeader from '../services/authHeader';
import { DOMAIN } from './domain';
export default class PassedCourses extends react.Component{
    constructor (props){
        super(props);
        this.state={
            courses:[]
        }
    }
    componentDidMount(){
        addHeader(localStorage.jwtToken);
        axios.get(DOMAIN+'/finished')
        .then((response)=>{
            this.setState({courses:response.data});
            console.log(this.state.courses);
        }).catch( (error)=>{
            localStorage.clear();
            this.props.history.push("/login")
        })
    }
    render() {
        return (
            <Card className={"border "}>
                <Card.Header>履修済みの科目</Card.Header>
                <Card.Body>
                    <Table>
                        <thead>
                            <tr>
                            <th>Course Code</th>
                            <th>Course Name</th>
                            <th>Instructor</th>
                            <th>Workload</th>
                            </tr>
                        </thead>
                        <tbody>
                            {
                                this.state.courses.length === 0 ? 
                                <tr align="center"><td colSpan="4">No course available</td></tr>:
                                this.state.courses.map((course)=>(
                                    <tr key={course.id}>
                                        <td>{course.coursename}</td>
                                        <td>{course.courseTitle}</td>
                                        <td>{course.instructor}</td>
                                        <td>{course.workload}</td>
                                    </tr>
                                ))
                        
                            }
                        </tbody>
                    </Table>
                </Card.Body>
            </Card>
            
        )
    }
}