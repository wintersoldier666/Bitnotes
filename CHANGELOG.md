# Changelog

All releases are debug builds signed with a debug key. For personal/sideloaded use only.

---

## v1.0.3 — 2026-03-20

**Download:** `Bitnotes-v1.0.3-debug.apk`

### Fixed
- Overlapping text in PIN input fields on first launch — the floating label ("PIN") and the field placeholder ("Enter PIN (min 6 digits)") were both visible at the same time, stacking on top of each other. Fixed by moving hints from `TextInputEditText` to `TextInputLayout` so only one label renders.

---

## v1.0.2 — 2026-03-20

**Download:** `Bitnotes-v1.0.2-debug.apk`

### Fixed
- "Create PIN" title and subtitle were left-aligned on first launch instead of centered
- Biometric unlock screen now auto-prompts the biometric dialog immediately on open (300ms delay for UI to settle), instead of requiring the user to tap the button
- Disabling biometric in Settings correctly reverts to PIN-only flow on next open
- PIN minimum raised from 4 to **6 digits** in both initial setup and Change PIN dialog

---

## v1.0.1 — 2026-03-20

**Download:** `Bitnotes-v1.0.1-debug.apk`

### Fixed
- Overflow menu items had invisible text (white text on white background) — fixed toolbar popup theme
- Search bar disappeared after adding the first note — search query is now preserved across menu re-creation
- Biometric toggle in Settings triggered the enable dialog immediately on screen open — listener is now attached after the initial switch state is set

---

## v1.0.0 — 2026-03-20

**Download:** `Bitnotes-v1.0.0-debug.apk`

### Initial release

- AES-256-GCM encryption for all notes
- PBKDF2-SHA256 key derivation (310,000 iterations)
- Android Keystore hardware-backed key storage
- PIN authentication with brute-force lockout (5 attempts → 30s)
- Biometric unlock (fingerprint / face)
- Create, edit, delete, pin, color-code, and tag notes
- Full-text search (decrypted in-memory only)
- Trash with restore
- Encrypted backup / restore via `.bitnotes` files (separate backup password)
- No INTERNET permission — fully offline
- Screenshot prevention via FLAG_SECURE
- Wipe all data option
