FROM gradle:3.5-jdk8

ADD . /home/gradle/project

USER root
RUN chown -R gradle:gradle /home/gradle/project
USER gradle

WORKDIR /home/gradle/project

CMD gradle run