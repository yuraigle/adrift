cd /D %~dp0
winscp.com /script=./deploy.txt
ssh user1@176.108.244.255 -i %USERPROFILE%/.ssh/id_sber1 -t "bash -i -c 'cd ~/adrift/webapp && pnpm i && pnpm generate'"
