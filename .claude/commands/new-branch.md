最新の origin/main を取り込み、新しいブランチで作業を開始する。

1. `git fetch origin` を実行する
2. AskUserQuestion ツールでブランチ名を確認する。`feature/` プレフィックスが必須であることを伝え、用途の例を示す
3. ユーザが `feature/` を付け忘れた場合は自動で補完する
4. `git checkout -b <ブランチ名> origin/main` で新しいブランチを作成する
5. 作成したブランチ名と現在の HEAD コミットを報告する
