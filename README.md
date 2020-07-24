# NAB - Assignment statement 2
<p>Based on requirement of Bank ABC, we need to build an backend webservice that handle an function to allow user purchase prepaid data for a SIM card by getting a voucher.</p>
<p>The Backend must have some characteristics:</p>
<ul>
  <li>High Available</li>
  <li>Scalability and reusability, as well as efficiency</li>
  <li>Work very well with containers, such as Docker</li>
  <li>Better fault isolation</li>
</ul>

<p>That why I choose <b>Spring Boot Microservices</b> and integreate with <b>Amazon Web Service</b>. The System Architecture same as image below:</p>

![](/imgForReadme/SystemArchitect.png)

<h3>Microserives Landscape</h3>
<ul>
  <li>Product Service: Stand as our Load Balancer. Proxy all requests to the microservices, using their application name.</li>
  <li>Phone Service: Our core service, handle some function such as Purchase Voucher, Get Vourcher information, SMS verification </li>
  <li>Eureka Server: Netflix Eureka implements client-side service discovery, meaning that the clients run software that talks to the discovery service, Netflix Eureka, to get information about the available microservice instances.</li>
 </ul>
 <h3>AWS Serverless</h3>
<ul>
  <li>Voucher Order Queue: 
    <ul>
      <li>Used to received Voucher Order from Phone Service.</li>
      <li>When recieved voucher order, this queue will trigger Lambda function to buy voucher from 3rd party</li>
      <li>After Lambda fisnihed to buy an voucher, it will push an message to Voucher Order Result Queue</li>
    </ul>
   </li>
  <li>Voucher Order Result Queue: 
    <ul>
      <li>Used to received Voucher result.</li>
      <li>After Lambda buy an Voucher, they will push Voucher Result to this queue.</li>
      <li>Our Phone Service listen this queue and update Voucher Result to databse</li>
    </ul>
   </li>
  <li>SMS Queue: Used to received SMS request, this queue will trigger Lambda function to send SMS</li>
  
  <li>Auth Code Timer Queue: 
    <ul>
      <li>Used to received message to start timer</li>
      <li>Timer used to count period time of SMS auth code</li>
      <li>This queue will trigger Lambda(timer). When timer expired, it will push message to Auth Code Timer Expired Queue</li>
    </ul>
  </li>
  
   <li>Auth Code Timer Expired Queue: 
    <ul>
      <li>Used to received message when Auth Code Timer expired</li>
      <li>Phone Service listen that queue and reset Auth SMS Code when Timer expired</li>
    </ul>
  </li>
 </ul>
 
 <h3>MySQL Database(phone-db)</h3>
 <ul>
  <li><b>voucher</b> table: Stored all voucher of users</li>
  <li><b>phone_verification</b> table: Store the Auth Code of user. Used to Auth user by sending SMS</li>
</ul>
 
 <h3>--------------------------Work Flow--------------------------</h3>
 <h4>1. Buy Voucher</h4>
 <p>- User/Client make a call to our API to buy an Voucher. Our system will create Voucher Order and <b>IMEDIATELY</b> send back to User with OrderID and message is "Voucher order request is being processed within 30 seconds". User can use OrderID to make a call to check a process of their Voucher Order</p>
 <p>- System send Voucher Order to SQS and let Lambda buy an Voucher from 3rd party. After finish to buy Voucher, Lambda will send messge to Result Queue</p>
 <p>- System will take a message from Result Queue and Update Voucher Order to DB. There are 3 cases:
  <ul>
    <li>Finish within 30 seconds: Just update Voucher result to DB</li>
     <li>Finish but more than 30 seconds: Update Voucher result to DB. Send SMS to User</li>
     <li>Error: Update Voucher result to DB. Send SMS to User</li>
</ul>
</p>
 <p>For more detail, We can check the flow below (Open image in new tab for easy to read)</p>
 
 ![](/imgForReadme/buyVoucherFlow.png)
 
  <h4>2. Get ALL Voucher Purchased</h4>
  <p>- That function need to be <b>Secured</b>. That why System will send Auth Code by SMS to user to verify that user is the owner of phone number</p>
  <p>- Auth Code will be <b>EXPIRED IN 60 seconds or Used by user</b></p>
  <p>- After received Auth Code on their phone, they can use that code to call our API to get all voucher that purchased by their phone</p>
  
 <p>For more detail, We can check the flow below (Open image in new tab for easy to read)</p>
 
  ![](/imgForReadme/getAllVoucherFlow.png)
  
  <h3>File&Folder Structure</h3>
  <ul>
  <li><b>/microservices</b>: Our microservices placed there. Included ProductService, PhoneService</li>
  <li><b>/spring-cloud</b>: Everything related to SpringCloud. For now, we only have Netflix Eureka as a discovery service(eureka-server)</li>
  <li><b>/serverless</b>: Our AWS Serverless functions</li>
  <li><b>/docker-compose.yml</b>: Our docker-compose file. Used to deploy our Microservice Landscape to docker</li>
  </ul>
  <h3>--------------------------API Description--------------------------</h3>
  <b>1. Buy Voucher</b>
  <ul>
  <li>Url: http://localhost:8080/api/phone/voucher/buy </li>
  <li>Method: POST</li>
  <li>Request Body:
<p>
{
    "phoneNumber" : "+84986329076",
    "mobileNetwork" : "Viettel",
    "price" : 555,
    "mockSpeed": "fast"
  }</p>
    <ul>
      <li>phoneNumber: Your phone number with <b>E.164 formatting</b>. example: +84986329076</li>
      <li>mobileNetwork: Your mobile network that you want to your voucher related to</li>
      <li>price: price of voucher</li>
      <li><b>mockSpeed</b>: Used to mock our process. Values are:
        <ul>
          <li>fast : process voucher with NORMAL/FAST speed. Return voucher in less than 30 seconds</li>
           <li>slow : process voucher with SLOW speed. Return voucher in MORE than 31 seconds</li>
           <li>error : process voucher with ERROR</li>
        </ul>
      </li>
    </ul>
  </li>
  </ul>
  
```
curl -H "Content-Type: application/json" -X POST http://localhost:8080/api/phone/voucher/buy --data "{\"phoneNumber\" : \"<YOUR_PHONE_NUMBER>\",\"mobileNetwork\" : \"Viettel\",\"price\" : 555,\"mockSpeed\": \"fast\"}" -s | jq
```
  
  <b>2. Get Voucher by OrderId</b>
  <ul>
   <li>Url: http://localhost:8080/api/phone/voucher/{YOUR_ORDER_ID} </li>
  <li>Method: GET</li>
  <li>Request Body: none</li>
  </ul>
  
 ```
 curl -X GET http://localhost:8080/api/phone/voucher/{YOUR_ORDER_ID} -s | jq
 ```
  
  
   <b>3. Send Auth Code by SMS</b>
  <ul>
   <li>Url: http://localhost:8080/api/phone/verification/sms </li>
  <li>Method: POST</li>
  <li>Request Body: 
    <p>{
    "phoneNumber" : "+84986329076"
}</p>
    <ul>
      <li>phoneNumber: Your phone number with <b>E.164 formatting</b>. example: +84986329076</li>
    </ul>
    
  </li>
  </ul>
  
```
curl -H "Content-Type: application/json" -X POST http://localhost:8080/api/phone/verification/sms --data "{\"phoneNumber\" : \"<YOUR_PHONE_NUMBER>\"}" -s | jq
```
  
  <b>4. Get all Voucher that you purchased</b>
    <ul>
  <li>Url: http://localhost:8080/api/phone/voucher/all </li>
  <li>Method: POST</li>
  <li>Request Body:
<p>
{
    "phoneNumber" : "+84986329076",
    "code" : "ABCDEF"
}</p>
    <ul>
      <li>phoneNumber: Your phone number with <b>E.164 formatting</b>. example: +84986329076</li>
      <li>code: Auth Code that received in your phone</li>
    </ul>
  </li>
  </ul>
  
 ```
curl -H "Content-Type: application/json" -X POST http://localhost:8080/api/phone/voucher/all --data "{\"phoneNumber\" : \"<YOUR_PHONE_NUMBER>\", \"code\" : \"<YOUR_CODE>\"}" -s | jq
```
  
  <p><b>Note:</b> You can import my Postman Collection(nab-phone-voucher.postman_collection.json) to use</p>
  
  <h3>--------------------------Let Run Our Application on Local-------------------------------</h3>
  <p><b>IMPORTANCE</b>:</p>
  <ul>
    <li>Download <i><b>demo_user_information.txt</b></i> file in attachment of Email that I sent you(If can't find, contact me to get it via thanhnam192@gmail.com)</li>
  <li>MUST have Docker on your machine</li>
</ul>
  
  <h4>1. Deploy AWS Serverless</h4>
  <p><b>ALREADY DEPLOYED</b>. If you want to know how to deploy, follow a steps below</p>
  <ul>
  <li>1. Install AWS CLI</li>
  <li>2. Install AWS SAM CLI</li>
  <li>3. Open cmd, run <b>aws configure</b> and update credential same with credential in demo_user_information.txt</li>
  <li>4. Open file Serverless/template.yaml and update TWILIO_ACCOUNT_SID, TWILIO_ACCOUNT_AUTH, TWILIO_PHONE_NUMBER with values same as demo_user_information.txt</li>
  <li>5. Open cmd at Serverless folder</li>
  <li>6. Run: npm install</li>
  <li>7. Run: npm run build</li>
  <li>8. Run: sam package --s3-bucket demo.build.nab --output-template-file demo-template.yaml --region ap-southeast-2</li>
  <li>9. Run: sam deploy --template-file <b>PATH_TO_FILE</b>\demo-template.yaml  --stack-name demo-nab --capabilities CAPABILITY_IAM --region ap-southeast-2</li>
  </ul>
  
  <h4>2. Deploy our Microservice Landscape on Docker. Do step by step as below to deploy our application:</h4>
  <ul>
  <li>1. Open docker-compose.yml file and update some params:
    <ul>
      <li>AWS_ACCESS_KEY_ID=<b>AWS_ACCESS_KEY_ID in demo_user_information.txt</b></li>
      <li>AWS_SECRET_ACCESS_KEY=<b>AWS_SECRET_ACCESS_KEY in demo_user_information.txt</b></li>
    </ul>
  </li>
  <li>2. Open CMD at root of project folder</li>
  <li>3. Run command: ./gradlew build</li>
  <li>4. Run command: docker-compose build</li>
  <li>5. Run command: docker-compose up -d</li>
  <li>6. Waiting(around 5 minutes) and Checking our application ALL UP via: http://localhost:8080/actuator/health</li>

</ul>

![](/imgForReadme/health.png)

<ul>
  <li>8. Install jq to your sytem:
    <ul>
      <li>Run command: sudo apt-get update </li>
      <li>Run command: sudo apt-get install jq </li>
      </ul>
  </li>
</ul>

 <h3>--------------------------Let Test Our Application-------------------------------</h3>
 <p><b>Note</b>: Change YOUR_PHONE_NUMBER, YOUR_CODE to your phone, code before run a cURL</p>
 
 <b>1. Buy Voucher with mockSpeed="fast"(Voucher order processed time less than 30s)</b>
  <p>- Buy Voucher</p>

```
curl -H "Content-Type: application/json" -X POST http://localhost:8080/api/phone/voucher/buy --data "{\"phoneNumber\" : \"<YOUR_PHONE_NUMBER>\",\"mobileNetwork\" : \"Viettel\",\"price\" : 555,\"mockSpeed\": \"fast\"}" -s | jq
```
  
 <p>- Get Voucher</p>
  
 ```
 curl -X GET http://localhost:8080/api/phone/voucher/{YOUR_ORDER_ID_FROM_PREVIOUS_STEP} -s | jq
 ```
 
  <b>2. Buy Voucher with mockSpeed="slow"(Voucher order processed time greater than 30s)</b>
  <p>- Buy Voucher</p>
  
```
curl -H "Content-Type: application/json" -X POST http://localhost:8080/api/phone/voucher/buy --data "{\"phoneNumber\" : \"<YOUR_PHONE_NUMBER>\",\"mobileNetwork\" : \"Viettel\",\"price\" : 555,\"mockSpeed\": \"slow\"}" -s | jq
```

 <p>- Get Voucher: You will get an Processing message</p>
  
 ```
 curl -X GET http://localhost:8080/api/phone/voucher/{YOUR_ORDER_ID_FROM_PREVIOUS_STEP} -s | jq
 ```
  
  <p>- Wait for 31 seconds and get Voucher Code in your phone</p>
  
 <b>3. Buy Voucher with mockSpeed="error"(Voucher order processed with ERROR)</b>
 
```
curl -H "Content-Type: application/json" -X POST http://localhost:8080/api/phone/voucher/buy --data "{\"phoneNumber\" : \"<YOUR_PHONE_NUMBER>\",\"mobileNetwork\" : \"Viettel\",\"price\" : 555,\"mockSpeed\": \"error\"}" -s | jq
```

<p>System will send SMS error message to your phone</p>

<b>4. Get all voucher that you purchased</b>
<p>- Send Auth Code via SMS</p>

```
curl -H "Content-Type: application/json" -X POST http://localhost:8080/api/phone/verification/sms --data "{\"phoneNumber\" : \"<YOUR_PHONE_NUMBER>\"}" -s | jq
```

<p>- Take Auth Code in your phone and let get all voucher that you purchased</p>

```
curl -H "Content-Type: application/json" -X POST http://localhost:8080/api/phone/voucher/all --data "{\"phoneNumber\" : \"<YOUR_PHONE_NUMBER>\", \"code\" : \"<YOUR_CODE>\"}" -s | jq
```

<b>5. Auth Timer Expired and Auth Code will be removed</b>
<p>- Send Auth Code via SMS</p>

```
curl -H "Content-Type: application/json" -X POST http://localhost:8080/api/phone/verification/sms --data "{\"phoneNumber\" : \"<YOUR_PHONE_NUMBER>\"}" -s | jq
```

<p>- You will received Auth SMS Code. You can check our code in our DB (phone_verification table)</p>
<p>- Wait for 60 seconds. Check your code in DB, auth code will be removed</p>

