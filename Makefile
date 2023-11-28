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

compile-gui: $(DIST_DIR)/classes
	@$(JAVAC) -d $(DIST_DIR)/classes --module-path lib/javafx --add-modules javafx.controls -cp app/src/main/java app/src/main/java/de/userk/consys/gui/GuiApp.java


jar-tests: $(DIST_DIR)/jars compile-tests
	@$(JAR) --create --file $(DIST_DIR)jars/consys-test.jar -e de.userk.consys.AllTests -C $(DIST_DIR)classes .

jar-gui: $(DIST_DIR)/jars compile-gui
	@$(JAR) --create --file $(DIST_DIR)jars/consys-gui.jar -e de.userk.consys.gui.GuiApp -C $(DIST_DIR)classes . -C app/resources/main .


run-tests: jar-tests
	@$(JAVA) -jar $(DIST_DIR)/jars/consys-test.jar

run-gui: jar-gui
	@$(JAVA) --module-path lib/javafx --add-modules javafx.controls -jar $(DIST_DIR)/jars/consys-gui.jar

clean:
	@rm -r ./$(DIST_DIR)/