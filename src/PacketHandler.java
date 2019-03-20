package Response;

import java.util.ArrayList;
import java.util.Iterator;

import net.sourceforge.jpcap.capture.PacketListener;
import net.sourceforge.jpcap.net.Packet;
import net.sourceforge.jpcap.net.TCPPacket;

public class PacketHandler implements PacketListener
{
	public static int       _packetNumber = 0;
	public static ArrayList _packetArray  = new ArrayList();
	
	// getting packets method 
	public void packetArrived(Packet packet)
    {
		try
        {	
        	// if the packet is tcp
			if (packet instanceof TCPPacket)
			{
				TCPPacket tcpPacket = (TCPPacket) packet;
				// adding a packet into the array
				_packetArray.add(tcpPacket);
				byte[] data1=tcpPacket.getTCPHeader();
				// increment packetNumber field
				_packetNumber++;
				String sData = new String(tcpPacket.getData());
			}
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
	
	// this method delete all packets from _packetArray
	public static void doPacketArrayClear()
	{
		
		Iterator itDel = _packetArray.iterator();
		
		while (itDel.hasNext())
		{
			itDel.next();
			itDel.remove();
		}
		
		_packetNumber = 0;
		
	}


}