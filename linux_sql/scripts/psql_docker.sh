su centos

#psql docker docs https://hub.docker.com/_/postgres
export PGPASSWORD='password'
#run psql
#docker run --rm --name jrvs-psql -e POSTGRES_PASSWORD=$PGPASSWORD -d -v pgdata:/var/lib/postgresql/data -p 5432:5432 postgres

# script usage 
# ./scripts/psql_docker.sh start|stop [password for database]

# function to run the container for psql
rundocker () {
	docker run -ti --name jrvs-psql -e POSTGRES_PASSWORD=$2 -d -v \
	pgdata:/var/lib/postgresql/data -p 5432:5432 postgres
	exit 0
}

# check if there are too many arguments
if (($#>2));
then
	echo "You have entered too many arguments. Please enter the arguments in the following format
psql_docker.sh start [password] OR psql_docker.sh stop" >&2
	exit 1
# check if the password is entered with start
elif [[ "$#" == 1 && "$1" == "start" ]];
then
	echo "Please enter a password" >&2
	exit 1
fi

# If less than three arguments
if [ "$1" == "start" ];
then
	# if docker status returns false, start it
	systemctl status docker || systemctl start docker
	# check if the container named jrvs-psql is running. If its not, only the headers are shown meaning two lines show up
	if [[ "$(docker ps -f name=jrvs-psql | wc -l)" == 2 ]]
	then
		echo "The container is already running" >&2
		exit 0;
	fi
	# if the volume has not been created
	if [[ "$(docker volume ls | egrep "pgdata1$" | wc -l)" == 0 ]];
	then
		# delete the container named jrvs-psql if there is one
		if [[ "$(docker ps -a -f name=jrvs-psql | wc -l)" == 2 ]];
		then
			docker stop jrvs-psql
			docker container rm jrvs-psql
		fi
		# create the volume and container
		docker volume create pgdata
		rundocker
	# check if the container is created or not
	elif [[ "$(docker container ls -a | egrep "jrvs-psql$" | wc -l)" == 0 ]];
	then	
		rundocker
	# start the stopped container
	else
		docker start jrvs-psql
		exit 0
	fi
# check if the command is to stop 
elif [[ "$1" == "stop" && "$#" == 1 ]];
then
	#stop the container & the docker
	systemctl status docker && docker stop jrvs-psql
	systemctl status docker && systemctl stop docker
	exit 0
else
	echo "Please enter the arguments in the following format 
psql_docker.sh start [password] OR psql_docker.sh stop" >&2
	exit 1
fi

exit 0
