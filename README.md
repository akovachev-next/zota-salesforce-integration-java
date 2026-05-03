# zota-salesforce-integration-java
REST controller for salesforce order creation object (Spring Boot)

In salesforce find Order Request and  OrderRequestTrigger. Click edit on OrderRequestTrigger and add some real emails in emails List.

## Setup

1. Clone the repo
2. Add your Salesforce credentials in application.properties:

salesforce.client-id=YOUR_CLIENT_ID  
salesforce.client-secret=YOUR_CLIENT_SECRET  

3. Run the app:

mvn spring-boot:run

4. Open:
http://localhost:8080

## Features

- OAuth 2.0 authentication with Salesforce
- Calls custom Apex REST endpoint
- Creates Order_Request__c records
