## Запуск
- [SpringMvcDispatcherServletInitializer.java](src%2Fmain%2Fjava%2Fru%2Fclevertec%2Fhouses%2Fconfig%2FSpringMvcDispatcherServletInitializer.java)
    ```java
        protected String[] getServletMappings() {
            return new String[]{"/Gradle___ru_clevertec___houses_1_0_SNAPSHOT_war/", "/"};
        }
    ```
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
- tomcat 10.1.12
## Endpoints

---
### Houses

#### 0. FindAll (GET)
- http://localhost:8081/Gradle___ru_clevertec___houses_1_0_SNAPSHOT_war/houses?pageNumber=1&pageSize=2
- http://localhost:8081/Gradle___ru_clevertec___houses_1_0_SNAPSHOT_war/houses

#### 1. FindHouseByUuid (GET)
- http://localhost:8081/Gradle___ru_clevertec___houses_1_0_SNAPSHOT_war/houses/5c786564-6331-3031-6661-111111111114

#### 2. FindHouseByUuidWithResidents (GET)
- http://localhost:8081/Gradle___ru_clevertec___houses_1_0_SNAPSHOT_war/houses/5c786564-6331-3031-6661-111111111114/with_residents

#### 3. CreateHouse (POST)
- http://localhost:8081/Gradle___ru_clevertec___houses_1_0_SNAPSHOT_war/houses/create

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

#### 4. UpdateHouse (PUT)
- http://localhost:8081/Gradle___ru_clevertec___houses_1_0_SNAPSHOT_war/houses/5c786564-6331-3031-6661-111111111114/update

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

#### 5. DeleteHouse (Нельзя удалить при наличии жильцов, а то и жить негде будет...) (DELETE)
- http://localhost:8081/Gradle___ru_clevertec___houses_1_0_SNAPSHOT_war/houses/delete?uuid=5c786564-6331-3031-6661-111111111114
---
### Person
#### 0. FindAll (GET)
- http://localhost:8081/Gradle___ru_clevertec___houses_1_0_SNAPSHOT_war/persons
- http://localhost:8081/Gradle___ru_clevertec___houses_1_0_SNAPSHOT_war/persons?pageNumber=1&pageSize=3

#### 1. FindByUuid (GET)
- http://localhost:8081/Gradle___ru_clevertec___houses_1_0_SNAPSHOT_war/persons/5c786564-6331-3031-6661-000000000007/with_houses

#### 2. FindPersonWithOwnHouses (GET)
- http://localhost:8081/Gradle___ru_clevertec___houses_1_0_SNAPSHOT_war/persons/5c786564-6331-3031-6661-000000000002/with_houses

#### 3. CreatePerson (POST)
- http://localhost:8081/Gradle___ru_clevertec___houses_1_0_SNAPSHOT_war/persons/create

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

#### 4. UpdatePersonInfo (PUT)
- http://localhost:8081/Gradle___ru_clevertec___houses_1_0_SNAPSHOT_war/persons/5c786564-6331-3031-6661-000000000006/update

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

#### 5. UpdatePersonHouses (PUT)
- http://localhost:8081/Gradle___ru_clevertec___houses_1_0_SNAPSHOT_war/persons/5c786564-6331-3031-6661-000000000006/update/houses
1.
```yaml
{
    "houses": []
}
```
2.
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

#### 6. DeletePerson (При удалении дома "открепляются")

- http://localhost:8081/Gradle___ru_clevertec___houses_1_0_SNAPSHOT_war/persons/delete?uuid=5c786564-6331-3031-6661-000000000009

---
