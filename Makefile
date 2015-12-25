all:
	javac *.java
	java Server

run_gui:
	java ClientGUI

run_server:
	java Server

run_client:
	java Client
