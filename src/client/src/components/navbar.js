import react ,{ Component } from 'react';
import { Navbar, Nav} from 'react-bootstrap';
import { Link } from 'react-router-dom';
import { connect } from 'react-redux';
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {
    faUserPlus,
    faSignInAlt,
    faSignOutAlt,
  } from "@fortawesome/free-solid-svg-icons";
import { logoutUser } from '../services';
import { withRouter } from 'react-router';


class Navigation extends Component{
    constructor(props){
        super(props);
    }

    logout = () =>{
        this.props.logoutUser();
    }
    render(){
        const guestMenu = (
            <>
                <div className="ms-auto"></div>
                <Nav className="navbar-right">
                    <Link to={"signup"} className="nav-link"><FontAwesomeIcon icon={faUserPlus} />Sign up</Link>
                    <Link to={"login"} className="nav-link"><FontAwesomeIcon icon={faSignInAlt} />Login</Link>
                </Nav>
            </>
        );
        const userMenu = (
            <>
                <Nav className="me-auto">
                    <Link to={"search"} className="nav-link">Course List</Link>
                    <Link to={"mycourse"} className="nav-link">My Courses</Link>
                    <Link to={"passedcourse"} className="nav-link">Past Courses</Link>
                </Nav>
                <Nav className="navbar-right">
                    <Link to={"logout"} className="nav-link" onClick={this.logout}><FontAwesomeIcon icon={faSignOutAlt} />Logout</Link>
                </Nav>
            </>
        );

        const adminMenu = (
            <>
                <Nav className="me-auto">
                    <Link to={"manageuser"} className="nav-link">Manage Users</Link>
                    <Link to={"managecourse"} className="nav-link">Manage Courses</Link>
                    <Link to={"addcourse"} className="nav-link">Add Course</Link>
                </Nav>
                <Nav className="navbar-right">
                    <Link to={"logout"} className="nav-link" onClick={this.logout}><FontAwesomeIcon icon={faSignOutAlt} />Logout</Link>
                </Nav>
            </>
        );
        return (
            <Navbar bg="light" variant="light">
                
                <Link to={localStorage.username? "home": ""} className="navbar-brand"><img src="/icon.png" width="45" height="45" alt="brand"/>{" "}XUE</Link>
                {localStorage.jwtToken ? (localStorage.role == "admin"? adminMenu:userMenu) : guestMenu}
            </Navbar>
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
        logoutUser: () => dispatch(logoutUser())
    };
}
export default withRouter(connect(mapStateToProps, mapDispatchToProps)(Navigation));
