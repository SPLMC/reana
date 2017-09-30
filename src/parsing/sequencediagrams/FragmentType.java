package parsing.sequencediagrams;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import parsing.exceptions.UnsupportedFragmentTypeException;

public enum FragmentType {
	LOOP("loop"),
	ALTERNATIVE("alt"),
	OPTIONAL("opt"),
	PARALLEL("par");
	
	private String typeName;
	
	private FragmentType(String typeName) {
		this.typeName = typeName;
	}
	
	/**
	 * gets the Fragment Type from the typeName
	 * @param typeName
	 * @return
	 * @throws UnsupportedFragmentTypeException
	 */
	public static FragmentType getType(String typeName) throws UnsupportedFragmentTypeException {
		for (FragmentType fragmentType : FragmentType.values()) {
			if(fragmentType.typeName.equals(typeName))
			{
				return fragmentType;
			}
		}
		
		throw new UnsupportedFragmentTypeException("Fragment of type " + typeName + " is not supported!");
	}
}
