cd ../
CDIR=$PWD
JDIR=$CDIR/src/main/java
JPRG=src.main.java.
JBACK=$PWD/scripts
cd $JDIR

jar cmf manifest.txt hw2.jar *.class *.jpg *.png
cd $JBACK