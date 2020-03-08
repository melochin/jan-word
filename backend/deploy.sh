name='japanese-end'
port='8080'

mvn package
sudo docker build -t $name .
sudo docker container rm -f $name
sudo docker run --name $name -d -p $port:8080 $name
