echo on

cls

set LIB=..\..\lib

set DEP=..\dependency
set CP=%LIB%\acclient.jar;%LIB%\hessian-3.2.1.jar;%DEP%\log4j-core-2.0-beta9.jar;%DEP%\log4j-api-2.0-beta9.jar;%DEP%\log4j-1.2-api-2.0-beta9.jar;%DEP%\guava-15.0.jar;%DEP%\netty-all-4.0.12.Final.jar;%CLASSPATH%


java -classpath %CP% com.cssweb.network.NettyServer

