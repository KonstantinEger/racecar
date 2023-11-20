JAVAC := javac
JAR := jar
JAVA := java
DIST_DIR := ./target/

$(DIST_DIR):
	@mkdir $@

$(DIST_DIR)/classes: $(DIST_DIR)
	@mkdir $@

$(DIST_DIR)/jars: $(DIST_DIR)
	@mkdir $@

compile-tests: $(DIST_DIR)/classes
	@$(JAVAC) -d $(DIST_DIR)/classes -cp app/src/main/java:app/src/test/java:testutils/src/main/java app/src/test/java/de/userk/consys/AllTests.java

jar-tests: $(DIST_DIR)/jars compile-tests
	@$(JAR) --create --file $(DIST_DIR)jars/consys-test.jar -e de.userk.consys.AllTests -C $(DIST_DIR)classes .

run-tests: jar-tests
	@$(JAVA) -jar $(DIST_DIR)/jars/consys-test.jar

clean:
	@rm -r ./$(DIST_DIR)/