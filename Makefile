all:
	@javac -s src/ -d out/production/AcessoriaEventos src/*/*.java
run:
	@java -cp ojdbc14.jar:out/production/AcessoriaEventos/ acessoria.Main
