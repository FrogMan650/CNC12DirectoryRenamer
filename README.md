# CNC12DirectoryRenamer
JavaFX program to rename CNC12 directories based on type of directory.
This is self contained, so it should run on any Windows machine regardless of Java installation.
If the respective folder (cncm, cnct, etc.) is found in the C: drive, it will be renamed in this manor: directoryName_cnc12Version_boardType_softwareType_date_time.
softwareType is only included if it's a cncm directory but isn't mill.
Be sure no files from said directory are open otherwise it won't work.
