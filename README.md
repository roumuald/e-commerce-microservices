# E-Commerce Microservices Project

## Description

This project is an **e-commerce platform based on a microservices architecture** using Spring Boot, Spring Cloud, and Docker.
It demonstrates a real-world distributed system with independent services communicating via REST APIs and messaging (Kafka).

## Architecture

The system is composed of the following microservices:

* **Customer Service** - Manages customers
* **Product Service** - Manages products and inventory
* **Order Service** - Handles orders creation and processing
* **Payment Service** - Manages payments and payment status
* **Notification Service** - Sends notifications (Kafka-based)
* **Eureka Server** - Service discovery
* **API Gateway** - Single entry point for all services

## Technologies Used

* Java 17+
* Spring Boot
* Spring Cloud (Eureka, Gateway, Config)
* Spring Data JPA
* PostgreSQL
* Kafka
* Docker & Docker Compose
* Maven

## Prerequisites

Make sure you have installed:

* Java 17+
* Maven
* Docker & Docker Compose

## How to Run the Project

### 1. Clone the repository

```bash
git clone https://github.com/roumuald/e-commerce-microservices.git
cd e-commerce-microservices
```

### 2. Start infrastructure (Docker)

```bash
docker-compose up -d
```

This will start:

* PostgreSQL databases
* Kafka
* Zookeeper
* Other required services

### 3. Run services

You can start each microservice individually:

```bash
cd customer-service
mvn spring-boot:run
```

Repeat for all services:

* product-service
* order-service
* payment-service
* notification-service
* eureka-server
* gateway-server

---

## Access URLs

* API Gateway: `http://localhost:8080`
* Eureka Dashboard: `http://localhost:8761`

---

## Features

* Microservices architecture
* Service discovery (Eureka)
* API Gateway routing
* Order processing workflow
* Payment processing simulation
* Kafka-based notifications
* Dockerized environment

---

## Author

* **Roumuald Noubiap**

---

## Notes

This project is developed for academic purposes to demonstrate:

* Microservices design
* Distributed systems communication
* DevOps integration with Docker
