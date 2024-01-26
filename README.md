## Build
- Cache: [CACHE_STARTER-HELP.md](cache-starter%2FCACHE_STARTER-HELP.md)
- [application.yaml](src%2Fmain%2Fresources%2Fapplication.yaml)
    ```yaml
    datasource:
        driver-class-name: org.postgresql.Driver
        username: postgres
        password: postgres
        url: jdbc:postgresql://localhost:3254/houses
    ```
- run > [schema.sql](src%2Fmain%2Fresources%2Fdb%2Fschema.sql)
- run > [data.sql](src%2Fmain%2Fresources%2Fdb%2Fdata.sql)
- ```
    ./gradlew clean
    ./gradlew build
  ```
---

## Swagger
- http://localhost:8081/swagger-ui/index.html

### Houses

#### CreateHouse 
- RequestBody:
```yaml
{
    "area": 72.3,
    "country": "Belarus",
    "city": "Minsk",
    "street": "Surganoav",
    "number": 24
}
```

#### UpdateHouse 
RequestBody:
```yaml
{
  "area": 72.3,
  "country": "Belarus",
  "city": "Minsk",
  "street": "Surganoav",
  "number": 15
}
```

### Persons

#### CreatePerson 
RequestBody:
```yaml
{
    "name": "Ivan",
    "surname": "kiaspsd",
    "sex": "MALE",
    "passportSeries": "FR",
    "passportNumber": "1234567",
    "houseLiveUuid": "5c786564-6331-3031-6661-111111111111"
}
```

RequestBody:
```yaml
{
    "name": "Unajs",
    "surname": "Ivansov",
    "sex": "MALE",
    "passportSeries": "QW",
    "passportNumber": "1234767",
    "houseLiveUuid": "5c786564-6331-3031-6661-363363303439"
}
```

#### UpdatePersonHouses 
1. RequestBody:
```yaml
{
    "houses": []
}
```
2. RequestBody:
```yaml
{
    "houses": 
    [
        {
            "uuid": "5c786564-6331-3031-6661-111111111111"
        },
        {
            "uuid": "5c786564-6331-3031-6661-111111111112"
        }
    ]
}
```
