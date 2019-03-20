package Response;


import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class OpenFileMenu extends JFrame
{
	OpenFileMenu()
	{
		super();
		this.setTitle("Open PCAP file");
		this.setSize(500, 360);
		JPanel jOpenPanel = new JPanel();
        JFileChooser chooser = new JFileChooser();
        jOpenPanel.setSize(500, 360);
        
        this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        
        // adding a file-filter for pcap files
        ExtFileFilter filter = new ExtFileFilter("pcap", "*.pcap — PCAP files");
        chooser.addChoosableFileFilter( filter );
        chooser.setAcceptAllFileFilterUsed( false );
        
        if( chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
        {
        	NodePacket.doFilePathConteinerClear();
        	startFileAnalysis(chooser.getSelectedFile());
        	//Main._webBrowser.setContent("");
	    
        }
       
        // Center the window
        MainFrame.doWindowCenter(this);
        
        jOpenPanel.add(chooser);
        this.add(jOpenPanel);
        this.setResizable(false);
             
	}
	
	private void startFileAnalysis(File f)
	{
		if (f.canRead())
        	try
        	{
        		Main._rTree.doTreeClear();
        		HTTPPacketArray._nodeCount = 0;
        		HTTPPacketArray httpPacketArray = new HTTPPacketArray(f.getAbsolutePath(), "tcp port 80");
        		httpPacketArray.doConstructTree(Main._rTree, "HTTP");
        		HTTPPacketArray pop3Array = new HTTPPacketArray(f.getAbsolutePath(), "tcp port 110");
        		pop3Array.doConstructTree(Main._rTree, "POP3");
        		HTTPPacketArray smtpArray = new HTTPPacketArray(f.getAbsolutePath(), "tcp port 25");
        		smtpArray.doConstructTree(Main._rTree, "SMTP");
        		Main._mainFrame.setTitle("HTTP Response Viewer - " + (String)f.getName());
        	}
        	catch(Exception e)
        	{
        		System.out.println(e.getMessage());
        	}
	}
	
}
