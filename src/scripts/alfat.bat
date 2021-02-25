Rem The path of the ALFAT jar file from a root
set AppPath=C:\Path\To\Jar\ALFAT.jar
 
Rem Get the absolute file path of the first argument
set "FILE=%cd%\%1"

Rem If the first argument is empty open the jar, if it is not open the file that was passed in
IF "%1"=="" (call java -jar %AppPath%) else (call %JavaLocation% -jar %AppPath% %FILE%) 