;Hannah Organick, Elizabeth Fawcett, Chunan Liu


.ORIG x3000

JSR Get_Start

JMP R6


;-----------------------------------------------------------------------------------	

Get_Loadscreen

LEA R6, LOADSCREEN

RET

;-----------------------------------------------------------------------------------	


LOADSCREEN .STRINGZ "\n\n\n\n~*~*~*~*~*~*~*~*~\n  BOWMAN V1.0!\n~*~*~*~*~*~*~*~*~\n\n"

;-----------------------------------------------------------------------------------	

;GET_PROJECTION calculates projected distance and stores in 	

;	distTraveled	

;-----------------------------------------------------------------------------------

;R2  base address register

;R3  angle

;R4  value of 1/sin(2*angle)

setMappedAngleValue

LEA R2, mappedValues

LD R3, angle

ADD R3, R3, #-1	;because degrees start at 1, not zero in table

ADD R2, R2, R3

LDR R4, R2, #0

ST R4, mappedAngleValue

RET

GET_PROJECTION


ST R7, projectionReturn

JSR setMappedAngleValue

LD R0, mappedAngleValue

LD R1, speed


JSR MULTIPLY


JSR Get_Product

LDR R0, R6, #0

ST R0, distTraveled


LD R7, projectionReturn

RET


projectionReturn .BLKW #1

mappedAngleValue    .BLKW #1

mappedValues 

.FILL	1

.FILL	2

.FILL	3

.FILL	4

.FILL	5

.FILL	6

.FILL	7

.FILL	8

.FILL	9

.FILL	10

.FILL	11

.FILL	12

.FILL	13

.FILL	14

.FILL	15

.FILL	16

.FILL	17

.FILL	18

.FILL	19

.FILL	20

.FILL	21

.FILL	22

.FILL	23

.FILL	24

.FILL	25

.FILL	26

.FILL	27

.FILL	28

.FILL	29

.FILL	30

.FILL	31

.FILL	32

.FILL	33

.FILL	34

.FILL	35

.FILL	36

.FILL	37

.FILL	38

.FILL	39

.FILL	40

.FILL	41

.FILL	42

.FILL	43

.FILL	44

.FILL	45

.FILL	44

.FILL	43

.FILL	42

.FILL	41

.FILL	40

.FILL	39

.FILL	38

.FILL	37

.FILL	36

.FILL	35

.FILL	34

.FILL	33

.FILL	32

.FILL	31

.FILL	30

.FILL	29

.FILL	28

.FILL	27

.FILL	26

.FILL	25

.FILL	24

.FILL	23

.FILL	22

.FILL	21

.FILL	20

.FILL	19

.FILL	18

.FILL	17

.FILL	16

.FILL	15

.FILL	14

.FILL	13

.FILL	12

.FILL	11

.FILL	10

.FILL	9

.FILL	8

.FILL	7

.FILL	6

.FILL	5

.FILL	4

.FILL	3

.FILL	2

.FILL	1

;-----------------------------------------------------------------------------------	

paragraph .STRINGZ "\n\n"

newLine .STRINGZ "\n"

anglePrompt .STRINGZ "\nEnter 2-digit angle between 0 and 90 degrees: "

speedPrompt .STRINGZ "\n\n\nEnter 2-digit speed: "


distString .STRINGZ "Distance:          "

distTraveledString .STRINGZ "Distance Traveled: "


asciiBuff   	.BLKW #6

asciiNumber   	.BLKW #2	


angle .BLKW 1

speed .BLKW 1

distance .BLKW 1

distTraveled .BLKW 1

playerCounter .FILL #0

promptCounter .FILL #1

;-----------------------------------------------------------------------------------	

;IS_ODD	writes positive if counter is odd

;	ie pos if player 1's turn

;	zero if player 2's turn

;-----------------------------------------------------------------------------------	

IS_ODD

LD R0, playerCounter

AND R0, R0, #1

RET






;-----------------------------------------------------------------------------------	

;-----------------------------------------------------------------------------------	

;-----------------------------------------------------------------------------------	

Get_Start

LEA R6, START

RET


Get_BeginGame

LEA R6, ReStart

RET



START

JSR Get_Loadscreen

AND R0, R6, R6

PUTS


JSR RANDOM

    JSR Get_X

    LDR R0, R6, #0

    ST R0, distance

BRnzp BeginGame



ReStart

LD R0, playerCounter

AND R0, R0, #0

ST R0, playerCounter

JSR RANDOM

    JSR Get_X

    LDR R0, R6, #0

    ST R0, distance



BeginGame

LD R0, playerCounter

ADD R0, R0, #1

ST R0, playerCounter

LEA R0, paragraph

PUTS

JSR IS_ODD

BRp PrintPlayer1

BRz PrintPlayer2

PrintPlayer1

LEA R0, player1String

PUTS

BRnzp GetInput

PrintPlayer2	

LEA R0, player2String

PUTS

GetInput	;---------------------------------------------------------

LD R0, promptCounter

ADD R0, R0, #-1

ST R0, promptCounter


BRz AnglePrint

BRn SpeedPrint

AnglePrint	

LEA R0, anglePrompt  

PUTS

BRnzp continueInput

SpeedPrint	

LEA R0, speedPrompt

PUTS


continueInput	

;input first 3-digit number

    IN 	

    AND R1, R0, R0

    IN

    AND R2, R0, R0

    ;IN

    ;AND R3, R0, R0


;store the input digits to asciiNumber

    LEA R0, asciiNumber    

    STR R1, R0, #0

    STR R2, R0, #1

    ;STR R3, R0, #2

;convert asciiNumber to a decimal number (result is stored in deciNumber)

LEA R2, asciiNumber  

    JSR CONVERT_ASCII

JSR Get_DeciNumber

LDR R0, R6, #0

LD R1, promptCounter

BRz storeAngle

BRnp storeSpeed

;if promptCounter == 0, load deciNumber and store at angle

storeAngle

ST R0, angle

BRnzp GetInput

;if promptCounter == 1, load deciNumber and store at speed 

storeSpeed

ST R0, speed 

AND R0, R0, #0

ADD R0, R0, #1

ST R0, promptCounter


LEA R0, newLine

PUTS

PUTS

PUTS


;Calculates the chance of a random event occuring
   
BRnzp calcrandEvent

calcDist 

;calculate the distance the arrow travels and store in memory distTraveled	

;distTraveled = function(dist, angle, speed)

    JSR GET_PROJECTION

    

    

    ;LEA R0, distString	;------- print distance string !!!!!!!!

    ;PUTS

    ;load address of ascii buffer to store string to R1 

    LD R0, distance

    LEA R1, asciiBuff

    JSR CONVERT_BINARY


    ;load the address of string to R2

    LEA R2, asciiBuff           

    ;JSR PRINT_BUFFER	;------- print distance  !!!!!!!!

    ;LD R0, newLine

    ;OUT




;LEA R0, distTraveledString	;------- print distance traveled string !!!!!!!!

;PUTS

  ;load address of ascii buffer to store string to R1 

    LD R0, distTraveled

    LEA R1, asciiBuff

    JSR CONVERT_BINARY


    ;load the address of string to R2

    LEA R2, asciiBuff           

    ;JSR PRINT_BUFFER	;------- print distance traveled !!!!!!!!

    ;LD R0, newLine

    ;OUT




LD R4, distTraveled

LD R6, distance

;accuracy checking

;dist - distTraveled

NOT R4, R4

ADD R4, R4, #1

ADD R4, R4, R6 ; Subtract R6 - R4 (Random distance - arrow's distance)


;if pos, then fell too short

BRp tooShort


;if zero then win

BRz wins


;if neg, add accuracy

LD R3, accuracy

ADD R4, R4, R3

;if n, then too far

BRn tooFar

;if zp, then win

BRzp wins

accuracy .fill #15

;----------------------------------------------------------------------------

player1String .STRINGZ "PLAYER 1"

player2String .STRINGZ "PLAYER 2"

shortMesg .STRINGZ "\nThe arrow fell short!\n\n"

farMesg .STRINGZ "\nThe arrow flew too far!\n\n"

hitMesg .STRINGZ "\nBullseye!\n"

defaultWinMesg .STRINGZ "\nYou shot yourself!\n"

winMesg .STRINGZ " WINS!! Play again? (y for YES or n for NO): "

randMesg .STRINGZ  "You hit a bird instead!"

randEvent

LEA R0, randMesg

PUTS

BRnzp BeginGame

tooShort	

LEA R0, shortMesg

PUTS

BRnzp BeginGame

tooFar

LEA R0, farMesg

PUTS

BRnzp BeginGame

calcrandEvent

JSR RANDOM

    JSR Get_X

    LDR R0, R6, #0
	
    ADD R0, R0, #-10
    ADD R0, R0, #-10
    ADD R0, R0, #-10
    ADD R0, R0, #-10    

    BRN randEvent

    BRp calcDist

JSR IS_ODD

BRp	Player1Wins

BRz	Player2Wins

wins

LEA R0, hitMesg

PUTS

JSR IS_ODD

BRp	Player1Wins

BRz	Player2Wins

Player1Wins

LEA R0, player1String

PUTS 

BRnzp continueWinFunction

Player2Wins

LEA R0, player2String

PUTS 

BRnzp continueWinFunction

continueWinFunction	

LEA R0, winMesg

PUTS

IN	; Recieve player response if they want to play again

;;check if ascii is correct

    LD R4, yAscii

    NOT R4, R4

    ADD R4, R4, #1

    ADD R0, R4, R0


BRz goToBegining   ; Game starts again when "y" is entered

BRnp GameOver



goToBegining

JSR Get_BeginGame

JMP R6	

byeMesg .STRINGZ "\nGame Over\nGoodbye!\n"

GameOver

LEA R0, byeMesg

PUTS


HALT

;-----------------------------------------------------------------------------------	

;-----------------------------------------------------------------------------------	

;-----------------------------------------------------------------------------------	

;-----------------------------------------------------------------------------------	

;CONVERT_ASCII "tens" digit is at the address stored in R2

;	assumes return value is in R7

;-----------------------------------------------------------------------------------

  CONVERT_ASCII    

    AND R0, R0, #0   	;clear R0

    LD R3, NegAsciiOffset   ;R3 <- -x0030


    LDR R4, R2, #0   	;R4 contains the "tens" ascii digit

    ADD R4, R4, R3   	;convert to decimal, R4 now contains decimal "tens"

    LEA R5, LookUp10    	;R5 now contains the 10s BASE

    ADD R5, R5, R4   	;R5 now points to address of correct 10s value

    LDR R4, R5, #0   	;R4 now contains the correct 10s value

    ADD R0, R0, R4   	;add to the running sum


    LDR R4, R2, #1   	;R4 contains the "ones" digit

    ADD R4, R4, R3   	;convert to decimal, R4 now contains decimal "ones"

    ADD R0, R0, R4   	;add to running sum in R0

    

    ST R0, deciNumber

    

    RET   	;return to whatever value is in R7

    deciNumber 	.BLKW 1


;-----------------------------------------------------------------------------------	

Get_DeciNumber

LEA R6, deciNumber

RET

;-----------------------------------------------------------------------------------	



    LookUp10    .FILL #0

   	.FILL #10

   	.FILL #20

   	.FILL #30

   	.FILL #40

   	.FILL #50

   	.FILL #60

   	.FILL #70

   	.FILL #80

   	.FILL #90


;----------------------------------------------------------------------------------

 	












;-----------------------------------------------------------------------------------	

;RANDOM

;	Next random number is stored at X

;-----------------------------------------------------------------------------------	


RANDOM: ST R7,BACK ; save return location

LD R0, M

LD R1, A

JSR Divide ; R0 / R1

;; q = m / a

LD R0, QUOTIENT ; R0 / R1

ST R0, Q 	

;; r = m mod a

LD R0, REMAINDER ; R0 mod R1

ST R0, R

        ;; x / q

LD R0, X

LD R1, Q

JSR Divide ; R0 / R1

LD R1, QUOTIENT

ST R1, TEMP2

LD R1, REMAINDER ; x mod q

ST R1, TEMP1

;; x ?  a ?  (x mod q) - r ?  (x / q)

;;      a * TEMP1 - r * TEMP2

LD R0, A

JSR MULTIPLY ; R2 <- R0 * R1

ST R2, TEMP1

;;      a * TEMP1 - r * TEMP2

LD R0, R

LD R1, TEMP2

JSR MULTIPLY ; R2 <- r * TEMP2

NOT R2,R2 ; -R2

ADD R2,R2,#1

ST R2, TEMP2 

LD R1, TEMP1

ADD R2, R2, R1 ; TEMP1 - TEMP2

TEST:	BRzp DONE ; if x < 0 then

LD R1, M

ADD R2, R2, R1 ; x ?  x + m

DONE:	ST R2, X

LD R7, BACK ; Restore return address

RET

A:	.FILL #7       ;; a , the multiplicative constant is given

M:	.FILL #202	;; maximum distance

X:	.FILL #10	    ;; x, the seed is given

R:	.FILL #0

Q:	.FILL #0

TEMP1:	.FILL #0

TEMP2:	.FILL #0

BACK:	.FILL #0


Get_X

LEA R6, X

RET

;-----------------------------------------------------------------------------------	





;-----------------------------------------------------------------------------------	

;MULTIPLY R2 <- R0 * R1

;	Also uses R3 to store SIGN

;-----------------------------------------------------------------------------------	

MULTIPLY: AND R2,R2,#0

  AND R3,R3,#0

  ADD R0,R0,#0 ; compare R0

  BRn MultNEG1

  BR  MULTCONT

MultNEG1: NOT R3,R3 ; flip SIGN

  NOT R0,R0

  ADD R0,R0,#1

MULTCONT: ADD R1,R1,#0 ; compare R1

  BRn MultNEG2

  BR MultInit

MultNEG2: NOT R3,R3 ; flip SIGN

  NOT R1,R1

  ADD R1,R1,#1

MultInit: ADD R0,R0,#0  ; have R0 set the condition codes

MultLoop: BRz MultDone

  ADD R2,R2,R1

  ADD R0,R0,#-1

  BR MultLoop

MultDone: ADD R0,R3,#0

  BRzp MultRet

  NOT R2,R2

  ADD R2,R2,#1

MultRet:

ST R2, PRODUCT  

RET	; R2 has the sum

PRODUCT .FILL #0


Get_Product

LEA R6, PRODUCT

RET

;-----------------------------------------------------------------------------------	



;-----------------------------------------------------------------------------------	

;DIVIDE	  R0 / R1

;   Also uses R3 to store SIGN

;           R4 to store -R1

;           R5 is QUOTIENT

;           R6 is REMAINDER

;          R2 temp

;-----------------------------------------------------------------------------------	

Divide:   AND R3,R3,#0

  ST R3, QUOTIENT

  ST R3, REMAINDER

  ADD R0,R0,#0 ; compare R0

  BRn DivNEG1

  BR  DIVCONT

DivNEG1:  NOT R3,R3 ; flip SIGN

  NOT R0,R0

  ADD R0,R0,#1

DIVCONT:  ADD R1,R1,#0 ; compare R1

  BRn DivNEG2

  BR DivInit

DivNEG2:  NOT R3,R3 ; flip SIGN

  NOT R1,R1

  ADD R1,R1,#1

DivInit:  ADD R4,R1,#0

  NOT R4,R4

  ADD R4,R4,#1

DivLoop:  ADD R2,R0,R4  ; have R2 set the condition codes

  BRn DivDone

  ADD R0,R0,R4

  LD R2,QUOTIENT

  ADD R2,R2,#1

  ST R2,QUOTIENT

  BR DivLoop

DivDone:  ADD R3,R3,#0 ; Negative?

  BRzp DivRet

  LD R2,QUOTIENT ; Yes, then negate R2

  NOT R2,R2

  ADD R2,R2,#1

  ST R2,QUOTIENT

DivRet:	  ST R0,REMAINDER

  RET	; R2 has the sum

QUOTIENT:	.FILL #0

REMAINDER:	.FILL #0

;-----------------------------------------------------------------------------------	





;-----------------------------------------------------------------------------------	

;PRINT_BUFFER	prints the ascii string stored starting at the address in R2

;	assumes return value is in R7

;-----------------------------------------------------------------------------------

PRINT_BUFFER

ADD R1, R7, #0	;copy return value to R1

AND R4, R4, #0   	;clear R4

PrintLoop

    ADD R5, R2, #0	;R5 now contains the address BASE of the string

    ADD R5, R5, R4   	;R5 now points to address of current ascii value

    LDR R6, R5, #0   	;R6 now contains the current ascii value

    AND R0, R6, R6   	;R0 now contains the current ascii value

    BRz    DonePrinting

    OUT

    ADD R4, R4, #1   	;increment counter

    BRnzp PrintLoop

 

 DonePrinting

 	JMP R1   

;-----------------------------------------------------------------------------------

;-----------------------------------------------------------------------------------	

;CONVERT_BINARY	converts binary num in R0 and stores ascii characters in 

;	an ascii buffer whose starting address is stored in R1

;	assumes return value is in R7

;-----------------------------------------------------------------------------------

CONVERT_BINARY    

    ADD R0, R0, #0   	;test if number is negative

    BRn    NegSign

    LD R2, PosAsciiSign   	;store a positive sign in asciiBuff

    STR R2, R1, #0

    BRnzp Begin1000


NegSign    LD R2, NegAsciiSign

    STR R2, R1, #0   	;store a negative sign in asciiBuff

    NOT R0, R0

    ADD R0, R0, #1   	;convert R0 to its absolute value

    

Begin1000    

    LD R2, AsciiOffset   	;prep for "thousands" digit

    LD R3, Neg1000    

Loop1000

    ADD R0, R0, R3   	;repeatedly subtract R3 from R0 until R0 is negative

    BRn End1000

    ADD R2, R2, #1   	;count number of times

    BRnzp Loop1000

End1000

    STR R2, R1, #1   	;store "thousands" digit in second idx at asciiBuff

    LD R3, Pos1000

    ADD R0, R0, R3   	;correct R0 for one-too-many subtracts




Begin100    

    LD R2, AsciiOffset   	;prep for "hundreds" digit

    LD R3, Neg100    

Loop100    

    ADD R0, R0, R3   	;repeatedly subtract R3 from R0 until R0 is negative

    BRn End100

    ADD R2, R2, #1   	;count number of times

    BRnzp Loop100

End100

    STR R2, R1, #2   	;store "hundreds" digit in third idx at asciiBuff

    LD R3, Pos100

    ADD R0, R0, R3   	;correct R0 for one-too-many subtracts




Begin10    

    LD R2, AsciiOffset   	;prep for "tens" digit

    LD R3, Neg10    

Loop10    

    ADD R0, R0, R3   	;repeatedly subtract R3 from R0 until R0 is negative

    BRn End10

    ADD R2, R2, #1   	;count number of times

    BRnzp Loop10

End10

    STR R2, R1, #3   	;store "tens" digit in fourth idx at asciiBuff

    LD R3, Pos10

    ADD R0, R0, R3   	;correct R0 for one-too-many subtracts




Begin1    

    LD R2, AsciiOffset   	;prep for "ones" digit

    ADD R2, R2, R0    

    STR R2, R1, #4   	;store "ones" digit in fifth idx at asciiBuff


    AND R2, R2, #0   	;clear R2

    STR R2, R1, #5   	;store "null" at sixth idx at asciiBuff


JMP R7



NegAsciiOffset  .FILL xFFD0      ;-x030

    AsciiOffset    .FILL x0030

    NegAsciiSign    .FILL x002D

    PosAsciiSign    .FILL x002B

    yAscii	.FILL x0079


    Neg1000   	.FILL xFC18

    Pos1000   	.FILL x3E8

    Neg100   	.FILL xFF9C

    Pos100   	.FILL x0064

    Neg10   	.FILL xFFF6

    Pos10   	.FILL #10


  ;-----------------------------------------------------------------------------------

.END
