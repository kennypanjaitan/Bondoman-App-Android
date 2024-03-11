# JMD

## Project setup
1. __Clone__ Repo
2. Use __android studio__ to open the project
3. __Checkout__ to branch `dev` and pull the latest changes
4. Create new branch for your feature from `dev`
## Branching and Commit Message
Setiap membuat branch baru harus __ambil basenya__ dari branch `dev` { _atau branch yang berkaitan dengan fitur yang dikerjakan_ }.

Format branch: `<type>/<title>`. Contoh: `feat/navbar`.\
Format commit message: `<type>: <message>`. Contoh: `feat: add navbar`, `fix: navbar not showing`.

__type:__
- `feat` : untuk fitur baru
- `fix` : untuk perbaikan bug
- `docs` : untuk perubahan pada dokumentasi
- `style` : untuk perubahan pada style (_missing semi colons, etc; no production code change_)
- `refactor` : untuk perubahan pada kode yang tidak memperbaiki bug dan menambah fitur (_renaming a variable_)
- `test` : untuk menambahkan test
- `chore` : untuk perubahan pada build process atau tools dan library pendukung lainnya

[Read here for Semantic Commit](https://nitayneeman.com/posts/understanding-semantic-commit-messages-using-git-and-angular/#perf)