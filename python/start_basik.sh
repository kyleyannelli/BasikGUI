#!/bin/sh
(&>/dev/null python3 -m uvicorn python.local_api:app --port 30108 &)
(&>/dev/null python3 python/run.py &> /dev/null &)