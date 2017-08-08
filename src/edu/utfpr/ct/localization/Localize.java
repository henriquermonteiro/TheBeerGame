package edu.utfpr.ct.localization;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Localize
{
	private static Map<String, Localize> localizes;

	private final String language;
	private HashMap<String, String> stringMapping;

	private Localize(String language)
	{
		this.language = language;
		changeLanguage(language);
	}

	public static Localize getInstance()
	{
		return getInstance(Locale.getDefault().getLanguage());
	}

	public static Localize getInstance(String language)
	{
		if(localizes == null)
			localizes = new HashMap<>();

		if(!localizes.containsKey(language))
			localizes.put(language, new Localize(language));

		return localizes.get(language);
	}

	public String getTextFor(String keyString)
	{
		String ret = stringMapping.get(keyString);

		if(ret == null)
		{
			ret = "Error when tried to grab String, check Localization package or language definition file.";
		}

		return ret;
	}

	public static String getTextForKey(String keyString)
	{
		return getInstance().getTextFor(keyString);
	}

	public final void changeLanguage(String language)
	{
		File f = new File("lang" + File.separator + language + ".map");

		if(!f.exists())
			f = new File("lang" + File.separator + "default.map");

		stringMapping = new HashMap<>();
		FileInputStream fis = null;

		try
		{
			fis = new FileInputStream(f);
			BufferedReader reader = new BufferedReader(new InputStreamReader(fis));

			String s;
			while((s = reader.readLine()) != null)
			{
				String[] ss = s.split("<>");

				if(ss.length == 2)
				{
					stringMapping.put(ss[0], ss[1]);
				}
			}

		}
		catch(FileNotFoundException ex)
		{
			ex.printStackTrace();
		}
		catch(IOException ex)
		{
			ex.printStackTrace();
		}
		finally
		{
			if(fis != null)
			{
				try
				{
					fis.close();
				}
				catch(IOException ex)
				{
				}
			}
		}
	}
}
