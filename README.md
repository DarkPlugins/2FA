# Simple 2FA

A lightweight two-factor authentication plugin for Spigot/Paper servers.

Designed for Minecraft **1.18+**, this plugin enhances account security while keeping the user experience simple and non-intrusive.

[Get it on SpigotMC](https://www.spigotmc.org/resources/simple-2fa.117855/)


---

## ✨ Features

- 🔐 Two-Factor Authentication for player accounts
- 📲 Code delivery via **QR code** or in-game chat
- 🔁 Optional **auto re-login** based on IP matching
- 🎯 Flexible activation modes:
  - **Choice Mode:** Players can enable 2FA voluntarily
  - **Permission Mode:** Only selected players (permission-based) are required
  - **Global Mode:** 2FA is required for all players
- 🌍 Multi-language support (English / German)
- ⚙️ Easy-to-use configuration system

---

## 📦 Installation

1. Place the plugin `.jar` file into your `plugins/` folder
2. Start or restart your server
3. Configure the generated files if needed
4. Assign permissions and inform your players

---

## 🧠 How it works

When a player logs in for the first time (or activates 2FA), a setup process is triggered.

Depending on the configuration, the player receives their authentication code via:

- QR code (for authenticator apps)
- In-game chat

After successful setup, players will be required to complete a verification step on future logins.

An optional IP-based auto re-login system can reduce repeated verification prompts for trusted devices.

---

## ⚙️ Configuration Modes

### Choice Mode
Players decide whether they want to enable 2FA.

### Permission Mode
Only players with a specific permission are required to use 2FA.

### Global Mode
All players are required to set up and use 2FA.
