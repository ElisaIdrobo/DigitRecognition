A handwritten digit classifier created for an artificial intelligence course. Trains/tests a single layer
network consisting of 784 input nodes and 1 bias node with 10 output nodes corresponding to digits 0-9

Compile/Run Instructions:

javac *.java

java DigitRecognition <labelFile> <trainingFile> <testFile> [<networkFile>]

example:
java -cp src DigitRecognition train_images/labels.bin training_paths/training_10000.txt test_paths.txt
