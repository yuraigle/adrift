cd /D %~dp0
call mvnw clean package -DskipTests

pscp -i %userprofile%/.ssh/id_sber1.ppk ^
    .\target\adrift.jar user1@176.108.244.255:/home/user1/adrift/

ssh user1@176.108.244.255 -i %userprofile%/.ssh/id_sber1 -t ^
    "bash -i -c 'sudo systemctl restart adrift && echo OK'"
