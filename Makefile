all:
	javac *.java
	java Server

run_gui:
	java ClientGUI

run_ser:
	java Server

run_ter:
	java Client

clear:
	rm -f *.class
	rm -f *.his
	rm -f new.jpg

clear_win:
	del *.class
	del *.his
	del new.jpg