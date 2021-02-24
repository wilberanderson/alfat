#ALFAT PowerShell start script. 
param(
  [string]$OpenFile
)
$AppPath = "D:\UMASS_499_01_Senior_Software_Eng_II\ALFATCode2\out\artifacts\ALFAT_jar\ALFAT.jar"
#Check if path to ALFAT exist
write-output "Attempting to start ALFAT..."
if (-not(Test-Path $AppPath)) {
  write-error "Path to ALFAT.jar not found..."
  write-output "Check if file path exist or include file "
  write-output "the path in AppPath of this script."
} else {
  if($OpenFile) {
    Start-Process -FilePath java.exe -ArgumentList "-jar $AppPath"
  } else {
    Start-Process -FilePath java.exe -ArgumentList "-jar $AppPath $OpenFile"
  }
}