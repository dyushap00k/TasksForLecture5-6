package com.task2;

public class BuildException extends Throwable{
   public BuildException(Throwable cause){
      System.err.println("there is no necessary condition for constructing a new object");
      cause.getCause();
   }
}
