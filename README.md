# sprouts-security-credential-spring-boot-starter

## Description

Spring boot starter with security credential.

## Configurations

* maven
  ```xml
  <dependencies>
    <dependency>
      <groupId>kr.sprouts.autoconfigure</groupId>
      <artifactId>sprouts-security-credential-spring-boot-starter</artifactId>
      <version>0.0.1-SNAPSHOT</version>
    </dependency>
  </dependencies>
  ```

* gradle
  ```groovy
  dependencies {
    implementation 'kr.sprouts.autoconfigure:sprouts-security-credential-spring-boot-starter:0.0.1-SNAPSHOT'
  }
  ```

* application.yml
  ```yml
  sprouts:
    security:
      credential:
        type: <ApiKey or BearerToken> 
        api-key:
          header: <String>
          key:
            algorithm: <AES, DES, ... HS256, HS512>
            encode-type: <BASE64 or BASE64URL>
            encoded-string: <base64 or base64url encoded secret key>
        bearer-token:
          header: <String>
          key:
            algorithm: <HS256, ..., RS256, ..., ES256, ..., PS512>
            encode-type: <BASE64 or BASE64URL>
            encoded-string: <base64 or base64url encoded secret key>
  ```
## Related dependencies
* [log4j](https://logging.apache.org/log4j/2.x/)
* [Java JWT](https://github.com/jwtk/jjwt)
