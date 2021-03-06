function apiCall(endPoint, method, load, currThis, callback){
    var httpRequest = new XMLHttpRequest();
    httpRequest.open(method, 'http://localhost:4567/' + endPoint);
    httpRequest.withCredentials = true;
    httpRequest.responseType = "json";
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

function getEvents_fromAPI(curThis,callback){
    const httpRequest = new XMLHttpRequest();
    httpRequest.open('GET','http://localhost:4567/myEvents');
    httpRequest.responseType = "json";
    httpRequest.withCredentials = true;
    httpRequest.send(null);
    httpRequest.onload = function() {
        console.log(httpRequest.status);
        var jsonResponse = httpRequest.response;
        callback(jsonResponse,curThis);
    };
}

class Logout extends React.Component{
    constructor(props) {
        super(props);
        this.logout = this.logout.bind(this)
    }
    logout(){
        document.cookie = "sessid=; expires=Thu, 01 Jan 1970 00:00:00 GMT";
        apiCall("logOut", "GET", "", this, function(status, response, currThis){
            window.location.href = "login.html";

            //currThis.props.onChange("isLoggedIn", false);
        });

    }
    render() {
        return(
            <nav className="navbar navbar-expand-lg navbar-light bg-light">
                <a className="navbar-brand">Evant Dashboard</a>
                <button className="navbar-toggler" type="button" data-toggle="collapse"
                        data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent"
                        aria-expanded="false" aria-label="Toggle navigation">
                    <span className="navbar-toggler-icon"></span>
                </button>
                    <div className="container-login100-form-btn">
                        <td>
                            <button onClick={this.logout} className="login100-form-btn" >Logout</button>
                        </td>
                    </div>
            </nav>

        );
    }
}

class UserDashboard extends React.Component {
    constructor(props) {
        super(props);
    }
    render(){
        return (
            <div>
                <Logout/>
                <UserEvents listMyEvents={this.props.listMyEvents} />
                <UpcomingEvents listMyEvents={this.props.listMyEvents} addToMyEvents={this.props.addToMyEvents}/>
            </div>
        )
    }
}
class UserEvents extends React.Component {
    constructor(props){
        super(props);
    };
    leaveEvent(eventName, eventOrg) {
        apiCall("leaveEvent", "POST", {"eventName":eventName, "orgName":eventOrg}, this, function(status, response, currThis){
            if(status != 200){
            }else{
                window.location.href = "dashboard.html";
                var currEvents = currThis.state.listEvents;
                var eventObjectIndex  = currEvents.findIndex(i => i.name == eventName && i.orgName == eventOrg)
                currThis.props.removeFromMyEvents(currEvents[eventObjectIndex]);
                currEvents.splice(eventObjectIndex,1);
                currThis.setState({listEvents:currEvents});
            }
        });
    }

    getEventPage(event){
        this.props.handleChange("eventSelect", true);
        this.props.handleChange("eventName", event.name);
        this.props.handleChange("eventDate", event.date);
        this.props.handleChange("eventMessage", event.eventMessage);
    }


    render() {
        return (
            <div>
                <hr />
                <h2>My Events</h2>
                <table className="table table-hover">
                    <thead className="thead-dark">
                    <tr>
                        <th scope="col">Event Name</th>
                        <th scope="col">Organization</th>
                        <th scope="col">Date</th>
                        <th scope="col">Message</th>
                        <th hidden={this.props.org} scope="col">Leave Event</th>

                    </tr>
                    </thead>
                    <tbody>
                    {this.props.listMyEvents.map( (e,i) => {

                        return (
                            <tr onClick={() => this.getEventPage(e)} key={i}>
                                <td> {e.name} </td>
                                <td> {e.orgName} </td>
                                <td> {e.date} </td>
                                <td> {e.eventMessage} </td>
                                <td hidden={this.props.org}>
                                    <button type="button" className="btn btn-primary"
                                            onClick={() => this.leaveEvent(e.name, e.orgName)}>Leave Event
                                    </button>
                                </td>


                            </tr>
                        )
                    })}
                    </tbody>
                </table>
            </div>
        );
    }
}
class UpcomingEvents extends React.Component {
    constructor(props){
        super(props);
        this.state = {listEvents: [""]};
        this.joinEvent = this.joinEvent.bind(this);
    };
    componentDidMount() {
        apiCall("upcomingEvents","GET", {}, this, function(status, response, currThis){
            var all_events = Object.values(response);
            function checkIfExists(element){
                for (var index=0; index<currThis.props.listMyEvents.length; index++){
                    var currElem = currThis.props.listMyEvents[index];
                    if (currElem.name == element.name && currElem.orgName == element.orgName){
                        return true;
                    }
                }
                return false;
            }
            var new_events = all_events.filter(i => !checkIfExists(i));
            currThis.setState({listEvents:new_events});
        });
    };
    componentDidUpdate(prevProps){
        if(this.props.listMyEvents != prevProps.listMyEvents){
            function checkIfExists(element, currThis){
                for (var index=0; index<currThis.props.listMyEvents.length; index++){
                    var currElem = currThis.props.listMyEvents[index];
                    console.log(currElem.name, currElem.orgName, element.name, element.orgName);
                    if (currElem.name == element.name && currElem.orgName == element.orgName){
                        return true;
                    }
                }
                return false;
            }
            var all_events = this.state.listEvents;
            var new_events = all_events.filter(i => !checkIfExists(i, this));
            this.setState({listEvents:new_events});
        }
    }
    joinEvent(eventName, eventOrg) {
        apiCall("joinEvent", "POST", {"eventName":eventName, "orgName":eventOrg}, this, function(status, response, currThis){
            if(status != 200){
            }else{
                var currEvents = currThis.state.listEvents;
                var eventObjectIndex  = currEvents.findIndex(i => i.name == eventName && i.orgName == eventOrg)
                currThis.props.addToMyEvents(currEvents[eventObjectIndex]);
                currEvents.splice(eventObjectIndex,1);
                currThis.setState({listEvents:currEvents});

            }
        });
    }

    render() {
        return (
            <div>
                <hr />
                <h2>Upcoming Public Events</h2>
                <table className="table">
                    <thead className="thead-dark">
                    <tr>
                        <th scope="col">Event Name</th>
                        <th scope="col">Organization</th>
                        <th scope="col">Date</th>
                        <th scope="col">Message</th>
                        <th scope="col">Join Event</th>
                    </tr>
                    </thead>
                    <tbody>
                    {this.state.listEvents.map( (e,i) => {
                        return (
                            <tr key={i}>
                                <td> {e.name} </td>
                                <td> {e.orgName} </td>
                                <td> {e.date} </td>
                                <td> {e.eventMessage} </td>
                                <td> <button type="button" className="btn btn-primary" onClick={() => this.joinEvent(e.name, e.orgName)} >Join Event</button></td>
                            </tr>
                        )
                    })}
                    </tbody>
                </table>
            </div>
        );
    }
}

class CreateEvent extends React.Component{
    constructor(props) {
        super(props);
        this.state={
            name:"",
            eventName:"",
            eventMessage:"",
            eventDate:"",
            error:false,
            errorMessage:""
        };
        this.createEvent = this.createEvent.bind(this);
        this.handleInputChange = this.handleInputChange.bind(this);
    }
    handleInputChange(e) {
        const target = e.target;
        const value = target.value;
        const name = target.name;
        this.setState({
            [name]: value
        });
    }
    createEvent(){
        console.log("creating event ", this.state.eventDate, this.state.eventMessage, this.state.eventName);
        var eventDate = this.state.eventDate.replace("T", " ");
        var eventDate = eventDate + ":00";
        var load = {
            eventName:this.state.eventName,
            eventDate:eventDate,
            eventMessage:this.state.eventMessage
        }
        apiCall("createEvent", "POST", load, this,function (status, body, currThis){
            if(status == 200){
                currThis.props.addToMyEvents({
                    name:load.eventName,
                    date:eventDate,
                    eventMessage:load.eventMessage,
                    orgName:currThis.props.orgName
                });
                currThis.setState({eventName:"", eventMessage:"", eventDate:""});
            }else{
                currThis.setState({error:true, errorMessage:body});
            }
        });
    }
    render() {
        return (
            <div>
                <hr />
                <div>
                    <span className="login100-form-title p-b-43">
                        Create a new event:
                    </span>
                <h4 style={{color:"red"}} hidden={!this.state.error}> ERROR: {this.state.errorMessage}</h4>

                <div className="wrap-input100">
                    <input
                        id="eventName"
                        type="text"
                        name="eventName"
                        value={this.state.eventName}
                        onChange={this.handleInputChange}
                        className="input100"
                    />
                    <span className="focus-input100"></span>
                    <span hidden={this.state.eventName != ""} className="label-input100">Event Name</span>
                </div>

                <div className="wrap-input100">
                    <input
                        id="eventMessage"
                        rows="4"
                        cols="25"
                        name="eventMessage"
                        value={this.state.eventMessage}
                        onChange={this.handleInputChange}
                        className="input100"
                    />
                    <span className="focus-input100"></span>
                    <span hidden={this.state.eventMessage != ""} className="label-input100">Event Message</span>
                </div>

                <div className="wrap-input100">
                    <input value={this.state.eventDate}
                           id="eventDate"
                           type="datetime-local"
                           name="eventDate"
                           value={this.state.eventDate}
                           onChange={this.handleInputChange}
                           className="input100"
                    />
                    <span className="focus-input100"></span>
                    <span hidden={this.state.eventDate != "" } className="label-input100">Event Date</span>
                </div>

                <div className="container-login100-form-btn">
                    <button className="login100-form-btn" disabled={!(this.state.eventName && this.state.eventDate)}
                            onClick={this.createEvent}> Create Event</button>
                </div>
            </div>
            </div>
        );
    }
}


class OrgDashboard extends React.Component {
    constructor(props) {
        super(props);
        this.state={
            eventSelect:false,
            eventName:"",
            eventDate:"",
            eventMessage:""
        };
        this.handleChange = this.handleChange.bind(this);
    }

    handleChange(key, value){
        this.setState({[key]:value});
    }
    render () {

        if(!this.state.eventSelect){
            return (
                <div>
                    <Logout/>

                    <UserEvents org={true} listMyEvents={this.props.listMyEvents} handleChange={this.handleChange}/>

                    <CreateEvent orgName={this.props.orgName} addToMyEvents={this.props.addToMyEvents}/>
                </div>
            )}
        return (
            <div>
                <Events eventName = {this.state.eventName} eventDate = {this.state.eventDate} eventMessage = {this.state.eventMessage}/>
            </div>
        )
    }
}

class Events extends React.Component {
    constructor(props) {
        super(props);
        this.state ={
            userList:[""]
        }
    }

    componentDidMount(){

        apiCall("eventSignups", "POST", {eventName:this.props.eventName}, this, function (status, body, currThis) {

            if (status == 200) {
                console.log(body[0]);
                console.log(body[1]);
                currThis.setState({userList:body});

                console.log(currThis.state.userList[0].gender);

            }
        });

    }

    render(){
        return(
            <div>
                <button onClick={()=>window.location.href = "dashboard.html"}>back</button>

                <h3>Event: {this.props.eventName} </h3>
                <p>Time: {this.props.eventDate}</p>
                <p>Description: {this.props.eventMessage}</p>

                <table className="table">
                    <thead className="thead-dark">
                    <tr>
                        <th scope="col">Name</th>
                        <th scope="col">Email</th>
                    </tr>
                    </thead>
                    <tbody>
                    {this.state.userList.map( (u,i) => {
                        return (
                             <tr key={i}>
                                <td> {u.name} </td>
                                <td> {u.email} </td>
                             </tr>
                        )
                    })}
                    </tbody>
                </table>

            </div>



        )
    }
}

class Dashboard extends React.Component {
    constructor (props){
        super(props);
        this.state = {
            org:false,
            orgName:"",
            listMyEvents: [""]};
        this.addToMyEvents = this.addToMyEvents.bind(this);
    }
    componentDidMount(){
        checkIfLoggedIn(this, function (currThis, status, respObj) {
            console.log(respObj);
            if(!status) {
                window.location.href = "login.html";
            }else if (respObj.hasOwnProperty('gender')){
                currThis.setState({org:false});
            }else{
                console.log("setting state to org");
                currThis.setState({org:true, orgName:respObj.name});
            }
        });
        getEvents_fromAPI(this,function(event_json,curThis) {
            const all_events = Object.values(event_json);
            curThis.setState({listMyEvents: all_events});
        });
    }
    addToMyEvents(eventObject){
        const all_events = this.state.listMyEvents;
        all_events.push(eventObject);
        this.setState({listMyEvents:all_events});
    }

    removeFromMyEvents(eventObject){
        const all_events = this.state.listMyEvents;
        all_events.pop(eventObject);
        this.setState({listMyEvents:all_events});
    }

    render() {

        if (this.state.org){
            return <OrgDashboard listMyEvents={this.state.listMyEvents} orgName={this.state.orgName} addToMyEvents={this.addToMyEvents}/>;
        }
        return <UserDashboard listMyEvents={this.state.listMyEvents} addToMyEvents={this.addToMyEvents}/>;
    }
}
ReactDOM.render(

    <Dashboard/>,
    document.getElementById('root')
);