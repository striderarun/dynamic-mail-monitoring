line1=$(cat email.pid | grep $1)
IFS='#' read -r -a arr <<< "$line1"
kill -9 ${arr[1]}
grep -v "$1" email.pid > temp && mv temp email.pid
