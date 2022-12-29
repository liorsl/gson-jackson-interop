[![Maven Build](https://github.com/liorsl/gson-jackson-interop/actions/workflows/maven-publish.yml/badge.svg)](https://github.com/liorsl/gson-jackson-interop/actions/workflows/maven-publish.yml)
[![Package Version](https://img.shields.io/nexus/r/dev.voigon/gson-jackson-interop?logo=Version&server=https%3A%2F%2Fmvn.apartiumservices.com)](https://mvn.apartiumservices.com/repository/voigon-releases/)

## Gson-Jackson-Interop
This library provides some sort of interoperability between Jackson to Gson. 
Mainly, right now it does two things:
1. Allows reading source json via Jackson ObjectMapper as JsonElement via the GsonModule
2. Allows converting between JsonNode and JsonElement and vice-versa via the NodeConverter utility class

### Including in your project
Gradle:
```groovy
repositories {
    maven {
        name = "voigon-releases"
        url = "https://mvn.apartiumservices.com/repository/voigon-releases"
    }
}

dependencies {
    implementation 'dev.voigon:gson-jackson-interop:1.0.0'
}

```

Maven:
```xml
<repositories>
    <repository>
        <id>voigon-releases</id>
        <url>https://mvn.apartiumservices.com/repository/voigon-releases</url>
    </repository>
</repositories>
<dependencies>
    <dependency>
        <groupId>dev.voigon</groupId>
        <artifactId>voigon:gson-jackson-interop</artifactId>
        <version>1.0.0</version>
    </dependency>
</dependencies>
```

### TODO List
- [X] Reading source json via Jackson ObjectMapper as JsonElement
- [X] Converting between JsonNode and JsonElement and vice-versa
- [ ] Delegating serializers and deserializers between Gson to Jackson