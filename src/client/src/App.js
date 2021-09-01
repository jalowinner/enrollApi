import './App.css';
import { BrowserRouter as Router, Switch, Route } from "react-router-dom";
import { Col, Container ,Row} from 'react-bootstrap';
import Login from "./components/login";
import SignUp from "./components/signup";
import Navigation from './components/navbar';
import Welcome from './components/welcome';
import MyCourses from './components/mycourses';
import Search from './components/listcourse';
import PassedCourses from './components/passedcourses';
import ManageCourses from './components/managecourse';
import ManageUsers from './components/manageuser';
import AddCourse from './components/addcourse';
import Hello from './components/guest';

function App() {
  return (
    
      <Router>

        <Navigation />
        <Container>
          <Row>
            <Col lg={12} className={"margin-top"}>
              <Switch>
                <Route path="/" exact component={Hello}/>
                <Route path="/home" exact component={()=>(<Welcome username={localStorage.username}/>)}/>
                <Route path="/mycourse" exact component={MyCourses}/>
                <Route path="/search" exact component={Search}/>
                <Route path="/passedcourse" exact component={PassedCourses}/>
                <Route path="/manageuser" exact component={ManageUsers}/>
                <Route path="/managecourse" exact component={ManageCourses}/>
                <Route path="/addcourse" exact component={AddCourse}/>
                <Route path="/login" exact component={Login}/>
                <Route path="/signup" exact component={SignUp}/>
                <Route path="/logout" exact component={() => (<Login message="User Logged Out Successfully." />)}/>
              </Switch>
            </Col>
          </Row>
        </Container>
      </Router>
    
    
  );
}

export default App;
