## ------------------------------------------------------- 
## Dockerfile for it.unibo.springboot.qak  
## ------------------------------------------------------- 
FROM openjdk:12.0.2

EXPOSE 8080

ADD ./build/distributions/it.unibo.springboot.qak-1.0.0.tar / 
ADD ./build/distributions/it.unibo.springboot.qak-boot-1.0.0.tar / 
WORKDIR it.unibo.springboot.qak-boot-1.0.0/bin
COPY ./*.json ./
RUN pwd
RUN ls  
CMD ["bash","it.unibo.springboot.qak"]

# ENTRYPOINT ["echo","docker exec -ti it.unibo.springboot.qak-boot-1.0.0/bin/it.unibo.springboot.qak /bash"]

## docker build -t springbootqak .
## docker run -it --name sbqak  -p8080:8080   springbootqak  /bin/bash  
## docker restart sbqak
## docker exec -ti sbqak /bin/bash

### docker run -it --name sbqak  -p8080:8080  -e "SPRING_PROFILES_ACTIVE=docker" springbootqak
 

## docker run  --name sbqak  -p8080:8080   springbootqak  


## docker cp C:\Didattica2018Work\iss2020LabBo\it.unibo.springboot.qak\src\main\resources\connQak\sysConnKb.kt sbqak:/sysConnKb.kt