import react from 'react';
import { Button, Card, Table } from 'react-bootstrap';
import axios from 'axios';
import addHeader from '../services/authHeader';
import { DOMAIN } from './domain';
export default class Search extends react.Component{
    constructor (props){
        super(props);
        this.state= this.initialState;
    }
    initialState = {
        courses:[],
        enrolledcourses:[],
        finishedcourses:[],
        availcourses:[]

    }
    componentDidMount(){
        addHeader(localStorage.jwtToken);

        setTimeout(() => {
            Promise.all([
                axios.get(DOMAIN+'/search'),
                axios.get(DOMAIN+'/enrolled'),
                axios.get(DOMAIN+'/finished')
              ]).then(([r1, r2, r3]) => {
                this.setState({courses:r1.data});
                this.setState({enrolledcourses:r2.data});
                this.setState({finishedcourses:r3.data});
                let d1 = this.state.courses.filter(x => !this.state.enrolledcourses.some(e=>e.id === x.id));
                let d2 = d1.filter(x => !this.state.finishedcourses.some(e=>e.id === x.id));
                this.setState({availcourses:d2});
              }).catch(error => {
                console.error(error.message);
                localStorage.clear();
                this.props.history.push("/login");
              });
        }, 200); 
        
    }


    enrollAction = coursename =>{
        addHeader(localStorage.jwtToken);
        axios.post(DOMAIN+"/enroll", {
            coursename: coursename
          })
        .then(response =>{
            console.log(response.data);
            this.setState({availcourses: this.state.availcourses.filter(course => {
                return course.coursename !== response.data.coursename;
            })});;
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
                <Card.Header>科目登録</Card.Header>
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
                                this.state.availcourses.length === 0 ? 
                                <tr align="center"><td colSpan="5">No course available to enroll</td></tr>:
                                this.state.availcourses.map((course)=>(
                                    <tr key={course.id}>
                                        <td>{course.coursename}</td>
                                        <td>{course.courseTitle}</td>
                                        <td>{course.instructor}</td>
                                        <td>{course.workload}</td>
                                        <td>
                                            <Button size="sm" variant="success" onClick={()=>this.enrollAction(course.coursename)}>enroll</Button>
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