; Homework #4 | "hangman game" Extra Credit (Ends when you guess wrong 3 times)


;----------- Program Start -----------
.ORIG x3000

LD R0, LOOP_CTRL          ; Set up game state where R0 holds the
LD R1, ATTEMPTS_LEFT      ; Set up game state where R1 holds ATTEMPTS_LEFT

JSR SET_LEN_OF_WORD_TO_GUESS
JSR CREATE_SECRET_WORD

JSR DISPLAY_HANGMAN

JSR START
MAIN 
    JSR DISPLAY_HANGMAN
    LD R1, ATTEMPTS_LEFT             ;loads the current number to check
    ADD R1,R1,#-1                    ;dec by 1 to check if all three attempts are up
    BRz YOU_LOSE
    JSR DISPLAY_GUESS_AGAIN          ;print to screen "guess again"
START 
    JSR DISPLAY_SECRET_STRING        ;print the secret string to screen    
    ;-------------------------------
    JSR DISPLAY_MSG_INPUT_CHAR       ; Input a character > 'X'
    JSR GET_INPUT                    ; gets char   << keyboard
    JSR DISPLAY_USER_INPUT_TO_SCREEN ; prints char >> screen
    ;-------------------------------

    JSR	COMPARE_AND_UPDATE_INPUT     ;check user input update string
    JSR IS_STRINGS_MATCHED           ;checks if strings match and updates the loop ctrl
    LD R0, LOOP_CTRL                 ;load the current loop control into r0 t0 check game state     

BRp MAIN ;ends when R0 = 0 b/c word is matched 

JSR DSIPLAY_YOU_WIN ;print to screen "you win"
JSR DISPLAY_WORD_WAS ;print to screen what the word was to guess

;---------  STOP PROGRAM FLOW  --------
HALT
JSR QUIT_NOW ;send PC to end to skip over subroutines/functions  

YOU_LOSE
JSR DSIPLAY_YOU_LOSE ;print to screen "you lose"
JSR DISPLAY_WORD_WAS ;print to screen what the word was to guess
HALT
JSR QUIT_NOW         ;send PC to end to skip over subroutines/functions  

;-----------      Data     -----------
;loop control 
LOOP_CTRL .FILL #1
;current user input
USER_INPUT .FILL #0
;Here is where the word to guess is stored ------------------------------------------------------------------- |  READ ME!!!!
WORD_TO_GUESS .STRINGZ	"AWKWARD"          ; <==  Change this string to change the word the user has to        |
WORD_TO_GUESS_LEN .FILL #0                 ;      guess. It can be up to 30 characters long                    |
; ------------------------------------------------------------------------------------------------------------ |
;secret word 
SECRET_WORD   .BLKW	31
;variable for the number of attempts left
ATTEMPTS_LEFT .FILL 4
;----------- Subroutines -----------
;Get user input()
GET_INPUT
    ST R7, REG_7       ;Save R7
    ST R0, REG_0       ;save R0

    TRAP x20            ;Read char from keyboard
    LD  R7, FORCECAP    ;change the input to capital (comment out if you so you use any characters in hangman)
    AND R0, R0, R7      ;R7 = x5F sets to cap
    ST  R0, USER_INPUT  ;store user input

    LD  R0, REG_0       ;restore R0
    LD  R7, REG_7       ;restore R7
 RET
FORCECAP .FILL x5F
;display user input to screen()
DISPLAY_USER_INPUT_TO_SCREEN
    ST R7, REG_7       ;save R7
    ST R0, REG_0       ;save R0

    LD	R0,	USER_INPUT ;load user input
    TRAP x21           ; Write character to screen

    LD R0, REG_0       ;restore R0    
    LD R7, REG_7       ;restore R7
RET

;display ask user for input
DISPLAY_MSG_INPUT_CHAR
    ST R7, REG_7       ;save R7
    ST R0, REG_0       ;save R0

    LEA R0, MSG_0      ;load address
    PUTS               ;print str to screen
    
    LD R0, REG_0       ;restore R0
    LD R7, REG_7       ;restore R7
RET


;display secret string
DISPLAY_SECRET_STRING
    ST R7, REG_7        ;save R7
    ST R1, REG_1        ;save R1
    ST R0, REG_0        ;save R0


    LEA R0, MSG_1       ;load address of newline
    PUTS                ;add newline to screen
    LEA R0, SECRET_WORD ;load address
    PUTS                ;print str to screen
    
    LD R0, REG_0        ;restore R0
    LD R7, REG_7        ;restore R7
RET
;display guess again
DISPLAY_GUESS_AGAIN
    ST R7, REG_7        ;save R7
    ST R1, REG_1        ;save R1
    ST R0, REG_0        ;save R0


    LEA R0, MSG_2       ;load address of "guess again"
    PUTS                ;print str to screen
    
    LD R0, REG_0        ;restore R0
    LD R1, REG_1        ;restore R1
    LD R7, REG_7        ;restore R7
RET
;display you win msg
DSIPLAY_YOU_WIN
    ST R7, REG_7        ;save R7
    ST R1, REG_1        ;save R1    
    ST R0, REG_0        ;save R0

    LEA R0, MSG_3       ;load address of "you win"
    PUTS                ;print str to screen
    
    LD R0, REG_0        ;restore R0
    LD R1, REG_1        ;restore R1    
    LD R7, REG_7        ;restore R7
RET

;tells the user what the word was 
DISPLAY_WORD_WAS
    ST R7, REG_7        ;save R7
    ST R1, REG_1        ;save R1    
    ST R0, REG_0        ;save R0

    LEA R0, MSG_5         ;load address of "word was"
    PUTS                  ;print str to screen
    LEA R0, WORD_TO_GUESS ;load address of "word was"
    PUTS                  ;print str to screen
    LEA R0, MSG_1         ;loads address of new line
    

    LD R0, REG_0        ;restore R0
    LD R1, REG_1        ;restore R1    
    LD R7, REG_7        ;restore R7
RET

;Set len of word to guess. And sets the LOOP ctrl
SET_LEN_OF_WORD_TO_GUESS
    ST R7, REG_7       ;save R7
    ST R0, REG_0       ;save R0
    ST R1, REG_1       ;save R1
    ST R2, REG_2       ;save R2
    
    LEA R0, WORD_TO_GUESS ;set R0 to be index of word_to_guess
    ADD R0, R0, #-1       ;dec pointer so that it can start at "0" in the loop
    AND R7, R7, #0        ;set R7 to 0 to increment it to count word len
    LOOP_1 
        AND R1, R1, #0 ;set R1 to 0 to cmp char to check if end of str  
        ADD R0, R0, #1 ;inc pointer R0++
        ADD R7, R7, #1 ;R7++
        LDR	R2,	R0,	#0 ;Load char into R2 if its NULL then it will stop
    BRp LOOP_1
    ; SHOULD I (--) the R7??? 
    ST  R7, WORD_TO_GUESS_LEN ;save the len of the word


    LD R2, REG_2       ;restore R2
    LD R1, REG_1       ;restore R1
    LD R0, REG_0       ;restore R0
    LD R7, REG_7       ;restore R7
RET

;create secret word
CREATE_SECRET_WORD
    ST R7, REG_7       ;save R7
    ST R0, REG_0       ;save R0
    ST R1, REG_1       ;save R1
    ST R2, REG_2       ;save R2

    LEA R0, SECRET_WORD        ;set R0 to be index of secret word
    ADD R0, R0, #-1            ;dec pointer so that it can start at "0" in the loop
    LD  R7, WORD_TO_GUESS_LEN  ;set R7 to 0 be the len of word_to_guess
    LD  R2, C_STAR             ;loads R2 with the address of '*' 
    LOOP_2 
        AND R1, R1, #0  ;set R1 to 0 to cmp char to check if end of str  
        ADD R0, R0, #1  ;inc pointer R0++
        STR	R2,	R0,	#0  ;store the * into the spot of the array
        ADD R7, R7, #-1 ;R7-- 
    BRp LOOP_2

    AND  R2, R2, #0 ;load R2 with address of null
    STR	R2,	R0,	#0  ;store the null into the spot of the array
    
    ;debug check to see if the right amount of stars work
    ;LEA R0, SECRET_WORD        ;set R0 to be index of secret word
    ;PUTS

    LD R2, REG_2       ;restore R2
    LD R1, REG_1       ;restore R1
    LD R0, REG_0       ;restore R0
    LD R7, REG_7       ;restore R7
RET


;-----------      Data     -----------
;Used to store register values when needed 
REG_0 .FILL #0
REG_1 .FILL #0
REG_2 .FILL #0
REG_3 .FILL #0
REG_4 .FILL #0
REG_5 .FILL #0
REG_6 .FILL #0
REG_7 .FILL #0


;compare user input to word to guess and update the secret word if there is a match
;also subtract attempts left if user input is not a match
COMPARE_AND_UPDATE_INPUT
    ST R7, REG_7       ;save R7
    ST R0, REG_0       ;save R0
    ST R1, REG_1       ;save R1
    ST R2, REG_2       ;save R2
    ST R3, REG_3       ;save R3
    ST R6, REG_6       ;save R6
    ST R4, REG_4       ;save R4
    ST R5, REG_4       ;save R5

    AND R5, R5, #0             ;clear R5 so that we can tell if any letters have been guessed correctly 
    LEA R0, SECRET_WORD        ;set R0 to be index of secret word
    ADD R0, R0, #-1            ;dec pointer so that it can start at "0" in the loop
    LEA R1, WORD_TO_GUESS      ;set R1 to be index of word_to_guess
    ADD R1, R1, #-1            ;dec pointer so that it can start at "0" in the loop
    LD  R6, WORD_TO_GUESS_LEN  ;set R6 to be the len of word_to_guess
    LD  R2, USER_INPUT         ;loads R2 with user input
    NOT R2, R2                 ;not the user input
    ADD R2, R2, #1             ;set user input to 2's comp. to compare 
    JSR	LOOP_3 
    STORE_CHAR
        ADD R5, R5, #1      ;R5++ if letters have been added
        LD  R2, USER_INPUT  ;loads R2 with user input
        STR	R2,	R0,	#0      ;store the matching user input into the secret string
        NOT R2, R2          ;not the user input
        ADD R2, R2, #1      ;set user input to 2's comp. to compare   
    LOOP_3 
        ADD R0, R0, #1  ;inc pointer R0++ (secret word)
        ADD R1, R1, #1  ;inc pointer R1++ (word_to_guess)
        LDR R4, R1, #0  ;load a char from the word to guess into R4 
        ADD R4, R4, R2  ;subtract R4-R2 if its 0 then the same
        BRz STORE_CHAR
        ADD R6, R6, #-1 ;R6-- 
    BRp LOOP_3

    ADD R5, R5, #0
    BRp CHAR_WAS_ADDED ;check to see if we need to (attempts_left--)
    LD  R5, ATTEMPTS_LEFT
    ADD R5, R5, #-1
    ST  R5, ATTEMPTS_LEFT

    ;debug check to see if the right amount of stars work
    ;LEA R0, SECRET_WORD        ;set R0 to be index of secret word
    ;PUTS

    CHAR_WAS_ADDED
    
    LD R5, REG_5       ;save R5
    LD R4, REG_4       ;restore R4
    LD R6, REG_6       ;restore R6
    LD R3, REG_3       ;restore R3
    LD R2, REG_2       ;restore R2
    LD R1, REG_1       ;restore R1
    LD R0, REG_0       ;restore R0
    LD R7, REG_7       ;restore R7
RET


;is string matched "compares secret word to guess word" if the two strings match R0 set to R0
IS_STRINGS_MATCHED
    ST R0, REG_0       
    ST R1, REG_1       
    ST R2, REG_2       
    ST R3, REG_3       
    ST R4, REG_4         
    ST R5, REG_5          
    ST R6, REG_6           
    ST R7, REG_7
    ; save registers
    
    LEA R7, SECRET_WORD        ;set R7 to be index of secret word
    ADD R7, R7, #-1            ;dec pointer so that it can start at "0" in the loop
    LEA R6, WORD_TO_GUESS      ;set R6 to be index of word_to_guess
    ADD R6, R6, #-1            ;dec pointer so that it can start at "0" in the loop
    LD  R5, WORD_TO_GUESS_LEN  ;set R2 to be the len of word_to_guess so that it can be used to end the loop
        ADD	R0,	R0,	#-1 ;set register 1 to zero b/c the word           
    LOOP_4 
        ADD R7, R7, #1  ;inc pointer R0++ (secret word)
        ADD R6, R6, #1  ;inc pointer R1++ (word_to_guess)
        LDR R4, R7, #0  ;load a char from the (word to guess) into R4
        LDR R3, R6, #0  ;load a char from the (secret word) into R3
        
        ;twos comp on (secret word) char 
        NOT	R3,	R3
        ADD	R3,	R3,	#1

        ADD R2, R3, R4  ;subtract R3-R4 if its p then not the 
        BRnp NOT_THE_SAME
        ADD R5, R5, #-1 ;R6-- 
    BRp LOOP_4
    AND	R0,	R0,	#0 ;clear the R0 register
    ST R0, LOOP_CTRL ;switch LOOP_CTRL to 0 because we want to end the program
    NOT_THE_SAME ;if not the same don't change LOOP_CTRL

    ;restore registers
    LD R7, REG_7           
    LD R6, REG_6
    LD R5, REG_5       
    LD R4, REG_4       
    LD R3, REG_3       
    LD R2, REG_2
    LD R1, REG_1 
    LD R0, REG_0
RET

;display hangman basically this will draw the hang man to the screen 
DISPLAY_HANGMAN
    ST R0, REG_0       
    ST R1, REG_1       
    ST R2, REG_2              
    ST R7, REG_7
    ;save registers

    LD R1, ATTEMPTS_LEFT    

    LEA R0, TOP_BEAM
    PUTS
    LEA R0, POLL_ROPE
    PUTS
    LEA R0, EMPTY_POLL
    PUTS
    ;if R1 <= 2 (head)
    ADD R2, R1, #-3
    BRp NO_DRAW_HEAD   
    LEA R0, HEAD        
    PUTS
    NO_DRAW_HEAD
    LEA R0, EMPTY_POLL
    PUTS
    ;if R1 <= 1 (body)
    ADD R2, R1, #-2
    BRp NO_DRAW_BODY   
    LEA R0, BODY        
    PUTS
    NO_DRAW_BODY
    LEA R0, EMPTY_POLL
    PUTS               
    ;if R1 <= 0 (legs)
    ADD R2, R1, #-1
    BRp NO_DRAW_LEGS   
    LEA R0, LEGS        
    PUTS 
    NO_DRAW_LEGS          
    LEA R0, BASE
    PUTS

    ;restore registers
    LD R7, REG_7              
    LD R2, REG_2
    LD R1, REG_1 
    LD R0, REG_0
RET

DSIPLAY_YOU_LOSE
    ST R0, REG_0 
    ST R7, REG_7  
    LEA R0, MSG_4
    PUTS          ;print you lose
    LD R7, REG_7  
    LD R0, REG_0  
RET

;-----------      Data     -----------
;Program massages 
MSG_0 .STRINGZ	"\nInput a character > "
MSG_1 .STRINGZ	"\n"
MSG_2 .STRINGZ	"\nGuess again\n"
MSG_3 .STRINGZ	"\nYOU WIN\n"
MSG_4 .STRINGZ	"\nYOU LOSE\n"
MSG_5 .STRINGZ	"Word was\n"

;hang man ascii art
C_STAR	     .FILL x2A  ;'*'
EMPTY_POLL .STRINGZ	"\n|"                    
TOP_BEAM   .STRINGZ	"\n|>>>>>>"             
POLL_ROPE  .STRINGZ	"\n|    |"               
HEAD       .STRINGZ	"    0 <-dis is you"  ;1
BODY       .STRINGZ	"  +-|-+"             ;2
LEGS       .STRINGZ	"   | |"              ;3
BASE       .STRINGZ	"\n|============|\n"       

QUIT_NOW .FILL #0
.END