package edu.utfpr.ct.util;

import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class IPFinder
{
	//From: http://www.techrepublic.com/blog/data-center/mac-address-scorecard-for-common-virtual-machine-platforms/
	private final String[] virtualMachineMACCodes =
	{
		"00-50-56", "00-0C-29", "00-05-69", //VMware ESX 3, Server, Workstation, Player
		"00-03-FF", //Microsoft Hyper-V, Virtual Server, Virtual PC
		"00-1C-42", //Parallells Desktop, Workstation, Server, Virtuozzo
		"00-0F-4B", //Virtual Iron 4
		"00-16-3E", //Red Hat Xen
		"00-16-3E", //Oracle VM
		"00-16-3E", //XenSource
		"00-16-3E", //Novell Xen
		"08-00-27", "0A-00-27" //Sun xVM VirtualBox
	};

	public List<String> findIP()
	{
		List<NetworkInterface> networkInterfaces;
		List<InetAddress> addresses;
		List<String> validAddresses = new ArrayList<>();

		try
		{
			networkInterfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
			for(NetworkInterface networkInterface : networkInterfaces)
			{
				if(!networkInterface.isUp())
					continue;
				if(networkInterface.getHardwareAddress() == null)
					continue;
				if(partialStringMatch(getMAC(networkInterface.getHardwareAddress())))
					continue;

				addresses = Collections.list(networkInterface.getInetAddresses());
				for(InetAddress address : addresses)
					if(address.isSiteLocalAddress() && tryConnection(address.getHostAddress()))
						validAddresses.add(address.getHostAddress());
			}
		}
		catch(Exception e)
		{
			System.out.println("IPFinder::findIP: " + e.getMessage());
		}

		return validAddresses;
	}

	private boolean partialStringMatch(String s)
	{
		for(String virtualMachineMACCode : virtualMachineMACCodes)
			if(s.contains(virtualMachineMACCode))
				return true;

		return false;
	}

	private String getMAC(byte[] mac)
	{
		StringBuilder sb = new StringBuilder();

		for(int i = 0; i < mac.length; i++)
			sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));

		return sb.toString();
	}

	private boolean tryConnection(String adress)
	{
		URL url;
		HttpURLConnection connection;

		try
		{
			url = new URL("http://" + adress + ":8081");

			connection = (HttpURLConnection) url.openConnection();
			connection.connect();

			if(HttpURLConnection.HTTP_OK == connection.getResponseCode())
				return true;
			else
				return false;
		}
		catch(Exception e)
		{
			System.out.println("IPTest::tryConnection" + e.getMessage());
			return false;
		}
	}
}
