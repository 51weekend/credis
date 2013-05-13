credis
======

redis demo


 
# 程序与配置 
#####部署要求
编译环境需求：安装maven
在项目根路径下执行
mvn clean install assembly:assembly -Dmaven.test.skip=true 
生成部署包


#####部署步骤描述：
1、  确保安装jdk、版本要求java version "1.6.0_31" 或以上
2、	确保安装jetty、版本要求jetty-6.1.22 或以上
3、	新建目录 /opt/app/ (如果目录不存在)
4、	解压  family360-api.tar.gz 到/opt/app/
5、	修改启动脚本/opt/app/family360/bin/service
修改对应的配置项目为相应环境上的配置
check JAVA_HOME /usr/local/jdk1.6.0_27 修改jdk的安装路径
check JETTY_HOME /usr/local/jetty-6.1.22 修改为jetty的安装路径
check JETTY_PORT 6068   修改为对外提供服务的端口号
check PROJECT_ENV dev  修改为对应的环境  目前为 local dev test prod 线上环境为prod
6、	修改对应环境相关配置/opt/app/family360/conf/family360_${env}.properties
env为对应的环境 分别为local dev test prod
7、	启动服务: 
/opt/app/family360/bin/service start

8、	停止服务
/opt/app/family360/bin/service stop

