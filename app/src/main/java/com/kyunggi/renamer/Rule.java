package com.kyunggi.renamer;
import android.util.*;
import java.io.*;

public class Rule
{

    private String TAG="Renamer rule";
	public void execute(File f) throws IOException
	{
		// TODO: Implement this method
        Log.v(TAG,"EXECUTE ");
        String name=f.getCanonicalPath();
        Log.v(TAG,name);
        name+=".txt";
        Log.v(TAG,name);
        File t=new File(name);
        boolean succ=f.renameTo(t);
        Log.v(TAG,""+succ);
	}
}
