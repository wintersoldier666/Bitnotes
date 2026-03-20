# Bitnotes

**Offline-first, encrypted notes and password storage for Android.**

Bitnotes keeps your notes and passwords locked behind AES-256-GCM encryption with zero network access. Nothing ever leaves your device unencrypted — not to a server, not to the cloud, not anywhere. The backup file you create is encrypted before it's written, so you can safely store it in Dropbox, Google Drive, or email it to yourself.

---

## What it's for

- Storing passwords you copy-paste manually
- Private notes you don't want synced anywhere
- Sensitive information (seed phrases, PINs, account details)
- Anything you want encrypted at rest on your device

---

## Security

| What | How |
|------|-----|
| Note encryption | AES-256-GCM, unique IV per note |
| Key derivation | PBKDF2-SHA256, 310,000 iterations |
| Key storage | Android Keystore (hardware-backed TEE) |
| Biometric unlock | Fingerprint / face, Keystore-wrapped key |
| Backup encryption | AES-256-GCM, separate password |
| Network access | None — no INTERNET permission in manifest |
| Screenshots | Blocked via FLAG_SECURE |
| Brute-force | Lockout after 5 wrong PINs |
| Cloud backup | Disabled — Android auto-backup turned off |

### Key hierarchy

```
Your PIN
    │
    ▼  PBKDF2-SHA256 · 310,000 iterations · 32-byte random salt
    │
Key Encryption Key (KEK)
    │
    ▼  AES-256-GCM wrap
    │
Data Encryption Key (DEK)  ──── stored encrypted in DataStore
    │
    ▼  AES-256-GCM · unique IV per note
    │
Encrypted notes  ──── stored in Room database
```

- Changing your PIN only re-wraps the DEK — notes are never re-encrypted
- Enabling biometric stores the KEK encrypted inside the Android Keystore
- The DEK only exists in memory while the app is unlocked

---

## Features

**Notes**
- Create, edit, delete notes
- Pin important notes to the top
- Color-code notes (6 colors)
- Add tags for organization
- Search (decrypted in-memory only, never written to disk)
- Trash with restore

**Security**
- 6-digit minimum PIN
- Biometric unlock (fingerprint / face ID)
- Auto-prompts biometric on open when enabled
- Change PIN without losing any notes
- Wipe everything — nukes all notes and keys instantly

**Backup & Restore**
- Export encrypted `.bitnotes` backup file
- Separate backup password (independent of your app PIN)
- Safe to store backup anywhere — encrypted before it's written
- Import backup on any Android device with Bitnotes installed

---

## Backup file format

```
BITNOTES_BACKUP_V1  (magic header)
32-byte PBKDF2 salt
12-byte GCM IV
AES-256-GCM ciphertext
    └─ JSON { version, exportedAt, notes: [...] }
```

The file is random-looking bytes to anyone without the backup password. Two backups of the same data look completely different (unique salt + IV each time). Safe to store in the cloud.

---

## Requirements

- Android 8.0+ (API 26)
- For biometric: device with enrolled fingerprint or face

---

## Tech stack

- **Kotlin** — MVVM + Repository pattern
- **Hilt** — dependency injection
- **Room** — local database (WAL mode)
- **DataStore** — encrypted config and key material
- **javax.crypto + Android Keystore** — all cryptography
- **AndroidX Biometric** — fingerprint / face auth
- **Material Design 3** — UI components

---

## Building

```bash
./gradlew assembleDebug    # debug build
./gradlew assembleRelease  # release build with ProGuard obfuscation
```

Release builds have ProGuard minification, debug logging stripped, and `isDebuggable = false`.
