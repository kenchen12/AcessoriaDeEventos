all:
	@mkdir -p bin
	@javac -s src/ -d bin src/*/*.java
run:
	@java -cp ojdbc14.jar:bin/ assessoria.Main
