FROM openjdk:17
WORKDIR /opt
ENV SPRING_DATA_MONGODB_.HOST mongo-container
ENV MONGO_URL mongodb://mongo-container:27017/notes
EXPOSE 8080
COPY inotes/webserver/target/*.jar /opt/app.jar
CMD ["java","-jar","app.jar"]