public class Client{
    private String hostName;
    private int port;
    private String userName;


    public Client(){

    }

    public Client(String hostName, int port){
        this.hostName = hostName;
        this.port = port;
    }
    
    //setter for userName
    public void setUserName(String userName){
        this.userName = userName;
    }
    // getter for userName
    public String getUserName(){
        return this.userName;
    }
    
    //setter for port
    public void setPort(int port) {
    	this.port = port;
    }
    
    //getter for port
    public int getPort() {
    	return this.port;
    }
    
    //setter for hostName
    public void setHostName(String hostName) {
    	this.hostName = hostName;
    }
    
    //getter for host name
    public String getHostName(){
    	return this.hostName;
    }
}