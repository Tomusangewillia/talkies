#!/bin/bash
# Builds the release APK, signed with the project's key, and attaches it to the GitHub release of the version in
# app/build.gradle as talkies.apk: what people install with adb, and what Talkies settings → Updates installs.
# Only the maintainer can run it: it needs release.properties (never in git) and the key it points to.
#   ./release-apk.sh            the release must already exist (gh release create vX.Y.Z …)
set -euo pipefail
HERE="$(cd "$(dirname "$0")" && pwd)"
cd "$HERE"
export JAVA_HOME="${JAVA_HOME:-/opt/homebrew/opt/openjdk@17/libexec/openjdk.jdk/Contents/Home}"
export ANDROID_HOME="${ANDROID_HOME:-/opt/homebrew/share/android-commandlinetools}"
[ -f release.properties ] || { echo "No release.properties: only the project's key can sign releases."; exit 1; }
VERSION="$(sed -n "s/.*versionName '\(.*\)'.*/\1/p" app/build.gradle)"
# The key Talkies checks updates against (SelfUpdate.RELEASE_CERT).
CERT="$(sed -n 's/.*RELEASE_CERT = "\([0-9a-f]*\)".*/\1/p' app/src/main/java/com/techpp/glass/SelfUpdate.java)"
echo "== Building $VERSION"
./gradlew -q clean assembleRelease
APK=app/build/outputs/apk/release/app-release.apk
SIGNER="$(ls -d "$ANDROID_HOME"/build-tools/*/ | tail -1)apksigner"
GOT="$("$SIGNER" verify --print-certs "$APK" | sed -n 's/.*certificate SHA-256 digest: //p' | head -1)"
[ "$GOT" = "$CERT" ] || { echo "!! The APK isn't signed with the release key (got $GOT)."; exit 1; }
cp "$APK" "$HERE/build/talkies.apk" 2>/dev/null || { mkdir -p build; cp "$APK" build/talkies.apk; }
echo "== Attaching talkies.apk ($(wc -c < build/talkies.apk | tr -d ' ') bytes) to v$VERSION"
gh release upload "v$VERSION" build/talkies.apk --clobber
echo "Done: https://github.com/rajupp/talkies/releases/tag/v$VERSION"
