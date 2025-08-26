cd /D %~dp0\..
call mvnw clean package

cd /D %~dp0
winscp.com /script=./winscp-backend.txt

ssh user1@176.108.244.255 -i C:/Users/yurai/.ssh/id_sber1 -t "bash -i -c 'sudo systemctl restart adrift && echo OK'"
