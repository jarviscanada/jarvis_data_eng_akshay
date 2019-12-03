## Introduction
The Linux Cluster Administration (LCA) team manages a Linux cluster of 10 nodes/servers which are running CentOS 7. These servers are internally connected through a switch and able to communicate through internal IPv4 addresses.

The LCA team needs to record the hardware specifications of each node and monitor node resource usages (e.g. CPU/Memory) in realtime. Cluster Monitor Agent is an internal tool that monitors the cluster resources. The collected data is stored in an RDBMS database. LCA team will use the data to generate some reports for future resource planning purposes (e.g. add/remove servers).


## Tables
There are two tables, host_info and host_usage. host_info is for hardware specifications of each connected node while host_usage contains almost real time resource usage information (updated every minute).

## host_info

id: Unique id number corresponding to each node, is a primary key in the table and auto-incremented by PostgreSQL. This MVP only has one node. 
hostname: The name of the node, has a unique constraint
cpu_number: Number of cores in the CPU
cpu_architecture: The CPU's architecture
cpu_model: Manufacturer and designation of the CPU
cpu_mhz: CPU speed in megahertz
L2_cache: Size of the L2 cache in kB
total_mem: Total amount of memory on the system in kB
timestamp: UTC timestamp of when the node hardware specifcations were collected

## host_usage

timestamp: UTC timestamp of which minute node resource usage information was collected
host_id: The node's id, corresponds to id in the host_info table and represents a foreign key constraint
memory_free: Amount of idle memory, measured in MB
cpu_idle: Percentage of total CPU time spent running kernel/system code
cpu_kernel: Percentage of total CPU time spent idle
disk_io: Number of current disk I/O operations in progress
disk_available: Amount of disk space available MB


## Usage

1) Run "./scripts/psql_docker.sh start" once to start the database with docker. Then run the ddl.sql file once to initialize the database tables.
2) `host_info.sh` collects the host hardware info and inserts it into the database. It will be run only once at the installation time.
3) `host_usage.sh` collects the current host usage (CPU and Memory) and then inserts into the database. It will be triggered by the crontab job every minute.
4) crontab is setup to run once every minute

## Improvements 

1) Improve not having duplicate code by using functions in my script files. 
2) Automate some more of the setting up process
3) Improved documentation and readme files