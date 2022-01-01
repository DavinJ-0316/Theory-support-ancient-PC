package animation.gears;

import java.awt.Color;
import animation.plots.Matrix2d;
import java.awt.Polygon;

public class Rack {
	/* A toothed edge.  In general, determined by 
	   oriented edge (unit vector)
	   (teeth on right), tooth width, 
	   velocity = a const (x direction) , 
	   start, current location, thickness
	   In this version always horizontal:
	   edge = y-coord locations = x-coord
	 */
	   
	float height, a, inita, omega;
	float ds, bottom;
	// bottom = far edge
	float left, right;
	// normalized tooth vertex locations
	float E[][];
	
	public Rack(float h, float b, float ds, float left, float right) {
		height = h;
		bottom = b;
		this.ds = ds;
		// a = distance from left hand edge
		a = inita = 0.0f;
		omega = 0.0f;
		this.left = left; this.right = right;
		int N = (int) ((right - left)/ds + 1);
		// ds = width, dh = height of tooth
		E = new float[2*N+2][2];
		float x = 0.0f;
		// starts at the inside of a tooth
		// from a right
		float dh = ds*(float) Math.sqrt(3)/2;
		if (bottom > height) {
			dh = -dh;
		}
		for (int i=0;i<=2*N;i += 2) {
			E[i][0] = x;
			E[i][1] = height-0.5f*dh;
			x += 0.5*ds;
			E[i+1][0] = x;
			E[i+1][1] = height+0.5f*dh;
			x += 0.5*ds;
		} 
	}
	
	// distance to left of boundary where a ttoth begins
	public float start() {
		float h = ds();
		int N = (int) ((a - left)/h);
		float x = a - N*h;
		if (x > left) x -= h;
		return(x);
	}
	
	public void setBoundary(float ell, float r) {
	}

	public void setStart(float a) {
		this.a = a;
	}

	public void setSpeed(float omega) {
		this.omega = omega;
	}

	public float getSpeed() {
		return(omega);
	}

	float ds() {
		return(ds);
	}
	
	// - meshing ------------------------------------------
	
	float phaseAt(float b) {
		float h = ds();
		
		int N = (int) ((b-a)/h);
		b -= N*h;
		b /= h;
		System.err.println("a, b, h = " + a + ", " + b + ", " + h);
		if (b < 0) b += 1;
		return(b);
	}

	// slide this to be in phase with g
	// the gears must be compatible, which means radius/count the same
	// phase depends on sign of rotation
	
	// - setting ---------------------------------------------------

	public void reset() {
		a = inita;
	}

	public float location() {
		return(a);
	}

	public void advance(float dt) {
		a += omega*dt;
	}
	
	public Polygon edge(Matrix2d m) {
		float x = start();
		float M[] = Matrix2d.translated(m.m, x, 0);
		Polygon p = Matrix2d.polygon(M, E);
		return(p);
	}
	
	// returns the corner of the marker at angle x
	public int[] marker(float t, float m[]) {
		int p[] = new int[2];
		return(p);
	}
	

}

