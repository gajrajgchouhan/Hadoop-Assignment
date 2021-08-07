#!/usr/bin/bash
set -e 

HADOOP_CLASSPATH=$(hadoop classpath)
LAB7_DIR="/Lab7"
INPUT_DIR="/Lab7/Input"
OUTPUT_DIR="/Lab7/Output"

cp './random_Data.csv' './input_data'
rm -rf ./java_classes/* *.jar

# compile java file to classes
printf "\n\nCompile to classes\n\n"
javac -classpath ${HADOOP_CLASSPATH} -d './java_classes/' './Lab7.java'

# make jar file of classes
printf "\n\nMaking jar files\n\n"
jar -cvf 'lab7.jar' -C './java_classes/' .

hadoop fs -rm -r -f $INPUT_DIR
hadoop fs -rm -r -f $OUTPUT_DIR

# lab7 directory

printf "\n\nmaking Lab7 directory\n\n"
if hadoop fs -test -d $LAB7_DIR  ; then
    echo ""
else
    hadoop fs -mkdir $LAB7_DIR
fi

# input directory

printf "\n\nmaking Input directory\n\n"
if hadoop fs -test -d $INPUT_DIR  ; then
    echo ""
else
    hadoop fs -mkdir $INPUT_DIR
fi

printf "\n\ncheck at http://localhost:50070/\n\n"
printf "\n\ncheck at http://localhost:8088/\n\n"

# copy input
printf "\n\nCopying input\n\n"
hadoop fs -put './input_data/random_Data.csv' /Lab7/Input

# use jar file in hadoop
#           jar file   public  input           output
#                      class   directory     directory
printf "\n\nExecuting jar\n\n"
hadoop jar 'lab7.jar' 'org.myorg.Lab7' '/Lab7/Input' $OUTPUT_DIR

# output
printf "\n\nCheck the output directory\n\n"
hadoop fs -ls '/Lab7/Output/'
hadoop fs -cat '/Lab7/Output/part-r-00000'

printf "go to http://localhost:50070/explorer.html#/ to see output"