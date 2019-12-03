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

#save this variable since its used multiple times
lscpu_out=`lscpu`

hostname=$(hostname -f)
cpu_number=$(echo "$lscpu_out"  | egrep "^CPU\(s\):" | awk '{print $2}' | xargs)
cpu_architecture=$(echo "$lscpu_out"  | egrep "Architecture:" | awk '{print $2}' | xargs)
cpu_model=$(echo "$lscpu_out" | grep "Model" |cut -f2 -d ":"|tail -1|sed -e 's/^[[:space:]]*//')
cpu_mhz=$(echo "$lscpu_out"  | egrep "CPU MHz:" | awk '{print $3}' | xargs)
L2_cache=$(echo "$lscpu_out"  | egrep "L2 cache:" | awk '{print $3}' | xargs |sed 's/K/000/')
total_mem=$(grep MemTotal /proc/meminfo | awk '{print $2;}')
timestamp=$(date +%F' '%T)

insert_statement=$(cat <<-END
insert into host_info (hostname, cpu_number, cpu_architecture, cpu_model, cpu_mhz, L2_cache, total_mem, "timestamp")
values ('$hostname', $cpu_number, '$cpu_architecture', '$cpu_model', $cpu_mhz, $L2_cache, $total_mem, '$timestamp')
END
)
# verify statement
echo $insert_statement

export PGPASSWORD=$psql_password
psql -h $host -p $port -U $user -d $db_name -c "$insert_statement"

exit 0