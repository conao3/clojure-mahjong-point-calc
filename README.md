# mahjong-point-calc

A Clojure application for calculating Japanese Mahjong (Riichi Mahjong) hand scores. This tool analyzes hand compositions, detects yaku (winning patterns), and computes the corresponding point values.

## Features

- Hand parsing and tile representation
- Complete yaku detection including:
  - Basic patterns: Toitoi, Sanankou, Suuankou
  - Full-hand patterns: Tanyao, Pinfu, Honitsu, Chinitsu, Chanta, Junchan
  - Adjacent patterns: Iipeikou, Ryanpeikou, Sanshoku Doujun, Sanshoku Doukou, Ikkitsuukan
  - Honor patterns: Shousangen, Daisangen, Shousuushii, Daisuushii
  - Special patterns: Honroutou, Chinroutou, Tsuuiisou, Ryuuiisou, Chuurenpoutou
- Schema validation with Malli
- Native binary compilation via GraalVM

## Requirements

- Clojure 1.12+
- Java 11+
- GraalVM (for native compilation)

## Getting Started

Clone the repository and start a REPL:

```bash
make repl
```

Run tests:

```bash
make test
```

## Building

Create an uberjar:

```bash
make uber
```

Compile to native binary:

```bash
make native
```

## Usage

Start the server:

```bash
java -jar target/mahjong-point-calc-standalone.jar serve
```

Or with the native binary:

```bash
./target/mahjong-point-calc serve
```

## Development

Lint the codebase:

```bash
make lint
```

Fetch lint configurations:

```bash
make fetch-lint-config
```

## License

Apache License 2.0
