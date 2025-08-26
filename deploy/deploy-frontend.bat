cd /D %~dp0
winscp.com /script=./winscp-frontend.txt

ssh user1@176.108.244.255 -i C:/Users/yurai/.ssh/id_sber1 -t "bash -i -c 'cd ~/adrift/webapp && pnpm i && pnpm generate'"
