#!/usr/bin/bash
if ! command -v javac &> /dev/null
then
	echo "ERROR: javac not installed or on PATH"
	exit
fi
find ./src -name "*.java" > files.txt
javac -d build @files.txt
rm files.txt
