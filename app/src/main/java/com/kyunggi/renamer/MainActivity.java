package com.kyunggi.renamer;

import android.app.*;
import android.content.*;
import android.os.*;
import android.util.*;
import android.view.*;
import android.widget.*;
import java.io.*;
import java.util.*;

public class MainActivity extends Activity 
{
    Button btFindDir;
    Button btStart;
    EditText etDir;
    private static final int REQUEST_SELECT_FILE = 12345678;

    private File targetDir;

    private static final String TAG="Renamer";

    public void Start()
    {
        // TODO: Implement this method
        StartRenaming2(targetDir);
        Log.v(TAG,"done");
        Toast.makeText(this,"done",3).show();
        return;
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        btFindDir=(Button) findViewById(R.id.btFindDir);
        btStart=(Button) findViewById(R.id.btStart);
        etDir=(EditText) findViewById(R.id.etPath);
        btFindDir.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View p1)
                {
                    // TODO: Implement this method
                    Intent i=new Intent(MainActivity.this, FileSelectorActivity.class);
                    startActivityForResult(i, REQUEST_SELECT_FILE);		
                    return;
                }      
        });
        btStart.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View p1)
                {
                    if( targetDir!=null)
                    {
                        if(targetDir.isDirectory())
                        {
                            MainActivity.this.Start();
                            return;
                        }
                    }
                    Toast.makeText(MainActivity.this,"Please select a folder first.",3).show();
                    return;
                }

                
            });
            rules=new Rules();
    }
	ArrayList<File> files=new ArrayList<File>();
	public void AddFiles(File dir)
	{
		if(!dir.isDirectory())
		{
			if(dir.isFile())
			{
				files.add(dir);
			}
			return;
		}
		File[] found=dir.listFiles();
		for(File f:found)
		{
			if(f.isDirectory())
			{
				AddFiles(f);
			}
			else if(f.isFile())
			{
				files.add(f);
			}
		}
	}
	public void StartRenaming()
	{
		for(File f:files)
		{
			Rule rule=rules.getRule(f);
			if(rule!=null)
			{
				try
                {
                    rule.execute(f);
                }
                catch (IOException e)
                {
                    Toast.makeText(this,Log.getStackTraceString(e),5);
                }
			}
		}
	}
	public void StartRenaming2(File root)
	{
        Log.v(TAG,"SR2 root="+root.getPath());
		if(!root.isDirectory())
		{
            Log.e(TAG,"root isnt dir");
			if(root.isFile())
			{
                Log.v(TAG,"Root is file");
                try{
				    rules.getRule(root).execute(root);
                } catch(NullPointerException|IOException e)
                {
                    Log.e(TAG,"",e);
                    Toast.makeText(this,Log.getStackTraceString(e),5).show();
                }
			}
			return;
		}
        Log.v(TAG,"listfile");
		File[] found=root.listFiles();
		ArrayList<File> todo=new ArrayList<File>();
        Log.v(TAG,"found"+Arrays.toString(found));
		for(File f:found)
		{
            
            Log.v(TAG,"f "+f.getPath());
			if(f.isDirectory())
			{
                Log.v(TAG,"IS DIR");  
				StartRenaming2(f);
			}
			else// if(f.isFile())
			{
                Log.v(TAG,"isnt dir");
				todo.add(f);
			}
		}
		for(File f:todo)
		{
            try{
                Log.v(TAG,"todo "+f.getPath());
			    rules.getRule(f).execute(f);
            }catch(NullPointerException|IOException e)
            {
                Log.e(TAG,"",e);
                Toast.makeText(this,Log.getStackTraceString(e),5).show();
            }
		}
	}
	Rules rules;
    
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == REQUEST_SELECT_FILE)
        {
            if (resultCode == Activity.RESULT_OK)
            {
                String path=data.getStringExtra("com.kyunggi.renamer.path");
                File file=new File(path);
                file=file.getParentFile();
                etDir.setText(file.getAbsolutePath());
                targetDir=file;
            }
        }
	}
}
