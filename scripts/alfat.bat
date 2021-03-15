:: The path of the ALFAT jar file from a root
set AppPath=D:\UMASS_499_01_Senior_Software_Eng_II\ALFATCode2\out\artifacts\ALFAT_jar\ALFAT.jar
 
:: Get the absolute file path of the first argument
set FILE=%1

:: If the first argument is empty open the jar, if it is not open the file that was passed in
IF %1.==. (call java -jar %AppPath%) else (call java -jar %AppPath% %FILE%)