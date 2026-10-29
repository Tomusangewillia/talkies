# Changelog

Versions are numbered *major.minor.fix*. While Talkies is in beta they start with 0: a new minor number brings new
features, a new fix number only fixes. The version shows in Talkies settings → About.

## 1.0.0 — 29 October 2026

### New
- **A ready APK, and updates from Talkies settings.** Each release has `talkies.apk`, signed with the project's key,
  to install with adb without building. Talkies settings → **Updates** shows whether a newer release is out (checked
  once a day) and installs it, with Android asking first. Only the APK from the releases takes updates; one built on
  a computer is told a new version is out. Adapted from Free My TV's updater (MIT).

### Fixed
- **No crash on a fresh install.** Right after installing, Android can connect Talkies' media listener before it allows
  it, and the refusal crashed Talkies on start (Home and the app icon did nothing). It now waits and tries again every
  30 seconds.

## 0.4.1 — 29 October 2026

### New
- **RAM and storage on the home screen**: two slim meters beside the icon buttons show how much of each is free,
  updated every minute (Talkies settings → RAM and storage hides them).
- **Send from your phone** (Talkies settings → Keys and location): a QR code opens a page on your phone to paste the
  TMDB key, the CricketData key and the weather area, instead of typing them with the remote. It works once, for 5
  minutes, only on your home network; each key is still checked before it's saved. Adapted from Free My TV (MIT).
- **Back up and restore your settings**: `./backup-settings.sh` copies Talkies settings, the dock and app order,
  hidden apps, remote keys, channels, football leagues, the keys and the weather area to `talkies-backup.json` on your
  computer; `./restore-settings.sh` puts them back after a reinstall or on another TV.

### Changed
- **Keys and location** is now the first row in Talkies settings.

### Fixed
- **Back online, fetched at once**: when the Wi-Fi drops and comes back, the weather, picks, scores and Bing photo
  that failed meanwhile are fetched straight away instead of at their next retry. An idea from Free My TV.

## 0.4.0 — 28 October 2026

### New
- **Bing wallpaper** (Talkies settings, off by default): Bing's picture of the day, with its credit in About.
- **Apps open from their tile**: an app grows out of the tile or card you chose (Talkies settings, on by default).
- **Now playing** in Control Center: the song or show playing on the TV, with artwork, Play / Pause, Next, Previous
  and Open. An idea from Luma.
- **Weather forecast** in Control Center: the next hours, and OK again for the next days.
- **Inputs know your consoles**: a PlayStation, Xbox, Switch, Apple TV, Fire TV or SHIELD shows under its real name,
  with a dot in its brand's colour (Luma's device list, MIT).
- **Proper tiles for apps without a TV banner**: the logo on its own colour, or the logo and name on white, instead of
  a small icon on a blank card (GlassLauncher's tile builder, MIT).
- **Animations** in Control Center: 1×, 0.5× or Off for the whole TV (with the `WRITE_SECURE_SETTINGS` grant).
- **Storage** in Control Center: free space at a glance. Both ideas from GlassLauncher.
- **A stopped Home service is noticed.** If Android stops Talkies' accessibility service, Talkies switches it off and
  on itself (with the `WRITE_SECURE_SETTINGS` grant), or says so on the home screen and in Control Center.

### Changed
- **Runs on Android 8.0 and later** (it needed Android 11). On Android 8, the Sleep timer and Turn off now are hidden,
  since standby needs Android 9. Untested on those versions so far.
- **Text stays readable on a wallpaper.** Talkies measures the photo and adds just enough veil behind the text for
  4.5:1 contrast, top to bottom; with Appearance on Auto it picks the light or dark look, whichever suits the photo.
- Talkies settings is the first row of Control Center.
- A stronger focus ring, with a soft halo, so the selected tile is easy to see from the sofa.
- Channel cards show what you watched last first, for apps that say when (Nuvio does).

## 0.3.0 — 28 October 2026

### New
- **Bluetooth in Control Center:** your paired speakers and headphones, with OK to connect or disconnect one.
- **Restart…** in Control Center, through the TV's own power menu, so it still asks first.
- **Talkies at power-on: Every time** (Talkies settings): Talkies also comes up when the TV wakes from standby, not
  only after a restart, unless you press a button first.
- **Hold jobs for remote keys:** a button with a job can do a second one when held.
- **Empty inputs: Under More inputs** (Talkies settings): Inputs lists only ports with something plugged in.

### Changed
- A new logo: a cinema ticket with a play button, on a warm dark background, for the app icon and the TV banner
  (it replaces the two glass cards from when Talkies was called Glass Home).
- Menus are fully opaque, so nothing behind them shows through the text.
- The ⚙ icon opens Android TV's own settings on TVs that hide the standard settings screen.
- The README covers pairing with `adb pair` for TVs that only have Wireless debugging.

## 0.2.0 — 28 October 2026

### New
- **Live football scores.** A Football row and score line like cricket's, from ESPN's public scoreboard, with no
  key needed: Premier League, Champions League, LaLiga, Serie A, Bundesliga, ISL, MLS and more. Each country starts
  with its own leagues; change them in Talkies settings → Football leagues. Updates every minute while a match is on.
- **Live scores setting:** cricket, football, both or off.
- **Pin and hide apps.** Hold OK on an app: **Add to dock**, **Remove from dock**, or **Hide** (it stays installed).
  Talkies settings → Hidden apps brings them back.
- **Keys and location on the TV.** Type or paste your TMDB key, CricketData key and weather location in Talkies
  settings → Keys and location; no computer needed. Keys are checked with the service before they're saved.
- **Talkies settings,** separate from Control Center. Control Center keeps the quick actions (sleep timer, turn off,
  memory, network, speed test, sound, screensaver); everything you set once moved to Talkies settings, opened from
  Control Center's last row or by holding OK on the ⚙ icon.

### Changed
- A Test match at stumps, lunch, tea or a rain delay shows as paused, not live; a live football match then takes
  the score line, and its row moves above Cricket.
- The score line shows the football league: "● LIVE  Serie A · 54' · SAS 2–4 LAZ".
- New installs start with Watch Next and each app's own home-screen row instead of the author's channel choice
  (TVs that had Talkies before keep theirs).
- On Android TV, Home goes to a launcher you picked as the home app; the README explains switching launchers on
  Google TV, which keeps its own launcher as the home app.
- `switch-home.sh` takes `talkies`, `stock`, any launcher's package name, or `list`.
- Transparent images (team badges) are cached as PNG, so they don't get black backgrounds.

### Fixed
- Score rows didn't update while focus rested on them, so a live match could show in the score line but not in
  the rows.

## 0.1.0 — 25 October 2026

First public version: a small, ad-free home screen for Android TV and Google TV. Weather, clock and date; For You
picks from TMDB with Continue, Trending and New in your country; a seven-app dock; channel rows; live cricket
scores; Control Center with sleep timer, turn off, speed test and HDMI-CEC inputs; remote key remapping; a
background that glows in the focused app's colour; light by day and dark at night. About 200 KB, no libraries,
MIT licensed.
