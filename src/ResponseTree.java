package Response;

import java.net.URL;

import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

public class ResponseTree extends JTree
{
	public static DefaultMutableTreeNode _root = 
		new DefaultMutableTreeNode("Response");
	
	ResponseTree()
	{
		super(_root);
		this.addSelectionListener();
	 }
	
	private DefaultMutableTreeNode getLastSelectedNode()
	{
		DefaultMutableTreeNode node = 
			(DefaultMutableTreeNode)(this.getLastSelectedPathComponent());
		return node;
	}
		
	private void addSelectionListener()
	{
		// adding selection listener
		// when a packet is select, the data from it shown in the right text pane
		this.addTreeSelectionListener(new TreeSelectionListener()
		{
			public void valueChanged(TreeSelectionEvent e)
			{
				DefaultMutableTreeNode node = getLastSelectedNode();
				if (node == null)
					return;
				if (node.getLevel() == 4)
				{
					try
					{
						String str = new String(((NodePacket)
								(node.getUserObject())).getInfo().toString());
						String path = ((NodePacket)
								(node.getUserObject())).getFilePath();
						System.out.println(path);
						Main._webBrowser.setURL(new URL(path));
						
					}
					catch(Exception ee)
					{
						Main._webBrowser.setContent(ee.getMessage());
					}
									
				} 
				else
				{
					Main._webBrowser.setContent("");
				}
			}

		});
	}
	
	void doTreeClear()
	{
		 //delete all we need't from tree
		_root.removeAllChildren();
		this.repaint();
	}
	
	public DefaultMutableTreeNode getRoot()
	{
		return _root;
	}
}