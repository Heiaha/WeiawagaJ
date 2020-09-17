package movegen;

public class Board {
    private long[] piece_bb = new long[Piece.NPIECES]; //bitboards
    private int[] board = new int[64]; // board of pieces
    private int side_to_play;
    private long hash;
    public int game_ply;
    public UndoInfo[] history = new UndoInfo[1000];

    public long checkers;
    public long pinned;

    public Board(){
        setFen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
    }

    public Board(boolean clear){
        if (!clear)
            setFen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
    }

    public void clear(){
        side_to_play = Side.WHITE;
        game_ply = 0;
        for (int piece = 0; piece < Piece.NPIECES; piece++)
            piece_bb[piece] = 0L;

        for (int sq = Square.A1; sq <= Square.H8; sq++)
            board[sq] = Piece.NONE;
    }


    public int pieceAt(int square){
        return board[square];
    }

    public int pieceTypeAt(int square){
        return Piece.typeOf(board[square]);
    }

    public void setPieceAt(int piece, int square){
        board[square] = piece;
        piece_bb[piece] |= Square.getBb(square);
        hash ^= Zobrist.ZOBRIST_TABLE[piece][square];
    }

    public void removePiece(int square){
        hash ^= Zobrist.ZOBRIST_TABLE[board[square]][square];
        piece_bb[board[square]] &= ~Square.getBb(square);
        board[square] = Piece.NONE;
    }

    public void movePiece(int fromSq, int toSq){
        hash ^= Zobrist.ZOBRIST_TABLE[board[fromSq]][fromSq] ^ Zobrist.ZOBRIST_TABLE[board[fromSq]][toSq] ^
                Zobrist.ZOBRIST_TABLE[board[toSq]][toSq];
        long mask = Square.getBb(fromSq) | Square.getBb(toSq);
        piece_bb[board[fromSq]] ^= mask;
        piece_bb[board[toSq]] &= ~mask;
        board[toSq] = board[fromSq];
        board[fromSq] = Piece.NONE;
    }

    public void movePieceQuiet(int fromSq, int toSq){
        hash ^= Zobrist.ZOBRIST_TABLE[board[fromSq]][fromSq] ^ Zobrist.ZOBRIST_TABLE[board[fromSq]][toSq];
        piece_bb[board[fromSq]] ^= (Square.getBb(fromSq) | Square.getBb(toSq));
        board[toSq] = board[fromSq];
        board[fromSq] = Piece.NONE;
    }

    public long hash(){
        return hash;
    }

    public long bitboardOf(int piece){
        return piece_bb[piece];
    }

    public long bitboardOf(int side, int pieceType){
        return piece_bb[Piece.makePiece(side, pieceType)];
    }

    public long diagonalSliders(int side){
        return side == Side.WHITE ? piece_bb[Piece.WHITE_BISHOP] | piece_bb[Piece.WHITE_QUEEN] :
                                 piece_bb[Piece.BLACK_BISHOP] | piece_bb[Piece.BLACK_QUEEN];
    }

    public long orthogonalSliders(int side){
        return side == Side.WHITE ? piece_bb[Piece.WHITE_ROOK] | piece_bb[Piece.WHITE_QUEEN] :
                piece_bb[Piece.BLACK_ROOK] | piece_bb[Piece.BLACK_QUEEN];
    }

    public long allPieces(int side){
        return side == Side.WHITE ? piece_bb[Piece.WHITE_PAWN] | piece_bb[Piece.WHITE_KNIGHT] |
                                 piece_bb[Piece.WHITE_BISHOP] | piece_bb[Piece.WHITE_ROOK] |
                                 piece_bb[Piece.WHITE_QUEEN] | piece_bb[Piece.WHITE_KING] :

                                 piece_bb[Piece.BLACK_PAWN] | piece_bb[Piece.BLACK_KNIGHT] |
                                 piece_bb[Piece.BLACK_BISHOP] | piece_bb[Piece.BLACK_ROOK] |
                                 piece_bb[Piece.BLACK_QUEEN] | piece_bb[Piece.BLACK_KING];
    }

    public long attackersFrom(int square, long occ, int side){
        return side == Side.WHITE ? (Attacks.pawnAttacks(square, Side.BLACK) & piece_bb[Piece.WHITE_PAWN]) |
                (Attacks.getKnightAttacks(square) & piece_bb[Piece.WHITE_KNIGHT]) |
                (Attacks.getBishopAttacks(square, occ) & (piece_bb[Piece.WHITE_BISHOP] | piece_bb[Piece.WHITE_QUEEN])) |
                (Attacks.getRookAttacks(square, occ) & (piece_bb[Piece.WHITE_ROOK] | piece_bb[Piece.WHITE_QUEEN])) :

                (Attacks.pawnAttacks(square, Side.WHITE) & piece_bb[Piece.BLACK_PAWN]) |
                (Attacks.getKnightAttacks(square) & piece_bb[Piece.BLACK_KNIGHT]) |
                (Attacks.getBishopAttacks(square, occ) & (piece_bb[Piece.BLACK_BISHOP] | piece_bb[Piece.BLACK_QUEEN])) |
                (Attacks.getRookAttacks(square, occ) & (piece_bb[Piece.BLACK_ROOK] | piece_bb[Piece.BLACK_QUEEN]));
    }

    public void push(Move m) {
        game_ply++;
        history[game_ply] = new UndoInfo();
        history[game_ply].entry = history[game_ply - 1].entry;
        history[game_ply].move = m.move();
        history[game_ply].halfmoveCounter = history[game_ply - 1].halfmoveCounter + 1;
        history[game_ply].entry |= (Square.getBb(m.to()) | Square.getBb(m.from()));

        if (Piece.typeOf(board[m.from()]) == PieceType.PAWN)
            history[game_ply].halfmoveCounter = 0;

        switch (m.flags()){
            case Move.QUIET:
                movePieceQuiet(m.from(), m.to());
                break;
            case Move.DOUBLE_PUSH:
                movePieceQuiet(m.from(), m.to());
                history[game_ply].epsq = m.from() + Square.relative_dir(Square.NORTH, side_to_play);
                break;
            case Move.OO:
                if (side_to_play == Side.WHITE){
                    movePieceQuiet(Square.E1, Square.G1);
                    movePieceQuiet(Square.H1, Square.F1);
                }
                else{
                    movePieceQuiet(Square.E8, Square.G8);
                    movePieceQuiet(Square.H8, Square.F8);
                }
                break;
            case Move.OOO:
                if (side_to_play == Side.WHITE){
                    movePieceQuiet(Square.E1, Square.C1);
                    movePieceQuiet(Square.A1, Square.D1);
                }
                else{
                    movePieceQuiet(Square.E8, Square.C8);
                    movePieceQuiet(Square.A8, Square.D8);
                }
                break;
            case Move.EN_PASSANT:
                movePieceQuiet(m.from(), m.to());
                removePiece(m.to() + Square.relative_dir(Square.SOUTH, side_to_play));
                break;
            case Move.PR_KNIGHT:
                removePiece(m.from());
                setPieceAt(Piece.makePiece(side_to_play, PieceType.KNIGHT), m.to());
                break;
            case Move.PR_BISHOP:
                removePiece(m.from());
                setPieceAt(Piece.makePiece(side_to_play, PieceType.BISHOP), m.to());
                break;
            case Move.PR_ROOK:
                removePiece(m.from());
                setPieceAt(Piece.makePiece(side_to_play, PieceType.ROOK), m.to());
                break;
            case Move.PR_QUEEN:
                removePiece(m.from());
                setPieceAt(Piece.makePiece(side_to_play, PieceType.QUEEN), m.to());
                break;
            case Move.PC_KNIGHT:
                removePiece(m.from());
                history[game_ply].captured = board[m.to()];
                removePiece(m.to());
                setPieceAt(Piece.makePiece(side_to_play, PieceType.KNIGHT), m.to());
                break;
            case Move.PC_BISHOP:
                removePiece(m.from());
                history[game_ply].captured = board[m.to()];
                removePiece(m.to());
                setPieceAt(Piece.makePiece(side_to_play, PieceType.BISHOP), m.to());
                break;
            case Move.PC_ROOK:
                removePiece(m.from());
                history[game_ply].captured = board[m.to()];
                removePiece(m.to());
                setPieceAt(Piece.makePiece(side_to_play, PieceType.ROOK), m.to());
                break;
            case Move.PC_QUEEN:
                removePiece(m.from());
                history[game_ply].captured = board[m.to()];
                removePiece(m.to());
                setPieceAt(Piece.makePiece(side_to_play, PieceType.QUEEN), m.to());
                break;
            case Move.CAPTURE:
                history[game_ply].captured = board[m.to()];
                history[game_ply].halfmoveCounter = 0;
                movePiece(m.from(), m.to());
                break;
        }
        history[game_ply].hash = hash();
        side_to_play = Side.flip(side_to_play);
    }

    public Move pop(){
        side_to_play = Side.flip(side_to_play);
        Move m = new Move(history[game_ply].move);
        switch (m.flags()){
            case Move.QUIET:
            case Move.DOUBLE_PUSH:
                movePieceQuiet(m.to(), m.from());
                break;
            case Move.OO:
                if (side_to_play == Side.WHITE){
                    movePieceQuiet(Square.G1, Square.E1);
                    movePieceQuiet(Square.F1, Square.H1);
                }
                else{
                    movePieceQuiet(Square.G8, Square.E8);
                    movePieceQuiet(Square.F8, Square.H8);
                }
                break;
            case Move.OOO:
                if (side_to_play == Side.WHITE){
                    movePieceQuiet(Square.C1, Square.E1);
                    movePieceQuiet(Square.D1, Square.A1);
                }
                else{
                    movePieceQuiet(Square.C8, Square.E8);
                    movePieceQuiet(Square.D8, Square.A8);
                }
                break;
            case Move.EN_PASSANT:
                movePieceQuiet(m.to(), m.from());
                setPieceAt(Piece.makePiece(Side.flip(side_to_play), PieceType.PAWN), m.to() + Square.relative_dir(Square.SOUTH, side_to_play));
                break;
            case Move.PR_KNIGHT:
            case Move.PR_BISHOP:
            case Move.PR_ROOK:
            case Move.PR_QUEEN:
                removePiece(m.to());
                setPieceAt(Piece.makePiece(side_to_play, PieceType.PAWN), m.from());
                break;
            case Move.PC_KNIGHT:
            case Move.PC_BISHOP:
            case Move.PC_ROOK:
            case Move.PC_QUEEN:
                removePiece(m.to());
                setPieceAt(Piece.makePiece(side_to_play, PieceType.PAWN), m.from());
                setPieceAt(history[game_ply].captured, m.to());
                break;
            case Move.CAPTURE:
                movePieceQuiet(m.to(), m.from());
                setPieceAt(history[game_ply].captured, m.to());
                break;
        }
        game_ply--;
        return m;
    }

    public int getSideToPlay(){
        return side_to_play;
    }

    public boolean kingAttacked(){
        final int us = side_to_play;
        final int them = Side.flip(side_to_play);
        final int ourKing = Bitboard.lsb(bitboardOf(us, PieceType.KING));

        if ((Attacks.pawnAttacks(ourKing, us) & bitboardOf(them, PieceType.PAWN)) != 0)
            return true;

        if ((Attacks.getKnightAttacks(ourKing) & bitboardOf(them, PieceType.KNIGHT)) != 0)
            return true;

        final long usBb = allPieces(us);
        final long themBb = allPieces(them);
        final long all = usBb | themBb;

        final long theirDiagSliders = diagonalSliders(them);
        final long theirOrthSliders = orthogonalSliders(them);

        if ((Attacks.getRookAttacks(ourKing, all) & theirOrthSliders) != 0)
            return true;

        if ((Attacks.getBishopAttacks(ourKing, all) & theirDiagSliders) != 0)
            return true;

        return false;
    }

    public boolean isThreefoldOrFiftyMove(){
        final int lookBack = Math.min(game_ply, history[game_ply].halfmoveCounter);
        int rep = 0;
        for (int i = 2; i <= lookBack; i += 2){
            if (hash == history[game_ply - i].hash) {
                rep++;
                if (rep >= 2)
                    return true;
            }
        }

        return history[game_ply].halfmoveCounter >= 100;
    }

    public MoveList generateLegalMoves(){
        MoveList moves = new MoveList();
        final int us = side_to_play;
        final int them = Side.flip(side_to_play);

        final long usBb = allPieces(us);
        final long themBb = allPieces(them);
        final long all = usBb | themBb;

        final int ourKing = Bitboard.lsb(bitboardOf(us, PieceType.KING));
        final int theirKing = Bitboard.lsb(bitboardOf(them, PieceType.KING));

        final long ourDiagSliders = diagonalSliders(us);
        final long theirDiagSliders = diagonalSliders(them);
        final long ourOrthSliders = orthogonalSliders(us);
        final long theirOrthSliders = orthogonalSliders(them);

        long b1, b2, b3;

        long danger = 0;
        danger |= Attacks.pawnAttacks(bitboardOf(them, PieceType.PAWN), them) | Attacks.getKingAttacks(theirKing);

        b1 = bitboardOf(them, PieceType.KNIGHT);
        while (b1 != 0){
            danger |= Attacks.getKnightAttacks(Bitboard.lsb(b1));
            b1 = Bitboard.extractLsb(b1);
        }

        b1 = theirDiagSliders;
        while(b1 != 0){
            danger |= Attacks.getBishopAttacks(Bitboard.lsb(b1), all ^ Square.getBb(ourKing));
            b1 = Bitboard.extractLsb(b1);
        }

        b1 = theirOrthSliders;
        while (b1 != 0){
            danger |= Attacks.getRookAttacks(Bitboard.lsb(b1), all ^ Square.getBb(ourKing));
            b1 = Bitboard.extractLsb(b1);
        }


        b1 = Attacks.getKingAttacks(ourKing) & ~(usBb | danger);

        moves.makeQ(ourKing, b1 & ~themBb);
        moves.makeC(ourKing, b1 & themBb);

        long captureMask, quietMask;
        int s;

        checkers = (Attacks.getKnightAttacks(ourKing) & bitboardOf(them, PieceType.KNIGHT))
                | (Attacks.pawnAttacks(ourKing, us) & bitboardOf(them, PieceType.PAWN));

        long candidates = (Attacks.getRookAttacks(ourKing, themBb) & theirOrthSliders)
                | (Attacks.getBishopAttacks(ourKing, themBb) & theirDiagSliders);

        pinned = 0;
        while (candidates != 0){
            s = Bitboard.lsb(candidates);
            candidates = Bitboard.extractLsb(candidates);
            b1 = Bitboard.between(ourKing, s) & usBb;

            if (b1 == 0)
                checkers ^= Square.getBb(s);
            else if (Bitboard.extractLsb(b1) == 0)
                pinned ^= b1;
        }

        final long notPinned = ~pinned;
        switch(Bitboard.popcount(checkers)){
            case 2:
                return moves;
            case 1:{
                int checkerSquare = Bitboard.lsb(checkers);
                switch (board[checkerSquare]){
                    case Piece.WHITE_PAWN:
                    case Piece.BLACK_PAWN:
                        // check to see if the checker is a pawn that can be captured ep
                        if (checkers == Bitboard.shift(Square.getBb(history[game_ply].epsq), Square.relative_dir(Square.SOUTH, us))){
                            b1 = Attacks.pawnAttacks(history[game_ply].epsq, them) & bitboardOf(us, PieceType.PAWN) & notPinned;
                            while (b1 != 0){
                                moves.add(new Move(Bitboard.lsb(b1), history[game_ply].epsq, Move.EN_PASSANT));
                                b1 = Bitboard.extractLsb(b1);
                            }
                        }
                        // FALL THROUGH INTENTIONAL
                    case Piece.WHITE_KNIGHT:
                    case Piece.BLACK_KNIGHT:
                        b1 = attackersFrom(checkerSquare, all, us) & notPinned;
                        while (b1 != 0){
                            moves.add(new Move(Bitboard.lsb(b1), checkerSquare, Move.CAPTURE));
                            b1 = Bitboard.extractLsb(b1);
                        }
                        return moves;
                    default:
                        // We have to capture the checker
                        captureMask = checkers;
                        // ...or block it
                        quietMask = Bitboard.between(ourKing, checkerSquare);
                        break;
                }
                break;
            }
            default:
                // No checkers, we can capture any enemy piece.
                captureMask = themBb;

                // or move to any square that isn't occupied.
                quietMask = ~all;

                if (history[game_ply].epsq != Square.NO_SQUARE){
                    //b1 contains pawns that can do an ep capture
                    b1 = Attacks.pawnAttacks(history[game_ply].epsq, them) & bitboardOf(us, PieceType.PAWN) & notPinned;
                    while (b1 != 0){
                        s = Bitboard.lsb(b1);
                        b1 = Bitboard.extractLsb(b1);

                        long attacks = Attacks.slidingAttacks(ourKing,
                                all ^ Square.getBb(s) ^ Bitboard.shift(Square.getBb(history[game_ply].epsq), Square.relative_dir(Square.SOUTH, us)),
                                Rank.getBb(Square.getRank(ourKing)));

                        if ((attacks & theirOrthSliders) == 0)
                            moves.add(new Move(s, history[game_ply].epsq, Move.EN_PASSANT));
                    }
                }

                if (0 == ((history[game_ply].entry & Bitboard.ooMask(us)) | ((all | danger) & Bitboard.ooBlockersMask(us))))
                    moves.add(us == Side.WHITE ? new Move(Square.E1, Square.G1, Move.OO) : new Move(Square.E8, Square.G8, Move.OO));

                if (0 == ((history[game_ply].entry & Bitboard.oooMask(us)) |
                        ((all | (danger & ~Bitboard.ignoreOOODanger(us))) & Bitboard.oooBlockersMask(us))))
                    moves.add(us == Side.WHITE ? new Move(Square.E1, Square.C1, Move.OOO) : new Move(Square.E8, Square.C8, Move.OOO));

                // For each pinned rook, bishop, or queen...
                b1 = ~(notPinned | bitboardOf(us, PieceType.KNIGHT));
                while (b1 != 0){
                    s = Bitboard.lsb(b1);
                    b1 = Bitboard.extractLsb(b1);

                    b2 = Attacks.attacks(Piece.typeOf(board[s]), s, all) & Bitboard.line(ourKing, s);
                    moves.makeQ(s, b2 & quietMask);
                    moves.makeC(s, b2 & captureMask);
                }

                // for each pinned pawn
                b1 = ~notPinned & bitboardOf(us, PieceType.PAWN);
                while (b1 != 0){
                    s = Bitboard.lsb(b1);
                    b1 = Bitboard.extractLsb(b1);

                    if (Square.getRank(s) == Rank.relative_rank(Rank.RANK_7, us)){
                        b2 = Attacks.pawnAttacks(s, us) & captureMask & Bitboard.line(ourKing, s);
                        moves.makePC(s, b2);
                    }
                    else{
                        b2 = Attacks.pawnAttacks(s, us) & themBb & Bitboard.line(s, ourKing);
                        moves.makeC(s, b2);

                        //single pawn pushes
                        b2 = Bitboard.shift(Square.getBb(s), Square.relative_dir(Square.NORTH, us)) & ~all & Bitboard.line(ourKing, s);
                        b3 = Bitboard.shift(b2 & Rank.getBb(Rank.relative_rank(Rank.RANK_3, us)), Square.relative_dir(Square.NORTH, us)) & ~all & Bitboard.line(ourKing, s);

                        moves.makeQ(s, b2);
                        moves.makeDP(s, b3);
                    }
                }
                //Pinned knights cannot move anywhere, so we're done with pinned pieces.
                break;

        }
        //non-pinned knight moves.
        b1 = bitboardOf(us, PieceType.KNIGHT) & notPinned;
        while (b1 != 0){
            s = Bitboard.lsb(b1);
            b1 = Bitboard.extractLsb(b1);
            b2 = Attacks.getKnightAttacks(s);
            moves.makeC(s, b2 & captureMask);
            moves.makeQ(s, b2 & quietMask);
        }

        b1 = ourDiagSliders & notPinned;
        while (b1 != 0){
            s = Bitboard.lsb(b1);
            b1 = Bitboard.extractLsb(b1);
            b2 = Attacks.getBishopAttacks(s, all);
            moves.makeC(s, b2 & captureMask);
            moves.makeQ(s, b2 & quietMask);
        }

        b1 = ourOrthSliders & notPinned;
        while(b1 != 0){
            s = Bitboard.lsb(b1);
            b1 = Bitboard.extractLsb(b1);
            b2 = Attacks.getRookAttacks(s, all);
            moves.makeC(s, b2 & captureMask);
            moves.makeQ(s, b2 & quietMask);
        }

        b1 = bitboardOf(us, PieceType.PAWN) & notPinned & ~Rank.getBb(Rank.relative_rank(Rank.RANK_7, us));

        // single pawn pushes
        b2 = Bitboard.shift(b1, Square.relative_dir(Square.NORTH, us)) & ~all;

        //double pawn pushes
        b3 = Bitboard.shift(b2 & Rank.getBb(Rank.relative_rank(Rank.RANK_3, us)), Square.relative_dir(Square.NORTH, us)) & quietMask;

        b2 &= quietMask;

        while (b2 != 0){
            s = Bitboard.lsb(b2);
            b2 = Bitboard.extractLsb(b2);
            moves.add(new Move(s - Square.relative_dir(Square.NORTH, us), s, Move.QUIET));
        }

        while (b3 != 0){
            s = Bitboard.lsb(b3);
            b3 = Bitboard.extractLsb(b3);
            moves.add(new Move(s - Square.relative_dir(Square.NORTH_NORTH, us), s, Move.DOUBLE_PUSH));
        }

        b2 = Bitboard.shift(b1, Square.relative_dir(Square.NORTH_WEST, us)) & captureMask;
        b3 = Bitboard.shift(b1, Square.relative_dir(Square.NORTH_EAST, us)) & captureMask;

        while (b2 != 0){
            s = Bitboard.lsb(b2);
            b2 = Bitboard.extractLsb(b2);
            moves.add(new Move(s - Square.relative_dir(Square.NORTH_WEST, us), s, Move.CAPTURE));
        }

        while (b3 != 0){
            s = Bitboard.lsb(b3);
            b3 = Bitboard.extractLsb(b3);
            moves.add(new Move(s - Square.relative_dir(Square.NORTH_EAST, us), s, Move.CAPTURE));
        }

        b1 = bitboardOf(us, PieceType.PAWN) & notPinned & Rank.getBb(Rank.relative_rank(Rank.RANK_7, us));
        if (b1 != 0){
            b2 = Bitboard.shift(b1, Square.relative_dir(Square.NORTH, us)) & quietMask;
            while (b2 != 0){
                s = Bitboard.lsb(b2);
                b2 = Bitboard.extractLsb(b2);

                moves.add(new Move(s - Square.relative_dir(Square.NORTH, us), s, Move.PR_KNIGHT));
                moves.add(new Move(s - Square.relative_dir(Square.NORTH, us), s, Move.PR_BISHOP));
                moves.add(new Move(s - Square.relative_dir(Square.NORTH, us), s, Move.PR_ROOK));
                moves.add(new Move(s - Square.relative_dir(Square.NORTH, us), s, Move.PR_QUEEN));
            }

            b2 = Bitboard.shift(b1, Square.relative_dir(Square.NORTH_WEST, us)) & captureMask;
            b3 = Bitboard.shift(b1, Square.relative_dir(Square.NORTH_EAST, us)) & captureMask;

            while (b2 != 0){
                s = Bitboard.lsb(b2);
                b2 = Bitboard.extractLsb(b2);

                moves.add(new Move(s - Square.relative_dir(Square.NORTH_WEST, us), s, Move.PC_KNIGHT));
                moves.add(new Move(s - Square.relative_dir(Square.NORTH_WEST, us), s, Move.PC_BISHOP));
                moves.add(new Move(s - Square.relative_dir(Square.NORTH_WEST, us), s, Move.PC_ROOK));
                moves.add(new Move(s - Square.relative_dir(Square.NORTH_WEST, us), s, Move.PC_QUEEN));
            }

            while (b3 != 0){
                s = Bitboard.lsb(b3);
                b3 = Bitboard.extractLsb(b3);

                moves.add(new Move(s - Square.relative_dir(Square.NORTH_EAST, us), s, Move.PC_KNIGHT));
                moves.add(new Move(s - Square.relative_dir(Square.NORTH_EAST, us), s, Move.PC_BISHOP));
                moves.add(new Move(s - Square.relative_dir(Square.NORTH_EAST, us), s, Move.PC_ROOK));
                moves.add(new Move(s - Square.relative_dir(Square.NORTH_EAST, us), s, Move.PC_QUEEN));
            }
        }
        return moves;
    }

    public MoveList generateLegalQMoves(){
        MoveList moves = new MoveList();
        final int us = side_to_play;
        final int them = Side.flip(side_to_play);

        final long usBb = allPieces(us);
        final long themBb = allPieces(them);
        final long all = usBb | themBb;

        final int ourKing = Bitboard.lsb(bitboardOf(us, PieceType.KING));
        final int theirKing = Bitboard.lsb(bitboardOf(them, PieceType.KING));

        final long ourDiagSliders = diagonalSliders(us);
        final long theirDiagSliders = diagonalSliders(them);
        final long ourOrthSliders = orthogonalSliders(us);
        final long theirOrthSliders = orthogonalSliders(them);

        long b1, b2, b3;

        long danger = 0;
        danger |= Attacks.pawnAttacks(bitboardOf(them, PieceType.PAWN), them) | Attacks.getKingAttacks(theirKing);

        b1 = bitboardOf(them, PieceType.KNIGHT);
        while (b1 != 0){
            danger |= Attacks.getKnightAttacks(Bitboard.lsb(b1));
            b1 = Bitboard.extractLsb(b1);
        }

        b1 = theirDiagSliders;
        while(b1 != 0){
            danger |= Attacks.getBishopAttacks(Bitboard.lsb(b1), all ^ Square.getBb(ourKing));
            b1 = Bitboard.extractLsb(b1);
        }

        b1 = theirOrthSliders;
        while (b1 != 0){
            danger |= Attacks.getRookAttacks(Bitboard.lsb(b1), all ^ Square.getBb(ourKing));
            b1 = Bitboard.extractLsb(b1);
        }


        b1 = Attacks.getKingAttacks(ourKing) & ~(usBb | danger);

        moves.makeC(ourKing, b1 & themBb);

        long captureMask, quietMask;
        int s;

        checkers = (Attacks.getKnightAttacks(ourKing) & bitboardOf(them, PieceType.KNIGHT))
                | (Attacks.pawnAttacks(ourKing, us) & bitboardOf(them, PieceType.PAWN));

        long candidates = (Attacks.getRookAttacks(ourKing, themBb) & theirOrthSliders)
                | (Attacks.getBishopAttacks(ourKing, themBb) & theirDiagSliders);

        pinned = 0;
        while (candidates != 0){
            s = Bitboard.lsb(candidates);
            candidates = Bitboard.extractLsb(candidates);
            b1 = Bitboard.between(ourKing, s) & usBb;

            if (b1 == 0)
                checkers ^= Square.getBb(s);
            else if (Bitboard.extractLsb(b1) == 0)
                pinned ^= b1;
        }

        final long notPinned = ~pinned;
        switch(Bitboard.popcount(checkers)){
            case 2:
                return moves;
            case 1:{
                int checkerSquare = Bitboard.lsb(checkers);
                switch (board[checkerSquare]){
                    case Piece.WHITE_PAWN:
                    case Piece.BLACK_PAWN:
                        // check to see if the checker is a pawn that can be captured ep
                        if (checkers == Bitboard.shift(Square.getBb(history[game_ply].epsq), Square.relative_dir(Square.SOUTH, us))){
                            b1 = Attacks.pawnAttacks(history[game_ply].epsq, them) & bitboardOf(us, PieceType.PAWN) & notPinned;
                            while (b1 != 0){
                                moves.add(new Move(Bitboard.lsb(b1), history[game_ply].epsq, Move.EN_PASSANT));
                                b1 = Bitboard.extractLsb(b1);
                            }
                        }
                        // FALL THROUGH INTENTIONAL
                    case Piece.WHITE_KNIGHT:
                    case Piece.BLACK_KNIGHT:
                        b1 = attackersFrom(checkerSquare, all, us) & notPinned;
                        while (b1 != 0){
                            moves.add(new Move(Bitboard.lsb(b1), checkerSquare, Move.CAPTURE));
                            b1 = Bitboard.extractLsb(b1);
                        }
                        return moves;
                    default:
                        // We have to capture the checker
                        captureMask = checkers;
                        // ...or block it
                        quietMask = Bitboard.between(ourKing, checkerSquare);
                        break;
                }
                break;
            }
            default:
                // No checkers, we can capture any enemy piece.
                captureMask = themBb;

                // or move to any square that isn't occupied.
                quietMask = ~all;

                if (history[game_ply].epsq != Square.NO_SQUARE){
                    //b1 contains pawns that can do an ep capture
                    b1 = Attacks.pawnAttacks(history[game_ply].epsq, them) & bitboardOf(us, PieceType.PAWN) & notPinned;
                    while (b1 != 0){
                        s = Bitboard.lsb(b1);
                        b1 = Bitboard.extractLsb(b1);

                        long attacks = Attacks.slidingAttacks(ourKing,
                                all ^ Square.getBb(s) ^ Bitboard.shift(Square.getBb(history[game_ply].epsq), Square.relative_dir(Square.SOUTH, us)),
                                Rank.getBb(Square.getRank(ourKing)));

                        if ((attacks & theirOrthSliders) == 0)
                            moves.add(new Move(s, history[game_ply].epsq, Move.EN_PASSANT));
                    }
                }


                // For each pinned rook, bishop, or queen...
                b1 = ~(notPinned | bitboardOf(us, PieceType.KNIGHT));
                while (b1 != 0){
                    s = Bitboard.lsb(b1);
                    b1 = Bitboard.extractLsb(b1);

                    b2 = Attacks.attacks(Piece.typeOf(board[s]), s, all) & Bitboard.line(ourKing, s);
                    moves.makeC(s, b2 & captureMask);
                }

                // for each pinned pawn
                b1 = ~notPinned & bitboardOf(us, PieceType.PAWN);
                while (b1 != 0){
                    s = Bitboard.lsb(b1);
                    b1 = Bitboard.extractLsb(b1);

                    if (Square.getRank(s) == Rank.relative_rank(Rank.RANK_7, us)){
                        b2 = Attacks.pawnAttacks(s, us) & captureMask & Bitboard.line(ourKing, s);
                        moves.makePC(s, b2);
                    }
                    else{
                        b2 = Attacks.pawnAttacks(s, us) & themBb & Bitboard.line(s, ourKing);
                        moves.makeC(s, b2);

                    }

                }
                //Pinned knights cannot move anywhere, so we're done with pinned pieces.
                break;

        }
        //non-pinned knight moves.
        b1 = bitboardOf(us, PieceType.KNIGHT) & notPinned;
        while (b1 != 0){
            s = Bitboard.lsb(b1);
            b1 = Bitboard.extractLsb(b1);
            b2 = Attacks.getKnightAttacks(s);
            moves.makeC(s, b2 & captureMask);
        }

        b1 = ourDiagSliders & notPinned;
        while (b1 != 0){
            s = Bitboard.lsb(b1);
            b1 = Bitboard.extractLsb(b1);
            b2 = Attacks.getBishopAttacks(s, all);
            moves.makeC(s, b2 & captureMask);
        }

        b1 = ourOrthSliders & notPinned;
        while(b1 != 0){
            s = Bitboard.lsb(b1);
            b1 = Bitboard.extractLsb(b1);
            b2 = Attacks.getRookAttacks(s, all);
            moves.makeC(s, b2 & captureMask);
        }

        b1 = bitboardOf(us, PieceType.PAWN) & notPinned & ~Rank.getBb(Rank.relative_rank(Rank.RANK_7, us));
        b2 = Bitboard.shift(b1, Square.relative_dir(Square.NORTH_WEST, us)) & captureMask;
        b3 = Bitboard.shift(b1, Square.relative_dir(Square.NORTH_EAST, us)) & captureMask;

        while (b2 != 0){
            s = Bitboard.lsb(b2);
            b2 = Bitboard.extractLsb(b2);
            moves.add(new Move(s - Square.relative_dir(Square.NORTH_WEST, us), s, Move.CAPTURE));
        }

        while (b3 != 0){
            s = Bitboard.lsb(b3);
            b3 = Bitboard.extractLsb(b3);
            moves.add(new Move(s - Square.relative_dir(Square.NORTH_EAST, us), s, Move.CAPTURE));
        }

        b1 = bitboardOf(us, PieceType.PAWN) & notPinned & Rank.getBb(Rank.relative_rank(Rank.RANK_7, us));
        if (b1 != 0){
            b2 = Bitboard.shift(b1, Square.relative_dir(Square.NORTH, us)) & quietMask;
            while (b2 != 0){
                s = Bitboard.lsb(b2);
                b2 = Bitboard.extractLsb(b2);

                moves.add(new Move(s - Square.relative_dir(Square.NORTH, us), s, Move.PR_KNIGHT));
                moves.add(new Move(s - Square.relative_dir(Square.NORTH, us), s, Move.PR_BISHOP));
                moves.add(new Move(s - Square.relative_dir(Square.NORTH, us), s, Move.PR_ROOK));
                moves.add(new Move(s - Square.relative_dir(Square.NORTH, us), s, Move.PR_QUEEN));
            }

            b2 = Bitboard.shift(b1, Square.relative_dir(Square.NORTH_WEST, us)) & captureMask;
            b3 = Bitboard.shift(b1, Square.relative_dir(Square.NORTH_EAST, us)) & captureMask;

            while (b2 != 0){
                s = Bitboard.lsb(b2);
                b2 = Bitboard.extractLsb(b2);

                moves.add(new Move(s - Square.relative_dir(Square.NORTH_WEST, us), s, Move.PC_KNIGHT));
                moves.add(new Move(s - Square.relative_dir(Square.NORTH_WEST, us), s, Move.PC_BISHOP));
                moves.add(new Move(s - Square.relative_dir(Square.NORTH_WEST, us), s, Move.PC_ROOK));
                moves.add(new Move(s - Square.relative_dir(Square.NORTH_WEST, us), s, Move.PC_QUEEN));
            }

            while (b3 != 0){
                s = Bitboard.lsb(b3);
                b3 = Bitboard.extractLsb(b3);

                moves.add(new Move(s - Square.relative_dir(Square.NORTH_EAST, us), s, Move.PC_KNIGHT));
                moves.add(new Move(s - Square.relative_dir(Square.NORTH_EAST, us), s, Move.PC_BISHOP));
                moves.add(new Move(s - Square.relative_dir(Square.NORTH_EAST, us), s, Move.PC_ROOK));
                moves.add(new Move(s - Square.relative_dir(Square.NORTH_EAST, us), s, Move.PC_QUEEN));
            }
        }

        return moves;
    }

    public void setFen(String fen){
        clear();
        history[0] = new UndoInfo();
        String squares = fen.substring(0, fen.indexOf(' '));
        String state = fen.substring(fen.indexOf(' ') + 1);

        String[] ranks = squares.split("/");
        int file;
        int rank = 7;
        for (String r : ranks) {
            file = 0;
            for (int i = 0; i < r.length(); i++) {
                char c = r.charAt(i);
                if (Character.isDigit(c)) {
                    file += Integer.parseInt(c + "");
                } else {
                    int sq = Square.encode(rank, file);
                    switch (c) {
                        case 'P' -> setPieceAt(Piece.WHITE_PAWN, sq);
                        case 'N' -> setPieceAt(Piece.WHITE_KNIGHT, sq);
                        case 'B' -> setPieceAt(Piece.WHITE_BISHOP, sq);
                        case 'R' -> setPieceAt(Piece.WHITE_ROOK, sq);
                        case 'Q' -> setPieceAt(Piece.WHITE_QUEEN, sq);
                        case 'K' -> setPieceAt(Piece.WHITE_KING, sq);
                        case 'p' -> setPieceAt(Piece.BLACK_PAWN, sq);
                        case 'n' -> setPieceAt(Piece.BLACK_KNIGHT, sq);
                        case 'b' -> setPieceAt(Piece.BLACK_BISHOP, sq);
                        case 'r' -> setPieceAt(Piece.BLACK_ROOK, sq);
                        case 'q' -> setPieceAt(Piece.BLACK_QUEEN, sq);
                        case 'k' -> setPieceAt(Piece.BLACK_KING, sq);
                    }
                    file++;
                }
            }
            rank--;
        }

        String[] flags = state.split(" ");

        side_to_play = state.toLowerCase().charAt(0) == 'w' ? Side.WHITE : Side.BLACK;

        if (!state.contains("K"))
            history[0].entry |= 0x0000000000000080L;
        if (!state.contains("Q"))
            history[0].entry |= 0x0000000000000001L;
        if (!state.contains("k"))
            history[0].entry |= 0x8000000000000000L;
        if (!state.contains("q"))
            history[0].entry |= 0x0100000000000000L;

        if (flags.length >= 3){
            String s = flags[2].toUpperCase().trim();
            if (!s.equals("-")){
                history[game_ply].epsq = Square.getSquareFromName(s);
            }
        }
    }

    public String getFen(){
        StringBuilder fen = new StringBuilder();
        int count = 0;
        int rankCounter = 1;
        int sqCount = 0;
        for(int rank = 7; rank >= 0; rank--){
            for(int file = 0; file <= 7; file++){
                int square = Square.encode(rank, file);
                int piece = board[square];
                if (piece != Piece.NONE){
                    if (count > 0) {
                        fen.append(count);
                    }
                    fen.append(Piece.getNotation(piece));
                    count = 0;
                }
                else{
                    count++;
                }
                if ((sqCount + 1) % 8 == 0){
                    if (count > 0){
                        fen.append(count);
                        count = 0;
                    }
                    if (rankCounter < 8){
                        fen.append("/");
                    }
                    rankCounter++;
                }
                sqCount++;
            }
        }
        if (side_to_play == Side.WHITE){
            fen.append(" w");
        }
        else{
            fen.append(" b");
        }

        String rights = "";
        if ((Bitboard.ooMask(Side.WHITE) & history[game_ply].entry) != 0)
            rights += "K";
        if((Bitboard.oooMask(Side.WHITE) & history[game_ply].entry) != 0)
            rights += "Q";
        if((Bitboard.ooMask(Side.BLACK) & history[game_ply].entry) != 0)
            rights += "k";
        if((Bitboard.oooMask(Side.BLACK) & history[game_ply].entry) != 0)
            rights += "q";

        if (rights.equals("")){
            fen.append(" -");
        }
        else{
            fen.append(" ").append(rights);
        }

        if (history[game_ply].epsq != Square.NO_SQUARE)
            fen.append(" ").append(Square.getName(history[game_ply].epsq));
        else
            fen.append(" -");

        return fen.toString();
    }

    public void pushFromString(String str){
        int fromSq = Square.getSquareFromName(str.substring(0, 2));
        int toSq = Square.getSquareFromName(str.substring(2, 4));
        String typeStr = "";
        if (str.length() > 4)
            typeStr = str.substring(4);

        Move move;
        if (board[toSq] != Piece.NONE){
            if (typeStr.equals("q"))
                move = new Move(fromSq, toSq, Move.PC_QUEEN);
            else if (typeStr.equals("n"))
                move = new Move(fromSq, toSq, Move.PC_KNIGHT);
            else if (typeStr.equals("b"))
                move = new Move(fromSq, toSq, Move.PC_BISHOP);
            else if (typeStr.equals("r"))
                move = new Move(fromSq, toSq, Move.PC_ROOK);
            else
                move = new Move(fromSq, toSq, Move.CAPTURE);
        }
        else{
            if (typeStr.equals("q"))
                move = new Move(fromSq, toSq, Move.PR_QUEEN);
            else if (typeStr.equals("n"))
                move = new Move(fromSq, toSq, Move.PR_KNIGHT);
            else if (typeStr.equals("b"))
                move = new Move(fromSq, toSq, Move.PR_BISHOP);
            else if (typeStr.equals("r"))
                move = new Move(fromSq, toSq, Move.PR_ROOK);
            else if ( Piece.typeOf(board[fromSq]) == PieceType.PAWN &&
                    board[toSq] == history[game_ply].epsq)
                move = new Move(fromSq, toSq, Move.EN_PASSANT);
            else if (Piece.typeOf(board[fromSq]) == PieceType.PAWN && Math.abs(fromSq - toSq) == 16)
                move = new Move(fromSq, toSq, Move.DOUBLE_PUSH);
            else if (Piece.typeOf(board[fromSq]) == PieceType.KING && Square.getFile(fromSq) == File.FILE_E && Square.getFile(toSq) == File.FILE_G)
                move = new Move(fromSq, toSq, Move.OO);
            else if (Piece.typeOf(board[fromSq]) == PieceType.KING && Square.getFile(fromSq) == File.FILE_E && Square.getFile(toSq) == File.FILE_C)
                move = new Move(fromSq, toSq, Move.OOO);
            else
                move = new Move(fromSq, toSq, Move.QUIET);
        }
        this.push(move);
    }


}


