export SCHEMA_REGISTRY_URL=http://localhost:8081

mvn -DBROKER_URL=http://localhost:9092 -DSCHEMA_REGISTRY_URL=http://localhost:8081 spring-boot:run
