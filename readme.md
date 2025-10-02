# Ola 4 - Store Systemer

## Made by

Lasse Hansen - cph-lh479@stud.ek.dk

Pelle Hald Vedsmand - cph-pv73@stud.ek.dk

Nicolai Rosendahl - cph-nr135@stud.ek.dk


## How to Run
The app is build on maven with Javelin, Jackson, slf4j and logback

Run the [App.java](/src/main/java/soft2/App.java) to start the project on port ```http://localhost:7000/```



## Endpoints 

```
GET /api/bikes – list available bikes

POST /api/reservations – reserve a bike { "user_id": "u1", "bike_id": 1 }

POST /api/rentals/start – start rental { "user_id": "u1", "reservation_id": "1" }

POST /api/rentals/end – end rental { "user_id": "u1", "bike_id": 1 }

POST /api/auth/login – simulate login { "user_id": "u1", "password": "secret" }

POST /api/admin/add-bike – admin adds a bike { "admin_id": "a1", "type": "City" }

POST /api/admin/remove-bike/{id} – admin removes a bike { "admin_id": "a1" }
```

## A) System Logging


System Logging has succesfully been done to capture different events for the application, requests and errors.

The [logback.xml](/src/main/resources/logback.xml) file defines the logging configuration for the application, including appenders (where logs are written), log rotation and retention rules, and separate loggers for system logs (console + logs/app.log) and audit logs (logs/audit.log in JSON format).


app.log can be found [here](/logs/app.log)

The logging level for System Logging is done at "INFO" level to get a more transparent logging of the requests and system events. "DEBUG" caused too much noise, which was not suitable even while developing. An example of the output with "DEBUG" can be found [here](/logs/app.2025-10-01.log)
We could have added conditional debugging for "DEBUG" with less verbose outputs to get the "best of both worlds".
Another way of handling it would be adding environmental conditions, ie using "DEBUG" on localhost or in a test environment.



The app writes system logs to show what is going on.
When the server starts or stops, this is written to the log. Each request is logged with the method, path, status, how long it took, and a correlation id. Normal events like making a reservation or adding a bike are logged at INFO. If the simulated check runs slow (over 800 ms), it is logged as a WARN.



```
11:06:28 WARN  s.controllers.ReservationController - Verification slow elapsed_ms=946 using_fallback=true
```




Errors are logged once at ERROR with the stack trace. We also add correlation_id and user_id so requests can be traced.

The app also writes audit logs for security. These show who did what, when, and from where. Actions like login, reservation, rental start, rental end, and admin inventory changes are written as JSON lines. Each log has the user id, the resource, the action type, the IP address, and the correlation id. Audit logs only use ids, never names, emails, or passwords.

Examples of the system logs:

```
09:29:59 INFO  soft2.App [9864b189-ca16-408c-a15f-432f80d500c0 u1] - Request handled method=POST path=/api/auth/login status=200 OK elapsed_ms=16
09:30:36 INFO  soft2.App [3542602b-aa5f-4a1a-973a-af4e20cb7b3e u1] - Request handled method=POST path=/api/auth/login status=200 OK elapsed_ms=0
09:30:38 INFO  soft2.App [a108307a-2e3d-48db-a21e-d8e39ba38f3b u1] - Request handled method=GET path=/api/bikes status=200 OK elapsed_ms=0
09:30:40 INFO  soft2.App [9a77c702-5dd4-46a4-a74a-f7dd1613ffef u1] - Request handled method=GET path=/api/bikes status=200 OK elapsed_ms=0
09:32:31 INFO  s.controllers.ReservationController [7e2e3429-496f-4278-8da3-006709cf7fef u1] - Verification ok elapsed_ms=582
09:32:31 INFO  soft2.App [7e2e3429-496f-4278-8da3-006709cf7fef u1] - Request handled method=POST path=/api/reservations status=201 Created elapsed_ms=598
09:32:32 INFO  soft2.App [a0530624-41a8-4cf9-a266-efb3791c8b36 u1] - Request handled method=POST path=/api/rentals/start status=201 Created elapsed_ms=2
09:32:34 INFO  soft2.App [2a059bde-9bcb-477f-8073-85fd2ea0719a u1] - Request handled method=POST path=/api/rentals/end status=200 OK elapsed_ms=1
09:32:36 INFO  soft2.App [0fc8c18b-4467-40d6-86ef-e961104bebdb admin] - Request handled method=POST path=/api/admin/add-bike status=201 Created elapsed_ms=1
09:32:37 INFO  soft2.App [c9c4683c-c340-4b09-ac92-55f6c485d44a admin] - Request handled method=POST path=/api/admin/remove-bike/2 status=200 OK elapsed_ms=1
```

## B) Audit Logs

Audit logs can be found [here](/logs/audit.log)


Audit logs are used to track user actions.
They record things like login success or failure, when a reservation is made, when a rental starts and ends, and when an admin changes the bikes.
Each audit log line is JSON and contains the user id, the action, the resource id, the IP address, and the correlation id.
The logs only use ids and never store personal details like names or passwords.

Example of Audit log:

```
{
  "@timestamp": "2025-10-01T10:50:52.854Z",
  "level": "INFO",
  "logger_name": "AUDIT",
  "message": "USER_ACTION: type=RESERVATION_CREATE user_id=u1 resource_id=bike:1 ip=[0:0:0:0:0:0:0:1]",
  "user_id": "u1",
  "correlation_id": "286120f1-96ef-47b5-a563-b05b9f069610",
  "log_type": "audit",
  "service": "city-bikes"
}
```

## Rotation and retention

Rotation and Retention

System logs are written to logs/app.log. This file is rotated once per day and old files are kept for 14 days.
Audit logs are written to logs/audit.log. This file is also rotated once per day but old files are kept for 180 days.
The rules for rotation and retention are set in the [logback.xml file](/src/main/resources/logback.xml)
