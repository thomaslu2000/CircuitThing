import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

import javax.swing.JOptionPane;

public class Resistor extends Connection{
	double resistance;
	int zigs;
	public Resistor(Node a, Node b){this(a,b,1);}
	public Resistor(Node a, Node b, float r){
		super(a,b,10);
		resistance=r;
		zigs=(int) (length/30);
	}
	
	public void setResistance(double r){resistance=r;}
	public double getResistance(){return resistance;}
	public void draw(Graphics2D g){
		g.setStroke(new BasicStroke(5));
		g.setColor(Color.BLUE);
	   // vector increment
	   double inv = 0.25 / (double) zigs;
	   double dx = (this.dx) * inv,
	          dy = (this.dy) * inv;

	   // perpendicular direction
	   double inv2 = width / Math.sqrt(dx * dx + dy * dy);
	   double px =  dy * inv2, 
	          py = -dx * inv2;

	   // loop
	   double x = a.getX(), y = a.getY();
	   for (int i = 0; i < zigs; i++)
	   {
	      g.drawLine((int) x                , (int) y                , 
	    		  (int) (x +       dx + px), (int) (y +       dy + py));
	      g.drawLine((int) (x +       dx + px), (int) (y +       dy + py),
	    		  (int) (x + 3.0 * dx - px), (int) (y + 3.0 * dy - py));
	      g.drawLine((int) (x + 3.0 * dx - px), (int) (y + 3.0 * dy - py),
	    		  (int) (x + 4.0 * dx     ), (int) (y + 4.0 * dy     ));
	      x += 4.0 * dx;
	      y += 4.0 * dy;
	   }
	}
	
	public void drawSelected(Graphics2D g){
		g.setStroke(new BasicStroke(10));
		g.setColor(Color.GREEN);
	   // vector increment
	   double inv = 0.25 / (double) zigs;
	   double dx = (this.dx) * inv,
	          dy = (this.dy) * inv;

	   // perpendicular direction
	   double inv2 = width / Math.sqrt(dx * dx + dy * dy);
	   double px =  dy * inv2, 
	          py = -dx * inv2;

	   // loop
	   double x = a.getX(), y = a.getY();
	   for (int i = 0; i < zigs; i++)
	   {
	      g.drawLine((int) x                , (int) y                , 
	    		  (int) (x +       dx + px), (int) (y +       dy + py));
	      g.drawLine((int) (x +       dx + px), (int) (y +       dy + py),
	    		  (int) (x + 3.0 * dx - px), (int) (y + 3.0 * dy - py));
	      g.drawLine((int) (x + 3.0 * dx - px), (int) (y + 3.0 * dy - py),
	    		  (int) (x + 4.0 * dx     ), (int) (y + 4.0 * dy     ));
	      x += 4.0 * dx;
	      y += 4.0 * dy;
	   }
	}
	public void drawInfo(Graphics2D g){
		super.drawInfo(g);
		g.drawString("Resistance: "+resistance, 20, 520);
		g.drawString("Set Resistance", 20, 550);		
	}
	public double voltageDrop(){
		return current*resistance;
	}
	public void askValue(){
		try{
			setResistance(Double.parseDouble(JOptionPane.showInputDialog("Input Resistance")));
		} catch(Exception e){
			setResistance(1);
		}
	}
}
