function apiCall(endPoint, method, load, currThis, callback){
    var httpRequest = new XMLHttpRequest();
    httpRequest.open(method, 'http://localhost:4567/' + endPoint);
    httpRequest.withCredentials = true;
    httpRequest.send(JSON.stringify(load));
    httpRequest.onload = function(){
        console.log(httpRequest.getResponseHeader("error"));

        callback(httpRequest.status, httpRequest.response, httpRequest.getResponseHeader("error"), currThis);
    };
}


class GuestGreeting extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            name: "",
            email: "",
            password: "",
            phone: "",
            birthday: "",
            gender: "true",
            isOrg: false,
            nameError:false,
            emailError:false,
            passwordError:false,
            phoneError:false,
            userError:false,
            error:false
        };
        this.handleInputChange = this.handleInputChange.bind(this);
    }

    handleInputChange(e) {
        if (this.state.isOrg) {
            userType:this.state.type = true;
            this.state.gender = true;
            this.state.birthday = "1111-11-11";
        }
        const target = e.target;
        var value = target.name === 'isOrg' ? target.checked : target.value;
        const name = target.name;
        this.setState({
            [name]: value
        });
    }


    signUp(){
        var load = {
            userName:this.state.name,
            userEmail:this.state.email,
            userPhone:this.state.phone,
            userBirthday:this.state.birthday,
            userGender:this.state.gender,
            userPassword:this.state.password,
            userType:this.state.type
        };

        apiCall("signUp", "POST", load, this,function (status, body, error, currThis){
            if (status != 200){
               currThis.setState({error: true});
               if (error == "Email format incorrect"){
                  currThis.setState({emailError:true})
               }
               else {
                   currThis.setState({emailError:false})
               }
               if (error == "Password not acceptable"){
                  currThis.setState({passwordError:true})
               }
               else {
                   currThis.setState({passwordError:false})
               }
               if (error == "Username not acceptable"){
                   currThis.setState({nameError:true})
               }
               else {
                   currThis.setState({nameError:false})
               }
               if (error == "userPhone not acceptable"){
                   currThis.setState({phoneError:true})
               }
               else {
                   currThis.setState({phoneError:false})
               }
               if (status == 409){
                   currThis.setState({userError:true})
               }
            }
            else {
                //currThis.setState({error: false});
                window.location.href = "login.html";
            }
        });
    }


    render(){
        return (
            <div className="wrap-login100">
                <div className="login100-form validate-form">
                    <img src="../img/calendar.png" id="img"/>
                    <span className="login100-form-title p-b-43">
                        Sign Up
                    </span>
                <p style={{color:"red"}} hidden={!this.state.error}>Error with signing up please try again</p>
                <p style={{color:"red"}} hidden={!this.state.userError}>A user with this email already exists</p>
                <div className="wrap-input100">
                    <input
                    id="userName"
                    type="text"
                    name="name"
                    value={this.state.name}
                    onChange={this.handleInputChange}
                    className="input100"
                    />
                    <span className="focus-input100"></span>
                    <span hidden={this.state.name != ""} className="label-input100">Name</span>
                </div>
                <p style={{color:"red"}} hidden={!this.state.nameError}>invalid user name</p>
                <div className="wrap-input100">
                    <input
                    id="userEmail"
                    type="text"
                    name="email"
                    value={this.state.email}
                    onChange={this.handleInputChange}
                    className="input100"
                    />
                    <span className="focus-input100"></span>
                    <span hidden={this.state.email != ""} className="label-input100">Email</span>
                </div>
                <p style={{color:"red"}} hidden={!this.state.emailError}>invalid email</p>
                <div className="wrap-input100">
                    <input
                    id="userPhone"
                    type="text"
                    name="phone"
                    value={this.state.phone}
                    onChange={this.handleInputChange}
                    className="input100"
                    />
                    <span className="focus-input100"></span>
                    <span hidden={this.state.phone != ""} className="label-input100">Phone</span>
                </div>
                <p style={{color:"red"}} hidden={!this.state.phoneError}>invalid phone number</p>
                <div className="wrap-input100">
                    <input value={this.state.birthday} hidden={this.state.isOrg}
                        id="userBirthday"
                        type="date"
                        name="birthday"
                        value={this.state.birthday}
                        onChange={this.handleInputChange}
                        className="input100"
                    />
                    <span className="focus-input100"></span>
                    <span hidden={this.state.birthday != "" } className="label-input100">Birthday</span>
                </div>
                <br hidden={this.state.isOrg}/>
                <div className="wrap-input100" hidden={this.state.isOrg}>
                    <label>Gender</label>
                    <select  className="input100" value={this.state.gender} id="userGender" name="gender" hidden={this.state.isOrg} onChange={this.handleInputChange}>
                        <option value="true">Male</option>
                        <option value="false">Female</option>
                        <option value="true">Other</option>
                    </select>
                    <span className="focus-input100"></span>
                </div>
                <div className="wrap-input100">
                        <input
                        id="userPassword"
                        type="password"
                        name="password"
                        value={this.state.password}
                        onChange={this.handleInputChange}
                        className="input100"
                        />
                        <span className="focus-input100"></span>
                        <span hidden={this.state.password != ""} className="label-input100">Password</span>
                </div>
                <p style={{color:"red"}} hidden={!this.state.passwordError}>password must have at least 6 characters</p>
                <br/>
                <label> Are you an organization? &nbsp;
                    <input
                    id="userType"
                    type="checkbox"
                    name="isOrg"
                    value={this.state.isOrg}
                    onChange={this.handleInputChange}
                    />
                </label>
                <div className="container-login100-form-btn">
                    <button
                            disabled={(this.state.isOrg && !(this.state.name && this.state.email && this.state.phone && this.state.password))||(!this.state.isOrg && !(this.state.name && this.state.email && this.state.phone && this.state.birthday && this.state.gender && this.state.password))}
                            onClick={() => this.signUp()}
                            className="login100-form-btn"> Signup </button>
                </div>
                <div className="container-login100-form-btn">
                    <label>Already signed up? </label>
                    <br></br>
                    <button onClick={() => window.location.href = "login.html"} className="login100-form-btn"> Log in here </button>
                </div>

                </div>
            </div>
        )
    }
}


class Signup extends React.Component {
    constructor(props){
        super(props);
        this.state = {
        isSignedUp:false,
        user:""
    };
        this.handleChange = this.handleChange.bind(this);
    }

    handleChange(key, value){
        this.setState({[key]:value});
    }

    render() {
        return <GuestGreeting onChange={this.handleChange}/>;
    }

}

ReactDOM.render(
    <Signup />,
    document.getElementById('root')
);


