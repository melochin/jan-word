
# 获取当前脚本文件的文件夹绝对路径
abs=$(readlink -f "$0")
dir=$(dirname $abs)

cd $dir
source ./jan-word.config

# 创建MYSQL容器
echo '请指定MySQL密码：'
read -s mysql_password
echo '请指定Redis密码：'
read -s redis_password

function deloyMysql() {
 # 如果容器不存在，删除会出现错误，禁止错误流输出
 sudo docker container rm -f $mysql_name &> /dev/null

 # 数据卷绑定，容器重建后数据丢失
 sudo docker run -p 3307:3306 --name $mysql_name \
 -v $docker_data_path/mysql:/var/lib/mysql \
 -e MYSQL_ROOT_PASSWORD=$mysql_password \
 -d mysql

 if [ $? != 0 ];then
  exit
 fi

 # 等待Mysql启动，创建shema
 sleep 5s
 sudo docker exec -i $mysql_name mysql -uroot -p$mysql_password < $dir/init.sql 
}


function deployRedis() {
 sudo docker container rm -f $redis_name &> /dev/null
 sudo docker run -d --name $redis_name -p $redis_port:6379 redis --requirepass "$redis_password"
}

function deployBackend() {
 cd $dir
 cd ../backend

 # 编译后端
 mvn package

 # 使用了docker内部的IP，因此使用映射前的端口号
 # 设置Dockerfile的环境变量，然后利用OS环境变量优先级高于Spring Boot配置文件的规则，覆盖配置文件的环境变量
 sudo docker build --build-arg SPRING_DATASOURCE_URL="jdbc:mysql://${mysql_ip}:3306/jan_word? characterEncoding=utf8&characterSetResults=utf8&autoReconnect=true&failOverReadOnly=false&serverTimezone=Asia/Shanghai" \
 --build-arg MYSQL_PASSWORD=$mysql_password \
 --build-arg SPRING_REDIS_HOST=$redis_ip \
 --build-arg REDIS_PASSWORD=$redis_password \
 -t $backend_name .
 sudo docker container rm -f $backend_name
 sudo docker run --name $backend_name -p $backend_port:8080 -d $backend_name
}

function deployFrontend() {
 cd $dir
 cd ../frontend
 pwd

 # 编译前端
 tyarn build:local

 sudo docker build -t $frontend_name .
 sudo docker container rm -f $frontend_name
 sudo docker run --name $frontend_name -p $frontend_port:80 -d $frontend_name
}

function getDockerIP() {
 echo $(sudo docker inspect --format='{{.NetworkSettings.IPAddress}}' $1)
}

deloyMysql
deployRedis

mysql_ip=$(getDockerIP $mysql_name 2>&1)
redis_ip=$(getDockerIP $redis_name 2>&1)

deployBackend
deployFrontend
