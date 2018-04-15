FROM java:8

# add directly the jar
ADD survey-rest-1.0.0-SNAPSHOT.jar /app.jar

ADD wrapper.sh wrapper.sh

RUN bash -c 'chmod +x /wrapper.sh'

# to create a modification date
RUN sh -c 'touch /app.jar'

ENTRYPOINT ["/bin/bash", "/wrapper.sh"]