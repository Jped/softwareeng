### signUp

#All fields present - should signUp properly
curl -i localhost:4567/signUp -X post --data "userName=Shifra&userPassword=shif123&userEmail=shifra@gmail.com&userPhone=7324324444&userType=0&gender=0"

# User (user email) exists already 
curl -i localhost:4567/signUp -X post --data "userName=Shifra&userPassword=shif123&userEmail=shifra@gmail.com&userPhone=7324324444&userType=0&gender=0"

# Missing fields
curl -i localhost:4567/signUp -X post --data "userName=Rebecca&userPassword=reb123&userPhone=7324444444"

# Wrong email
curl -i localhost:4567/signUp -X post --data "userName=Rebecca&userPassword=reb123&userEmail=rebgmail.com&userPhone=7324324444&userType=0&gender=0"
curl -i localhost:4567/signUp -X post --data "userName=Rebecca&userPassword=reb123&userEmail=@gmail.com&userPhone=7324324444&userType=0&gender=0"
curl -i localhost:4567/signUp -X post --data "userName=Rebecca&userPassword=reb123&userEmail=reb@gmail&userPhone=7324324444&userType=0&gender=0"
curl -i localhost:4567/signUp -X post --data "userName=Rebecca&userPassword=reb123&userEmail=blah&userPhone=7324324444&userType=0&gender=0"

# Wrong password
curl -i localhost:4567/signUp -X post --data "userName=Rebecca&userPassword=reb&userEmail=reb@gmail.com&userPhone=7324324444&userType=0&gender=0"
curl -i localhost:4567/signUp -X post --data "userName=Rebecca&userPassword=Rebecca&userEmail=reb@gmail.com&userPhone=7324324444&userType=0&gender=0"
curl -i localhost:4567/signUp -X post --data "userName=Rebecca&userPassword=reb@gmail.com&userEmail=reb@gmail.com&userPhone=7324324444&userType=0&gender=0"
curl -i localhost:4567/signUp -X post --data "userName=Rebecca&userPassword=7324324444&userEmail=reb@gmail.com&userPhone=7324324444&userType=0&gender=0"

# Wrong username
#curl -i localhost:4567/signUp -X post --data "userName=***&userPassword=reb123&userEmail=reb@gmail.com&userPhone=7324324444&userType=0&gender=0"

# Wrong userType (same code for gender)
curl -i localhost:4567/signUp -X post --data "userName=Rebecca&userPassword=reb123&userEmail=reb@gmail.com&userPhone=7324324444&userType=1&gender=0"
curl -i localhost:4567/signUp -X post --data "userName=Rebecca&userPassword=reb123&userEmail=reb@gmail.com&userPhone=7324324444&userType=org&gender=0"
# Correct userType
curl -i localhost:4567/signUp -X post --data "userName=Rebecca&userPassword=reb123&userEmail=reb_user1@gmail.com&userPhone=7324324444&userType=TRUE&gender=0"
curl -i localhost:4567/signUp -X post --data "userName=Rebecca&userPassword=reb123&userEmail=reb_user2@gmail.com&userPhone=7324324444&userType=FaLse&gender=0"

# Wrong phone
curl -i localhost:4567/signUp -X post --data "userName=Rebecca&userPassword=reb123&userEmail=reb@gmail.com&userPhone=73243244&userType=0&gender=0"
curl -i localhost:4567/signUp -X post --data "userName=Rebecca&userPassword=reb123&userEmail=reb@gmail.com&userPhone=blah&userType=0&gender=0"


######### logIn

# Should login properly
curl -i localhost:4567/logIn -X post --data "userEmail=shifra@gmail.com&userPassword=shif123"

# Missing fields
curl -i localhost:4567/logIn -X post --data "userEmail=shifra@gmail.com"
curl -i localhost:4567/logIn -X post --data "userPassword=shif123"
# Wrong email not registered
curl -i localhost:4567/logIn -X post --data "userEmail=shifra1111@gmail.com&userPassword=shif123"
# Wrong password
curl -i localhost:4567/logIn -X post --data "userEmail=shifra@gmail.com&userPassword=shif123000"



######## createEvent
# First register an org
curl -i localhost:4567/signUp -X post --data "userName=ChabadBowery&userPassword=chabad123&userPhone=7326063409&userEmail=chabad@bowery.com&userType=true"
# Create an event (should be successful)
curl -i localhost:4567/createEvent -X post --data "eventName=ShabbatDinner&orgName=ChabadBowery&eventDate=2020-09-22 12:30:34&orgEmail=chabad@bowery.com&eventMessage=blah"

# Missing fields
curl -i localhost:4567/createEvent -X post --data "eventName=ShabbatDinner&orgName=ChabadBowery&eventDate=2020-09-22 12:30:34&eventMessage=blah"
# Unregistered email
curl -i localhost:4567/createEvent -X post --data "eventName=ShabbatDinner&orgName=ChabadBowery&eventDate=2020-09-22 12:30:34&orgEmail=chabad@bowery_2.com&eventMessage=blah"
# Mismatch between email and username
curl -i localhost:4567/createEvent -X post --data "eventName=ShabbatDinner&orgName=Shifra&eventDate=2020-09-22 12:30:34&orgEmail=chabad@bowery.com&eventMessage=blah"
curl -i localhost:4567/createEvent -X post --data "eventName=ShabbatDinner&orgName=ChabadBowery&eventDate=2020-09-22 12:30:34&orgEmail=ch@bowery.com&eventMessage=blah"
# Reregister an event that exists
curl -i localhost:4567/createEvent -X post --data "eventName=ShabbatDinner&orgName=ChabadBowery&eventDate=2020-09-22 12:30:34&orgEmail=chabad@bowery.com&eventMessage=blah"

# Register a second org
curl -i localhost:4567/signUp -X post --data "userName=HillelNYU&userPassword=hillel123&userPhone=7326063409&userEmail=hillel@nyu.com&userType=true"
# Create an event with same name as other event but dif org
curl -i localhost:4567/createEvent -X post --data "eventName=ShabbatDinner&orgName=HillelNYU&eventDate=2020-09-22 12:30:34&orgEmail=hillel@nyu.com&eventMessage=blah"


