#!/bin/bash

###############################################################################
# Change directory to MobileRT root
###############################################################################
cd "$(dirname "${BASH_SOURCE[0]}")/.." || exit
###############################################################################
###############################################################################

###############################################################################
# Get arguments
###############################################################################
type="${1:-release}"
ndk_version="${2:-21.3.6528147}"
cmake_version="${3:-3.10.2}"
###############################################################################
###############################################################################

###############################################################################
# Get helper functions
###############################################################################
source scripts/helper_functions.sh
###############################################################################
###############################################################################

###############################################################################
# Run unit tests natively
###############################################################################

# Set path to reports
reports_path=./app/build/reports
callCommand mkdir -p ${reports_path}

# Capitalize 1st letter
type="$(tr '[:lower:]' '[:upper:]' <<<"${type:0:1}")${type:1}"
echo "type: '${type}'"

function runUnitTests() {
  echo "Calling Gradle test"
  callCommand ./gradlew test"${type}"UnitTest --profile --parallel \
    -DndkVersion="${ndk_version}" -DcmakeVersion="${cmake_version}" \
    --console plain \
    2>&1 | tee ${reports_path}/log_native_tests_"${type}".log
  resUnitTests=${PIPESTATUS[0]}
}
###############################################################################
###############################################################################

runUnitTests

###############################################################################
# Exit code
###############################################################################
printCommandExitCode "${resUnitTests}" "Unit tests"
###############################################################################
###############################################################################
