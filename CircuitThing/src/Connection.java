import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

public class Connection {
	Node a,b,fromNode, toNode;
	double current=0;
	int dx, dy;
	float width, length, centerX, centerY, arrowRatio;
	public Connection(Node a, Node b, float width){
		this.a=a; this.b=b;
		addConnections();
		dx=b.getX()-a.getX();
		dy=b.getY()-a.getY();
		length=(float)Math.sqrt(dx*dx+dy*dy);
		arrowRatio = (10/length);
		centerX=(b.getX()+a.getX())/2;
		centerY=(a.getY()+b.getY())/2;
		this.width=width;
	}
	public void addConnections(){
		a.addConnection(this);
		b.addConnection(this);
	}
	public void drawSelected(Graphics2D g){
		g.setStroke(new BasicStroke(10));
		g.setColor(Color.GREEN);
		g.drawLine(a.getX(),a.getY(),b.getX(),b.getY());
	}
	public void drawInfo(Graphics2D g){
		g.setColor(Color.black);
		g.drawString("Current: "+String.format("%.5f", current), 20, 500);
	}
	public void drawCurrentDirection(Graphics2D g){
		if (Math.abs(current)< CircuitPanel.acceptableError) return;
		g.setColor(Color.RED);
		g.setStroke(new BasicStroke(10));
		Node back=a, front=b;
		if(current>0 && fromNode == b || current<0 && fromNode == a) {back=b;front=a;}
		int startX=(int) centerX, startY = (int) centerY;
		int ddx = (int) (2*(front.getX()-back.getX())*arrowRatio);
		int ddy = (int) (2*(front.getY()-back.getY())*arrowRatio);
		int[] xPoints = {startX+ddx*2,startX+ddx-ddy,startX+ddx+ddy};
		int[] yPoints = {startY+ddy*2,startY+ddy+ddx,startY+ddy-ddx};
		g.fillPolygon(xPoints, yPoints, 3);
		g.drawLine(startX, startY, startX+ddx, startY+ddy);
		
		
	}

	public float getLength(){return length;}
	public boolean clicked(Point p) {
        double m = p.getX();
        double n = p.getY();
        return (Math.abs((dy * m - dx * n - dy * centerX + dx * centerY) / length) <= width) && (Math.abs((dx * m + dy * n - dx * centerX - dy * centerY) / length) <= length / 2);
    }
	public Connection remove(){
		a.removeConn(this);
		b.removeConn(this);
		return this;
	}
	public void askValue(){
	}
	public Node otherEnd(Node n){
		return n==a? b : a;
	}
	public void setFromNode(Node n){
		fromNode=n;
		toNode = n==a? b : a;
		toNode.removeChild(this);
	}
	public void setupWrongDirection(Node endNode){
		toNode.setup(this, endNode);
	}
	public void flipCurrent(Node endNode){
		toNode.addChild(this);
		setFromNode(toNode);
	}
	public boolean terminates(Node endNode){
		return toNode.children()==0 && toNode!=endNode;
	}
	public double getAndUpdateVoltage(){
		return voltageDrop() +toNode.update();
	}
	public double voltageDrop(){
		return 0;
	}
	public void addAndPropogateCurrent(double c){
		current+=c;
		toNode.addAndPropogateCurrent(c);
	}
	public void resetCurrent(){current=0;};
}
