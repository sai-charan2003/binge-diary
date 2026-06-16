#!/bin/bash

# Default to "debug" if no argument is provided
BUILD_TYPE=${1:-debug}

# Validate input
if [[ "$BUILD_TYPE" != "debug" && "$BUILD_TYPE" != "release" ]]; then
    echo "Error: Build type must be either 'debug' or 'release'."
    exit 1
fi

echo "Setting buildType to \"$BUILD_TYPE\" in shared/module.yaml..."

# Use sed to replace the buildType line in shared/module.yaml
sed -i '' "s/buildType: \".*\"/buildType: \"$BUILD_TYPE\"/" shared/module.yaml

echo "Running Amper build for android-app ($BUILD_TYPE variant)..."
./kotlin build -m android-app -v "$BUILD_TYPE"

if [ "$BUILD_TYPE" = "debug" ]; then
    BUILD_TYPE_CAP="Debug"
else
    BUILD_TYPE_CAP="Release"
fi

# Determine output path: Use second argument, or default to android-app/release directory
OUTPUT_PATH=${2:-"android-app/release/binge-diary-${BUILD_TYPE}.apk"}

# Ensure the destination directory exists
DEST_DIR=$(dirname "$OUTPUT_PATH")
mkdir -p "$DEST_DIR"

APK_SOURCE="build/tasks/_android-app_buildAndroid${BUILD_TYPE_CAP}/gradle-project-${BUILD_TYPE}.apk"
AAR_SOURCE="build/tasks/_android-app_aarAndroid${BUILD_TYPE_CAP}/android-app-jvm.aar"

if [ -f "$APK_SOURCE" ]; then
    echo "Copying APK to $OUTPUT_PATH..."
    cp "$APK_SOURCE" "$OUTPUT_PATH"
    echo "Success! APK placed at: $OUTPUT_PATH"
else
    echo "Warning: Could not find the generated APK at $APK_SOURCE"
fi

if [ -f "$AAR_SOURCE" ]; then
    AAR_OUTPUT_PATH="${DEST_DIR}/binge-diary-${BUILD_TYPE}.aar"
    echo "Copying AAR to $AAR_OUTPUT_PATH..."
    cp "$AAR_SOURCE" "$AAR_OUTPUT_PATH"
    echo "Success! AAR placed at: $AAR_OUTPUT_PATH"
else
    echo "Warning: Could not find the generated AAR at $AAR_SOURCE"
fi
