package animation.gears;

import java.applet.*;
import java.awt.*;
import animation.plots.AnimatedCanvas;
import animation.plots.UnstickyButton;
import animation.plots.SliderListener;
import animation.plots.SliderCanvas;

class RacksCanvas extends AnimatedCanvas {
	float ctr[] = {0, 0};
	static float MAX0 = 4.0f, MAX1 = 4.0f;
	Gear B;
	Rack R0, R1;
	float rb = 1.0f;
	int nb = 17;
	float s0 = -2, s1 = 0.5f, sg = 0.5f*(s0+s1);;
	
	public RacksCanvas(int w, int h) {
		super(w, h);
		matrix.setHeight(-2, 2);
		B = new Gear(ctr, rb, 0, nb);
		B.setSpeed(-(s0-s1)*0.5f/rb);
		float b[] = matrix.getCorners();
		R0 = new Rack(rb, rb+1, B.ds(), b[0], b[2]);
		R0.setSpeed(s0);
		float phr, phg;
		phr = 1 - R0.phaseAt(0);
		phg = B.phaseAt((float) Math.PI/2.0f);
		System.err.println("R, G = " + phr + ", " + phg);
		R0.setStart(R0.a + (phr - (1 - phg - 0.5f))*R0.ds());
		R1 = new Rack(-rb, -rb-1, B.ds(), b[0], b[2]);
		R1.setSpeed(s1);
		phr = R1.phaseAt(0);
		phg = B.phaseAt((float) -Math.PI/2.0f);
		R1.setStart(R1.a - (phr - (1 - phg - 0.5f))*R1.ds());

		B.makePolygons(matrix);
	}
	
	public synchronized void step() {
		super.step();
		B.advance(dt);
		ctr[0] += sg*dt;
		R0.advance(dt);
		R1.advance(dt);
	}
	
	public void reset() {
		B.reset();
	}
	
	public void setSpeed() {
		R0.setSpeed(s0);
		R1.setSpeed(s1);
		B.setSpeed(-(s0-s1)*0.5f/rb);
	}

	Color moon = new Color((float) 0.6, (float) 0.6, (float) 1.0);
	Color sun = new Color((float) 1.0, (float) 1.0, (float) 0.0);
    //	Color purple = new Color(1.0f, 1.0f, 0f);
        Color purple = new Color(1.0f, 1.0f, 0.5f); // actually yellow
	public synchronized void draw() {
		clear();
		Color spot = new Color(0.9f, 0.9f, 0.9f);

		Gear g = B;
		Polygon bpoly = g.circumference(matrix);
		fillPolygon(bpoly, purple);
		drawPolygon(bpoly, Color.black);
		int c[] = matrix.transform(g.ctr);
		gfx.fillOval(c[0]-2, c[1]-2, 4, 4);
		c = g.marker(0, matrix.m);
		gfx.setColor(Color.black);
		gfx.fillOval(c[0]-2, c[1]-2, 4, 4);
		
		Rack r;
		
		r = R0;
		Polygon p;
		
		p = r.edge(matrix);
		drawPolygon(p, Color.black);
		
		r = R1;
		p = r.edge(matrix);
		drawPolygon(p, Color.black);
		
	}

}

public class RacksApplet extends Applet implements SliderListener {
	RacksCanvas gc;
	UnstickyButton bc;
	SliderCanvas sl2, sl5;
	
	public void init() {
		setBackground(Color.white);
		setLayout(new BorderLayout(0, 0));
		setFont(new Font("Helvetica", Font.BOLD, 14));
		String S = getParameter("sleeptime");
		int s = Integer.parseInt(S);
		gc = new RacksCanvas(bounds().width, bounds().height - 24);
		gc.animator.setNapTime(60);
		gc.setInterval(0.016f);
		Panel p = new Panel();
		bc = new UnstickyButton("Run", 40, 36);
		p.add(bc);
		Panel q = new Panel();
		q.setLayout(new GridLayout(2, 1));
		sl2 = new SliderCanvas(100, 18, this, 0);
		sl2.setValue(0.5f + 0.5f*(gc.s0/gc.MAX0));
		q.add(sl2);
		sl5 = new SliderCanvas(100, 18, this, 1);
		sl5.setValue(0.5f + 0.5f*(gc.s1/gc.MAX1));
		q.add(sl5);
		sl2.setMark(0.25f);
		sl2.setMark(0.50f);
		sl2.setMark(0.75f);
		sl5.setMark(0.25f);
		sl5.setMark(0.50f);
		sl5.setMark(0.75f);
		p.add(q);
		add("South", p);
		add("Center", gc);
		show();
	}
	
	public void stop() {
		gc.animator.stop();
	}
	
	public boolean handleEvent(Event e) {
		if (e.target == bc) {
			if (e.id == Event.MOUSE_DOWN) {
				gc.animator.start();
			} else if (e.id == Event.MOUSE_UP) {
				gc.animator.stop();
			}
		}
		return(super.handleEvent(e));
	}
	public void sliderPress(int id) {
	}
	
	float fraction = 0.5f;
	// x is in the range [0, 1]
	public void sliderMove(int id, float x) {
		// lock x to nearest 16th
		x += 0.03125;
		x = (int) (16*x);
		x = x/16;
		fraction = x;
		float y = 2.0f*(x-0.5f);
		if (id == 0) {
			gc.s0 = y*gc.MAX0;
			gc.sg = 0.5f*(gc.s0+gc.s1);
		} else {
			gc.s1 = y*gc.MAX1;
			gc.sg = 0.5f*(gc.s0+gc.s1);
		}
		gc.setSpeed();
		gc.draw();
		gc.render();
	}
	
	public void sliderRelease(int id) {
		if (id == 0) {
		} else {
		}
	}
}
