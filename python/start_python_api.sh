#!/bin/sh
python3 -m uvicorn python.local_api:app --port 30108 --reload > /dev/null &
$! > .py_api_pid
python3 python.live_audio > /dev/null &
$! > .py_basik_pid