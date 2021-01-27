.ORIG   x3000

;***********************************************************
; Main 
;***********************************************************
                    JSR   GAME_BOARD
START              JSR   DISPLAY_START
                    TRAP  x20                        ; get input into R0
                    TRAP  x21                        ; print to screen
                    LD    R3, ASCII_Q_COMPLEMENT     ; load the 2's complement of ASCII 'Q'
                    ADD   R3, R0, R3                 ; compare the first character with 'Q'
                    BRz   EXIT                       ; if input was 'Q', exit
                    ADD   R1, R0, #0                 ; move R0 into R1 to get second input
                    TRAP  x20                        ; get another character into R0
                    TRAP  x21                        ; print to the screen
                    JSR   CHECK_VALIDITY      
BREAK               JSR   CONVERT_MOVE             ; convert move into {0..6} coordinates
                    ADD   R3, R3, #0                 ; R3 will be zero if the move was valid
                    BRz   VALID_MOVE
		    BRp   WRONG
                    LEA   R0, INVALID_MOVE_STRING    ; if the move was invalid, output corresponding
                    TRAP  x22                        ; message and go back to START
                    BR    START
WRONG		LEA R0, INCORRECT_MOVE_STRING		; make sure that a column is matched with a compatible row
			TRAP x22
			BR START
VALID_MOVE          
            JSR   IS_OCCUPIED         
                    ADD   R3, R3, #0                 ; R3 will be zero if the space was unoccupied
                    BRz   UNOCCUPIED
                    LEA   R0, OCCUPIED_STRING        ; if the place was occupied, output corresponding
                    TRAP  x22                        ; message and go back to START
                    BR    START
UNOCCUPIED          JSR   APPLY_MOVE                 ; apply the move
                    JSR   NUMBER_COMPLETED            ; returns the number of boxes completed by this move in R3
                    ADD   R0, R3, #0                 ; move the number of completed boxes to R0 for UPDATE_GAME
                    JSR   UPDATE_GAME               ; change the score and the player as needed

                    JSR   GAME_BOARD
                    JSR   IS_GAME_OVER      
                    ADD   R3, R3, #0                 ; R3 will be zero if there was a winner
                    BRnp  START                     ; otherwise, loop back
EXIT                LEA   R0, END_STRING
                    TRAP  x22                        ; output a gooGBye message
                    TRAP  x25                        ; halt
END_STRING      .STRINGZ "\nThank you for playing.\n"
ASCII_Q_COMPLEMENT  .FILL  xFFAF                      ; two's complement of ASCII code for 'Q'
INVALID_MOVE_STRING .STRINGZ "\nInvalid move. \nYou can enter letter (A-G) for the column and number (0-6) for the row.\n"
INCORRECT_MOVE_STRING .STRINGZ "\nInvalid move. \nColumns A,C,E,G correspond to odd row numbers. Columns B,D,F correspond to even row \ numbers. \n"
OCCUPIED_STRING     .STRINGZ "\nThis position is already occupied. Please try again.\n"
;***********************************************************
; All Subroutines
;***********************************************************

;***********************************************************
; GAME_BOARD: Displays the game board and the current score
;***********************************************************

GAME_BOARD       ST    R0, GB_R0                  ; save registers
                    ST    R1, GB_R1
                    ST    R2, GB_R2
                    ST    R3, GB_R3
                    ST    R7, GB_R7

                    AND   R1, R1, #0                 ; R1 will be loop counter
                    ADD   R1, R1, #6
                    LEA   R2, ROW0                   ; R2 will be pointer to row
                    LEA   R3, ZERO                   ; R3 will be pointer to row number
                    LD    R0, ASCII_NEWLINE
                    OUT
                    OUT
                    LEA   R0, COL
                    PUTS
                    LD    R0, ASCII_NEWLINE
                    OUT
GB_ROWOUT           ADD   R0, R3, #0                 ; move address of row number to R0
                    PUTS
                    ADD   R0, R2, #0                 ; move address of row to R0
                    PUTS
                    LD    R0, ASCII_NEWLINE
                    OUT
                    ADD   R2, R2, #8                 ; increment R2 to point to next row
                    ADD   R3, R3, #3                 ; increment R3 to point to next row number
                    ADD   R1, R1, #-1
                    BRzp  GB_ROWOUT
                    JSR   CURRENT_SCORE

                    LD    R0, GB_R0                  ; restore registers
                    LD    R1, GB_R1
                    LD    R2, GB_R2
                    LD    R3, GB_R3
                    LD    R7, GB_R7
                    RET

GB_R0               .BLKW #1
GB_R1               .BLKW #1
GB_R2               .BLKW #1
GB_R3               .BLKW #1
GB_R7               .BLKW #1

;***********************************************************
; CURRENT_SCORE
;***********************************************************

CURRENT_SCORE       ST    R0, CS_R0                   ; save registers
                    ST    R7, CS_R7

                    LEA   R0, CS_STRING_1
                    TRAP  x22                         ; print out the first part of the score string
                    LD    R0, SCORE_PLAYER_ONE
                    LD    R7, ASCII_OFFSET
                    ADD   R0, R0, R7                  ; create the ASCII for first player's score
                    TRAP  x21                         ; output it
                    LEA   R0, CS_STRING_2
                    TRAP  x22                         ; print out the second part of the score string
                    LD    R0, SCORE_PLAYER_TWO
                    LD    R7, ASCII_OFFSET
                    ADD   R0, R0, R7                  ; create the ASCII for second player's score
                    TRAP  x21                         ; output it
                    LD    R0, ASCII_NEWLINE
                    TRAP  x21

                    LD    R0, CS_R0                   ; restore registers
                    LD    R7, CS_R7
                    RET

CS_R0              .BLKW   #1
CS_R7              .BLKW   #1
CS_STRING_1    .STRINGZ "SCORES:\n Player 1: "
CS_STRING_2    .STRINGZ " | Player 2: "
;***********************************************************
; CHECK_COMPLETION
; Input      R1   the column number of the square center (0-6)
;      R0   the row number of the square center (0-6)
; Returns   R3   zero if the square is complete; -1 if not complete
;***********************************************************

CHECK_COMPLETION     ST    R0, CC_R0                  ; save registers
                    ST    R1, CC_R1         
                    ST    R2, CC_R2         
                    ST    R4, CC_R4         
                    ST    R7, CC_R7

                    ADD   R0, R0, #-1                 ; check the top pipe
                    JSR   CHECK_ROW_COL
                    ADD   R3, R3, #0
                    BRnp  CC_NON_COMPLETE
                    JSR   IS_OCCUPIED
                    ADD   R3, R3, #0
                    BRz   CC_NON_COMPLETE

                    ADD   R0, R0, #2                  ; check the bottom pipe
                    JSR   CHECK_ROW_COL
                    ADD   R3, R3, #0
                    BRnp  CC_NON_COMPLETE
                    JSR   IS_OCCUPIED
                    ADD   R3, R3, #0
                    BRz   CC_NON_COMPLETE

                    ADD   R0, R0, #-1                 ; check the left pipe
                    ADD   R1, R1, #-1
                    JSR   CHECK_ROW_COL
                    ADD   R3, R3, #0
                    BRnp  CC_NON_COMPLETE
                    JSR   IS_OCCUPIED
                    ADD   R3, R3, #0
                    BRz   CC_NON_COMPLETE

                    ADD   R1, R1, #2                  ; check the right pipe
                    JSR   CHECK_ROW_COL
                    ADD   R3, R3, #0
                    BRnp  CC_NON_COMPLETE
                    JSR   IS_OCCUPIED
                    ADD   R3, R3, #0
                    BRz   CC_NON_COMPLETE

                    ADD   R1, R1, #-1                 ; back to original square

                    AND   R3, R3, #0
                    BR    CC_EXIT

CC_NON_COMPLETE    AND   R3, R3, #0
                    ADD   R3, R3, #-1   

CC_EXIT            LD    R0, CC_R0                  ; restore registers
                    LD    R1, CC_R1         
                    LD    R2, CC_R2         
                    LD    R4, CC_R4         
                    LD    R7, CC_R7
                    RET

CC_R0             .BLKW  #1
CC_R1             .BLKW  #1
CC_R2             .BLKW  #1
CC_R4             .BLKW  #1
CC_R7             .BLKW  #1


;***********************************************************
; NUMBER_COMPLETED
; Input   R1   the column number (0-6)
;      R0   the row number (0-6)
; Returns
;       R3  the number of boxes this move completed
;***********************************************************

NUMBER_COMPLETED    ST    R7, NC_R7                 ; save registers
                   ST    R4, NC_R4

                   JSR   GET_ADDRESS                ; get address in game board structure where line will be drawn
                   AND   R4,R1,#1
                   BRz   NC_VERTICAL               ; true if the line drawn was vertical   

                   AND   R4, R4, #0                 ; R4 will hold the number of boxes completed
                   ADD   R0, R0, #-1                ; is the top square complete?
                   JSR   CHECK_COMPLETION
                   ADD   R3, R3, #0                 ; R3 will be zero if square is complete
                   BRnp  NC_SKIP1
                   ADD   R4, R4, #1                 ; we have one complete
                   JSR   FILL_BOX
NC_SKIP1          ADD   R0, R0, #2                 ; is the bottom square complete?
                   JSR   CHECK_COMPLETION
                   ADD   R3, R3, #0                 ; R3 will be zero if square is complete
                   BRnp  NC_SKIP2
                   ADD   R4, R4, #1
                   JSR   FILL_BOX
NC_SKIP2          ADD   R0, R0, #-1                ; restore R0
                   BRnzp NC_EXIT

NC_VERTICAL       AND   R4, R4, #0
                   ADD   R1, R1, #-1                ; is left square complete?
                   JSR   CHECK_COMPLETION
                   ADD   R3, R3, #0                 ; R3 will be zero if square is complete
                   BRnp  NC_SKIP3
                   ADD   R4, R4, #1
                   JSR   FILL_BOX
NC_SKIP3          ADD   R1, R1, #2                 ; is right square complete?
                   JSR   CHECK_COMPLETION
                   ADD   R3, R3, #0                 ; R3 will be zero if square is complete
                   BRnp  NC_SKIP4
                   ADD   R4, R4, #1
                   JSR   FILL_BOX
NC_SKIP4          ADD   R1, R1, #-1                ; restore R1

NC_EXIT           ADD   R3, R4, #0                 ; move the number of completed squares to R3
                   LD    R7,NC_R7                  ; restore registers
                   LD    R4,NC_R4
                   RET

NC_R7             .BLKW #1
NC_R4             .BLKW #1

;***********************************************************
; CHECK_ROW_COL
; Input       R1    numeric column
;      R0    numeric row (either may be invalid)
; Returns   R3   zero if valid; -1 if invalid
;***********************************************************
 
CHECK_ROW_COL       ADD   R1, R1, #0                 ; Column Check
                   BRn   CRC_HUGE_ERROR    
                   ADD   R3, R1, #-6
                   BRp   CRC_HUGE_ERROR
 
                   ADD   R0, R0, #0                 ; Row check
                   BRn   CRC_HUGE_ERROR
                   ADD   R3, R0, #-6
                   BRp   CRC_HUGE_ERROR

                   AND   R3, R3, #0                 ; valid move, return 0
                   BR    CRC_DONE
CRC_HUGE_ERROR      AND   R3, R3, #0
                   ADD   R3, R3, #-1                ; invalid move, return -1   

CRC_DONE            RET

CRC_NEGA            .FILL #-65
CRC_NEGZERO         .FILL #-48

;***********************************************************
; Global constants 
;***********************************************************

COL                .STRINGZ "  ABCDEFG"
ZERO               .STRINGZ "0 "
ONE                .STRINGZ "1 "
TWO                .STRINGZ "2 "
THREE              .STRINGZ "3 "
FOUR               .STRINGZ "4 "
FIVE               .STRINGZ "5 "
SIX                .STRINGZ "6 "
ASCII_OFFSET       .FILL   x0030
ASCII_NEWLINE      .FILL   x000A

;***********************************************************
; This is the data structure for the game board
;***********************************************************
ROW0               .STRINGZ "* * * *"
ROW1               .STRINGZ "       "
ROW2               .STRINGZ "* * * *"
ROW3               .STRINGZ "       "
ROW4               .STRINGZ "* * * *"
ROW5               .STRINGZ "       "
ROW6               .STRINGZ "* * * *"
 
;***********************************************************
; this data stores the state for whose turn it is 
; and what the score is
;***********************************************************
NOW_PLAYING     .FILL   #1 ; initially player 1 goes
SCORE_PLAYER_ONE   .FILL   #0
SCORE_PLAYER_TWO   .FILL   #0
;***********************************************************
;***********************************************************
; GET_ADDRESS
; Input      R1   the column number (0-6)
;      R0   the row number (0-6)
; Returns   R3   the corresponding address in the data structure
;***********************************************************    
GET_ADDRESS      
    ;Store address from R7
    ST    R7, GA_BACK
        ;R3 is the ADDRESSES
    AND     R3, R3, #0    ;SET R3 TO ZERO
    LEA    R3, ROW0    ;LOAD MEMORY ADDRESS OF ROW0, 
    AND     R5, R5, #0 ;R5 IS OUR COUNTER
    ADD     R5, R5, R0
    BRP    MULTIPLY    
    BR    GA_DONE
MULTIPLY
    ADD    R3, R3, #8    ;ADD 8 TO SUM
    ADD    R5, R5, #-1    ;DECREMENT COUNTER
    BRP    MULTIPLY
GA_DONE    
    ADD    R3, R3, R1    
    LD    R5, GA_R5
    LD    R7, GA_BACK
        RET                   
    GA_R5             .BLKW  #1
    GA_BACK        .BLKW    #1
;*********************************************************** D
; APPLY_MOVE (write - or | in appropriate place)
; Input      R1   the column number (0-6)
;      R0   the row number (0-6)
;***********************************************************
APPLY_MOVE       AND    R3, R3, #0
        AND     R4, R4, #0
        AND     R5, R5, #0       
                LD     R2, ODD_MASK    ;CHECK IF ROW NUMBER IS ODD
            AND    R4, R0, R2    
        BRP    DRAW_PIPE        ;ROW NUMBER IS ODD, DRAW PIPE
        BR    DRAW_HYPEN        ;ROW NUMBER IS EVEN, DRAW HYPEN            
DRAW_PIPE    LD    R3, PIPE    ;LOAD PIPE
        AND    R2, R2, #0
        ADD     R2, R0, #-1
        BRZ    MOD_ROW1
        ADD     R5, R0, #-3
        BRZ    MOD_ROW3
        AND    R4, R4, #0
        ADD     R4, R0, #-5
        BRZ    MOD_ROW5
DRAW_HYPEN    LD    R3, HYPEN    ;LOAD HYPEN
        ADD     R2, R0, #0
        BRZ    MOD_ROW0
        ADD     R5, R0, #-2
        BRZ    MOD_ROW2
        AND    R4, R4, #0
        ADD     R4, R0, #-4
        BRZ    MOD_ROW4
        AND    R2, R2, #0
        ADD     R2, R0, #-6
        BRZ    MOD_ROW6
MOD_ROW0    LEA     R6, ROW0    ; LOAD MEMORY ADDRESS OF ROW0
        ADD     R6, R6, R1                
        STR    R3, R6, #0    ; STORE R3 IN M[R6]
        BR    AM_DONE
MOD_ROW2    LEA     R6, ROW2    ; LOAD MEMORY ADDRESS OF ROW2
        ADD     R6, R6, R1                
        STR    R3, R6, #0    ; STORE R3 IN M[R6]
        BR    AM_DONE
MOD_ROW1    LEA     R6, ROW1    ; LOAD MEMORY ADDRESS OF ROW1
        ADD     R6, R6, R1                
        STR    R3, R6, #0    ; STORE R3 IN M[R6]
        BR    AM_DONE
MOD_ROW3    LEA     R6, ROW3    ; LOAD MEMORY ADDRESS OF ROW3
        ADD     R6, R6, R1                
        STR    R3, R6, #0    ; STORE R3 IN M[R6]
        BR    AM_DONE
MOD_ROW5    LEA     R6, ROW5    ; LOAD MEMORY ADDRESS OF ROW5
        ADD     R6, R6, R1                
        STR    R3, R6, #0    ; STORE R3 IN M[R6]
        BR    AM_DONE
MOD_ROW4    LEA     R6, ROW4    ; LOAD MEMORY ADDRESS OF ROW4
        ADD     R6, R6, R1                
        STR    R3, R6, #0    ; STORE R3 IN M[R6]
        BR    AM_DONE
MOD_ROW6    LEA     R6, ROW6    ; LOAD MEMORY ADDRESS OF ROW6
        ADD     R6, R6, R1                
        STR    R3, R6, #0    ; STORE R3 IN M[R6]
        BR    AM_DONE
AM_DONE         LD     R2, AM_R2
        LD    R3, AM_R3
        LD    R4, AM_R4
        LD    R5, AM_R5
        LD    R6, AM_R6
        RET
            
    HYPEN         .fill x002D
    PIPE         .fill x007C
    ODD_MASK     .fill x0001
    COUNTER     .FILL #8
    AM_R2             .BLKW  #1
    AM_R3             .BLKW  #1
    AM_R4             .BLKW  #1
    AM_R5             .BLKW  #1
    AM_R6             .BLKW  #1

;***********************************************************
; UPDATE_GAME
; Input      R0  number of boxes completed this turn
;   updates score and determines who goes next. 
; if a player just completed a box, he can go again
;***********************************************************
UPDATE_GAME
	ST R7, US_BACK
	JSR 	GET_ADDRESS
	AND 	R2, R2, #0
	AND 	R3, R3, #0
	AND 	R4, R4, #0
	AND 	R5, R5, #0
	AND 	R6, R6, #0
	LD R2, NOW_PLAYING
	ADD 	R6, R2, #0 
	LD R3, SCORE_PLAYER_ONE 
	LD R4, SCORE_PLAYER_TWO 
	ADD 	R2, R2, #-1
	BRZ 	PLAY_TWO
	ADD 	R5, R5, #1
	ADD 	R4, R4, R0
	ST	R4, SCORE_PLAYER_TWO
	ADD 	R0, R0, #0
	BRP 	NOCHANGE
	BR 	UPDT
PLAY_TWO 	ADD R5, R5, #2
		ADD R3, R3, R0
		ST R3, SCORE_PLAYER_ONE
		ADD 	R0, R0, #0
	BRP 	NOCHANGE
		BR UPDT
UPDT		ST R5, NOW_PLAYING
		BR DONE
NOCHANGE	ST R6, NOW_PLAYING
		BR DONE		
DONE		LD R7, US_BACK
		LD R2, US_R2
		LD R3, US_R3
		LD R4, US_R4
		LD R5, US_R5
		LD R6, US_R6
         	RET

US_BACK .BLKW #1
US_R2 .BLKW #1
US_R3 .BLKW #1
US_R4 .BLKW #1
US_R5 .BLKW #1
US_R6 .BLKW #1

;***********************************************************
; IS_GAME_OVER
; Outputs winner of game is over.
; Returns   R3   zero if there was a winner; -1 if no winner yet
;***********************************************************
IS_GAME_OVER
	ST R7, GO_BACK
	JSR GET_ADDRESS
	AND R0, R0, #0
	AND R1, R1, #0
	AND R2, R2, #0
	AND R5, R5, #0	
	LD R1, SCORE_PLAYER_ONE
	LD R2, SCORE_PLAYER_TWO
	ADD R5, R1, R2
	ADD R5, R5, #-9
	BRZ OVER
	BR CONTINUE
OVER	LEA R0, GO_MSG
	TRAP X22
	AND R0, R0, 0
	AND R3, R3, #0
	ADD R1, R1, #-5
	BRN TWO_WINS
	LEA R0, WIN_ONE
	TRAP X22
	BR GDONE
TWO_WINS LEA R0, WIN_TWO
	TRAP X22
	BR GDONE       
CONTINUE ADD    R3, R3, #-1
GDONE 	
	;LD R0, GO_R0
	;LD R1, GO_R1
	;LD R2, GO_R2
	;LD R5, GO_R5
	LD R7, GO_BACK
         RET            
            ; .FILLS and other data for IS_GAME_OVER goes here
GO_BACK .BLKW #1
GO_MSG .STRINGZ "\nGame over \n"
;GO_R0 .BLKW #1
;GO_R1 .BLKW #1
;GO_R2 .BLKW #1
;GO_R5 .BLKW #1
WIN_ONE .STRINGZ "\nThe winner is Player 1. \n"
WIN_TWO .STRINGZ "\nThe winner is Player 2. \n"
;***********************************************************
; FILL_BOX
; Input      R1   the column number of the square center (0-6)
;      R0   the row number of the square center (0-6)
;   fills in the box with the current player's number
;***********************************************************    
FILL_BOX
        ;Store address from R7
        ST    R7, FB_BACK
        JSR       GET_ADDRESS            ;GET THE ADDRESS OF MEMORY     
        AND     R2, R2, #0
        LD    R2, NOW_PLAYING    ;LOAD CURRENT PLAYER IN R2
        ADD    R2, R2, #-1
        BRP    DRAW_TWO
        BR    DRAW_ONE
DRAW_TWO    LD    R5, ASCIITWO    
        STR    R5, R3, #0
        BR    FB_DONE
DRAW_ONE    LD    R5, ASCIIONE
        STR    R5, R3, #0
FB_DONE        LD    R5, FB_R5  
        LD    R2, FB_R2  
        LD    R7, FB_BACK     
        RET
            
     FB_R2    .BLKW    #1
    FB_R5     .BLKW     #1
    ASCIIONE .FILL 49
    ASCIITWO .FILL 50
    FB_BACK    .BLKW    #1

;***********************************************************
; DISPLAY_START
; prompts the player, specified by location NOW_PLAYING, to input a move
;***********************************************************
DISPLAY_START 
	ST    R7, DP_BACK
        JSR       GET_ADDRESS
	AND	R2, R2, #0
	LD 	R2, NOW_PLAYING
	ADD 	R2, R2, #-1
	BRZ 	PL_ONE
	AND 	R0, R0, #0
	LEA 	R0, GO_TWO
	BR 	PRMPT
PL_ONE	LEA 	R0, GO_ONE
	BR 	PRMPT
PRMPT	TRAP X22
	LD 	R7, DP_BACK
        RET
GO_ONE .STRINGZ "\n Player 1, enter (column)(row). \n Or, type 'Q' to quit: \n"
GO_TWO .STRINGZ "\n Player 2, enter (column)(row). \n Or, type 'Q' to quit: \n"
DP_BACK .BLKW #1

;*********************************************************** K
; IS_OCCUPIED
; Input      R1   the column number (0-6)
;      R0   the row number (0-6)
; Returns   R3   zero if the place is unoccupied; -1 if occupied
;***********************************************************


IS_OCCUPIED      
        ;Store address from R7
        ST    R7, IO_BACK        
        JSR    GET_ADDRESS
        LDR    R4, R3, #0    ;LOAD M[R3] INTO R4
        LD    R5, BLANK
        ADD    R5, R5, R4
        BRZ    UNO
        BR    OCC
OCC        AND     R3, R3, #0
        ADD    R3, R3, #-1    ;set R3 to -1
        BR    IO_DONE
UNO        AND     R3, R3, #0    ;set R3 to 0

IO_DONE         LD    R5, IO_R5
        LD    R4, IO_R4
        LD    R7, IO_BACK
        RET
            
            ; .FILLS and other data for IS_OCCUPIED goes here
        IO_R5    .BLKW #1
        IO_R4    .BLKW #1
        IO_BACK    .BLKW #1
        BLANK    .FILL -32


;*********************************************************** 
; CONVERT_MOVE
; Input      R1   the ASCII code for the column ('A'-'G')
;      R0   the ASCII code for the row ('0'-'6')
; Returns   R1   the column number (0-6)
;      R0   the row number (0-6)
;***********************************************************

CONVERT_MOVE    LD R2, ASCII_INVERSE_OFFSET     ; load ascii inverse offset
        LD R4, A_TO_0_OFFSET
        ADD R0, R0, R2          ; Convert ('0'-'6') to (0-6)

        ADD R1, R1, R4             ;  Convert ('A'-'G') to (0-6)
            ADD R1, R1, R2


CM_DONE       LD    R2, CV_R2
        LD    R4, CV_R2
        RET
            
        
        ASCII_INVERSE_OFFSET     .fill #-48
        A_TO_0_OFFSET         .fill #-17
        CM_R2                 .BLKW  #1
        CM_R4                .BLKW  #1
;*********************************************************** 
; CHECK_VALIDITY
; Input      R1  ASCII character for column
;       R0  ASCII character for row
; Returns   R3  zero if valid; -1 if invalid; 1 if row/column are mismatched
;***********************************************************

CHECK_VALIDITY  
	ST R7, CV_BACK
	JSR GET_ADDRESS
	AND 	R2, R2, #0    
        AND    R4, R4, #0
        AND    R5, R5, #0
	AND R6, R6, #0      
        AND    R3, R3, #0
	 	
	    LD    R6, NASCIISIX       ;NEGATIVE OF ASCII 6 IN R6           
	AND R4, R4, #0                                                 
            ADD    R4, R0, R6      ;CHECK IF INPUT IS LESS THAN 6
        BRP    INVALID     ;IF THIS IS POSITVE, INPUT IS GREATER THAN 6, INPUT IS INVALID

        LD    R6, NASCIIZERO       ;NEGATIVE OF ASCII 0 IN R6
	AND R5, R5, #0
        ADD    R5, R0, R6          ;CHECK IF INPUT IS GREATER THAN ZERO
        BRN    INVALID    ;IF THIS IS NEGATIVE, INPUT IS LESSER THAN 0, INPUT IS INVALID

        LD    R6, NASCIIG           ;NEGATIVE OF ASCII G IN R6  
        ADD    R3, R1, R6      ;CHECK IF INPUT IS LESS THAN G     
        BRP    INVALID    ;IF THIS IS POSITIVE, INPUT IS GREATER THAN G, INPUT IS INVALID
        
        LD    R6, NASCIIA           ;NEGATIVE OF ASCII A IN R6
	AND 	R2, R2, #0
        ADD    R2, R1, R6          ;CHECK IF INPUT IS GREATER THAN A    
        BRN    INVALID     ;IF THIS IS NEGATIVE, INPUT IS LESSER THAN A, INPUT IS INVALID
        
	AND 	R2, R2, #0    
        AND    R4, R4, #0
        AND    R5, R5, #0
	AND R6, R6, #0
	LD R2, I_A_O
	ADD R4, R0, #0
	ADD R4, R4, R2
	LD R2, I_N_O
	ADD R5, R1, #0
	ADD R5, R5, R2
	LD R2, I_A_O
	ADD R5, R5, R2
	ADD R4, R4, R5
	AND R2, R2, #0
	LD R6, ODD_CHECK
	AND R2, R4, R6 
	BRZ INCORRECT

        
	AND    R3, R3, #0    ; move is valid, set R3 to 0
        BR    CV_DONE
        ;LEA   R0, VALIDP        ; if the move was invalid, output corresponding
        ;TRAP  x22

INVALID        AND R3, R3, #0
		ADD R3, R3, #-1
		BR CV_DONE
INCORRECT 	AND R3, R3, #0
		ADD R3, R3, #1
		BR CV_DONE 
CV_DONE    
		LD    R2, CV_R2
        	LD    R4, CV_R4
                LD    R5, CV_R5
                LD    R6, CV_R6
		LD R7, CV_BACK
        RET
            
        NASCIIX .FILL -120
          NASCIISIX .FILL -54
          NASCIIZERO .FILL -48
        NASCIIA .FILL -65
           NASCIIG .FILL -71
	ODD_CHECK .FILL x0001
        VALIDP    .STRINGZ "VALID ENTRY"
	I_A_O	.FILL -48
	I_N_O	.FILL -17
        CV_R2             .BLKW  #1
        CV_R4             .BLKW  #1
        CV_R5             .BLKW  #1
        CV_R6             .BLKW  #1
	CV_BACK .BLKW #1
.END




