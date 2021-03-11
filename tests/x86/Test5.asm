INCLUDE SmallWin.inc


GetLenOfStr     PROTO, the_str:PTR BYTE
MyReadString    PROTO, _str:PTR BYTE, _strLen:DWORD, _strLenMax:DWORD
MyWriteString   PROTO, _str:PTR BYTE, _strLen:DWORD
ChangeTextColor PROTO, _color:WORD
; --------------------------------------------------------
; Constants											    --
; --------------------------------------------------------
BUFFER   = 250
STRLEN   = 26
GRADELEN = 2
STUDENT_NUM = 20
WHITE  = 1111b
RED    = 0100b
YELLOW = 1110b
GREEN  = 0010b

; --------------------------------------------------------
; Structures                                            --
; --------------------------------------------------------
; STUDENT:                                              --
; Used to hold all the data of a student entered        --
; myName: holds the students name                       --
; myNameLen: holds the length of a students name        --
; Grade: holds the grade of the sudent                  --
; GradeColor: holds the color associated with the grade --
; --------------------------------------------------------
STUDENT STRUCT
 myNameLen  DWORD STRLEN
 GradeColor WORD  WHITE
 myName     BYTE  STRLEN DUP  (00h)
 Grade      BYTE  GRADELEN DUP(00h)
STUDENT ENDS

; --------------------------------------------------------
; MACROS                                                --
; --------------------------------------------------------

mGetConsoleHandles MACRO
 pushad
 INVOKE GetStdHandle, STD_INPUT_HANDLE
 mov consoleInHandle, eax
 INVOKE GetStdHandle, STD_OUTPUT_HANDLE
 mov consoleOutHandle, eax
 popad
ENDM


mSetConsoleTitle MACRO
LOCAL titleStr
.data
titleStr BYTE "LAB #4", 0
.code
 pushad
 INVOKE SetConsoleTitle, ADDR titleStr
 popad
ENDM


; Write Text To Screen
; --------------------------------------------------------
mWriteText MACRO text :=<' '>
LOCAL str, strSize
.data
str BYTE text, 0
strSize DWORD  0
.code
 ;Get Length of str
 pushad
 INVOKE GetLenOfStr, ADDR str ;, strSize
 mov strSize, ebx
 popad
 ;Write to screen
 mWriteString str, strSize
ENDM


mWriteTextNewLine MACRO text :=<' '>
LOCAL str, strSize
.data
str BYTE text, 0, 0Dh, 0Ah
strSize DWORD 0
.code
 ; Get Length of str
 pushad
 INVOKE GetLenOfStr, ADDR str
 mov strSize, ebx
 add strSize, 3
 popad
 ; Write to screen
 mWriteString str, strSize
ENDM


; Output String to screen
; --------------------------------------------------------
mWriteString MACRO str:REQ, len:REQ
 pushad
 lea edi, str
 mov ebx, len
 call MyWriteString_MACRO
 popad
ENDM

mWriteString_newLine MACRO str:REQ, len:REQ
 mWriteString str, len
 mWriteText_newLine
ENDM



; ///////////////////////////////////////////////////////////////////
; //DATA Setup                                          
; ///////////////////////////////////////////////////////////////////
.DATA
;file name/path
;--- --- --- --- --- --- --- 
theFileName BYTE "C:\lab4.txt", 0  ;Make Sure to set file path
colorRED WORD RED
colorGREEN WORD GREEN


;Student Variables
;--- --- --- --- --- --- --- 
students STUDENT STUDENT_NUM DUP(<>)
NumOfStudents BYTE 00h, 00h
KeepStudentESI   DWORD 0
saveSTUesi       EQU <mov KeepStudentESI, esi>
restoreSTUesi    EQU <mov esi, KeepStudentESI>

;UI Variables
;--- --- --- --- --- --- --- 
SaveScreenAndTextColor WORD 0
quitInput BYTE 0
ScreenBuffer CONSOLE_SCREEN_BUFFER_INFO <>

;Input & Output Variables
;--- --- --- --- --- --- --- 
bytesROW DWORD 0 ; number of bytes read or wrote
nameMAX  DWORD STRLEN
charMAX  DWORD GRADELEN
singleChar DWORD 1

;Console Handles
;--- --- --- --- --- --- --- 
consoleInHandle  DWORD 0
consoleOutHandle DWORD 0
fileHandle       DWORD 0

;Clear Scrren Variables
;--- --- --- --- --- --- --- 
screenEmpty BYTE BUFFER DUP(' ')
cursorLoc COORD <0, 0>

;Wait Msg Variables
;--- --- --- --- --- --- --- 
trash DWORD 0
junk BYTE STRLEN DUP (00h)

;The Input Buffer used when entering text
InputBuffer BYTE BUFFER DUP(00h)


; ///////////////////////////////////////////////////////////////////
; //Main of Porgram
; ///////////////////////////////////////////////////////////////////
.CODE
main PROC
; Display start up promts and such 
call startUps
; Accept inputs from user 
START:
	; Ask user for name
	; ---- ---- ---- ---- ---- ----
	call getName
	; Does use want to stop inputing?
	; ---- ---- ---- ---- ---- ----
	call quitInputCheck
	; Ask user for grade
	; ---- ---- ---- ---- ---- ----
	call getGrade
    ; Done inputing or is there 20 Students?
    ; ---- ---- ---- ---- ---- ----
	cmp quitInput, 1
	je DOIQUIT
	cmp NumOfStudents, STUDENT_NUM
	je DISPLAY
jmp START
; Quit Program or view entires and print to text doc
DOIQUIT:
 call makeDecision
 cmp quitInput, 1 
 jne YESQUIT
;Display names & grades
DISPLAY:
 call printNameandGrade
 call writeTofile
YESQUIT:
call WaitMsg
main ENDP
exit

;                                         Start Up Procedures
;+----------------------------------------------------------------------------------------------------------+
startUps PROC
 mGetConsoleHandles
 mSetConsoleTitle
 mWriteTextNewLine "Program started!"
 mWriteTextNewLine "Hello, this program will allow a user to enter a name "
 mWriteTextNewLine "and letter grade for a student. You can enter up to 20 students."
 mWriteTextNewLine "Students name and color for a grade entered respectively will be displayed."
 mWriteTextNewLine "All entered student names and grades will be saved in a .txt file when done."
 mWriteTextNewLine "You may quit the program and see what is entered at any time."
 mWriteTextNewLine "To do so, simply type !! when prompted for name then enter."
 call WaitMsg
ret
startUps ENDP
;+----------------------------------------------------------------------------------------------------------+

;                                         UI Procedures
;+----------------------------------------------------------------------------------------------------------+
getName PROC
 call ClearScreen
 mWriteTextNewLine "Please enter student's name..."
 mov eax, SIZEOF STUDENT
 mul NumOfStudents
 mov esi, eax
 saveSTUesi

 INVOKE MyReadString, ADDR (STUDENT PTR students[esi]).myName, ADDR (STUDENT PTR students[esi]).myNameLen, nameMAX
 inc NumOfStudents
ret
getName ENDP
;+----------------------------------------------------------------------------------------------------------+

; +---------------------------------------------------------------------------------------------------------- +
getGrade PROC
 cmp quitInput, 1
 jz SKIP
jumpWhile:
     call ClearScreen
	 ; Prompts for user to enter grade
	 mWriteText "Please enter "
	 restoreSTUesi
	 INVOKE MyWriteString, ADDR (STUDENT PTR students[esi]).myName, (STUDENT PTR students[esi]).myNameLen
	 mWriteTextNewLine "'s grade..."
	 mWriteTextNewLine "NOTE: Valid inputs are A,B,C,D,F (including lowercases)"
	 ;Collect  input
	 INVOKE MyReadString, ADDR (STUDENT PTR students[esi]).Grade, ADDR trash, charMAX
	 ;Checks input
	 restoreSTUesi
	 AND (STUDENT PTR students[esi]).Grade, 0DFh; force capitalization
	 call gradecheckInput
	 cmp quitInput, 0
	 jz jumpWhile
	 mov quitInput, 0 ;restore quitInput
 SKIP:
ret
getGrade ENDP
; +----------------------------------------------------------------------------------------------------------+

; +---------------------------------------------------------------------------------------------------------- +
gradecheckInput PROC
 restoreSTUesi
 cmp (STUDENT PTR students[esi]).Grade, 'A' 
 mov(STUDENT PTR students[esi]).GradeColor, WHITE
 jz vaildInput
 cmp (STUDENT PTR students[esi]).Grade, 'B'
 mov(STUDENT PTR students[esi]).GradeColor, WHITE
 jz vaildInput
 cmp (STUDENT PTR students[esi]).Grade, 'C'
 mov (STUDENT PTR students[esi]).GradeColor, WHITE
 jz vaildInput
 cmp (STUDENT PTR students[esi]).Grade, 'D'
 mov (STUDENT PTR students[esi]).GradeColor, YELLOW
 jz vaildInput
 cmp (STUDENT PTR students[esi]).Grade, 'F'
 mov (STUDENT PTR students[esi]).GradeColor, RED
 jz vaildInput
 jmp invalidInput
vaildInput:
 mov quitInput, 1  
invalidInput:
 ret
gradecheckInput ENDP
; +---------------------------------------------------------------------------------------------------------- +

; +---------------------------------------------------------------------------------------------------------- +
quitInputCheck PROC
 push eax
 restoreSTUesi
 mov al, (STUDENT PTR students[esi]).myName
 mov ah, (STUDENT PTR students[esi]).myName[1]
 cmp al, '!' 
 je L1
 jne nope
 L1:
 cmp ah, '!' 
 jne nope
 ;Clear Str and dec numberOfStudents
 mov ecx, STRLEN
 lea edi, (STUDENT PTR students[esi]).myName
 mov al, 0
 rep stosb 
 mov quitInput, 1  ;user wants to stop input 
 dec NumOfStudents
nope:
 pop eax
 ret
quitInputCheck ENDP
; +---------------------------------------------------------------------------------------------------------- +

; +---------------------------------------------------------------------------------------------------------- +
makeDecision PROC
jumpWhile:
    call ClearScreen
	mWriteTextNewLine "Do you want to view your inputs and write"
	mWriteTextNewLine "them to text doc before you exit? (Y/N)"

    INVOKE MyReadString, ADDR Junk, ADDR trash, charMAX
	AND junk, 0DFh
	cmp junk, 'Y'
	je YESPLZ
	cmp junk, 'N'
	je NOPLZ
jmp jumpWhile
YESPLZ:
 mov quitInput, 1
NOPLZ:
 ret
makeDecision ENDP
; +---------------------------------------------------------------------------------------------------------- +

; +---------------------------------------------------------------------------------------------------------- +
printNameandGrade PROC
 call ClearScreen
 call ColumnsSetup
 mov al, NumOfStudents
 movzx ecx, al
L1:
	push ecx
	call saveDefTextColor
	; row offset of 5
	;--- --- --- ---
	mWriteText "     "
	; Print student name
	; --- --- --- ---
	mov eax, SIZEOF STUDENT
	mul NumOfStudents[1]
	mov esi, eax
	saveSTUesi
	INVOKE MyWriteString, ADDR (STUDENT PTR students[esi]).myName, (STUDENT PTR students[esi]).myNameLen
	; Prtin letter grade
	; --- --- --- ---
	call spacer32
	restoreSTUesi
	INVOKE ChangeTextColor, (STUDENT PTR students[esi]).GradeColor
	INVOKE MyWriteString, ADDR (STUDENT PTR students[esi]).Grade, singleChar
	mWriteTextNewLine
	call restoreDefTextColor
	inc NumOfStudents[1]
	pop ecx
	dec ecx
	cmp ecx, 0; UGH it won't let me use LOOP b/c says jump too far :(
	je PLZSTOP  

jmp L1
PLZSTOP:
 mWriteTextNewLine
ret
printNameandGrade ENDP

ColumnsSetup PROC
 mov ecx, 2 ; 3 columns down 
ColumnLoop:
	mWriteTextNewLine
LOOP ColumnLoop
ret
ColumnsSetup ENDP

spacer32 PROC
 pushad
 mov ecx, 27; ecx = 27 b/c 32 - 5(row offset)
 restoreSTUesi
 sub ecx, (STUDENT PTR students[esi]).myNameLen
col32Loop:
	mWriteText " "
LOOP col32Loop
 popad
ret
spacer32 ENDP

saveDefTextColor PROC
 pushad
 INVOKE GetConsoleScreenBufferInfo, consoleOutHandle, ADDR ScreenBuffer
 mov bx, ScreenBuffer.wAttributes
 mov SaveScreenAndTextColor, bx
 popad
ret
saveDefTextColor ENDP

restoreDefTextColor PROC
 pushad
 INVOKE SetConsoleTextAttribute, consoleOutHandle, SaveScreenAndTextColor
 popad
ret
restoreDefTextColor ENDP

ChangeTextColor PROC, _color:WORD
 pushad
 INVOKE SetConsoleTextAttribute, consoleOutHandle, _color
 popad
ret
ChangeTextColor ENDP
; +---------------------------------------------------------------------------------------------------------- +


;                          Make Text Buffer and Make/Write text doc Procedures
; +---------------------------------------------------------------------------------------------------------- +
writeTofile PROC
 call saveDefTextColor
 call OpenOrCreateFile

 ; Error Check For File
 cmp fileHandle, INVALID_HANDLE_VALUE
 jne fileGOOD
 INVOKE ChangeTextColor, colorRED
 mWriteTextNewLine "Error! Something went wrong with create file... :("
 jmp SKIP

fileGOOD:
 ; Write string to text file Then Close File
 call getInputBuffer
 call WriteToTextDoc
 INVOKE ChangeTextColor, colorGREEN
 mWriteTextNewLine "File creation successful!"

SKIP:
call restoreDefTextColor
ret
writeTofile ENDP

OpenOrCreateFile PROC
 pushad
 INVOKE CreateFile,
 ADDR theFileName,       ; File name
 GENERIC_WRITE,          ; Access mode
 DO_NOT_SHARE,           ; Share mode
 NULL,                   ; ptr to security
 CREATE_ALWAYS,          ; file creation options
 FILE_ATTRIBUTE_NORMAL,  ; file attributes
 0                       ; handle to template
 mov fileHandle, eax ;get file handle
 popad
ret
OpenOrCreateFile ENDP


WriteToTextDoc PROC
 pushad
 INVOKE WriteFile,
 fileHandle,          ; The consoleHandle
 ADDR InputBuffer,    ; string to wirte in file
 BUFFER,              ; buffer for string (amount of chars to write in)
 ADDR bytesROW,       ; num of bytes written
 0                    ; NULL for synchronous
 ;close the file!
 INVOKE CloseHandle, fileHandle
 popad
ret
WriteToTextDoc ENDP


getInputBuffer PROC
 pushad
 mov al, NumOfStudents
 movzx ecx, NumOfStudents
 mov ebx, 0
 mov NumOfStudents[1], 0
 mov edi, OFFSET InputBuffer

L1:
	push ecx
	mov eax, SIZEOF STUDENT
	mul NumOfStudents[1]
	lea esi, (STUDENT PTR students[eax]).myName
	mov ebx, eax
	mov ecx, (STUDENT PTR students[ebx]).myNameLen
	rep movsb
	mov al, ':'
	stosb
	mov al, (STUDENT PTR students[ebx]).Grade
	stosb
	mov al, 0Dh
	stosb
	mov al, 0Ah
	stosb
	inc NumOfStudents[1]
    pop ecx
LOOP L1
popad
ret
getInputBuffer ENDP
; +---------------------------------------------------------------------------------------------------------- +




;                                         Input & Output Procedures                            
;+----------------------------------------------------------------------------------------------------------+
MyReadString PROC, _str:PTR BYTE, _strLen:DWORD, _strLenMax:DWORD
 ;Read Text from screen
 call ReadIntoBuffer
 ;Copy input buffer to stirng
 pushad
 mov eax, _strLenMax
 dec eax
 sub BytesRow, 2
 cmp eax, BytesROW
 jb TRIM
 ;Copy len of BytesROW into string
 ;then set size to BytesROW 
 mov ecx, BytesRow
 mov eax, ecx
 mov edi, _strLen
 cld
 stosd
 mov esi, OFFSET InputBuffer
 mov edi, _str
 cld
 rep movsb
 jmp DONE

;Trim end of str
TRIM:
 mov esi, OFFSET InputBuffer
 mov edi, _str
 movzx ecx, al
 rep movsb

DONE: 
 popad
 ;Clear Input Buffer
 call ResetInputBuffer
ret
MyReadString ENDP


ReadIntoBuffer PROC
 pushad	
 INVOKE ReadConsole, consoleInHandle, ADDR InputBuffer, BUFFER, ADDR bytesROW, 0
 popad
ret
ReadIntoBuffer ENDP


ResetInputBuffer PROC
 pushad
 mov edi, OFFSET InputBuffer
 mov al, 00h
 mov ecx, BUFFER
 rep stosb
 popad
ret
ResetInputBuffer ENDP
;+----------------------------------------------------------------------------------------------------------+


;Used with mwriteText macros
;+----------------------------------------------------------------------------------------------------------+
GetLenOfStr PROC, the_str:PTR BYTE
 ;cmp checkSize, 0
 ;jne alreadyFound
 mov ebx, 0
 mov esi, the_str
L1:
	inc ebx
	inc esi
	mov al, [esi]
	cmp al, 00h
jnz L1
;alreadyFound:
ret
GetLenOfStr ENDP 
;+----------------------------------------------------------------------------------------------------------+


;+----------------------------------------------------------------------------------------------------------+
MyWriteString PROC, _str:PTR BYTE, _strLen:DWORD
 pushad
 INVOKE WriteConsole, consoleOutHandle, _str, _strLen, ADDR bytesROW, 0
 popad
ret
MyWriteString ENDP
;+----------------------------------------------------------------------------------------------------------+


;+----------------------------------------------------------------------------------------------------------+
MyWriteString_MACRO PROC
 pushad
 INVOKE WriteConsole, consoleOutHandle, ADDR [edi], ebx, ADDR bytesROW, 0
 popad
ret
MyWriteString_MACRO ENDP
;+----------------------------------------------------------------------------------------------------------+

;                                         Wait Message
;+----------------------------------------------------------------------------------------------------------+
WaitMsg PROC
 mWriteText "Press enter when ready to continue..."
 INVOKE MyReadString, ADDR junk, ADDR trash, nameMAX
 call ClearScreen
ret
WaitMsg ENDP
;+----------------------------------------------------------------------------------------------------------+


;                                         Clear Screen Procedures
;+----------------------------------------------------------------------------------------------------------+
ClearScreen PROC
 call SetCursorDefault
 call WriteBlankSpaces
 call SetCursorDefault
ret 
ClearScreen ENDP

WriteBlankSpaces PROC
push ecx
mov ecx, BUFFER
L1:
 push ecx
 mWriteString screenEmpty, BUFFER
 pop ecx
LOOP L1
 pop ecx
ret
WriteBlankSpaces ENDP

SetCursorDefault PROC
 pushad
 INVOKE SetConsoleCursorPosition, consoleOutHandle, cursorLoc
 popad
ret
SetCursorDefault ENDP
;+----------------------------------------------------------------------------------------------------------+

; ///////////////////////////////////////////////////////////////////
; //END Of Program
; ///////////////////////////////////////////////////////////////////
END main