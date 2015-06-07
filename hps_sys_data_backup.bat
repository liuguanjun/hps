
set timestamp=%1
cd "C:\Program Files\MySQL\MySQL Server 5.5\bin"

mysqldump -uroot -proot hps > "C:\hpsdataback\hps_%timestamp%.sql"

cd .
rar a "C:\hpsdataback\hps_%timestamp%.rar" "C:\hpsdataback\hps_%timestamp%.sql"
del "C:\hpsdataback\hps_%timestamp%.sql"

