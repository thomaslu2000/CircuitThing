import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

import javax.swing.JOptionPane;

public class Battery extends Connection{
	double voltage;
	int[][] lines= new int[2][4];
	double mainCurrent = 1;
	
	public Battery(Node a, Node b){
		this(a,b,10);
	}
	public Battery(Node a, Node b, float v){
		super(a,b,30);
		voltage=v;
		int ax = (int) (width*dy/length);
		int ay = (int) (width*dx/length);
		int[] botLine={a.getX()-ax,a.getY()+ay,a.getX()+ax,a.getY()-ay};
		lines[0]=botLine;
		ax*=2;ay*=2;
		int[] topLine={b.getX()-ax,b.getY()+ay,b.getX()+ax,b.getY()-ay};
		lines[1]=topLine;
		
	}
	public void draw(Graphics2D g){
		g.setColor(Color.ORANGE);
		g.setStroke(new BasicStroke(5));
		for(int[] l : lines) g.drawLine(l[0], l[1], l[2], l[3]);
	}
	public void drawSelected(Graphics2D g){
		g.setColor(Color.GREEN);
		g.setStroke(new BasicStroke(10));
		for(int[] l : lines) g.drawLine(l[0], l[1], l[2], l[3]);
	}
	public void drawInfo(Graphics2D g){
		super.drawInfo(g);
		g.drawString("Voltage: "+voltage, 20, 520);
		g.drawString("Set Voltage", 20, 550);		
	}
	public void setVoltage(double v){voltage=v;}
	public double voltageDrop(){
		return fromNode==a? -voltage : voltage;
	}
	public void propogateCurrent(){
		b.addAndPropogateCurrent(mainCurrent);
	}
	public void runCurrent(){
		propogateCurrent();
		double endVolt = b.update();
		if(endVolt == 0){
			mainCurrent = Double.POSITIVE_INFINITY;
			propogateCurrent();
			CircuitPanel.updateError(0);
		}
		else mainCurrent += CircuitPanel.updateError(voltage - endVolt)/1000;
	}
	
	public void initialSetup(){
		b.setup(this, a);
		mainCurrent = 1;
	}
	public void finishCurrent() {
		current=mainCurrent;
	}
	public void askValue(){
		try{
			setVoltage(Double.parseDouble(JOptionPane.showInputDialog("Input Voltage")));
		} catch(Exception e){
			setVoltage(10);
		}
	}
	public boolean dead(){return b.children()==0;}
	
}
