#!/bin/bash
# Copies Talkies' settings from the TV to talkies-backup.json here: Talkies settings, the dock and app order, hidden
# apps, remote keys, channels, football leagues, the keys and the weather area (and your wallpaper.jpg, if you have
# one, to wallpaper-backup.*). Run it before uninstalling Talkies or moving to another TV; restore-settings.sh puts
# it back. The file holds your keys: it stays here, git ignores it.
#   ./backup-settings.sh [tv-ip:port]   (default: TALKIES_TV or the .tv file, see tv-address.sh)
set -euo pipefail
cd "$(dirname "$0")"
. ./tv-address.sh
DEV="${1:-$(tv_default)}"; need_tv
DIR=/sdcard/Android/data/com.techpp.glass/files
adb connect "$DEV" >/dev/null 2>&1 || true
adb -s "$DEV" shell -n rm -f "$DIR/talkies-backup.json"
adb -s "$DEV" shell -n am start -n com.techpp.glass/.HomeActivity --ez talkies_backup true >/dev/null 2>&1
for _ in 1 2 3 4 5 6 7 8 9 10; do
  sleep 1
  adb -s "$DEV" shell -n "test -s $DIR/talkies-backup.json" && break
done
adb -s "$DEV" pull -q "$DIR/talkies-backup.json" talkies-backup.json 2>/dev/null \
  || { echo "Talkies didn't write a backup. Is it installed, and is it version 0.4.1 or later?"; exit 1; }
adb -s "$DEV" shell -n rm -f "$DIR/talkies-backup.json"
chmod 600 talkies-backup.json
for ext in jpg png webp; do
  if adb -s "$DEV" shell -n "test -f $DIR/wallpaper.$ext"; then adb -s "$DEV" pull -q "$DIR/wallpaper.$ext" "wallpaper-backup.$ext"; fi
done
echo "Saved talkies-backup.json ($(wc -c < talkies-backup.json | tr -d ' ') bytes). It holds your keys: keep it private."
