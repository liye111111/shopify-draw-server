#!/bin/bash
set -e
set -x

WORK_DIR=$(cd "$(dirname "$0")/.."; pwd)
BUILD_CMD="mvn clean install -DskipTests"

echo "WORK_DIR: $WORK_DIR"

# 定义需要构建的模块路径（相对 WORK_DIR）
MODULES=(
  "framework/boms-3rd"
  "framework/boms"
  "framework"
  "."
)

for module in "${MODULES[@]}"; do
  cd "$WORK_DIR/$module"
  $BUILD_CMD
done