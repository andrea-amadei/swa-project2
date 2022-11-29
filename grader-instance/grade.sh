#!/bin/bash

wget http://grader-api:4000/api/textonly?submissionID=$SUBMISSION_ID --quiet --output-document=/app/submission/submitted_code.c

RND=$(( $RANDOM % 10 ))

sleep $(( $RND + 5 ))

if [ $RND -lt 5 ]; then
  RES="PASS"
elif [ $RND -lt 9 ]; then
  RES="FAIL"
else
  RES="ERROR"
fi

echo $RES
