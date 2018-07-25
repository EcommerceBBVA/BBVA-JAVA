![BBVA Bancomer Java](http://www.openpay.mx/img/github/java.jpg)[![Build Status](https://travis-ci.org/open-pay/openpay-java.png?branch=master)](https://travis-ci.org/open-pay/openpay-java)
===============

Java client for Bancomer services

This is a client implementing the payment services for Bancomer.


Installation
----------------

To install, add the following dependency to your pom.xml:

```xml
<dependency>
	<groupId>mx.bancomer</groupId>
	<artifactId>bancomer-api-client</artifactId>
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
BancomerAPI api = new BancomerAPI("https://sandbox-api.openpay.mx", privateKey, merchantId);
```

#### Creating a customer ####

```java
Address address = new Address()
		.line1("Calle Morelos #12 - 11")
		.line2("Colonia Centro")             // Optional
		.line3("Cuauhtémoc")                 // Optional
		.city("Distrito Federal")
		.postalCode("12345")	
		.state("Queretaro")
		.countryCode("MX");                  // ISO 3166-1 two-letter code
		    
Customer customer = api.customers().create(new Customer()
        .name("John")
        .lastName("Doe")
        .email("johndoe@example.com")
        .phoneNumber("554-170-3567")
        .address(address));
```

#### Charging ####

Charging a credit card:		

```java
Card card = new Card()
		.cardNumber("5555555555554444")          // No dashes or spaces
		.holderName("Juan Pérez Nuñez")         
		.cvv2("422")            
		.expirationMonth(9)
		.expirationYear(14);

Charge charge = api.charges().create(customer.getId(), new CreateCardChargeParams()
		.description("Service charge")
		.amount(new BigDecimal("200.00"))       // Amount is in MXN
		.orderId("Charge0001")                  // Optional transaction identifier
		.card(card));
```

Refunding a card charge:

```java
Charge refundedCharge = api.charges().refund(customer.getId(), new RefundParams()
		.chargeId(charge.getId()));
```

Create a charge to be paid by bank transfer:

```java
Charge charge = api.charges().create(customer.getId(), new CreateBankChargeParams()
		.description("Service charge")
		.amount(new BigDecimal("100.00"))
		.orderId("Charge0002"));
```