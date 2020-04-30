function apiCall(endPoint, method, load, currThis, callback){
    var httpRequest = new XMLHttpRequest();
    httpRequest.open(method, 'http://localhost:4567/' + endPoint);
    httpRequest.withCredentials = true;
    httpRequest.send(JSON.stringify(load));
    httpRequest.onload = function(){
        callback(httpRequest.status, httpRequest.response, currThis);
    };
}
// Found online on stackoverflow
function getCookie(name) {
    var value = "; " + document.cookie;
    var parts = value.split("; " + name + "=");
    if (parts.length == 2) return parts.pop().split(";").shift();
}


function checkIfLoggedIn(currThis, callback){
    if (document.cookie == "") {
        callback(currThis, false)
    } else {
        // check if the cookie is a valid on
        apiCall("isValidUser", "POST", {"userSesh": getCookie("sessid")}, currThis, function (status, response, currThis) {
            if (status != 200) {
                callback(currThis, false, response);
            } else {
                callback(currThis, true, response);
            }
        });
    }
}


class Logout extends React.Component{
    constructor(props) {
        super(props);
        this.logout = this.logout.bind(this)
    }
    logout(){
        document.cookie = "sessid=; expires=Thu, 01 Jan 1970 00:00:00 GMT";
        apiCall("logOut", "GET", "", this, function(status, response, currThis){
            currThis.props.onChange("isLoggedIn", false);
        });

    }
    render() {
        return(
          <button onClick={this.logout}>logout</button>
        );
    }
}



class GuestGreeting extends React.Component{
    constructor(props) {
        super(props);
        this.state = {
            email: "",
            password: "",
            isOrg: false,
            error:false,

        };
        this.handleInputChange = this.handleInputChange.bind(this);
        this.logIn= this.logIn.bind(this)
    }

    handleInputChange(e) {
        const target = e.target;
        const value = target.name === 'isOrg' ? target.checked : target.value;
        const name = target.name;
        this.setState({
            [name]: value
        });
    }
    logIn() {
        var load = {
            userEmail:this.state.email,
            userPassword:this.state.password,
            userType:this.state.isOrg
        };
        apiCall("logIn", "POST", load, this,function (status, body, currThis){
            if (status != 200){
                currThis.setState({error: true});
            }else {
                window.location.href = "dashboard.html";
            }
        });
    }
    render() {
        return (
            <div className="wrap-login100">
                <div className="login100-more" style={{backgroundDomage:"url('img/member.jpg')"}}>
                </div>
                <div className="login100-form validate-form">
					<span className="login100-form-title p-b-43">
						Login to continue
					</span>
                    <p style={{color:"red"}} hidden={!this.state.error}>Error with logging in please try again</p>
                    <div className="wrap-input100">
                        <input className="input100" id="userEmail" value={this.state.email} onChange={this.handleInputChange} type="text" name="email" />
                        <span className="focus-input100"></span>
                        <span className="label-input100">Email</span>
                    </div>


                    <div className="wrap-input100 validate-input">
                        <input className="input100" type="password" name="password" id="userPassword" value={this.state.password} onChange={this.handleInputChange} />
                            <span className="focus-input100"></span>
                            <span className="label-input100">Password</span>
                    </div>
                    <label> Are you an organization < /label>
                        <input
                            id="orgtype"
                            type="checkbox"
                            name="isOrg"
                            checked={this.state.isOrg}
                            onChange={this.handleInputChange} /><br/>
                    <div className="container-login100-form-btn">
                        <button disabled={!(this.state.password && this.state.email)} onClick={this.logIn} className="login100-form-btn">
                            Login
                        </button>
                    </div>
                </div>
            </div>
    )
    }
}

class Login extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            isLoggedIn:false,
            user:"",
            isOrgAccount:false
        };
        this.handleChange = this.handleChange.bind(this);
    }
    componentDidMount() {
        checkIfLoggedIn(this, function(currThis, resp, userBody){
            currThis.setState({isLoggedIn:resp});
            if (resp){
                window.location.href = "dashboard.html";
            }
        });
    }
    handleChange(key, value){
        this.setState({[key]:value});
    }
    render() {
        return <GuestGreeting onChange={this.handleChange}/>;
    }
}


ReactDOM.render(
  <Login />,
  document.getElementById("root")
);


