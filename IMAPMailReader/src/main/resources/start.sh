./stop.sh $1
line1=$(grep $1 config.txt)
IFS='#' read -r -a arr <<< "$line1"
v1="--EMAIL=imaps://${arr[1]}@mail.abc.com:993/inbox"
./mail.sh $v1 $1

