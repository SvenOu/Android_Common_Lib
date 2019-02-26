package com.tts.android.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {
	public static String getListFileAndFolderNames(File parentDir) {
	    String name="";
	    File[] files = parentDir.listFiles();
	    for (File file : files) {
	        if (file.isDirectory()) {
	        	name+="folder:"+file.getName()+"\n";
	        } else {
	        	name+="file:"+file.getName()+"\n";
	        }
	    }
	    return name;
	}
	public static List<String> getListFileNames(File parentDir) {
		List<String> names= new ArrayList<>();
	    File[] files = parentDir.listFiles();
	    for (File file : files) {
	        if (!file.isDirectory()) {
				names.add(file.getName());
	        }
	    }
	    return names;
	}

}
