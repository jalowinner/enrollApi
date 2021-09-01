import react from 'react';
import { Button,Card, Table } from 'react-bootstrap';
import axios from 'axios';
import addHeader from '../services/authHeader';
import { DOMAIN } from './domain';
export default class MyCourses extends react.Component{
    constructor (props){
        super(props);
        this.state=this.initialState
    }
    initialState={
        courses:[]
    }
    componentDidMount(){
        addHeader(localStorage.jwtToken);
        axios.get(DOMAIN+'/enrolled')
        .then((response)=>{
            this.setState({courses:response.data});
            console.log(this.state.courses);
        }).catch( (error)=>{
            console.log(error);
            localStorage.clear();
            this.props.history.push("/login")
        })
    }
    withdrawAction = (coursename) =>{
        addHeader(localStorage.jwtToken);
        axios.delete(DOMAIN+"/withdraw", { params:{coursename: coursename}})
        .then(response =>{
            console.log(response.data);
            this.setState({courses: this.state.courses.filter(course => {
                return course.coursename !== response.data.coursename;
            })});
        })
        .catch(error=>{
            console.error(error.message);
            localStorage.clear();
            this.props.history.push("/login");
        });
    } 
    render() {
        return (
            <Card className={"border "}>
                <Card.Header>登録した科目</Card.Header>
                <Card.Body>
                    <Table>
                        <thead>
                            <tr>
                            <th>Course Code</th>
                            <th>Course Name</th>
                            <th>Instructor</th>
                            <th>Workload</th>
                            <th>Action</th>
                            </tr>
                        </thead>
                        <tbody>
                            {
                                this.state.courses.length === 0 ? 
                                <tr align="center"><td colSpan="5">No course enrolled</td></tr>:
                                this.state.courses.map((course)=>(
                                    <tr key={course.id}>
                                        <td>{course.coursename}</td>
                                        <td>{course.courseTitle}</td>
                                        <td>{course.instructor}</td>
                                        <td>{course.workload}</td>
                                        <td>
                                            <Button size="sm" variant="warning" onClick={()=>this.withdrawAction(course.coursename)}>withdraw</Button>
                                        </td>
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