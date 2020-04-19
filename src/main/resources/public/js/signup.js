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

class UserGreeting extends React.Component {
    constructor(props) {
        super(props);
    }
    render() {
        return (
            <div>
                <h1>Hi {this.props.name}! You are signed up.</h1>
            </div>
        );
    }
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
            gender: "",
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
        const target = e.target;
        const value = target.name === 'isOrg' ? target.checked : target.value;
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
            //userType:this.state.isOrg
        };

        apiCall("signUp", "POST", load, this,function (status, body, error, currThis){
            if (status != 200){
               currThis.setState({error: true});
               if (error == "Email format incorrect"){
                  currThis.setState({emailError:true})
               }
               if (error == "Password not acceptable"){
                  currThis.setState({passwordError:true})
               }
               if (error == "Username not acceptable"){
                   currThis.setState({nameError:true})
               }
               if (error == "userPhone not acceptable"){
                   currThis.setState({phoneError:true})
               }
               if (status == 409){
                   currThis.setState({userError:true})
               }
            }
            else {
                currThis.props.onChange("user", JSON.parse(body).name);
                currThis.props.onChange("isSignedUp", true);
                currThis.setState({error: false});
                currThis.setState({emailError:false})
                currThis.setState({passwordError:false})
                currThis.setState({nameError:false})
                currThis.setState({phoneError:false})
                currThis.setState({userError:false})
            }
        });
    }


    render(){
        return (
            <div>
            <h3>Sign Up</h3>
            <p style={{color:"red"}} hidden={!this.state.error}>Error with signing up please try again</p>
            <p style={{color:"red"}} hidden={!this.state.userError}>A user with this email already exists</p>
            <label> name </label>
            <input
                id="userName"
                type="text"
                name="name"
                value={this.state.name}
                onChange={this.handleInputChange}
            />
            <p style={{color:"red"}} hidden={!this.state.nameError}>invalid user name</p>
            <br/>
            <label> email </label>
            <input
                id="userEmail"
                type="text"
                name="email"
                value={this.state.email}
                onChange={this.handleInputChange}
            />
            <p style={{color:"red"}} hidden={!this.state.emailError}>invalid email</p>
            <br/>
            <label> phone </label>
            <input
                id="userPhone"
                type="text"
                name="phone"
                value={this.state.phone}
                onChange={this.handleInputChange}
            />
            <p style={{color:"red"}} hidden={!this.state.phoneError}>invalid phone number</p>
            <br/>
            <label hidden={this.state.isOrg}> birthday </label>
            <input hidden={this.state.isOrg}
                   id="userBirthday"
                   type="date"
                   name="birthday"
                   value={this.state.birthday}
                   onChange={this.handleInputChange}
            />
            <br hidden={this.state.isOrg}/>
            <label hidden={this.state.isOrg}> gender </label>
            <select id="userGender" name="gender" hidden={this.state.isOrg} value={this.state.gender} onChange={this.handleInputChange}>
                    <option value="male">Male</option>
                    <option value="female">Female</option>
                    <option value="other">Other</option>

            </select>
            <br hidden={this.state.isOrg}/>
            <label> password </label>
            <input
                id="userPassword"
                type="password"
                name="password"
                value={this.state.password}
                onChange={this.handleInputChange}
            />
            <p style={{color:"red"}} hidden={!this.state.passwordError}>password must have at least 6 characters</p>
            <br/>
            <label> Are you an organization < /label>
                <input
                    id="orgType"
                    type="checkbox"
                    name="isOrg"
                    value={this.state.isOrg}

                    onChange={this.handleInputChange}
                />
                <button disabled={(this.state.isOrg && !(this.state.name && this.state.email && this.state.phone && this.state.password))||(!this.state.isOrg && !(this.state.name && this.state.email && this.state.phone && this.state.birthday && this.state.gender && this.state.password))} onClick={() => this.signUp()}> signup </button>
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
        if(this.state.isSignedUp) {
            return <UserGreeting name={this.state.user} onChange={this.handleChange}/>;
        }
        return <GuestGreeting onChange={this.handleChange}/>;
    }

}

ReactDOM.render(
    <Signup />,
    document.getElementById('root')
);

