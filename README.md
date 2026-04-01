# Steve Can Oshikko (SteveCO)

**A Fabric mod that adds a bladder urgency mechanic to Minecraft. Hold it in or face the consequences.**

**Minecraft に尿意メカニクスを追加する Fabric Mod。我慢するか、漏らすか。**

---

## Features / 特徴

### Bladder Urgency System / 尿意システム

Your bladder fills up over time. Ignore it, and things get... messy.

時間経過で尿意が蓄積。放置すると大変なことに。

| Stage | Urgency | Effect |
|-------|---------|--------|
| Normal / 正常 | 0 ~ 24 | None / なし |
| Mild / 軽度 | 25 ~ 49 | None / なし |
| Moderate / 中度 | 50 ~ 74 | None / なし |
| Severe / 重度 | 75 ~ 99 | Slowness I / 移動速度 -20% |
| **Critical / 限界突破** | **100** | **Involuntary release! / 漏出!** |

When urgency hits 100, a yellow fluid block appears at your feet — along with Slowness II for 5 seconds. Embarrassing.

尿意が 100 に達すると、足元に黄色い水が出現。さらに 5 秒間の移動速度低下ペナルティ付き。

### HUD Indicator / HUD 表示

A face icon above your hotbar shows your current urgency stage — from calm to panic.

ホットバー上の顔アイコンが尿意段階をリアルタイムで表示。平常顔からパニック顔まで 5 段階。

### Chamber Pot / おまるブロック

Craft a duck-shaped chamber pot to relieve yourself safely. Right-click to use — urgency resets to 0.

アヒル型おまるをクラフトして安全に用を足そう。右クリックで尿意が 0 にリセット。

```
[   ] [   ] [Fe ]
[Fe ] [   ] [Fe ]
[Fe ] [Fe ] [Fe ]
```

6 Iron Ingots / 鉄インゴット 6 個

### Pee Fluid / 黄色い水

- Flows up to 3 blocks / 最大 3 ブロック流れる
- Disappears after 60 seconds / 60 秒で自然消滅
- Cannot be picked up with a bucket / バケツ取得不可
- Does not form infinite sources / 無限水源化しない

## Commands / コマンド

Operator-only commands (permission level 2) for debugging and testing.

OP 権限（レベル 2）が必要なデバッグ・テスト用コマンド。

| Command | Description |
|---------|-------------|
| `/urgency get` | Show your current urgency value / 現在の尿意値を表示 |
| `/urgency set <0-100>` | Set your urgency value / 尿意値を設定 |

## Requirements / 動作環境

| | Version |
|---|---------|
| Minecraft | 26.1 |
| Java | 25+ |
| Fabric Loader | 0.18+ |
| [Fabric API](https://modrinth.com/mod/fabric-api) | Required / 必須 |
| [Fabric Language Kotlin](https://modrinth.com/mod/fabric-language-kotlin) | Required / 必須 |

## Installation / 導入方法

1. Install [Fabric Loader](https://fabricmc.net/) for Minecraft 26.1
2. Download and place [Fabric API](https://modrinth.com/mod/fabric-api) and [Fabric Language Kotlin](https://modrinth.com/mod/fabric-language-kotlin) in your `mods/` folder
3. Download SteveCO and place it in your `mods/` folder
4. Launch the game!

---

1. Minecraft 26.1 用の [Fabric Loader](https://fabricmc.net/) を導入
2. [Fabric API](https://modrinth.com/mod/fabric-api) と [Fabric Language Kotlin](https://modrinth.com/mod/fabric-language-kotlin) を `mods/` フォルダに配置
3. SteveCO を `mods/` フォルダに配置
4. ゲームを起動!

## License / ライセンス

[MIT](LICENSE)
