clear;
javac -encoding utf8 -cp 'src;test;test/junit-4.13.jar;test/hamcrest-core-1.3.jar' test/DoubleSortingTest.java;
java -cp 'src;test;test/junit-4.13.jar;test/hamcrest-core-1.3.jar' org.junit.runner.JUnitCore DoubleSortingTest;