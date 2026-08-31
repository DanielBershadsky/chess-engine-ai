# chess-engine-ai
A Java chess engine with full move legality, check, checkmate, and stalemate detection, plus a minimax + alpha-beta pruning AI opponent.

This program was originally an AP Computer Science A final project from my sophomore year of high school in 2022, written in Java.

I found this old project and decided to:
1. Optimize the inefficient past code, which spanned thousands of lines due to having separate methods for white and black pieces.
2. Apply skills from my recent AI/ML engineering internship and from founding my own company, TextGoAI LLC, by implementing an AI opponent.

## How this was done

The original project had separate `Wmove`/`Bmove`, `WKingInCheck`/`BKingInCheck`, and similar redundant methods for nearly every piece of logic, duplicating white and black logic over 3,550 lines.


I merged the white/black duplication into a single set of methods, which kept the logic intact but cut down on the length of the codebase.


I fixed bugs which I found in the original code through testing. It included illegal moves slipping through on retry, a crash on reaching the last rank, kings being allowed to stand next to each other, and incorrect king check tracking.

Implemented `ChessAI.java`: a minimax search with alpha-beta pruning over the existing move generator. The AI truly doesn't know how to play chess but its positions are scored with material count, mobility, and king safety.
