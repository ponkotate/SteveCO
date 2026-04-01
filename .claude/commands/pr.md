PR の作成または更新を行う。タイトル・本文は日本語で記述する。

## 手順

1. `git status` と `git log --oneline origin/main..HEAD` を実行し、現在の状態を把握する
2. 未コミットの変更がある場合は、先にコミットするか確認する
3. `git push -u origin HEAD` でリモートにプッシュする
4. `gh pr view --json number` で既存 PR の有無を確認する

### PR が存在しない場合 (新規作成)

5. `git diff origin/main...HEAD` と全コミット履歴からPR内容をまとめる
6. 以下のフォーマットで `gh pr create` する:

```
gh pr create --title "<日本語タイトル (70文字以内)>" --body "$(cat <<'EOF'
## 概要
<箇条書きで変更内容を要約>

## 変更内容
<変更したファイルや機能の説明>

## テスト
- [ ] <テスト項目のチェックリスト>

🤖 Generated with [Claude Code](https://claude.com/claude-code)
EOF
)"
```

### PR が既に存在する場合 (更新)

5. 既存 PR のタイトルと本文を `gh pr view` で取得する
6. 新しいコミット内容を反映してタイトル・本文を更新する
7. `gh pr edit` でタイトルと本文を更新する

## ルール

- タイトルは簡潔に、本文で詳細を説明する
- 最新コミットだけでなく、PR に含まれる全コミットを考慮する
- draft PR にはしない
