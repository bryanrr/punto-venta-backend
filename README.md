# Punto Venta Backend
An stateless app in charge of providing products and sales information of an store.  
This app is a series of REST endpoints that works in conjunction with the punto-venta-frontend app, which is a SPA developed in Angular.

#### Stateless App.
To make it stateless we used JWT as a way to provide authorization to users.  
In order for invalidating a JWT from an ended session, we declared a JWTBlacklistService, which is dedicated to store still valid JWT's but whose user session has already ended.  
In a real state environment this service should be a separate and centralized app, where all apps can declare its blacklisted tokens.

>Note: This app gets conencted to a SQL database, so in order to work should get connected to an external SQL database and have set an specific database design.
