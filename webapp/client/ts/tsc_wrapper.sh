in_file=$(basename $1)

out_filename=$(echo $in_file | sed s/.ts\$/.js/)
out_file="../js/$out_filename"

tsc_command="tsc $in_file --outFile $out_file"

echo $tsc_command
$tsc_command
