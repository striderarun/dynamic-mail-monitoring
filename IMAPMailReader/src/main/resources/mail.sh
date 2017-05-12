EXPR="java -jar Mail.jar $1 &"; 
eval $EXPR; 
pid=$!
[ ! -f email.pid ] && echo "#This is the email config file." >> email.pid
echo "$2#$pid" >> email.pid
