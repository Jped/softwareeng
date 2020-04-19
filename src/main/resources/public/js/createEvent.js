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

class CreateEvent extends React.Component{
    constructor(props) {
        super(props);
        this.state={
                        name:"",
                        eventName:"",
                        eventMessage:"",
                        eventDate:"",
                        successMessage:false,
                        error:false,
                        errorMessage:""
                    };
        this.handleInputChange = this.handleInputChange.bind(this);
    }
    componentDidMount(){
        checkIfLoggedIn(this, function (currThis, status, response) {
            var respObj = JSON.parse(response);
            if(!status || (status && respObj.hasOwnProperty('gender')) ){
                window.location.href="index.html";
            }else{
                currThis.setState({name:respObj.name});
            }
        });
    }
    handleInputChange(e) {
        const target = e.target;
        const value = target.value;
        const name = target.name;
        this.setState({
            [name]: value
        });
        this.createEvent =this.createEvent.bind(this);
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
                currThis.setState({successMessage:true});
            }else{
                currThis.setState({error:true, errorMessage:body});
            }
        });
    }
    render() {
        if (this.state.successMessage) {
            return <h1>Your event as been successfully created, view your events here.</h1>;
        } else {
            return (
                <div>
                    <h1>Hi {this.state.name}</h1>
                    <h3>Create a new event:</h3>
                    <h4 style={{color:"red"}} hidden={!this.state.error}> ERROR: {this.state.errorMessage}</h4>
                    <label>Event Name</label>
                    <input
                        id="eventName"
                        type="text"
                        name="eventName"
                        value={this.state.eventName}
                        onChange={this.handleInputChange}/> <br/>
                    <label>Event Message</label>
                    <textarea
                        id="eventMessage"
                        rows="4"
                        cols="25"
                        name="eventMessage"
                        value={this.state.eventMessage}
                        onChange={this.handleInputChange}/> <br/>
                    <label>Event Date </label>
                    <input
                        id="eventDate"
                        type="datetime-local"
                        name="eventDate"
                        value={this.state.eventDate}
                        onChange={this.handleInputChange}/> <br/>
                    <button disabled={!(this.state.eventName && this.state.eventDate)}
                            onClick={this.createEvent}> Create Event
                    </button>
                </div>
            );
        }
    }
}

ReactDOM.render(
    <CreateEvent />,
    document.getElementById("root")
);