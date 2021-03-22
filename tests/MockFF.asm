      DISPLYMSG_0
FOOBAR
FOOBA2                                               
 OOBAR
 SOOBA
*This is a mock pseudo code language to demonstrate a
*Fixed field language compatibility in an extremely 
*strict fixed field format. 
*1234567890123456789012345678901234567890123456789012
*Count from zero to 100
      START .MAIN
      MOVE  0     EAX
 LAB00CALL  ADD1
      IF    EAX    <    100                    LAB00
      DISPLYMSG_0
      
      PRINT MSG_0
      END   .MAIN


 ADD1 STPROC
      ADD   EAX   1
      CALL  FOO
      ENPROC

 FOO  STPROC
* DO SOMETHING
      ENPROC

 MSG_0STR   "Program done"