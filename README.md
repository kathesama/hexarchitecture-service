# hexarchitecture-service

```
com
  └── myproject
      ├── application
      │   ├── port
      │   │   ├── in (Input Ports)
      │   │   └── out (Output Ports)
      │   └── service
      ├── domain
      │   ├── model
      │   └── exception
      ├── infrastructure
      │   ├── adapter
      │   │   ├── rest (Input Adapter)
      │   │   │   ├── controller
      │   │   │   ├── dto/models
      │   │   │   └── mapper
      │   │   └── persistence (Output Adapter)
      │   │       ├── entity
      │   │       ├── repository
      │   │       └── mapper
      │   ├── interceptor
      │   └── configuration
      └── util
```
