
# NG
# ./gradlew \
#      -Dspring.main.web-application-type=servlet \
#      -Dspring.main.allow-bean-definition-overriding=true \
#      -Dspring.batch.job.enabled=false \
#      bootRun

# OK
export WEB_APPLICATION_TYPE=servlet
export JOB_ENABLED=false
./gradlew bootRun


# OK
# java -Dspring.main.web-application-type=servlet \
#      -Dspring.main.allow-bean-definition-overriding=true \
#      -Dspring.batch.job.enabled=false \
#      -jar ./build/libs/demo-0.0.1-SNAPSHOT.jar
 