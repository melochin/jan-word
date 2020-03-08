name='japanese'
port='80'
tyarn build
sudo docker build -t $name .
sudo docker container rm -f $name
sudo docker run --name $name -d -p $port:80 $name
