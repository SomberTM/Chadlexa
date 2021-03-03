@echo off
mvn package
java -cp target/Chadlexa-0.1.jar src.main.java.com.chadlexa.app.Program
