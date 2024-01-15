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
- http://localhost:8081/Gradle___ru_clevertec___houses_1_0_SNAPSHOT_war/house/find_all?pageNumber=1&pageSize=2
- http://localhost:8081/Gradle___ru_clevertec___houses_1_0_SNAPSHOT_war/house/find_all

#### 1. FindHouseByUuid (GET)
- http://localhost:8081/Gradle___ru_clevertec___houses_1_0_SNAPSHOT_war/house/find_by_uuid?uuid=5c786564-6331-3031-6661-111111111114

#### 2. FindHouseByUuidWithResidents (GET)
- http://localhost:8081/Gradle___ru_clevertec___houses_1_0_SNAPSHOT_war/house/find/with_residents?uuid=5c786564-6331-3031-6661-111111111114

#### 3. CreateHouse (PUT)
- http://localhost:8081/Gradle___ru_clevertec___houses_1_0_SNAPSHOT_war/house/create

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

#### 4. UpdateHouse (POST)
- http://localhost:8081/Gradle___ru_clevertec___houses_1_0_SNAPSHOT_war/house/update

RequestBody:
```yaml
{
  "uuid": "5c786564-6331-3031-6661-111111111114",
  "area": 72.3,
  "country": "Belarus",
  "city": "Minsk",
  "street": "Surganoav",
  "number": 15
}
```

#### 5.DeleteHouse (Нельзя удалить при наличии жильцов, а то и жить негде будет...) (DELETE)
- http://localhost:8081/Gradle___ru_clevertec___houses_1_0_SNAPSHOT_war/house/delete?uuid=5c786564-6331-3031-6661-111111111114
---
### Person
#### 0. FindAll (GET)
- http://localhost:8081/Gradle___ru_clevertec___houses_1_0_SNAPSHOT_war/person/find_all
- http://localhost:8081/Gradle___ru_clevertec___houses_1_0_SNAPSHOT_war/person/find_all?pageNumber=1&pageSize=3

#### 1. FindByUuid (GET)
- http://localhost:8081/Gradle___ru_clevertec___houses_1_0_SNAPSHOT_war/person/find_by_uuid?uuid=5c786564-6331-3031-6661-000000000000

#### 2. FindPersonWithOwnHouses (GET)
- http://localhost:8081/Gradle___ru_clevertec___houses_1_0_SNAPSHOT_war/person/find/with_houses?uuid=5c786564-6331-3031-6661-000000000002

#### 3. CreatePerson (PUT)
- http://localhost:8081/Gradle___ru_clevertec___houses_1_0_SNAPSHOT_war/person/create

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

#### 4. UpdatePersonInfo (POST)
- http://localhost:8081/Gradle___ru_clevertec___houses_1_0_SNAPSHOT_war/person/update

RequestBody:
```yaml
{
    "uuid": "5c786564-6331-3031-6661-000000000000",
    "name": "Unajs",
    "surname": "Ivansov",
    "sex": "MALE",
    "passportSeries": "QW",
    "passportNumber": "1234767",
    "houseLiveUuid": "5c786564-6331-3031-6661-363363303439"
}
```

#### 5. UpdatePersonHouses (PUT)
- http://localhost:8081/Gradle___ru_clevertec___houses_1_0_SNAPSHOT_war/person/update/houses
1.
```yaml
{
    "personUuid": "5c786564-6331-3031-6661-000000000000",
    "houses": []
}
```
2.
```yaml
{
    "personUuid": "5c786564-6331-3031-6661-00000000000",
    "houses": 
    [
        {
            "uuid": "5c786564-6331-3031-6661-111111111111"
        },
        {
            "uuid": "5c786564-6331-3031-6661-111111111111"
        }
    ]
}
```

#### 6. DeletePerson (При удалении дома "открепляются")

- http://localhost:8081/Gradle___ru_clevertec___houses_1_0_SNAPSHOT_war/person/delete?uuid=5c786564-6331-3031-6661-000000000009

---

