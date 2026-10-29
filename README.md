# Modpack Version Checker

[![Build and test](https://github.com/THOMASS47/modpackVersionChecker/actions/workflows/build-and-test.yml/badge.svg)](https://github.com/THOMASS47/modpackVersionChecker/actions/workflows/build-and-test.yml)

A **Forge 1.7.10** mod that checks whether a player's modpack version matches the server's.

Mismatched versions show the red X in the server list and a warning before joining.

Servers can also be configured to reject mismatched or missing versions which is enabled do by default.

## Screenshots

| Server list | Join warning | Connection rejected |
| --- | --- | --- |
| [<img src="docs/screenshots/server-list.png" width="260" alt="Server list version mismatch">](docs/screenshots/server-list.png) | [<img src="docs/screenshots/join-warning.png" width="260" alt="Warning before joining">](docs/screenshots/join-warning.png) | [<img src="docs/screenshots/connection-rejected.png" width="260" alt="Connection rejected for a version mismatch">](docs/screenshots/connection-rejected.png) |

## Setup

1. Download a JAR from [Releases](https://github.com/THOMASS47/modpackVersionChecker/releases).
2. Put it in the client and server `mods` folders.
3. Launch each once to generate `config/modpackversionchecker.cfg`.
4. Set the same pack version on both sides, then restart. Include the client config with your modpack.

## Configuration

```text
general {
    B:kickOnVersionMismatch=true
    S:version=1.0.0
}
```

- `version`: your modpack version. Use the same non-empty string on both sides.
- `kickOnVersionMismatch`: set to `false` on the server to allow mismatched clients. Client warnings still appear.

## License

[PolyForm Noncommercial License 1.0.0](LICENSE)
