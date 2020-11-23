javac -d ../classes -cp . ../src/stream/TCPServerMultiThreaded.java ../src/stream/ClientThread.java
cd ../classes
java -cp . stream/TCPServerMultiThreaded 1200