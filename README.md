<h1 align="center">Weiawaga</h1>

A UCI chess engine written in Java. 

I have been ported to Rust, and a more recent version of my code can be found [HERE](https://github.com/Heiaha/Weiawaga).

https://lichess.org/@/Weiawaga

## Features

  - Board representation
    - [Bitboards](https://en.wikipedia.org/wiki/Bitboard)
  - Move generation
    - [Magic bitboard hashing](https://www.chessprogramming.org/Magic_Bitboards)
  - Search
    - [Negamax with alpha beta pruning](https://en.wikipedia.org/wiki/Negamax#Negamax_with_alpha_beta_pruning)
    - [Iterative deepening](https://en.wikipedia.org/wiki/Iterative_deepening_depth-first_search)
    - [Quiescence search](https://en.wikipedia.org/wiki/Quiescence_search)
    - [Aspiration windows](https://www.chessprogramming.org/Aspiration_Windows)
    - [Null move pruning](https://www.chessprogramming.org/Null_Move_Pruning)
    - [Check extensions](https://www.chessprogramming.org/Check_Extensions)
  - Evaluation
    - [Material imbalance](https://www.chessprogramming.org/Material)
    - [Piece square tables](https://www.chessprogramming.org/Piece-Square_Tables)
    - [Pawn structure](https://www.chessprogramming.org/Pawn_Structure)
    - [King safety](https://www.chessprogramming.org/King_Safety)
    - [Bishop pairs](https://www.chessprogramming.org/Bishop_Pair)
    - [Evaluation tapering](https://www.chessprogramming.org/Tapered_Eval)
    - [Mobility](https://www.chessprogramming.org/Mobility)
    - [Rooks on open file](https://www.chessprogramming.org/Rook_on_Open_File)
  - Move ordering
    - [Hash move](https://www.chessprogramming.org/Hash_Move)
    - [MVV/LVA](https://www.chessprogramming.org/MVV-LVA)
    - [Killer heuristic](https://www.chessprogramming.org/Killer_Heuristic)
    - [History heuristic](https://www.chessprogramming.org/History_Heuristic)
  - Other
    - [Zobrist hashing](https://www.chessprogramming.org/Zobrist_Hashing) / [Transposition table](https://en.wikipedia.org/wiki/Transposition_table)
    - [Logistic Regression via Texel's Tuning Method](https://www.chessprogramming.org/Texel%27s_Tuning_Method)

Move generation inspired by [surge](https://github.com/nkarve/surge).
