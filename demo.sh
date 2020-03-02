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


#curl -i localhost:4567/signUp -X post --data "userName=Rebecca&userPassword=reb123&userEmail=reb@gmail.com&userPhone=7324324444&userType=0&gender=0"
