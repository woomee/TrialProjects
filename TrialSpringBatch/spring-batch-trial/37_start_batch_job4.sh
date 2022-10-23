
# NG
# ./gradlew \
#      -Dspring.main.web-application-type=none \
#      -Dspring.main.allow-bean-definition-overriding=true \
#      -Dspring.batch.job.names=sampleJob2 \
#      -Dspring.batch.job.enabled=true \
#      bootRun

export WEB_APPLICATION_TYPE=none
export JOB_ENABLED=true
export JOB_NAMES=Job4_Chunk
./gradlew bootRun

# OK
# java -Dspring.main.web-application-type=none \
#      -Dspring.batch.job.names=sampleJob2 \
#      -Dspring.batch.job.enabled=true \
#      -jar ./build/libs/demo-0.0.1-SNAPSHOT.jar
 