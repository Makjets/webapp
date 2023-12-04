#!/bin/bash

# Check if the required parameter is provided
if [ -z "$1" ]; then
  echo "Usage: $0 <autoscaling-group-name>"
  exit 1
fi

autoscaling_group_name="$1"
iteration=1

while true; do
    refresh_status=$(aws autoscaling describe-instance-refreshes --auto-scaling-group-name "$autoscaling_group_name" --query 'InstanceRefreshes[0].Status' --output text)

    echo "Iteration $iteration:"
    echo $refresh_status

    if [ "$refresh_status" == "Successful" ]; then
        echo "Instance refresh completed successfully."
        break
    elif [ "$refresh_status" == "Failed" ]; then
        echo "Instance refresh failed."
        exit 1
    else
        echo "Instance refresh in progress..."
        sleep 20
    fi

    ((iteration++))
done
