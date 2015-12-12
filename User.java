public class User{

	static int userNum;
	private int id;
	private String name;
	private String password;

	public User(int id, String name, String password){
		this.id = id;
		this.name = name;
		this.password = password;	
	}

	public void printUserInfo(){
		System.out.format("%d name: %s, password: %s%n", this.id, this.name, this.password);
	}

	public String getName(){
		return this.name;
	}

	public String getPassword(){
		return this.password;
	}

	public int getId(){
		return this.id;
	}




}
