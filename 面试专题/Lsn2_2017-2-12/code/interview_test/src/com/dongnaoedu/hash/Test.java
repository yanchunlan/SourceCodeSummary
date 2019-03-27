package com.dongnaoedu.hash;

public class Test {
	
	int hashCode = 99999;
	
	String name;

	public static void main(String[] args) {
		Integer a = 5;
		Integer b = 5;
		
		System.out.println(a == b);
	}
	
	@Override
	public int hashCode() {
		return hashCode;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Test other = (Test) obj;
		if (hashCode != other.hashCode)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	
	

}
