#!/bin/bash

###############################################################################
# Change directory to MobileRT root
###############################################################################
cd "$( dirname "${BASH_SOURCE[0]}" )/.." || exit
###############################################################################
###############################################################################


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
# Run Gradle linter
###############################################################################

# Set path to reports
reports_path=./app/build/reports
callCommand mkdir -p ${reports_path}

echo "Print Gradle version";
callCommand ./gradlew --version;

echo "Calling the Gradle linter";
callCommand ./gradlew check --profile --parallel \
  -DndkVersion="${ndk_version}" -DcmakeVersion="${cmake_version}" \
  --console plain \
  2>&1 | tee ${reports_path}/log_check_"${type}".log;
resCheck=${PIPESTATUS[0]};
###############################################################################
###############################################################################


###############################################################################
# Exit code
###############################################################################
printCommandExitCode "${resCheck}" "Check"
###############################################################################
###############################################################################
