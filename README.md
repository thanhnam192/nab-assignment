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
 
 <h2>Work Flow</h2>
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
  <h3>--------------------------Let Run Our Application on Local-------------------------------</h3>
  <h4>Do step by step below to run our application:</h4>
  <ul>
  <li>1. Down load <b>demo_user_information.txt</b> file in attachment of Email(If can't find, contact me to get it via thanhnam192@gmail.com)</li>
  <li>2. Open docker-compose.yml file and update some params:
    <ul>
      <li>AWS_ACCESS_KEY_ID=<b>AWS_ACCESS_KEY_ID in demo_user_information.txt</b></li>
      <li>AWS_SECRET_ACCESS_KEY=<b>AWS_SECRET_ACCESS_KEY in demo_user_information.txt</b></li>
    </ul>
  </li>
  <li>Open CMD at root of project folder</li>
  <li>Run command: ./gradlew build</li>
  <li>Run command: docker-compose build</li>
  <li>Run command: docker-compose up -d</li>
  <li>Waiting and Checking our application ALL UP via: http://localhost:8080/actuator/health</li>
</ul>

![](/imgForReadme/health.png)
  
