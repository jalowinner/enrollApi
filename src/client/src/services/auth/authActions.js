import { FAILURE, LOGIN_REQUEST, SUCCESS ,LOGOUT_REQUEST} from './authTypes';
import axios from "axios";

const LOGIN_URL = "http://localhost:8080/login";
export const authenticateUser = (username, password)  =>

{
    return dispatch => {
        dispatch(loginRequest());

        setTimeout(()=> {
            axios.post(LOGIN_URL, {
                username: username,
                password: password,
            })
            .then(
            response=> {
                localStorage.setItem("jwtToken", response.data.token);
                dispatch(success({ username: response.data.username, isLoggedIn: true }));
                
            })
            .catch(error=>{
                dispatch(failure());
            });
        }, 200) 
    };
}
/*=> async (dispatch) => {
    dispatch(loginRequest());
    let body = new FormData()
    body.append("username",username)
    body.append("password",password)
    try {
      const response = await axios.post(LOGIN_URL, body);
      dispatch(success({ username: response.data.username, isLoggedIn: true }));
      return Promise.resolve(response.data);
    } catch (error) {
      dispatch(failure());
      return Promise.reject(error);
    }
};*/







export const logoutUser = () => {
    return dispatch => {
      dispatch(logoutRequest());
      localStorage.clear();
      dispatch(success({ username: "", isLoggedIn: false }));
    };
};

const logoutRequest = () => {
    return {
        type: LOGOUT_REQUEST
    };
}

const loginRequest = () => {
    return {
        type: LOGIN_REQUEST
    };
}

const success = isLoggedIn => {
    return {
        type: SUCCESS,
        payload: isLoggedIn
    };
}

const failure = () => {
    return {
        type: FAILURE,
        payload: false
    };
}