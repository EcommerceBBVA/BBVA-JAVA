![BBVA Java]()
===============

Java client for BBVA services

This is a client implementing the payment services for BBVA.


Installation
----------------

To install, add the following dependency to your pom.xml:

```xml
<dependency>
	<groupId>mx.bbva</groupId>
	<artifactId>bbva-api-client</artifactId>
	<version>1.0.0</version>
</dependency>
```

Compatibility
----------------

As of now Java 6 is required.

Examples
----------------

#### Starting the API ####

```java
String publicIp = "138.84.62.109";
BbvaAPI api = new BbvaAPI("https://sand-api.ecommercebbva.com/", privateKey, merchantId, publicIp);
```

#### Creating a customer ####

```java
ParameterContainer address = new ParameterContainer("address");
        address.addValue("line1", "Calle Morelos #12 - 11");
        address.addValue("line2", "Colonia Centro"); // Optional
        address.addValue("line3", "Cuauhtémoc"); // Optional
        address.addValue("city", "Distrito Federal");
        address.addValue("postal_code", "12345");
        address.addValue("state", "Queretaro");
        address.addValue("country_code", "MX"); // ISO 3166-1 two-letter code
		    
ParameterContainer customer = new ParameterContainer("customer");
        customer.addValue("name","John");
        customer.addValue("last_name", "doe");
        customer.addValue("email", "johndoe@example.com");
        customer.addValue("phone_number", "554-170-3567");
        customer.addMultiValue(address);
```

#### Charging ####

Charging:		

```java

ParameterContainer charge = new ParameterContainer("charge");
        charge.addValue("affiliation_bbva", "781500");
        charge.addValue("amount", "100.00");
        charge.addValue("description", "Pago de taxi");
        charge.addValue("currency", "MXN");
        charge.addValue("order_id", "order-00051");
        charge.addValue("redirect_url", "https://sand-portal.ecommercebbva.com/");
        charge.addMultiValue(customer);

HashMap chargeAsMap = api.charges().create(charge.getParameterValues());
```

Refunding a card charge:

```java
HashMap refundedCharge = api.charges().refund(customerAsMap.get("id").toString(), new RefundParams()
    .chargeId(chargeAsMap.get("id").toString()));
```
