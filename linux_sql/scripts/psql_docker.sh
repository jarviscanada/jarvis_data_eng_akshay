# script usage 
# ./scripts/psql_docker.sh start|stop [password for database]

# function below will create a container for postgreSQL
dockerrun () {
	docker run -ti --name jrvs-psql -e POSTGRES_PASSWORD=$2 -d -v \
	pgdata:/var/lib/postgresql/data -p 5432:5432 postgres
	exit 0
}

# check if there is too many arguments
if (($#>2));
then
	echo "WARNING: You have entered too many arguments...
Format: psql_docker.sh start [password] OR psql_docker.sh stop" >&2
	exit 1
# check if the password is entered with start
elif [[ "$1" == "start" && "$#" == 1 ]];
then
	echo "WARNING: You did not enter password..." >&2
	exit 1
fi

# check if it is start or stop
if [ "$1" == "start" ];
then
	# start docker if not started yet
	systemctl status docker || systemctl start docker
	# check if the container named jrvs-psql is running
	if [[ "$(docker ps -f name=jrvs-psql | wc -l)" == 2 ]]
	then
		exit 0;
	fi
	# check if the volume is created or not
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
		dockerrun
	# check if the container is created or not
	elif [[ "$(docker container ls -a | egrep "jrvs-psql$" | wc -l)" == 0 ]];
	then	
		dockerrun
	# start the stopped container
	else
		docker start jrvs-psql
		exit 0
	fi
# check if it is stop 
elif [[ "$1" == "stop" && "$#" == 1 ]];
then
	#stop the container & the docker
	systemctl status docker && docker stop jrvs-psql
	systemctl status docker && systemctl stop docker
	exit 0
else
	# wrong arguments
	echo "WARNING: The arguments you entered is wrong... 
Format: psql_docker.sh start [password] OR psql_docker.sh stop" >&2
	exit 1
fi

exit 0
