package edu.utfpr.ct.report;

import edu.utfpr.ct.datamodel.Game;
import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

abstract class AbstractReport
{
	private final String defaultFolder;
	private final String separator;
	private final String extension;

	protected AbstractReport(String extension)
	{
		this.defaultFolder = "Report";
		this.separator = Paths.get(System.getProperty("user.dir")).getFileSystem().getSeparator();
		this.extension = extension;
	}

	public abstract boolean generateReport(Game game);

	public abstract Game[] loadReports();

	protected final File createFile(String fileName)
	{
		Path path;

		try
		{
			path = Paths.get(defaultFolder);
			if(!Files.exists(path))
				path = Files.createDirectory(path);

			fileName = path + separator + fileName;
			path = Paths.get(fileName);
			path = Files.createFile(path);

			return path.toFile();
		}
		catch(IOException e)
		{
			System.out.println("AbstractReport::createFile(String fileName): " + e.getMessage());
			return null;
		}
	}

	protected boolean deleteFile(String fileName)
	{
		Path path;

		try
		{
			fileName = defaultFolder + separator + fileName;
			path = Paths.get(fileName);

			return Files.deleteIfExists(path);
		}
		catch(IOException e)
		{
			System.out.println("AbstractReport::deleteFile(String fileName): " + e.getMessage());
			return false;
		}
	}

	protected final boolean deleteAllFiles()
	{
		Path path;

		try
		{
			for(String file : listAllFiles())
			{
				file = defaultFolder + separator + file;
				path = Paths.get(file);
				Files.deleteIfExists(path);
			}

			return true;
		}
		catch(IOException e)
		{
			System.out.println("AbstractReport::deleteAllFiles(): " + e.getMessage());
			return false;
		}
	}

	protected final String[] listAllFiles()
	{
		List<String> files = new ArrayList<>();
		Path path;
		DirectoryStream<Path> stream;

		try
		{
			path = Paths.get(defaultFolder);
			if(Files.exists(path))
			{
				stream = Files.newDirectoryStream(path, "*" + extension);
				for(Path entry : stream)
					files.add(path.toString() + separator + entry.getFileName().toString());
				stream.close();
			}
		}
		catch(IOException e)
		{
			System.out.println("AbstractReport::listAllFiles(): " + e.getMessage());
		}

		return files.toArray(new String[0]);
	}

	protected final String getFileName(Game game)
	{
		DateFormat dateFormat;
		String fileName;

		dateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
		fileName = dateFormat.format(new Date(game.timestamp));
		fileName = fileName + " - " + game.name + extension;

		return fileName;
	}
}
