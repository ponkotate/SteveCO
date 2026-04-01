# Steve Can Oshikko

Minecraft Fabric Mod (Kotlin) — 尿意メカニクスを追加する Mod。

## 仕様・計画

仕様と開発計画は docs/ に集約している。実装判断に迷ったら必ず参照すること。

- @docs/specification.md
- @docs/development-plan.md

## ビルド・テスト

- ビルド: `./gradlew build`
- クライアント起動: `./gradlew runClient`
- サーバー起動: `./gradlew runServer`

## コードスタイル

- 言語: Kotlin (Java は使わない)
- Mod ID: `steveco`
- パッケージ: `com.steveco`
- Fabric API + Fabric Language Kotlin を使用
- データクラスや sealed class など Kotlin らしい書き方を優先する

## ワークフロー

- コード変更後は spec-guardian エージェントで仕様との整合性を検証する
- テクスチャやモデル JSON 等のリソースファイルは手動作成しない。実装フェーズで指示があるまで placeholder で進める
