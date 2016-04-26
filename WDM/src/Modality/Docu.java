package Modality;

import java.util.HashMap;
import java.util.regex.Pattern;

public class Docu {
	public double len = 0;
	public int wordnum = 0;
	public HashMap<String,Double> WordList = new HashMap<String,Double>();
	public int Text_ID = -1;
	public int Para_ID = -1;
	public void getID(String article_id)
	{
		Pattern pattern = Pattern.compile("_");
		String[] List = pattern.split(article_id);
		Text_ID = Integer.parseInt(List[0]);
		if (List.length>1)
		Para_ID = Integer.parseInt(List[1]);
	}
}

