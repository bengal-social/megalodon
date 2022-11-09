package org.joinmastodon.android.model;

import org.joinmastodon.android.api.RequiredField;
import org.parceler.Parcel;

import java.util.List;

@Parcel
public class Hashtag extends BaseModel{
	@RequiredField
	public String name;
	@RequiredField
	public String url;
	public boolean following;
	public List<History> history;

	@Override
	public String toString(){
		return "Hashtag{"+
				"name='"+name+'\''+
				", url='"+url+'\''+
				", following="+following+
				", history="+history+
				'}';
	}
}
