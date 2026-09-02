# Litorea

> An educational desktop application for exploring the Russian national
> cryptographic standards — **Kuznyechik**, **Magma**, and **Streebog** —
> with step-by-step visualizations of what happens inside each algorithm.

![Java](https://img.shields.io/badge/Java-21-orange?logo=openjdk&logoColor=white)
![Swing](https://img.shields.io/badge/GUI-Swing-blue)
![Status](https://img.shields.io/badge/status-v0.2%20preview-yellow)
![Platform](https://img.shields.io/badge/platform-Windows%20%7C%20macOS%20%7C%20Linux-lightgrey)

---

## Table of Contents

- [About](#about)
- [Features](#features)
- [Implemented Algorithms](#implemented-algorithms)
- [Visualizations](#visualizations)
- [Project Structure](#project-structure)
- [Getting Started](#getting-started)
  - [Prerequisites](#prerequisites)
  - [Dependencies](#dependencies)
  - [Build & Run](#build--run)
- [License](#license)

---

## About

**Litorea** is a Java Swing application built as a learning tool for
cryptography. Instead of treating ciphers as black boxes, it lets you walk
through them round by round: expand a key and watch each round key appear,
step forward and backward through a Feistel network, or follow a message
through all three stages of a hash computation.

The application ships as an MDI workspace — files, encryption dialogs, and
visualization windows all open as internal frames inside a single desktop pane.

> **Note**
> This is an educational project. The implementations are written for clarity
> and inspection, not for production use. Do not use them to protect real data.

---

## Features

| Area | Capability |
|---|---|
| **Files** | Create and open files as text (`.txt`), binary (`.bin`), or images (`.png`, `.bmp`) |
| **Encryption** | Kuznyechik and Magma across five block cipher modes |
| **Decryption** | Same cipher/mode matrix as encryption |
| **Hashing** | Streebog-256 |
| **Authentication** | Message signing, HMAC-256 built on Streebog |
| **Visualization** | Interactive step-through of round transformations, key expansion, and hashing |
| **Analysis** | Partial-key recovery from a key template with `*` wildcards |

### Supported Modes of Operation

- Electronic Codebook — **ECB**
- Cipher Block Chaining — **CBC**
- Cipher Feedback — **CFB**
- Counter — **CTR**
- Output Feedback — **OFB**

### Padding Schemes

Three padding procedures are available (`algorithms/Padding.java`):

- **Procedure 1** — append zero bytes
- **Procedure 2** — append `0x80` followed by zero bytes
- **Procedure 3** — variant retained for compatibility

---

## Implemented Algorithms

### Kuznyechik — GOST R 34.12-2015

An SP-network block cipher.

| Parameter | Value |
|---|---|
| Block size | 128 bits (16 bytes) |
| Key size | 256 bits (32 bytes) |
| Rounds | 9 (10 round keys) |
| Structure | Substitution–permutation network |

Round transformation is built from **X** (round key addition), **S** (non-linear
byte substitution), and **L** (linear transformation over a shift register).

### Magma — GOST R 34.12-2015

The modernized successor to GOST 28147-89.

| Parameter | Value |
|---|---|
| Block size | 64 bits (8 bytes) |
| Key size | 256 bits (32 bytes) |
| Rounds | 32 |
| Structure | Feistel network |

Each round applies addition modulo 2³², S-box substitution, a cyclic shift of
11 bits to the left, and XOR with the opposite half-block.

### Streebog — GOST R 34.11-2012

| Parameter | Value |
|---|---|
| Internal block size | 512 bits (64 bytes) |
| Digest size | 256 bits |

Hashing proceeds in three stages:

1. **Initialization** of the hash code and all required parameters.
2. **Compression** — the G-function (XOR → S → P → L → E → XOR) applied over
   each 512-bit message block, plus bitwise exclusive OR.
3. **Finalization** — the compression function is applied to the sum of all
   message blocks, and the message length is hashed in addition.

### HMAC

`HMAC-256` is constructed over Streebog (`mac/HMAC.java`).

---

## Visualizations

Every visualization window supports stepping **forward and backward** through
the algorithm, with intermediate values shown in hexadecimal and colour-coded
by role.

| Menu path | What it shows |
|---|---|
| `Visualization → Kuznyechik cipher → Round transformation` | X, S and L applied to a block, with the active round key |
| `Visualization → Kuznyechik cipher → Key expansion` | Derivation of all 10 round keys |
| `Visualization → Magma cipher → Round transformation` | One Feistel round: subblocks L/R, round key, and each of the four transformations |
| `Visualization → Magma cipher → Key expansion` | Round key schedule for all 32 rounds |
| `Visualization → Hash functions → Streebog-256` | Guided three-stage walkthrough of the full hash computation |


---

## Project Structure

```
src/
├── App.java                 # Entry point — builds MainWindow
├── algorithms/              # Block cipher modes (ECB, CBC, CFB, CTR, OFB),
│                            # MAC and Padding
├── analysis/                # Key-space search, language detection, statistics
├── ciphers/                 # Cipher base class, Kuznyechik, Magma
├── hashfunctions/           # HashFunction base class, Streebog
├── mac/                     # HMAC over Streebog
├── statement/               # State / StateManager — records intermediate
│                            # values so visualizations can replay them
├── utils/                   # Conversions, Computing, Generation, OSInfo,
│                            # StreebogConstants, RoundedBorder
├── visuals/                 # Input dialog and menu item constructors
├── widgets/                 # Internal frames: text, binary, image, hex fields
├── windows/                 # MainWindow and all visualization windows
└── workers/                 # SwingWorker background executors
```

**How visualization works:** ciphers implement the `Stateful` interface and push
intermediate values to a `StateManager` via `addStep()` / `finalizeStep()`. The
visualization windows then read that recorded `State` list, which is what makes
stepping backwards possible.

---

## Getting Started

### Prerequisites

- **JDK 21** or newer
- A desktop environment (the application is Swing-based)

### Dependencies

The `analysis` and `utils` packages require libraries that are **not currently
vendored in this repository**. You will need them on the classpath:

| Library | Used by | Purpose |
|---|---|---|
| `com.github.pemistahl:lingua` | `analysis/LangDetector.java` | Detecting the language of decrypted candidates |
| `com.google.code.gson:gson` | `analysis/LangDetector.java` | JSON parsing |
| `org.apache.commons:commons-math3` | `analysis/LangDetector.java` | Chi-square test |
| `jakarta.xml.bind:jakarta.xml.bind-api` | `utils/Conversions.java` | `DatatypeConverter` hex encoding/decoding |

> **Warning**
> Without these JARs, `src/analysis/` and `src/utils/Conversions.java` will not
> compile. All other packages — including every cipher, the hash function, and
> all visualization windows — compile cleanly on their own.

### Build & Run

Place the dependency JARs in a `lib/` directory, then:

**Linux / macOS**

```bash
javac -encoding cp1251 -cp "lib/*" -d out $(find src -name '*.java')
java -cp "out:lib/*" App
```

**Windows (PowerShell)**

```powershell
javac -encoding cp1251 -cp "lib/*" -d out (Get-ChildItem -Recurse -Filter *.java src | % FullName)
java -cp "out;lib/*" App
```

The main window opens at a minimum size of 800×600.

---

## License

No license is currently specified for this repository. Until one is added, all
rights are reserved by the author.

---

<sub>Built with Java Swing. For study and experimentation — not for protecting real data.</sub>
