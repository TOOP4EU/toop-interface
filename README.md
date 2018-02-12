# toop-interface

# Compile
mvn clean compile assembly:single

# Run the REST server
java -jar target/toop-dc-interface-1.0.0-SNAPSHOT-jar-with-dependencies.jar

# Try it out
http://localhost:4567/dc-data-response
