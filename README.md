# Simpel Currency
A simple physical and digital minecraft currency plugin. It allows for items as currency. 
You can use custom model data as well that-way you can have your custom textures without replacing default ones.

**Vault support is planned but not yet implemented.**

## Installation
1. Download the latest release from [https://github.com/Evitonative/minecraft-simple-currency/releases/](https://github.com/Evitonative/minecraft-simple-currency/releases/)
2. Place the `.jar` in your plugin folder
3. Start your server
4. Adjust the configurations in `/plugins/SimpleCurrency` to your liking.
5. Restart the server to apply the settings

> ℹ️ `/screload` might work instead of restarting, though it might miss a few details or create data inconsistencies when changing the database.

## Commands & Permissions

| Command                      | Permission                       | Description                                                              |
|------------------------------|----------------------------------|--------------------------------------------------------------------------|
|                              | `simple-currency.*`              | Get access to all commands.                                              |
| `/bal`                       | `simple-currency.bal`            | Get the balance in your account.                                         |
| `/bal [player]`              | `simple-currency.bal.others`     | Get the balance of another player.                                       |
| `/bal set [player] [amount]` | `simple-currency.bal.set`        | Set someones balance.                                                    |
| `/bal set [player] [amount]` | `simple-currency.bal.set.others` | Set someones elses balance.                                              |
| `/bal set [player] [amount]` | `simple-currency.bal.set.own`    | Set someones own balance.                                                |
| `/bank`                      | `simple-currency.bank`           | Get a chat gui of all player important links.                            |
| `/deposit`                   | `simple-currency.deposit`        | Deposit the currency item you have in your hand into your bank.          |
| `/pay [player] [amout]`      | `simple-currency.pay`            | Pay a player a specified amount.                                         |
| `/screload`                  | `simple-currency.reload`         | Reload the plugins configuration.                                        |
| `/withdraw [amount]`         | `simple-currency.withdraw`       | Get a specific amout of digital money in your bank as physical currency. |



## Configuration
### Default Amount
Sets the amout of (digital) currency a player should receive on joining the first time.
Set to 0 to disable.
```yaml
default-amount: 50
```

### Auto Balance
Give every player a certain amout of coins every n seconds. 

| Value      | Description                                                                                                             |
|------------|-------------------------------------------------------------------------------------------------------------------------|
| enable     | If autobalance should be used.                                                                                          |
| delay      | The delay in seconds between every player should be given currency.                                                     |
| amout      | The amout that should be given.                                                                                         |
| ignore-afk | Disables the afk checking based on position. When set to false a player get the automatic currency only once while afk. |

```yaml
autobalance:
  enabled: true
  delay: 300
  amount: 5
  ignore-afk: false
```

### Data Storage
Here you can choose whether to save players balance in a MySQL Database or in an SQLite database.
When you choose MySQL set enabled to `true` and set the relevant details. 
When to keep enabled on `false` and you can ignore the other details. The SQLite database will be created in this plugins' directory.

> ⚠️ Switching the switching between MySQL and SQLite, changing the table prefix or the database might result in data loss or inconsistencies of data 

```yaml
mysql:
  enabled: false
  url: 127.0.0.1
  port: 3306
  user: minecraft
  password: password
  database: minecraft
  prefix: sc_
```

### Items
Here you can set physical representations of digital money.
To disable this feature you could remove all items, though it would be best to set the `simple-currency.withdraw` and `simple-currency.deposit` permissions to false for everyone.
To add a new item copy an existing one and change the details.

| Value           | Description                                                                                                                                            |
|-----------------|--------------------------------------------------------------------------------------------------------------------------------------------------------|
| value           | The amout of digital money this item should be worth.                                                                                                  |
| item            | The item material which should be used. A list of all materials is available [here](https://hub.spigotmc.org/javadocs/spigot/org/bukkit/Material.html) |
| name            | A custom name for the item. Use & format codes to format the name. RGB is not supported (yet).                                                         |
| lore            | A custom lore for the item. Use & format codes to format the name. RGB is not supported (yet). Use {0} to get the value of the given item.             |
 | customModelData | The value of a custom model in a ressource pack. Set to `null` to not use it.                                                                          |


```yaml
items:
  - value: 1
    item: "GOLD_NUGGET"
    name: "&r&6&lGold Nugget"
    lore:
      - "&5{0} coin"
    customModelData: null
  - value: 5
    item: "GOLD_INGOT"
    name: "&r&6&lGold Ingot"
    lore:
      - "&5{0} coins"
    customModelData: 1
  - value: 10
    item: "GOLD_BLOCK"
    name: "&r&6&lGold Block"
    lore:
      - "&5{0} coins"
    customModelData: 2
```

### Messages
The messages send to the player/console for specific actions.

```yaml
messages:
  balance: "[$] &aYour balance: &e{0} coins"
  balance-others: "[$] &a{0}s balance: &e{1} coins"
  set: "[$] &aSuccessfully changed {0} balance."
  payed: "[$] &aYou successfully gave {0} &e{1} coins."
  received: "[$] &a{0} gave you &e{1} coins."
  bank:
    title: "[$] &6&l{0}s coin account"
    current-balance: "&7Current balance: &e{0} coins"
    deposit: "» &aDeposit Coins"
    withdraw: "» &aWithdraw Coins"
    pay: "» &aPay someone"
  withdrawal:
    title: "[$] &6&lYou received:"
    items: "» &a{0} {1}"
  deposit:
    success: "[$] &aYou deposited {0} coins into your account"
    invalid: "[$] &cYou do not have a currency item in your hand."
  errors:
    console: "&cThis command is only available to players."
    no-self: "[$] &cYou can't pay yourself."
    no-negatives: "[$] &cPlease use a positive amount."
    not-enough-money: "[$] &cYou do not have enough coins. You need {0} coins but only have {1}."
    not-enough-space: "[$] &cYou do not have enough space in you inventory."
    invalid-player: "[$] &cPlease provide a player on the server."
    invalid-player-offline: "[$] &cThis player is not know on this server."
    int-required: "[$] &cPlease provide an integer for argument {0}"
    missing-arguments: "[$] &cPlease add all arguments"
    missing-permissions: "[$] &cYou are not permitted to do that."
```