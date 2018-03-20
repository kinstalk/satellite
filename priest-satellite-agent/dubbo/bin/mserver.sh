#!/bin/bash
cd `dirname $0`
if [ "$1" = "start" ]; then
	./mstart.sh
else
	if [ "$1" = "stop" ]; then
		./mstop.sh
	else
		if [ "$1" = "debug" ]; then
			./mstart.sh debug
		else
			if [ "$1" = "restart" ]; then
				./mrestart.sh
			else
				if [ "$1" = "dump" ]; then
					./mdump.sh
				else
					echo "ERROR: Please input argument: start or stop or debug or restart or dump"
				    exit 1
				fi
			fi
		fi
	fi
fi
