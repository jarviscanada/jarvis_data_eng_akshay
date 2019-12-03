#!/bin/bash

# Make sure that there are 5 paramers passed in
if [ "$#" -ne 5 ]; then
        echo "Please pass in parameters like this: [host_usage.sh host port db_name user password]"
        exit 1
fi

host=$1
port=$2
db_name=$3
user=$4
password=$5

timestamp=$(date +%F' '%T)
hostname=$(hostname -f)
#host_id=$(psql -h $host -U $user -d $db_name -c "select id from host_info where hostname='$hostname'")
#echo $(psql -h "localhost" 5432 "host_agent" "postgres" "password" -c "select id from host_info where hostname='$(hostname -f)'")
memory_free=$(free | grep "Mem:" |awk '{print $4;}')
#disk=`df -BM /`
#cpu_idle="$disk" | awk '{if(NR==2) print 100-$5}' | xargs
cpu_idle=$(vmstat -t | awk 'FNR == 3 {print $14;}')
cpu_kernel=$(vmstat -t | awk 'FNR == 3 {print $15;}')
disk_io=$(($(vmstat -t | awk 'FNR == 3 {print $9;}') + $(vmstat -t | awk 'FNR == 3 {print $10;}')))
disk_available=$(df -m | grep "/dev/sda1"|awk '{print $4;}')

# Prepare insert statement that will be run against the $db_name database
insert_statement=$(cat <<-EOF
insert into host_usage (timestamp, host_id, memory_free, cpu_idle, cpu_kernel, disk_io, disk_available)
values ('$timestamp', (SELECT id from host_info where hostname='$hostname'), $memory_free, $cpu_idle, $cpu_kernel, $disk_io, $disk_available);
EOF
)
# verify statement
echo $insert_statement

export PGPASSWORD=$password

psql -h $host -p $port -U $user -d $db_name -c "$insert_statement"

exit 0
