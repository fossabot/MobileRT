#!/bin/bash

###############################################################################
# Get arguments
###############################################################################
type="${1:-release}"
ndk_version="${2:-21.2.6472646}"
cmake_version="${3:-3.10.2}"
###############################################################################
###############################################################################


###############################################################################
# Get helper functions
###############################################################################
source scripts/helper_functions.sh;
###############################################################################
###############################################################################


###############################################################################
# Compile for Android
###############################################################################

# Set path to reports
reports_path=./app/build/reports
callCommand mkdir -p ${reports_path}

callCommand ./gradlew check --profile --parallel \
  -DndkVersion="${ndk_version}" -DcmakeVersion="${cmake_version}" \
  --console plain \
  | tee ${reports_path}/log_check_${type}.log 2>&1;
resCheck=${PIPESTATUS[0]};
###############################################################################
###############################################################################


###############################################################################
# Exit code
###############################################################################
echo "########################################################################"
echo "Results:"
if [ ${resCheck} -eq 0 ]; then
  echo "Check: success"
else
  echo "Check: failed"
  exit ${resCheck}
fi
###############################################################################
###############################################################################
