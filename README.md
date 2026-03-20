# 🔒 Bitnotes — Encrypted Offline Notes App for Android

A highly secure, fully offline note-taking app for Android with military-grade encryption and encrypted backup/restore.

---

## Security Architecture

### Encryption Stack
| Layer | Algorithm | Details |
|-------|-----------|---------|
| Note Encryption | **AES-256-GCM** | Authenticated encryption, unique IV per note |
| Key Derivation | **PBKDF2-SHA256** | 310,000 iterations (OWASP 2023 recommendation) |
| Key Storage | **Android Keystore** | Hardware-backed TEE/SE when available |
| Backup Encryption | **AES-256-GCM** | Separate password, PBKDF2-derived key |

### Key Hierarchy
```
User PIN
    │
    ▼ PBKDF2-SHA256 (310,000 iterations + 32-byte random salt)
    │
Key Encryption Key (KEK)
    │
    ▼ AES-256-GCM wrap
    │
Data Encryption Key (DEK)  ◄─── stored encrypted in DataStore
    │
    ▼ AES-256-GCM (unique IV per note)
    │
Encrypted Notes  ◄─────────── stored in Room database
```

This architecture means:
- **PIN change** only re-wraps the DEK (no re-encryption of notes)
- **Biometric unlock** stores KEK encrypted with Android Keystore key
- **All notes** use the same DEK (efficient), protected by KEK

---

## Features

### Security
- ✅ **AES-256-GCM** encryption for all note data
- ✅ **PBKDF2-SHA256** (310k iterations) key derivation
- ✅ **Android Keystore** hardware-backed key storage
- ✅ **Zero network access** — no INTERNET permission
- ✅ **Screenshot prevention** — FLAG_SECURE on all activities
- ✅ **No cloud backup** — all auto-backup disabled
- ✅ **Brute-force protection** — 5 attempts → 30s lockout
- ✅ **Biometric unlock** — fingerprint/face with Keystore-backed KEK
- ✅ **PIN change** without re-encrypting notes
- ✅ **Wipe all data** — complete secure erase
- ✅ **Encrypted backup** — AES-256-GCM with separate password

### App Features
- 📝 Create, edit, delete notes
- 📌 Pin notes to top
- 🎨 Color-code notes (6 colors)
- 🏷️ Tags for organization
- 🔍 Encrypted search (decrypted in-memory only)
- 🗑️ Trash with restore capability
- 📦 Encrypted backup (.bitnotes files)
- 🔄 Import/restore from backup
- 💾 Share backups via any app

---

## Backup Format

Backup files use the `.bitnotes` extension:

```
[BITNOTES_BACKUP_V1 header]
[32-byte PBKDF2 salt]
[12-byte GCM IV]
[AES-256-GCM encrypted JSON]
    └─ BackupData {
        version, exportedAt,
        notes: [{ title, content, tags, ... }]
       }
```

- **Encrypted with a separate backup password** (not your app PIN)
- The backup file is **useless without the backup password**
- Safe to store in cloud storage (Dropbox, Drive, etc.)

---

## Tech Stack

- **Language**: Kotlin
- **Min SDK**: 26 (Android 8.0)
- **Architecture**: MVVM + Repository
- **DI**: Hilt
- **Database**: Room (WAL mode)
- **Crypto**: `javax.crypto` + Android Keystore
- **Auth**: AndroidX Biometric
- **Storage**: DataStore (encrypted config)
- **UI**: Material Design 3

---

## Building

```bash
./gradlew assembleRelease
```

The release build has:
- ProGuard minification + obfuscation
- Debug logging stripped
- `isDebuggable = false`
- No network traffic allowed

---

## Security Considerations

1. **Notes plaintext never persists** — decryption only in memory
2. **DEK cleared from memory** when app locks/backgrounds
3. **Unique IV** generated per encryption operation (prevents IV reuse attacks)
4. **GCM authentication tag** (128-bit) prevents ciphertext tampering
5. **Salt** unique per installation (prevents rainbow table attacks)
6. **PIN hashed** separately from key derivation (timing-safe comparison)
