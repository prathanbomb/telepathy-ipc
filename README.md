# Telepathy-IPC

A simple library for sending data between appication.

## Installation

Add this to your gradle.build

```gradle
implementation 'th.co.digio.poolshark:telepathy-ipc:1.0.2_20201029'
```

## Usage

### Start Connection
```kotlin
Synapse.instance().startConnection(object : Synapse.ConnectionListener {
            override fun onConnected() {
                // TODO when connected
            }

            override fun onDisconnected() {
                // TODO when disconnected
            }
        }, target_package_name)
```

### Listening for incoming data
```kotlin
Synapse.instance().registerListener(object : Synapse.DataListener {
            override fun onReceived(data: Bundle?) {
                // TODO when receive data
            }
        })
```

### Transmit data
```kotlin
Synapse.instance().transmit(dataBundle)
```

## Contributing
Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

Please make sure to update tests as appropriate.

## License
[MIT](https://choosealicense.com/licenses/mit/)
