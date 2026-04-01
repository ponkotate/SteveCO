---
description: Git のブランチ命名規則とコミットメッセージ規約
---

# Git ルール

## ブランチ命名規則

- ブランチ名は必ず `feature/` プレフィックスで開始すること
- 例: `feature/urgency-system`, `feature/pee-block`, `feature/hud-overlay`

## コミットメッセージ規約 (AngularJS Convention)

### フォーマット

```
<type>(<scope>): <subject>

<body>

<footer>
```

- ヘッダー (`<type>(<scope>): <subject>`) は必須。scope は任意
- 全行 100 文字以内

### type (必須)

| type | 用途 |
|------|------|
| `feat` | 新機能 |
| `fix` | バグ修正 |
| `docs` | ドキュメントのみの変更 |
| `style` | コードの意味に影響しない変更 (フォーマット等) |
| `refactor` | バグ修正でも新機能でもないコード変更 |
| `perf` | パフォーマンス改善 |
| `test` | テストの追加・修正 |
| `chore` | ビルドプロセスや補助ツールの変更 |

### scope (任意)

変更の影響範囲を示す。例: `urgency`, `block`, `network`, `hud`, `registry`

### subject (必須)

- 命令形で書く: "change" であって "changed" や "changes" ではない
- 先頭を大文字にしない
- 末尾にピリオドをつけない

### body (任意)

- 変更の動機と以前の動作との対比を書く
- 命令形の現在時制を使う

### footer (任意)

- 破壊的変更は `BREAKING CHANGE:` で始める
- GitHub Issue の参照はクローズキーワードで記述する
