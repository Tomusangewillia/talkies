#!/bin/bash
# Puts back the settings backup-settings.sh saved (talkies-backup.json here, and wallpaper-backup.* if there is one).
# Talkies must be installed and opened once first, so its folder exists. It applies the backup as it starts again,
# then deletes it from the TV.
#   ./restore-settings.sh [tv-ip:port]   (default: TALKIES_TV or the .tv file, see tv-address.sh)
set -euo pipefail
cd "$(dirname "$0")"
[ -s talkies-backup.json ] || { echo "No talkies-backup.json here: run ./backup-settings.sh first."; exit 1; }
. ./tv-address.sh
DEV="${1:-$(tv_default)}"; need_tv
DIR=/sdcard/Android/data/com.techpp.glass/files
adb connect "$DEV" >/dev/null 2>&1 || true
adb -s "$DEV" shell -n "test -d $DIR" \
  || { echo "Talkies' folder isn't on the TV yet: open Talkies once (press Home), then run this again."; exit 1; }
for ext in jpg png webp; do
  if [ -f "wallpaper-backup.$ext" ]; then adb -s "$DEV" push -q "wallpaper-backup.$ext" "$DIR/wallpaper.$ext" >/dev/null; fi
done
adb -s "$DEV" push -q talkies-backup.json "$DIR/talkies-backup.json" >/dev/null
adb -s "$DEV" shell -n am start -n com.techpp.glass/.HomeActivity --ez talkies_restore true >/dev/null 2>&1
for _ in 1 2 3 4 5 6 7 8 9 10; do
  sleep 1
  adb -s "$DEV" shell -n "test -f $DIR/talkies-backup.json" || { echo "Restored. Talkies has your settings back."; exit 0; }
done
echo "The backup is on the TV but Talkies hasn't applied it yet: press Home on the remote."
