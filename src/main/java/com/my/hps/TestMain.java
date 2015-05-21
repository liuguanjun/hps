package com.my.hps;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TestMain {

	public static void main(String[] args) throws Exception{
	    //System.out.println();
	    Path newFile = Paths.get("new.txt");
	    Files.createFile(newFile.toAbsolutePath());
	    //Files.createFile(newFile);
	    System.out.println(newFile);
	    //System.out.println(newFile.toAbsolutePath());
	}

}
