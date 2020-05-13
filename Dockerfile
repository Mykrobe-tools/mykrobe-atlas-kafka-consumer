FROM maven:3.5-jdk-8  
COPY src /usr/src/app/src  
COPY pom.xml /usr/src/app  
RUN mvn -f /usr/src/app/pom.xml generate-sources clean compile


ENTRYPOINT ["mvn","-f", "/usr/src/app/pom.xml", "spring-boot:run"]