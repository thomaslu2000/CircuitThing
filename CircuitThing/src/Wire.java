import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

public class Wire extends Connection{
	public Wire(Node a, Node b){
		super(a,b,5);
	}
	public void draw(Graphics2D g2d){
		g2d.setStroke(new BasicStroke(5));
		g2d.setColor(Color.black);
		g2d.drawLine(a.getX(),a.getY(),b.getX(),b.getY());
	}
}
