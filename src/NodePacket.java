package Response;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

public class NodePacket 
{
	Integer _count;
	StringBuffer _info = new StringBuffer("");
	String _filePath;
	static ArrayList _filePathConteiner = new ArrayList();
	
	NodePacket (Integer Count)
	{
		_count = Count;
	}
	
	void addInfo(String str)
	{
		_info.append(str);
	}
	
	StringBuffer getInfo()
	{
		return _info;
	}
	
	public String toString()
	{
		return _count.toString();
	}
    
	public void setFilePath(String path)
	{
		_filePath = path;
	}

	public String getFilePath()
	{
		return _filePath;
	}
	
	
	public static void doFilePathConteinerClear()
	{
		Iterator itDel = _filePathConteiner.iterator();
		
		while (itDel.hasNext())
		{
			String filePath = new String((String)itDel.next());
			File f = new File(filePath);
			System.out.println("delete " + filePath);
			f.delete();
			itDel.remove();
		}
		
	}
}

