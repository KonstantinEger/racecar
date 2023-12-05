# Racecar

## Configuration

Das ANT Build-Skript unterstützt 3 Varianten. Jede Variante besteht aus 3
Schritten:

- `compile-*`
- `jar-*`
- `run-*`

Die Schritte sind abhängig von einander, d.h. `run-*` führt erst `compile-*` und
dann `jar-*` aus.

> ACHTUNG: Varianten mit GUI benötigen die javafx JARs & DLLs im Ordner
> `lib/javafx`, falls nicht in JDK vorhanden.

### GUI-GUI

Ausführen:

```sh
ant clean
# ant compile-gui-gui
# ant jar-gui-gui
ant run-gui-gui
```

Diese Variante startet die GUI, liest Sensorwerte aus den Slidern und zeigt die
Entscheidung anhand der Auto-Grafik an.

### SIL-GUI

Ausführen:

```sh
ant clean
# ant compile-sil-gui
# ant jar-sil-gui
ant run-sil-gui
```

Diese Variante nimmt die (aktuell 4) Testszenarien aus der Klasse
`src/test/java/de/userk/consys/SILTest.java` und spielt sie nacheinander in der
GUI ab. Die Slider zeigen dabei den aktuellen Sensorwert und die Grafik die
Entscheidung an. Es werden keine Assertions bezüglich des SILTests gemacht.

### SIL-Test

Ausführen:

```sh
ant clean
# ant compile-sil-test
# ant jar-sil-test
ant run-sil-test
```

Diese Variante führt die (aktuell 4) Testszenarien aus der Klasse
`src/test/java/de/userk/consys/SILTest.java` als Unit-Test parallel aus.
