package edu.utfpr.ct.report;

import edu.utfpr.ct.datamodel.Game;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

class BinaryReport extends AbstractReport
{
	public BinaryReport()
	{
		super(".bin");
	}
	
	@Override
	public boolean generateReport(Game game)
	{
		return writeFile(getFileName(game), serialization(game));
	}

	@Override
	public Game[] loadReport()
	{
		List<Game> games = new ArrayList<>();
		byte[] buffer;

		for(String list : listAllFiles())
		{
			buffer = readFile(list);
			games.add((Game) deserialization(buffer));
		}

		return games.toArray(new Game[0]);
	}
	
	private byte[] readFile(String fileName)
	{
		byte[] buffer;
		File file;
		FileInputStream fis;

		try
		{
			file = new File(fileName);
			buffer = new byte[(int) file.length()];
			fis = new FileInputStream(file);
			fis.read(buffer);
			fis.close();

			return buffer;
		}
		catch(FileNotFoundException e)
		{
			System.out.println("BinaryReport::readFile(String fileName): " + e.getMessage());
			return null;
		}
		catch(IOException e)
		{
			System.out.println("BinaryReport::readFile(String fileName): " + e.getMessage());
			return null;
		}
	}

	private boolean writeFile(String fileName, byte[] buffer)
	{
		FileOutputStream fos;

		try
		{
			fos = new FileOutputStream(createFile(fileName));
			fos.write(buffer);
			fos.close();

			return true;
		}
		catch(FileNotFoundException e)
		{
			System.out.println("BinaryReport::writeFile(String fileName, byte[] buffer): " + e.getMessage());
			return false;
		}
		catch(IOException e)
		{
			System.out.println("BinaryReport::writeFile(String fileName, byte[] buffer): " + e.getMessage());
			return false;
		}
	}

	private byte[] serialization(Serializable object)
	{
		ByteArrayOutputStream bos;
		ObjectOutputStream out;

		try
		{
			bos = new ByteArrayOutputStream();
			out = new ObjectOutputStream(bos);
			out.writeObject(object);
			out.close();

			return bos.toByteArray();
		}
		catch(Exception e)
		{
			System.out.println("BinaryReport::serialization(Serializable object): " + e.getMessage());
			return null;
		}
	}

	private Object deserialization(byte[] buffer)
	{
		ByteArrayInputStream bis;
		ObjectInputStream in;
		Object object;

		try
		{
			bis = new ByteArrayInputStream(buffer);
			in = new ObjectInputStream(bis);
			object = in.readObject();
			in.close();

			return object;
		}
		catch(IOException | ClassNotFoundException e)
		{
			System.out.println("BinaryReport::deserialization(byte[] buffer): " + e.getMessage());
			return null;
		}
	}
}
