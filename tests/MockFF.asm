*This is a mock pseudo code language to demonstrate 
*Fixed filed language compatibility in an extremely 
*sticked fixed filed format. 
*1234567890123456789012345678901234567890123456789012
*Count from zero to 100
      START .MAIN
      MOVE  0     EAX
 LAB00CALL  ADD1
      IF    EAX    <    100                    LAB00
      DISPLYMSG_0

      CALL  ADD1
      END   .MAIN

      
 ADD1 STPROC
      ADD   EAX   1
      CALL  FOO
      ENPROC

 FOO  STPROC
* DO SOMETHING
      ENPROC

 MSG_0STR   "Program done"