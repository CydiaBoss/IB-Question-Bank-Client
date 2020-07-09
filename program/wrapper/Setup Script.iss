; Script generated by the Inno Setup Script Wizard.
; SEE THE DOCUMENTATION FOR DETAILS ON CREATING INNO SETUP SCRIPT FILES!

#define MyAppName "IBQB"
#define MyAppVersion "1.0.4.9"
#define MyAppPublisher "Andrew Wang"
#define MyAppURL "https://github.com/CydiaBoss"
#define MyAppExeName "IBQB-Proto.exe"

[Setup]
; NOTE: The value of AppId uniquely identifies this application. Do not use the same AppId value in installers for other applications.
; (To generate a new GUID, click Tools | Generate GUID inside the IDE.)
AppId={{8C43E904-4D2F-4F45-8397-FEF60F38A90A}
AppName={#MyAppName}
AppVersion={#MyAppVersion}
;AppVerName={#MyAppName} {#MyAppVersion}
AppPublisher={#MyAppPublisher}
AppPublisherURL={#MyAppURL}
AppSupportURL={#MyAppURL}
AppUpdatesURL={#MyAppURL}
DefaultDirName={commonpf64}\IB Question Bank
DisableProgramGroupPage=yes
; Uncomment the following line to run in non administrative install mode (install for current user only.)
;PrivilegesRequired=lowest
OutputDir=C:\Users\andre\Documents\Eclipse\IB\program
OutputBaseFilename=IBQB Setup
SetupIconFile=C:\Users\andre\Documents\Eclipse\IB\program\wrapper\IBRR.ico
Compression=lzma
SolidCompression=yes
WizardStyle=modern

[Languages]
Name: "English"; MessagesFile: "compiler:Default.isl"; InfoBeforeFile: "C:\Users\andre\Documents\Eclipse\IB\program\wrapper\infobefore-English.txt"
Name: "French"; MessagesFile: "compiler:Languages\French.isl"; InfoBeforeFile: "C:\Users\andre\Documents\Eclipse\IB\program\wrapper\infobefore-French.txt"

[Tasks]
Name: "desktopicon"; Description: "{cm:CreateDesktopIcon}"; GroupDescription: "{cm:AdditionalIcons}"; Flags: unchecked

[Files]
Source: "C:\Users\andre\Documents\Eclipse\IB\program\IBQB-Proto.exe"; DestDir: "{app}"; Flags: ignoreversion
Source: "..\jvm\*"; DestDir: "{app}\jvm\"; Flags: ignoreversion recursesubdirs createallsubdirs
; NOTE: Don't use "Flags: ignoreversion" on any shared system files

[Icons]
Name: "{autoprograms}\{#MyAppName}"; Filename: "{app}\{#MyAppExeName}"
Name: "{autodesktop}\{#MyAppName}"; Filename: "{app}\{#MyAppExeName}"; Tasks: desktopicon
Name: "{group}\{cm:UninstallProgram, {#MyAppName}}"; Filename: "{uninstallexe}"

[Run]
Filename: "{app}\{#MyAppExeName}"; Description: "{cm:LaunchProgram,{#StringChange(MyAppName, '&', '&&')}}"; Flags: nowait postinstall skipifsilent

[UninstallDelete]
Type: files; Name: "{app}\config.ibqb"
Type: files; Name: "{app}\history.ibqb"
Type: filesandordirs; Name: "{app}\offline"
