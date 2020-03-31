function login(){
    httpRequest = new XMLHttpRequest();
    httpRequest.open('POST', 'localhost:4567/logIn');
    //console.log(document.getElementById('userEmail').value);
    var j = {
        "userEmail":document.getElementById('userEmail').value,
        "last_name":document.getElementById('userPassword').value,
    };
    httpRequest.send(JSON.stringify(j));
    httpRequest.onreadystatechange=(e)=>{
        console.log(httpRequest.status);
    };
    //httpRequest.send('userEmail=' + encodeURIComponent(document.getElementById('userEmail').value), 'userPassword=' + encodeURIComponent(document.getElementById('userPassword').value));
    console.log(httpRequest.status);
}