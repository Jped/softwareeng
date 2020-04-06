function getEvents(){
        httpRequest = new XMLHttpRequest();
        httpRequest.open('GET','http://localhost:4567/myEvents');
        httpRequest.withCredentials = true;
        httpRequest.onreadystatechange=(e)=>{
            console.log(httpRequest.status);
            console.log(httpRequest.responseText);
            console.log(httpRequest.responseXML);
        };
        httpRequest.send(null);
}
    //httpRequest.send();
    //console.log(document.getElementById('userEmail').value);
    //var j = {
     //   "userEmail":document.getElementById('userEmail').value,
     //   "last_name":document.getElementById('userPassword').value,
    //};
    //httpRequest.send(JSON.stringify(j));

    //httpRequest.onreadystatechange=(e)=>{
        //console.log(httpRequest.status);
    //};
    //httpRequest.send('userEmail=' + encodeURIComponent(document.getElementById('userEmail').value), 'userPassword=' + encodeURIComponent(document.getElementById('userPassword').value));
    //console.log(httpRequest.status);
    //console.log(httpRequest.response);




/*
function EventList(props) {
    const all_events = props.ev;
    const listEvents = all_events.map((e) => (<li>{e}</li>))
    return (
        <ul>{listEvents}</ul>
    );
}

const fruits = Array.from(['Apple', 'Banana', 'Oj']);
const events = ({"ev" : fruits});
ReactDOM.render(
    <EventList ev={events} />,
    document.getElementById('root')
);
*/

