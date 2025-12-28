mkdir -p classes
javac -d classes src/main/java/shiftreader/*.java
jar cfm shiftjavareader.jar Manifest.txt -C classes .
rm -rf classes