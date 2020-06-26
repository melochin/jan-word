mysql_name='jan-word-mysql'
mysql_port=3307
mysql_password='root'
redis_password='root'
docker_data_path=$(cd ~;pwd)/'.jan-word-data'

# 获取当前脚本文件的文件夹绝对路径
abs=$(readlink -f "$0")
dir=$(dirname $abs)

# 创建MYSQL容器
echo '请指定MySQL密码：'
read -s mysql_password
echo '请指定Redis密码：'
read -s redis_password

# 如果容器不存在，删除会出现错误，禁止错误流输出
sudo docker container rm -f $mysql_name &> /dev/null

# 数据卷绑定，容器重建后数据丢失
echo $docker_data_path/mysql:/var/lib/mysql
sudo docker run -p 3307:3306 --name $mysql_name \
-v ${docker_data_path}/mysql:/var/lib/mysql \
-e MYSQL_ROOT_PASSWORD=$mysql_password \
-d mysql

if [ $? != 0 ];then
 exit
fi

# 创建shema
sleep 1s
sudo docker exec -i $mysql_name mysql -uroot -p$mysql_password < $dir/init.sql 

mysql_ip=$(sudo docker inspect --format='{{.NetworkSettings.IPAddress}}' ${mysql_name})


# Redis
redis_name='jan-word-redis'
redis_port=6379

sudo docker container rm -f $redis_name &> /dev/null
sudo docker run -d --name ${redis_name} -p ${redis_port}:6379 redis --requirepass "${redis_password}"
redis_ip=$(sudo docker inspect --format='{{.NetworkSettings.IPAddress}}' ${redis_name})


# 部署后端程序
backend_name='jan-word-backend'
backend_port='8080'
cd $dir
cd ../backend

# 编译后端
mvn package

# 使用了docker内部的IP，因此使用映射前的端口号
sudo docker build --build-arg SPRING_DATASOURCE_URL="jdbc:mysql://${mysql_ip}:3306/jan_word?characterEncoding=utf8&characterSetResults=utf8&autoReconnect=true&failOverReadOnly=false&serverTimezone=Asia/Shanghai" \
--build-arg MYSQL_PASSWORD=$mysql_password \
--build-arg SPRING_REDIS_HOST=$redis_ip \
--build-arg REDIS_PASSWORD=$redis_password \
-t $backend_name .
sudo docker container rm -f $backend_name
sudo docker run --name $backend_name -d -p $backend_port:8080 $backend_name

# 部署前端程序
frontend_name='jan-word-frontend'
frontend_port='80'

cd $dir
cd ../frontend

# 编译前端
tyarn build

sudo docker build -t $frontend_name .
sudo docker container rm -f $frontend_name
sudo docker run --name $frontend_name -d -p $frontend_port:80 $frontend_name
