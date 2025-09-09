ssh user1@176.108.244.255 -i %userprofile%/.ssh/id_sber1 -t ^
  "bash -i -c 'cd ~/adrift && git pull && cd ./webapp/ && pnpm i && pnpm generate'"
