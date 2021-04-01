*This is a mock pseudo code language to demonstrate a
*Fixed field language compatibility in an extremely 
*strict fixed field format. 
*1234567890123456789012345678901234567890123456789012
*Call a function to add up to some 100
      START .MAIN
      MOVE    0   EAX
      CALL  C100  
      END   .MAIN
 C100 STPROC
 CLOOPMOVE  0     count
      ADD     1   eax                   
      IF    count  <     100                   CLOOP 
      ENDIF
      ENPROC