

./34_start_batch_job1.sh > logs/job1.log &
./35_start_batch_job2.sh > logs/job2.log &
echo Batch Job1, Job2 started

./38_start_batch_job5.sh > logs/job5.log  &
./39_start_batch_job6.sh > logs/job6.log  &
echo Batch Job5, Job6 Started

