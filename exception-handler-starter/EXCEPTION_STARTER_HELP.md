### Publish to mavenLocal

```groovy
./gradlew publishToMavenLocal
```

### Add to project:

build.gradle
```groovy
implementation 'ru.clevertec:exception-handler-starter:1.0.0'
```

### Using
Java-class annotation [@EntityNotFoundResponse](src%2Fmain%2Fjava%2Fru%2Fclevertec%2Fexceptionhandlerstarter%2Fannotation%2FEntityNotFoundResponse.java)
```java
@EntityNotFoundResponse
public class Controller{}
    
@EntityNotFoundResponse(errorCode = HOUSE_NOT_FOUND, message = "Error", httpStatus = 404)
public class Controller{}
```
Catch [EntityNotFoundException](src%2Fmain%2Fjava%2Fru%2Fclevertec%2Fexceptionhandlerstarter%2Fexception%2FEntityNotFoundException.java)

Controller endpoints return 
[ResponseErrorResponseDto](src%2Fmain%2Fjava%2Fru%2Fclevertec%2Fexceptionhandlerstarter%2Fdomain%2FErrorResponseDto.java)

### Aspect
[EntityNotFoundExceptionAspect.java](src%2Fmain%2Fjava%2Fru%2Fclevertec%2Fexceptionhandlerstarter%2Faspect%2FEntityNotFoundExceptionAspect.java)
