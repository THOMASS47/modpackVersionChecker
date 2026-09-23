# Modpack Version Checker

[![Build and test](https://github.com/THOMASS47/modpackVersionChecker/actions/workflows/build-and-test.yml/badge.svg)](https://github.com/THOMASS47/modpackVersionChecker/actions/workflows/build-and-test.yml)

Modpack Version Checker is a small Minecraft Forge 1.7.10 mod that gives modpack maintainers a clear, user-friendly way to identify pack version mismatches before a player joins a server.

The server advertises its configured pack version through Forge's existing server-list status response. A client with the mod compares that version with its own configuration, changes Forge's compatibility indicator to a red X when they differ, and displays both versions in the tooltip. Attempting to connect opens a warning screen before Forge starts the login process.

Server owners can also choose whether mismatched or unknown client versions should be rejected during the Forge handshake.

## Features

- Detects version mismatches from the normal Forge server-list ping—no MOTD encoding or custom status service required.
- Reuses Forge's existing compatibility indicator and displays the server and client pack versions.
- Warns players before connecting, with options to return to the server list or continue anyway.
- Optionally rejects mismatched clients with a clear, colored disconnect message.
- Treats a missing advertised version as `unknown`.
- Keeps the mod's release version separate from the modpack version you configure.

## Requirements

- Minecraft 1.7.10
- Minecraft Forge 10.13.4.1614
- Java 8-compatible runtime

Install the mod on both the client and server for the complete experience. Server-only installation can enforce versions but cannot provide the pre-connect client UI. Client-only installation warns that servers without the mod have an `unknown` version, but those servers cannot enforce a pack version.

## Installation

1. Download a JAR from the [Releases page](https://github.com/THOMASS47/modpackVersionChecker/releases), or build it from source.
2. Place the JAR in the `mods` directory of the client and server.
3. Start each side once to generate `config/modpackversionchecker.cfg`.
4. Set the pack version on the client and server. Distribute the configured client file with the modpack.
5. Restart the game or server after changing the configuration.

## Configuration

```properties
general {
    # Whether the server rejects clients whose modpack version differs or is unavailable.
    B:kickOnVersionMismatch=true

    # The modpack version.
    S:version=1.0.0
}
```

`version` is the modpack version to compare. It can use any value your pack release process uses, provided matching clients and servers use the same string.

`kickOnVersionMismatch` is only meaningful on the server:

- `true` rejects clients with a different version or no advertised version.
- `false` leaves enforcement disabled; clients with the mod still receive the server-list indicator and pre-connect warning.

## Behavior

| Client state | Server state | Result |
| --- | --- | --- |
| Same configured version | Mod installed | Normal Forge compatibility indicator; connection proceeds. |
| Different configured version | Mod installed | Red indicator and pre-connect warning; the server rejects the connection when enforcement is enabled. |
| Mod installed | Mod missing | The client reports the server version as `unknown` and shows the warning. |
| Mod missing | Mod installed | No client-side warning; the server rejects the connection when enforcement is enabled. |

Choosing **I know what I'm doing** bypasses the client warning for that connection. It does not bypass server-side enforcement.

## Building from source

Clone the repository and run:

```bash
./gradlew build
```

On Windows:

```powershell
.\gradlew.bat build
```

Built JARs are written to `build/libs`.

## License

Modpack Version Checker is available under the [MIT License](LICENSE).
