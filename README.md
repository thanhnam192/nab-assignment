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

<h3>Microserives Lanscape</h3>
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
 
 <h3>--------------------------Work Flow--------------------------</h3>
 <h4>Buy Voucher</h4>
 <p>(Open image in new tab for easy to read)</p>
 
 ![](/imgForReadme/buyVoucherFlow.png)
 
  <h4>Get ALL Voucher Purchased</h4>
 <p>(Open image in new tab for easy to read)</p>
 
  ![](/imgForReadme/getAllVoucherFlow.png)
  
  <h3>File&Folder Structure</h3>
  <ul>
  <li><b>/microservices</b>: Our microservices placed there. Included ProductService, PhoneService</li>
  <li><b>/spring-cloud</b>: Everything related to SpringCloud. For now, we only have Netflix Eureka as a discovery service(eureka-server)</li>
  <li><b>/serverless</b>: Our AWS Serverless functions</li>
  <li><b>/docker-compose.yml</b>: Our docker-compose file. Used to deploy our Microservice Lanscape to docker</li>
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
  <b>2. Get Voucher by OrderId</b>
  <ul>
   <li>Url: http://localhost:8080/api/phone/voucher/{YOUR_ORDER_ID} </li>
  <li>Method: GET</li>
  <li>Request Body: none</li>
  </ul>
  
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
  
  
  <h3>--------------------------Let Run Our Application on Local-------------------------------</h3>
  <h4>1. Deploy AWS Serverless</h4>
  <p><b>It will take time. I deployed it for you</b>. If you want to deploy, follow a steps in file serverless/script-deploy.txt</p>
  
  <h4>2. Deploy our Microservice Lanscape on Docker. Do step by step as below to deploy our application:</h4>
  <ul>
  <li>1. Download <b>demo_user_information.txt</b> file in attachment of Email(If can't find, contact me to get it via thanhnam192@gmail.com)</li>
  <li>2. Open docker-compose.yml file and update some params:
    <ul>
      <li>AWS_ACCESS_KEY_ID=<b>AWS_ACCESS_KEY_ID in demo_user_information.txt</b></li>
      <li>AWS_SECRET_ACCESS_KEY=<b>AWS_SECRET_ACCESS_KEY in demo_user_information.txt</b></li>
    </ul>
  </li>
  <li>3. Open CMD at root of project folder</li>
  <li>4. Run command: ./gradlew build</li>
  <li>5. Run command: docker-compose build</li>
  <li>6. Run command: docker-compose up -d</li>
  <li>7. Waiting and Checking our application ALL UP via: http://localhost:8080/actuator/health</li>

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
 curl -X GET http://localhost:8080/api/phone/voucher/{YOUR_ORDER_ID_FROM_PRVEIOUS_STEP}c -s | jq
 ```
 
  <b>2. Buy Voucher with mockSpeed="slow"(Voucher order processed time greater than 30s)</b>
  <p>- Buy Voucher</p>
  
```
curl -H "Content-Type: application/json" -X POST http://localhost:8080/api/phone/voucher/buy --data "{\"phoneNumber\" : \"<YOUR_PHONE_NUMBER>\",\"mobileNetwork\" : \"Viettel\",\"price\" : 555,\"mockSpeed\": \"slow\"}" -s | jq
```

 <p>- Get Voucher: You will get an Processing message</p>
  
 ```
 curl -X GET http://localhost:8080/api/phone/voucher/{YOUR_ORDER_ID_FROM_PRVEIOUS_STEP}c -s | jq
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
