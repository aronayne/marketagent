#Steps to create the container

#sbt dist
#note the message: [info] Your package is ready in /Users/aronayne/marketagent/target/universal/marketagent-1.0.zip


FROM ysihaoy/scala-play:2.12.2-2.6.0-sbt-0.13.15

# caching dependencies
COPY ["build.sbt", "/tmp/build/"]
COPY ["project/plugins.sbt", "project/build.properties", "/tmp/build/project/"]
RUN cd /tmp/build && \
 sbt compile && \
 sbt test:compile && \p
 rm -rf /tmp/build

# copy code
COPY . /root/app/
WORKDIR /root/app
RUN sbt compile && sbt test:compile

EXPOSE 9000
CMD ["sbt" , "run"]

# Steps to build and run locally
# sudo docker build -t marketagent .
# docker run -it -d -p 9000:9000 marketagent
# docker ps
# CONTAINER ID        IMAGE               COMMAND             CREATED             STATUS              PORTS                    NAMES
# b5b77c1054cc        marketagent         "sbt"               8 seconds ago       Up 7 seconds        0.0.0.0:9000->9000/tcp   dreamy_beaver
# docker attach dreamy_beaver


#https://docs.aws.amazon.com/AmazonECR/latest/userguide/docker-push-ecr-image.html
#
#https://linuxacademy.com/blog/linux-academy/deploying-a-containerized-flask-application-with-aws-ecs-and-docker/