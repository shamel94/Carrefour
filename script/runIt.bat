@echo off
IF %1.==. GOTO No1
IF %2.==. GOTO No2

set input_dir=%1
set output_dir=%2

java -jar ../bin/phenix-challenge.jar %input_dir% %output_dir%

GOTO End1

:No1
  ECHO input directory missing
GOTO End1
:No2
  ECHO output directory missing
GOTO End1

:End1


