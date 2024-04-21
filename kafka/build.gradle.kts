plugins {
	java
	id("org.springframework.boot") version "3.2.4"
	id("io.spring.dependency-management") version "1.1.4"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_17
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
}

dependencies {
//	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.kafka:spring-kafka")
	compileOnly("org.projectlombok:lombok")
	implementation("jakarta.validation:jakarta.validation-api:3.0.2")
//	implementation("org.apache.kafka:kafka-clients")
	implementation("org.apache.commons:commons-csv:1.10.0")
	implementation("org.springframework:spring-aspects:6.0.9")
//	runtimeOnly("com.oracle.database.jdbc:ojdbc11")
	annotationProcessor("org.projectlombok:lombok")
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.2.0")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.kafka:spring-kafka-test")
	testImplementation("org.springframework.security:spring-security-test")
	implementation("io.jsonwebtoken:jjwt:0.9.1")

}

tasks.withType<Test> {
	useJUnitPlatform()
}
