
public class Position {
	private int id;
	private int level;
	private String name;
	private boolean grant;
	private int minAge;
	
	public Position(int id, int level, String name, boolean grant, int minAge) {
		this.id = id;
		this.level = level;
		this.name = name;
		this.grant = grant;
		this.minAge = minAge;
	}
	
	public int id() {
		return id;
	}
	public int level() {
		return level;
	}
	public String name() {
		return name;
	}
	public boolean grant() {
		return grant;
	}
	public int minAge() {
		return minAge;
	}
	//debugging
	public String toString() {
		return "ID=" + id
				+ "\tLevel=" + level
				+ "\tName=" + name
				+ "\tGrant=" + grant
				+ "\tAge=" + minAge;
	}
}
