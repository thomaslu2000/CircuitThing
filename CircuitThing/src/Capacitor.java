import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

import javax.swing.JOptionPane;

public class Capacitor extends Battery{
	double capacitance = 1;
	double charge = 0;
	int[][] lines= new int[2][4];
	int bToA = 1;
	public Capacitor(Node a, Node b){
		super(a,b,30);
		int ax = (int) (width*dy/length);
		int ay = (int) (width*dx/length);
		int[] botLine={a.getX()-ax,a.getY()+ay,a.getX()+ax,a.getY()-ay};
		lines[0]=botLine;
		int[] topLine={b.getX()-ax,b.getY()+ay,b.getX()+ax,b.getY()-ay};
		lines[1]=topLine;
		voltage=0;
	}
	public void draw(Graphics2D g){
		g.setColor(Color.LIGHT_GRAY);
		g.setStroke(new BasicStroke(5));
		for(int[] l : lines) g.drawLine(l[0], l[1], l[2], l[3]);
	}
	public void drawSelected(Graphics2D g){
		g.setColor(Color.GREEN);
		g.setStroke(new BasicStroke(10));
		for(int[] l : lines) g.drawLine(l[0], l[1], l[2], l[3]);
	}
	public void drawInfo(Graphics2D g){
		g.setColor(Color.black);
		g.drawString("Current: "+String.format("%.5f", current), 20, 500);
		g.drawString("Capacitance: "+capacitance, 20, 520);
		g.drawString("Set Capacitance", 20, 550);	
		g.drawString("Charge: "+String.format("%.5f", charge), 20, 570);
		g.drawString("Voltage Drop: "+String.format("%.5f", voltageDrop()), 20, 590);
	}
	public void setFromNode(Node n){
		fromNode=n;
		toNode = n==a? b : a;
		bToA = n==a? 1 : -1;
		toNode.removeChild(this);
	}
	public void initialSetup(){
		b.setup(this, a);
		mainCurrent = 1;
	}
	public void setCapacitance(double v){capacitance=v;}
	public double voltageDrop(){
		return charge/capacitance;
	}
	public void askValue(){
		try{
			setCapacitance(Double.parseDouble(JOptionPane.showInputDialog("Input Capacitance")));
		} catch(Exception e){
			setCapacitance(1);
		}
	}
	public void updateCapacitor(double dt){
		charge+=current==Double.POSITIVE_INFINITY? 0.000001*dt : current*dt;
		voltage = -charge/capacitance;
	}
	public void resetCharge() {
		charge =0;
		voltage=0;
	}
}
