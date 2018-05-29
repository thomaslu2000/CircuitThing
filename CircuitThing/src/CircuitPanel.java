import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

public class CircuitPanel extends JPanel implements MouseListener{
	final static int screen_X = 520;
	final static int screen_Y = 650;
	static double acceptableError = 0.000000004;
	ArrayList<Node> nodes = new ArrayList<Node>();
	ArrayList<Wire> wires = new ArrayList<Wire>();
	ArrayList<Battery> batteries = new ArrayList<Battery>();
	ArrayList<Resistor> resistors = new ArrayList<Resistor>();
	ArrayList<Capacitor> capacitors = new ArrayList<Capacitor>();
	Battery bb;
	double time = 0;
	double deltaTime = 0.005;
	Node node1=null;
	Connection selectedConnection=null;
	public enum Type {WIRE, BATTERY, RESISTOR, CAPACITOR}
	Type myType = Type.WIRE;
	static double error;
	boolean setup = false;
	boolean boxPainted=false;
	Rectangle box = new Rectangle(10, 535, 100, 20);
	
	
	JButton wireButton, batteryButton, resistorButton, currentButton, capacitorButton, playButton, resetButton;
	
	CircuitPanel(){
    	setBackground(Color.WHITE);
    	initButtons();
	}
	
	public void initButtons(){
		currentButton = new JButton();
		wireButton = new JButton();
    	batteryButton = new JButton();
    	resistorButton = new JButton();
    	capacitorButton = new JButton();
    	playButton = new JButton();
    	resetButton = new JButton();
    	
    	currentButton.setAction(new AbstractAction(){
    		public void actionPerformed(ActionEvent e){
    			if(batteries.size()>0) bb= batteries.get(0);
    			else if(capacitors.size()>0) bb= capacitors.get(0);
    			else return;
    			setUp();
    			if(bb.dead()){
    				JOptionPane.showMessageDialog(getParent(), "Circuit is incomplete");
    				return;}
    			setup=true;
    			findCurrents();
    		}
    	});
    	playButton.setAction(new AbstractAction(){
    		public void actionPerformed(ActionEvent e){
    			if(batteries.size()>0) bb= batteries.get(0);
    			else if(capacitors.size()>0) bb= capacitors.get(0);
    			else return;
    			setUp();
    			if(bb.dead()){JOptionPane.showMessageDialog(getParent(), "Circuit is incomplete");
    				return;}
    			setup=true;
    			playCap();		
    		}
    	});
    	wireButton.setAction(new AbstractAction(){
			@Override
			public void actionPerformed(ActionEvent e) {
				myType = Type.WIRE;
				System.out.println("Wires");
			}
    	});
    	batteryButton.setAction(new AbstractAction(){
			@Override
			public void actionPerformed(ActionEvent e) {
				myType = Type.BATTERY;
				System.out.println("Batteries");
			}
    	});
    	resistorButton.setAction(new AbstractAction(){
			@Override
			public void actionPerformed(ActionEvent e) {
				myType = Type.RESISTOR;
				System.out.println("Resistors");
			}
    	});
    	capacitorButton.setAction(new AbstractAction(){
			@Override
			public void actionPerformed(ActionEvent e) {
				myType = Type.CAPACITOR;
				System.out.println("Capacitors");
			}
    	});
    	resetButton.setAction(new AbstractAction() {
    		@Override
			public void actionPerformed(ActionEvent e) {
				resetCap();
				repaint();
			}
    	});
    	
    	currentButton.setText("Calculate Current");
    	wireButton.setText("Add Wires");
    	batteryButton.setText("Add Batteries");
    	resistorButton.setText("Add Resistors");
    	capacitorButton.setText("Add Capacitors");
    	playButton.setText("Play");
    	resetButton.setText("Reset Capacitors");
    	
    	add(wireButton);
    	add(batteryButton);
    	add(resistorButton);
    	add(capacitorButton);
    	add(currentButton);
    	add(playButton);
    	add(resetButton);
    	
	}
	
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(screen_X, screen_Y);
	}
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		boxPainted=false;
		if(node1!=null) node1.drawSelected(g);
		else if(selectedConnection !=null){
			selectedConnection.drawSelected(g2);
			//Draw Box
			g.setColor(Color.LIGHT_GRAY);
			if(!(selectedConnection instanceof Wire)){
				boxPainted = true;
				g.fillRect(10, 535, 100, 20);
			}
			selectedConnection.drawInfo(g2);
		}
		g.setColor(Color.black);
		g.drawString("Time: "+String.format("%.2f", time), 200, 70);
		for(Wire b : wires) b.draw(g2);
		for(Resistor r: resistors) r.draw(g2);
		for(Battery b: batteries) b.draw(g2);
		for(Capacitor c : capacitors) c.draw(g2);
		for(Node n : nodes) n.draw(g);
		if(selectedConnection!=null) selectedConnection.drawCurrentDirection(g2);
		int dx = 10;
		int dy = 0;
		
		
	}
	public void setUp(){
		resetCurrents();
		error=0;
		for(Node n : nodes) n.init();
		bb.initialSetup();
		for(Node n : nodes) n.resetCurrentProportions();
	}
	public void findCurrents(){
		error=1;
		for(int i = 0; error>acceptableError;i++){
			error=0;
			resetCurrents();
			bb.runCurrent();
		}
		bb.finishCurrent();
	}
	public void resetCurrents(){
		for(Node n : nodes) n.resetCurrent();
		for(Wire w : wires) w.resetCurrent();
		for(Resistor r : resistors) r.resetCurrent();
		for(Battery bat : batteries) bat.resetCurrent();
		for(Capacitor c : capacitors) c.resetCurrent();
	}
	public static double updateError(double d){
		error=Math.max(error, Math.abs(d));
		return d;
	}
	public void playCap(){
		Timer timer = new Timer(10, null);
		timer.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				for(Capacitor c : capacitors) c.updateCapacitor(deltaTime);
				findCurrents();
				time+=deltaTime;
				repaint();
				for(Capacitor c : capacitors) if (Math.abs(c.current)>acceptableError) return;
				timer.stop();
			}
		});
		timer.start();
	}
	public void resetCap(){
		time = 0;
		for(Capacitor c : capacitors) c.resetCharge();
	}
	@Override
	public void mouseClicked(MouseEvent e) {
		
	}
	@Override
	public void mousePressed(MouseEvent e) {
		
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		if(boxPainted && box.contains(e.getX(),e.getY())){
			selectedConnection.askValue();
			if(setup) findCurrents();
			repaint();
			return;
		}
		for(Node n : nodes) if(n.clicked(e.getPoint())){
			if(e.getButton()==MouseEvent.BUTTON3){
				node1=null;
				nodes.remove(n);
				setup=false;
				repaint();
				return;
			}
//			if(n.currentProportions!=null) for(double d: n.currentProportions) System.out.print(d+" ");
//			System.out.println();
			if(node1==null) node1=n;
			else if(node1==n) node1=null;
			else{
				addNewObject(node1, n);
				node1=null;}
			selectedConnection=null;
			repaint();
			return;
		}
		
		for(Wire w : wires) if(w.clicked(e.getPoint())){
			if(e.getButton()==MouseEvent.BUTTON3){
				wires.remove(w.remove());
				selectedConnection=null;
				setup=false;
				repaint();
				return;
			}
			selectConnection(w);
			return;
		}
		for(Battery b : batteries) if(b.clicked(e.getPoint())){
			if(e.getButton()==MouseEvent.BUTTON3){
				batteries.remove(b.remove());
				selectedConnection=null;
				setup=false;
				repaint();
				return;
			}
			selectConnection(b);
			return;
		}
		for(Resistor r : resistors) if(r.clicked(e.getPoint())){
			if(e.getButton()==MouseEvent.BUTTON3){
				resistors.remove(r.remove());
				selectedConnection=null;
				setup=false;
				repaint();
				return;
			}
			selectConnection(r);
			return;
		}
		for(Capacitor c : capacitors) if(c.clicked(e.getPoint())){
			if(e.getButton()==MouseEvent.BUTTON3){
				capacitors.remove(c.remove());
				selectedConnection=null;
				setup=false;
				repaint();
				return;
			}
			selectConnection(c);
			return;
		}
		selectedConnection=null;
		if(e.getButton()==MouseEvent.BUTTON3){
			node1=null;
			repaint();
			return;
		}
		Node n = new Node(e.getX(),e.getY());
		nodes.add(n);
		if(node1!=null) addNewObject(node1,n);
		node1=n;
		repaint();
	}
	public void selectConnection(Connection c){
		selectedConnection = c;
		node1 = null;
		repaint();
	}
	
	public void addNewObject(Node a, Node b){
		switch(myType){
		case WIRE:
			wires.add(new Wire(a,b));
			break;
		case BATTERY:
			batteries.add(new Battery(a,b));
			break;
		case RESISTOR:
			resistors.add(new Resistor(a,b ));
			break;
		case CAPACITOR:
			capacitors.add(new Capacitor(a,b));
			break;
		}
	}
	
	@Override
	public void mouseEntered(MouseEvent e) {
		
	}
	@Override
	public void mouseExited(MouseEvent e) {
		
	}
}
