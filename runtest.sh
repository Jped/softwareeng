curl localhost:4567/signUp -X post --data "userName=RebeccaG&userPassword=reb123&userPhone=7326063409&userEmail=reb@garten.com"

curl localhost:4567/signUp -X post --data "userName=ChabadBowery&userPassword=chabad123&userPhone=7326063409&userEmail=chabad@bowery.com"

curl localhost:4567/createEvent -X post --data "eventName=ShabbatDinner&orgName=ChabadBowery&eventDate=2/20/2020&orgEmail=chabad@bowery.com&eventMessage=blah"

curl -X GET  localhost:4567/upcomingEvents --data "userName=ChabadBowery&userPassword=chabad123&userPhone
=7326063409&userEmail=chabad@bowery.com"

curl localhost:4567/joinEvent -X post --data "userEmail=reb@garten.com&eventName=ShabbatDinner"

curl localhost:4567/myEvents?userEmail=reb@garten.com&eventName=ShabbatDinner
