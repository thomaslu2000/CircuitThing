import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;

public class Node {
	static final int nodeSize=10;
	static final int selectedNodeSize=18;
	int x;
	int y;
	Rectangle hitbox;
	ArrayList<Connection> connections = new ArrayList<Connection>();
	ArrayList<Connection> children;
	ArrayList<Double> currentProportions;
	double current=0;
	
	public Node(int x, int y){
		this.x=x;
		this.y=y;
		hitbox = new Rectangle(x-selectedNodeSize/2, y-selectedNodeSize/2, selectedNodeSize, selectedNodeSize);
	}
	public void draw(Graphics g){
		g.setColor(Color.GRAY);
		g.fillOval(x-nodeSize/2, y-nodeSize/2, nodeSize, nodeSize);
	}
	public void drawSelected(Graphics g){
		g.setColor(Color.green);
		g.fillOval(x-selectedNodeSize/2, y-selectedNodeSize/2, selectedNodeSize, selectedNodeSize);
	}
	public boolean clicked(Point p){
		return hitbox.contains(p);
	}
	public void addConnection(Connection c){connections.add(c);}
	public void removeConn(Connection c){connections.remove(c);}
	public ArrayList<Connection> getConnections(){return connections;}
	public int getX(){return x;}
	public int getY(){return y;}
	
	public void setup(Connection r, Node endNode){
		if(this==endNode){
			children.clear();
			return;
		}
		children.remove(r);
		for(Connection a : children) a.setFromNode(this);
		for(int i = 0; i<children.size();i++){
			Connection a = children.get(i);
			a.setupWrongDirection(endNode);
			if(a.terminates(endNode)) a.flipCurrent(endNode);
		}
	}
	public void init(){
		children = (ArrayList<Connection>) connections.clone();
	}
	public void resetCurrentProportions(){
		currentProportions = new ArrayList<Double>();
		for(int i = 0; i<children(); i++) currentProportions.add(1.0/children());
	}
	public void addChild(Connection r){
		children.add(r);
	}
	public void removeChild(Connection r){
		children.remove(r);
	}
	public double update(){
		if(children()==0) return 0;
		double[] voltages = new double[children()];
		double sum =0.0;
		for(int i =0; i<children(); i++) {
			voltages[i]=children.get(i).getAndUpdateVoltage();
			sum+=voltages[i];
		}
		double ave = sum/children();
		for(int i =0; i<children(); i++) {
			currentProportions.set(i,currentProportions.get(i)+CircuitPanel.updateError(ave-voltages[i])/100);
		}
		return ave;
	}
	public void resetCurrent(){
		current=0;
	}
	public int children(){return children.size();}
	public void addAndPropogateCurrent(double c){
		current+=c;
		for(int i = 0; i<children(); i++){
			if(currentProportions.get(i)<CircuitPanel.acceptableError) return;
			children.get(i).addAndPropogateCurrent(c*currentProportions.get(i));
		
		}
	}
}
