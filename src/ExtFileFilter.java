package Response;

import java.io.File;


class ExtFileFilter extends javax.swing.filechooser.FileFilter
{
	String _ext;
	String _description;
	
	ExtFileFilter(String ext, String descr)
	{
	    this._ext = ext;
	    _description = descr;
	}

    // this method check if the file has extention we need (_ext)
	public boolean accept(File f)
	{
	    if (f != null)
	    {
	    	if (f.isDirectory())
	    	{
	    		return true;
	    	}
	    	String extension = getExtension(f);
	    	if (extension == null )
	    		return (_ext.length() == 0);
	    	return _ext.equals(extension);
	    }
	    return false;
	}

    
    public String getExtension(File f)
    {
        if (f != null)
        {
            String filename = f.getName();
            int i = filename.lastIndexOf('.');
            if (i >  0 && i < filename.length() - 1)
            {
                return filename.substring(i + 1).toLowerCase();
            }
        }
        return null;
    }

    public String getDescription()
    {
        return _description;
    }
    
  

}
