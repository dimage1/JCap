package Response;

import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import net.sourceforge.jpcap.capture.PacketCapture;
import net.sourceforge.jpcap.net.TCPPacket;

public class HTTPPacketArray
{
	// packet array
	public  TCPPacket[] _packetArr;
	// number of packets
	public int _packetsCount;
	public static int _nodeCount = 0;
	
	HTTPPacketArray(String path, String filter)
	{
		// creating new packet capture
		PacketCapture pcap = new PacketCapture();
		try
		{
			// open pcap file for analizing
			pcap.openOffline(path);
			// set filter to gett only HTTP packets
			pcap.setFilter(filter, true);
			// starting capture the packets
			pcap.addPacketListener(new PacketHandler());
			// packets until the end of file
			pcap.capture(-1);
			pcap.close();
		}
		catch (Exception e)
		{
			Main._webBrowser.setContent(e.getMessage());
		}
		
		Main._webBrowser.setContent("File analysis has successfully done");
		// getting packets into new array
		// String data;
		Iterator it = PacketHandler._packetArray.iterator();
		_packetArr = new TCPPacket[PacketHandler._packetNumber];
		_packetsCount = 0;
		
		while (it.hasNext())
		{
			TCPPacket pk = (TCPPacket)it.next();
			_packetArr[_packetsCount] = pk;
			_packetsCount++;
			
		}
		
		this.doSortPacketArray();
		PacketHandler.doPacketArrayClear();
	}
	
	// sorting array method
	// sort by 1) destinationIP
	//		   2) sourceIP
	//		   3) packetID
	private void doSortPacketArray()
	{
		boolean mustSort = false;
		if (_packetsCount > 0 )
		for (int i = 0; i < this.getPacketsCount() - 1; i++)
			for (int j = 0; j < this.getPacketsCount() - i - 1; j++)
			{

				TCPPacket locTCPPack;
				if (_packetArr[j].getDestinationAddressAsLong() == 
					_packetArr[j + 1].getDestinationAddressAsLong())
				{
										
					if (_packetArr[j].getSourceAddressAsLong() > 
					    _packetArr[j + 1].getSourceAddressAsLong())
					{
						mustSort = true;
					}
					else
						if ( _packetArr[j].getSourceAddressAsLong() ==
							_packetArr[j + 1].getSourceAddressAsLong())
						{	
							if (_packetArr[j].getAcknowledgementNumber() > _packetArr[j + 1].getAcknowledgementNumber())
								mustSort = true;
							//if (_packetArr[j].getId() > _packetArr[j + 1].getId())
								//mustSort = true;
						}
				
				
				} 
				else 
					if (_packetArr[j].getDestinationAddressAsLong() > 
					    _packetArr[j + 1].getDestinationAddressAsLong())
						{  
							mustSort = true;
						}

				if (mustSort)
				{
					locTCPPack = _packetArr[j];
					_packetArr[j] = _packetArr[j + 1];
					_packetArr[j + 1] = locTCPPack;
					mustSort = false;
				}

			}
	}
	
	public TCPPacket getPacket(int i)
	{
		return _packetArr[i];
	}
	
	public int getPacketsCount()
	{
		return _packetsCount;
	}
	
	public void doConstructTree(ResponseTree tree, String firstBranchName)
	{
		DefaultMutableTreeNode root = tree.getRoot();
		int k = this.getPacketsCount();
		DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
		DefaultMutableTreeNode firstBr = 
			new DefaultMutableTreeNode(firstBranchName);
		root.add(firstBr);
		if (k > 0)
		{
		
			// adding new nodes
			NodePacket  node1;
			DefaultMutableTreeNode destinationNode;
			DefaultMutableTreeNode sourceNode;
			DefaultMutableTreeNode packetNode; 
			destinationNode = new DefaultMutableTreeNode("From "
					  + "[" + this.getPacket(0).getDestinationAddress()
					  + "]");
			sourceNode = new DefaultMutableTreeNode("To " + "["
					  + this.getPacket(0).getSourceAddress() + "]");
			
			Integer packetCount = 1;
			Integer i = 0;
			int t = 0;
			Integer packetNumberlastDest = 0;
			Integer packetNumberlastSource = 0;
			// checking all packets
			// adding into the tree if include text/html for HTTP and text/plain for mail data
			// if packet has two or more fragments, adding them all
			while (i < k)
			{
				StringBuffer allData = new StringBuffer();
				String data = new String(this.getPacket(i).getTCPData());
				if(firstBranchName == "HTTP")
					t = data.indexOf("Content-Type: text/html");
				else
					t = data.indexOf("Content-Type: text/plain");
				 
				if (t >= 0)
				{	
					t = 0;
					if(firstBranchName == "HTTP")
						t = data.toLowerCase().indexOf("<html");
					if(t >= 0)
					{
						_nodeCount++;
						packetCount++;
						
							// adding source and destination branches if needed
							if (this.getPacket(i).getSourceAddressAsLong() != 
								this.getPacket(packetNumberlastSource).getSourceAddressAsLong() 
								|| packetNumberlastSource == 0)
							{
								packetNumberlastSource = i;
								if (this.getPacket(i).getDestinationAddressAsLong() != 
									this.getPacket(packetNumberlastDest).getDestinationAddressAsLong()
									|| packetNumberlastDest == 0)
								{
									packetNumberlastDest = i;
									destinationNode = new DefaultMutableTreeNode("To "
											+ "[" + this.getPacket(i).getDestinationAddress()
											+ "]");
									firstBr.add(destinationNode);
								}
								sourceNode = new DefaultMutableTreeNode("From " + "["
										     + this.getPacket(i).getSourceAddress() + "]");
								destinationNode.add(sourceNode);
								packetCount = 1;
						    }
					
						node1 = new NodePacket(packetCount);
						DefaultMutableTreeNode treeNode = new DefaultMutableTreeNode(node1);
						//node1.addInfo(data);
						allData.append(data.substring(t));
						node1.addInfo(data.substring(t));
						
						boolean isEnd=false;
						long ack = this.getPacket(i).getAcknowledgmentNumber();
						
						sourceNode.add(treeNode);
						i++;	
						while ( i < k-1 && !isEnd)
						{
							String data1 = new String(this.getPacket(i).getData());
							long ack1 = this.getPacket(i).getAcknowledgmentNumber();
							if (ack1 != ack)
								isEnd = true;
							else
							{						
								//System.out.println(id + " ");
								data = new String(this.getPacket(i).getTCPData());
								i++;
								allData.append(data);
								node1.addInfo(data);
							}
						}
						
						if (firstBranchName != "HTTP")
						{
							int htmlIndex = allData.toString().toLowerCase().indexOf("<html");
							String beforeHtml;
							StringBuffer afterHtml;
							if (htmlIndex >=0 )
							{
								beforeHtml = allData.substring(0, htmlIndex);
								afterHtml = new StringBuffer(allData.substring(htmlIndex));
							}
							else
							{
								beforeHtml = allData.toString();
								afterHtml = new StringBuffer ("");
							}
							StringBuffer formatedMailMessage = new StringBuffer("");
							int nextStringIndex = beforeHtml.indexOf("\n");
							while (nextStringIndex >= 0)
							{
								//if (nextStringIndex > 0)
								   formatedMailMessage.append(beforeHtml.
										   substring(0, nextStringIndex ));
								
								formatedMailMessage.append("<br>");
								
								if (nextStringIndex < beforeHtml.length() - 1)
									beforeHtml = beforeHtml.substring(nextStringIndex + 1);
								else
									beforeHtml = "";
								nextStringIndex = beforeHtml.indexOf("\n");
									
								
							}
							//allData.delete(0, allData.length() - 1);
							allData = formatedMailMessage.append(afterHtml);
						
						}
						
						String filePath = "C:\\htmlLoc" + _nodeCount + ".html";
						FileOutputStream fo;
						try 
						{
							fo = new FileOutputStream(filePath);
							DataOutputStream dos = new DataOutputStream(fo);
							try
							{
								dos.writeUTF(allData.toString());
								NodePacket._filePathConteiner.add(filePath);
								filePath = "file://" + filePath;
								node1.setFilePath(filePath);
							}
							catch (IOException e)
							{
								e.printStackTrace();
							}
						
						}
						catch (FileNotFoundException e)
						{
							e.printStackTrace();
						}
											
					}
					else
					{
						long ack = this.getPacket(i).getAcknowledgmentNumber();
						i++;
						while(ack == this.getPacket(i).getAcknowledgmentNumber())
							i++;
					}
				}
				else
					i++;
			}
			
		}
		//repaint the tree
		tree.repaint();
		model.reload();
		tree.setVisible(true);
	}
}
