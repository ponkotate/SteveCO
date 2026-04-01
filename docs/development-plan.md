# Steve Can Oshikko - 開発計画

## フェーズ 1: プロジェクトセットアップ

- [ ] Fabric Mod テンプレートで Kotlin プロジェクトを生成
  - `build.gradle.kts`, `gradle.properties`, `fabric.mod.json` 等
  - Fabric Language Kotlin 依存の追加
- [ ] Mod エントリポイント作成 (`SteveCOMod.kt`)
- [ ] ビルド・起動確認

## フェーズ 2: 尿意システム (コアロジック)

- [ ] `UrgencyManager` — プレイヤーごとの尿意値の管理
  - 値の蓄積 (tick ベース)
  - 閾値チェック
  - リセット処理
- [ ] プレイヤーの NBT への保存・読み込み (`PlayerDataMixin` or Fabric API の `ServerPlayerEvents`)
- [ ] 死亡時リセット (`ServerPlayerEvents.AFTER_RESPAWN`)
- [ ] 尿意 段階3 での Slowness 付与

## フェーズ 3: ネットワーク同期

- [ ] カスタムパケット定義 (`S2CUrgencySyncPayload`)
- [ ] サーバーからクライアントへ尿意値を送信 (値変化時)
- [ ] クライアント側でのローカルキャッシュ (`ClientUrgencyData`)

## フェーズ 4: ブロック実装

### 黄色い水ブロック

- [ ] `PeeBlock` クラス (通過可能、自然消滅タイマー)
- [ ] ブロックステート・モデル JSON
- [ ] テクスチャ作成 (黄色半透明)
- [ ] ブロック登録

### おまるブロック

- [ ] `ChamberPotBlock` クラス (右クリックで尿意リセット)
- [ ] カスタムブロックモデル JSON
- [ ] テクスチャ作成
- [ ] クラフトレシピ JSON
- [ ] ブロック & アイテム登録

## フェーズ 5: HUD 描画

- [ ] 顔テクスチャ 5段階分の作成
- [ ] `UrgencyHudOverlay` — ホットバー上にスティーブの顔を描画
  - Fabric の `HudRenderCallback` を使用
  - 段階に応じたテクスチャ切り替え

## フェーズ 6: 漏出演出

- [ ] 尿意値 100 到達時の処理
  - 足元にブロック設置
  - Slowness II 付与
  - サウンド再生
  - パーティクル表示
- [ ] サウンドイベント登録 & サウンドファイル用意

## フェーズ 7: 仕上げ

- [ ] テスト (シングル・マルチ)
- [ ] アイコン・Mod メタデータ整備
- [ ] バランス調整 (タイミング、ペナルティ)

## ディレクトリ構成 (予定)

```
src/main/
  kotlin/com/steveco/
    SteveCOMod.kt              # Mod エントリポイント
    block/
      PeeBlock.kt              # 黄色い水ブロック
      ChamberPotBlock.kt       # おまるブロック
    registry/
      ModBlocks.kt             # ブロック登録
      ModItems.kt              # アイテム登録
      ModSounds.kt             # サウンド登録
    urgency/
      UrgencyManager.kt        # サーバー側・尿意管理
      ClientUrgencyData.kt     # クライアント側キャッシュ
    network/
      ModNetworking.kt         # パケット登録
      S2CUrgencySyncPayload.kt # 同期パケット
    client/
      SteveCOClient.kt         # クライアントエントリポイント
      UrgencyHudOverlay.kt     # HUD 描画
  resources/
    fabric.mod.json
    assets/steveco/
      textures/
        block/
          pee_block.png
          chamber_pot.png
        gui/
          face_normal.png
          face_mild.png
          face_moderate.png
          face_severe.png
          face_critical.png
      models/
        block/
          pee_block.json
          chamber_pot.json
      blockstates/
        pee_block.json
        chamber_pot.json
      sounds.json
      sounds/
        pee_release.ogg
        chamber_pot_use.ogg
    data/steveco/
      recipe/
        chamber_pot.json
      loot_table/blocks/
        pee_block.json
        chamber_pot.json
```
