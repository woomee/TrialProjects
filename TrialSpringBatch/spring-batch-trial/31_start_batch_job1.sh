
# NG
# ./gradlew \
#      -Dspring.main.web-application-type=none \
#      -Dspring.main.allow-bean-definition-overriding=true \
#      -Dspring.batch.job.names=sampleJob \
#      -Dspring.batch.job.enabled=true \
#      bootRun

# NG
# ./gradlew \
#      bootRun \
#      -Dspring.main.web-application-type=none \
#      -Dspring.main.allow-bean-definition-overriding=true \
#      -Dspring.batch.job.names=sampleJob \
#      -Dspring.batch.job.enabled=true

# ./gradlew \
#      --system-prop spring.main.web-application-type=none \
#      --system-prop spring.main.allow-bean-definition-overriding=true \
#      --system-prop spring.batch.job.names=sampleJob \
#      --system-prop spring.batch.job.enabled=true \
#      bootRun

# ./gradlew \
#      -Dorg.gradle.jvmargs="
#         -Dspring.main.web-application-type=none
#         -Dspring.main.allow-bean-definition-overriding=true
#         -Dspring.batch.job.names=sampleJob
#         -Dspring.batch.job.enabled=true" \
#      -Dorg.gradle.daemon=true \
#      -Dorg.gradle.configureondemand=true \
#      bootRun

# ./gradlew \
#      -Dorg.gradle.jvmargs="-Dspring.main.web-application-type=none -Dspring.main.allow-bean-definition-overriding=true -Dspring.batch.job.names=sampleJob -Dspring.batch.job.enabled=true" \
#      bootRun

# OK
export WEB_APPLICATION_TYPE=none
export JOB_ENABLED=true
export JOB_NAMES=sampleJob1
./gradlew bootRun

# OK
# java -Dspring.main.web-application-type=none \
#      -Dspring.batch.job.names=sampleJob1 \
#      -Dspring.batch.job.enabled=true \
#      -jar ./build/libs/demo-0.0.1-SNAPSHOT.jar
 