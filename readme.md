# Ola 4 

#### By Nicolai, Lasse and Pelle


## How to Run
The app is build on maven with Javelin, Jackson, slf4j and logback

Run the [App.java]("/src/main/java/soft2/App.java") to start the project on port ```http://localhost:7000/```



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

The [logback.xml]("/src/main/resources/logback.xml") file defines the logging configuration for the application, including appenders (where logs are written), log rotation and retention rules, and separate loggers for system logs (console + logs/app.log) and audit logs (logs/audit.log in JSON format).


app.log can be found [here]("/logs/app.log")

The app writes system logs to show what is going on.
When the server starts or stops, this is written to the log. Each request is logged with the method, path, status, how long it took, and a correlation id. Normal events like making a reservation or adding a bike are logged at INFO. If the simulated check runs slow (over 800 ms), it is logged as a WARN. 

```
11:06:28 WARN  s.controllers.ReservationController - Verification slow elapsed_ms=946 using_fallback=true
```

Errors are logged once at ERROR with the stack trace. We also add correlation_id and user_id so requests can be traced.

The app also writes audit logs for security. These show who did what, when, and from where. Actions like login, reservation, rental start, rental end, and admin inventory changes are written as JSON lines. Each log has the user id, the resource, the action type, the IP address, and the correlation id. Audit logs only use ids, never names, emails, or passwords.

Examples of a few system logs:

```
11:04:29 INFO  soft2.App - Request handled method=POST path=/api/reservations status=201 Created elapsed_ms=833
11:04:31 INFO  soft2.App - Request handled method=GET path=/api/bikes status=200 OK elapsed_ms=0
11:06:23 INFO  s.controllers.ReservationController - Verification ok elapsed_ms=233
11:06:23 ERROR s.controllers.ReservationController - Reservation failed bike_id=1 error=IllegalStateException
java.lang.IllegalStateException: Bike not available
```

## B) Audit Logs

Audit logs can be found [here]("/logs/audit.log")


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
The rules for rotation and retention are set in the logback.xml file.