package TFIDF;

import java.io.File;
import java.io.FileFilter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyFileFilter implements FileFilter {
	String regexp;
	public MyFileFilter(String reg)
	{
		regexp = reg;
	}
    /**
     * ƥ���ļ�����
     */
    public boolean accept(File file) {
      try {
        Pattern pattern = Pattern.compile(regexp);
        Matcher match = pattern.matcher(file.getName());                
        return match.matches();
      } catch (Exception e) {
        return true;
      }
    }
  }