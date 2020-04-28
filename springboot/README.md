Table of contents
* [Introduction](#introduction)
* [Quick Start](#quick-start)
* [Architecture](#architecture)
* [REST API usage](#rest-api-usage)
* [Docker Deployment](#docker-deployment)
* [Improvements](#improvements)

# Introduction
This project is a proof of concept of an online stock trading simulation REST API. Its a MicroService which is implemented with Springboot for internal dependency management, and Maven for 
external dependency management. The users of this application can get market quotes for stock tickers, details about user data, 
positions, and sercurity orders, all of which are stored in the Postgres SQL database. Each order is associated with an account and 
trader, both of can be created through the app. All of the data relating to quotes comes from IEX Cloud in real time, and the app
is deployed using Docker. 

# Quick Start
- Prequiresites: Docker, CentOS 7, JDK version 1.8, IEX cloud account, Maven, Swagger
- Docker scritps with description
	- build images
  - create docker network
  - start containers
- Try trading-app with SwaggerUI (screenshot)

# Architecture
- Draw a component diagram which contains controllers, services, DAOs, psql, IEX Cloud, WebServlet/Tomcat, HTTP client, and SpringBoot. 
- briefly explain the following components and services (3-5 sentences for each)
  - Controller layer (e.g. handles user requests....)
  - Service layer
  - DAO layer
  - SpringBoot: webservlet/TomCat and IoC
  - PSQL and IEX

# REST API Usage
## Swagger
What's swagger (1-2 sentences, you can copy from swagger docs). Why are we using it or who will benefit from it?
## Quote Controller
- High-level description for this controller. Where is market data coming from (IEX) and how did you cache the quote data (PSQL). Briefly talk about data from within your app
- briefly explain each endpoint
  e.g.
  - GET `/quote/dailyList`: list all securities that are available to trading in this trading system blah..blah..
## Trader Controller
- High-level description for trader controller (e.g. it can manage trader and account information. it can deposit and withdraw fund from a given account)
- briefly explain each endpoint
##Order Controller
- High-level description for this controller.
- briefly explain each endpoint
## App controller
- briefly explain each endpoint
## Optional(Dashboard controller)
- High-level description for this controller.
- briefly explain each endpoint

# Docker Deployment
- docker diagram including images, containers, network, and docker hub
e.g. https://www.notion.so/jarviscanada/Dockerize-Trading-App-fc8c8f4167ad46089099fd0d31e3855d#6f8912f9438e4e61b91fe57f8ef896e0
- describe each image in details (e.g. how psql initialize tables)

# Improvements
If you have more time, what would you improve?
- at least 5 improvements
