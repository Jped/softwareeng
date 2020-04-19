
function getEvents_fromAPI(curThis,callback){
    const httpRequest = new XMLHttpRequest();
    httpRequest.open('GET','http://localhost:4567/myEvents');
    httpRequest.responseType = "json";
    httpRequest.withCredentials = true;
    httpRequest.send(null);
    httpRequest.onload = function() {
        console.log(httpRequest.status);
        var jsonResponse = httpRequest.response;
        console.log(jsonResponse);
        callback(jsonResponse,curThis);
    };
}

class EventList extends React.Component {
    constructor(props){
        super(props);
        this.state = {listEvents: [""]}
    };
    componentDidMount() {
        getEvents_fromAPI(this,function(event_json,curThis) {
            const all_events = Object.values(event_json);
            curThis.setState ({listEvents: all_events});
            console.log(all_events);
            //console.log(typeof all_events[0].date);
            //console.log(JSON.stringify(all_events[0].date.toLocaleString()));
        });
    };
    render() {
       let lis =null;
       lis = (
           <div>
           <table className='eventList'>
                <tr>
                    <td>EventName</td>
                    <td>Organization</td>
                    <td>Date</td>
                    <td>Message</td>
                </tr>

                {this.state.listEvents.map( (e,i) => {
                    return (
                        <tr key={i}>
                            <td> {e.name} </td>
                            <td> {e.orgName} </td>
                            <td> {e.date} </td>
                            <td> {e.eventMessage} </td>
                        </tr>
                    )
                })}
            </table>
        </div>
       );
       return lis;
    }
}

ReactDOM.render(
    //<EventList evants = {getEvents_fromAPI()}/>,
    <EventList />,
    document.getElementById('root')
);