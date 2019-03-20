package Response;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

public class MainFrame extends JFrame
{
	MainFrame()
	{
		super();
		this.setTitle("HTTP Response Viewer");
		this.setSize(900, 680);
		//this.setResizable(false);
			
	}
	
	protected void processWindowEvent(WindowEvent e)
	{
	  super.processWindowEvent(e);
	  if (e.getID() == WindowEvent.WINDOW_CLOSING)
	  {
		  NodePacket.doFilePathConteinerClear();
		  System.exit(0);
	  }
	 
	}

	static public void doWindowCenter(JFrame frame)
	{
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = frame.getSize();
		if (frameSize.height > screenSize.height)
			frameSize.height = screenSize.height;
		if (frameSize.width > screenSize.width)
			frameSize.width = screenSize.width;
		frame.setLocation((screenSize.width - frameSize.width) / 2,
				(screenSize.height - frameSize.height) / 2);
	}

}