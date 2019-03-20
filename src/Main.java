package Response;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.jdesktop.jdic.browser.WebBrowser;

public class Main
{
	
	public static MainFrame _mainFrame = new MainFrame();
	public static ResponseTree _rTree = new ResponseTree();
	private JPanel _mainPanel;
	private JButton _buttonOpen;
	private JPanel _rightPanel;
	private JScrollPane _leftPanel;
	public static WebBrowser _webBrowser = new WebBrowser();
	
	Main()
	{
		init();
	}

	protected void init()
	{
        
		_mainPanel = new JPanel();
		_mainPanel.setBackground(new java.awt.Color(247, 238, 225));
		_mainPanel.setLayout(null);
		_mainFrame.add(_mainPanel);

		// init open file button
		_buttonOpen = new JButton();
		_buttonOpen.setLocation(0, 0);
		_buttonOpen.setSize(910, 20);
		_buttonOpen.setBackground(new java.awt.Color(231, 229, 237));
		_buttonOpen.setText("Open PCAP file");
		
		// add action listener on button for filechoser menu open
		_buttonOpen.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				OpenFileMenu openMenu = new OpenFileMenu();
			}
		});
		BorderLayout bL = new BorderLayout();
		_mainPanel.setLayout(bL);
		_mainPanel.add(_buttonOpen, bL.NORTH);
		_rightPanel = new JPanel();
		_rightPanel.setLocation(230, 25);
		_rightPanel.setSize(660, 620);
		_mainPanel.add(_rightPanel, bL.CENTER);
		_rightPanel.setLayout(new BorderLayout());
		_rightPanel.add(_webBrowser, BorderLayout.CENTER);
		_webBrowser.setContent("<html><body><h4>Welcome to HTTP" +
								"Response Viewer</h4></body></html>");
		
		_leftPanel = new JScrollPane(_rTree);
		_leftPanel.setBackground(Color.white);
		_leftPanel.setLocation(5, 25);
		_leftPanel.setSize(220, 620);
		_leftPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
		_mainPanel.add(_leftPanel, bL.WEST);
						
		// center the window
		MainFrame.doWindowCenter(_mainFrame);
		//_mainFrame.pack();
		_mainFrame.setVisible(true);
		
	}
	
	public static void main(String[] args) {
		Main Application = new Main();
	}
	
}
