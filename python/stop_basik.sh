#!/bin/sh
RED='\033[0;31m'
BLUE='\033[0;34m'
REG='\033[0m'

curl -X PUT http://localhost:30108/input -F input_number="0" 2>/dev/null

curl -X PUT http://localhost:30108/output -F output_number="0" 2>/dev/null

curl -X PUT http://localhost:30108/cli -F command="q" 2>/dev/null

echo "${BLUE}Basik${REG} has stopped..."

sleep 2

curl -X DELETE http://localhost:30108/stop-api 2>/dev/null

echo "${RED}Basik API${REG} has stopped.."
echo "Done!"
