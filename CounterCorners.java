package VSsamples;
import robocode.*;


public class CounterCorners extends Robot {

	public void run() {
		final double BH, BW;
		BH = getBattleFieldHeight();
		BW = getBattleFieldWidth();
	// positions itself in the center of the battlefield and starts moving 	
		double x0,y0,h0;
		x0 = getX();
		y0 = getY();
		h0 = getHeading();
		/*
			we divide the initial state into 24 different cases:
				- 4 quadrants were the robot can start
				- 2 different directions at which turning would be most efficient for each quadrant (depending on original heading h0)
				- 2 conditions in for each turning direction
				- + 4 border cases, y0 == BH/2 when x0<BW/2 and when x0>BW/2, and when x0 == BW/2 when y0<BH/2 and when y0>BH/2
					- each with 2 different cases, when iitial heading (h0) is <180 and >180
		*/
		double X,Y,phi,d;
		// second and third quadrants: x0 < BW/2
		if (x0 < BW/2) {
			X = BW/2 - x0;
			if (y0 > BH/2) { // second quadrant
				Y = y0 - BH/2;
				phi = (Math.atan(Y/X))*180/Math.PI;
				d = Math.sqrt(Math.pow(X,2) + Math.pow(Y,2));
				if (h0 < phi+90) {
					turnRight(90+phi-h0);
				}
				else if (h0 >= phi+90 && h0 < 180) {
					turnLeft(h0-phi-90);
				}
				else if (h0 >= 180 && h0 < phi+270) {
					turnLeft(h0-90-phi);
				}
				else if (h0 >= phi+270) {
					turnRight(360-h0+90+phi);
				}
				ahead(d);
			}
			else if (y0 < BH/2) { // third quadrant
				Y = BH/2 - y0;
				phi = (Math.atan(Y/X))*180/Math.PI;
				d = Math.sqrt(Math.pow(X,2) + Math.pow(Y,2));
				if (h0 < 90-phi) {
					turnRight(90-phi-h0);
				}
				else if (h0 >= 90-phi && h0 < 180) {
					turnLeft(h0-90+phi);
				}
				else if (h0 >= 180 && h0 < 270-phi) {
					turnLeft(h0-90+phi);
				}
				else if (h0 >= 270-phi) {
					turnRight(360-h0+90-phi);
				}
				ahead(d);
			}
            else {  // y0 == BH/2
				d = X;
                if (h0 < 90) {
                    turnRight(90-h0);
                }
                else if (h0 >= 90 && h0 < 270) {
                    turnLeft(h0-90);
                }
                else if (h0 >= 270) {
                    turnRight(360-h0+90);
                }
				ahead(d);
		    }
        }
		// first and fourth quadrants: x0 > BW/2
		else if (x0 > BW/2) {
			X = x0 - BW/2;
			if (y0 > BH/2) { // first quadrant
				Y = y0 - BH/2;
				phi = (Math.atan(Y/X))*180/Math.PI;
				d = Math.sqrt(Math.pow(X,2) + Math.pow(Y,2));
				if (h0 < 90-phi) {
					turnLeft(h0+90+phi);
				}
				else if (h0 >= 90-phi && h0 < 180) {
					turnRight(270-h0-phi);
				}
				else if (h0 >= 180 && h0 < 270-phi) {
					turnRight(270-phi-h0);
				}
				else if (h0 >= 270-phi) {
					turnLeft(phi-270+h0);
				}
				ahead(d);
			}
			else if (y0 < BH/2) { // fourth quadrant
				Y = BH/2 - y0;
				phi = (Math.atan(Y/X))*180/Math.PI;
				d = Math.sqrt(Math.pow(X,2) + Math.pow(Y,2));
				if (h0 < phi+90) {
					turnLeft(90-phi+h0);
				}
				else if (h0 >= phi+90 && h0 < 180) {
					turnRight(180-h0+90+phi);
				}
				else if (h0 >= 180 && h0 < phi+270) {
					turnRight(270-h0+phi);
				}
				else if (h0 >= phi+270) {
					turnLeft(90-phi-360+h0);
				}
				ahead(d);
			}
			else { // y0 == BH/2 
				d = X;
				if (h0 < 90) {
					turnLeft(h0+90);
				}
				else if (h0 >= 90 && h0 < 270) {
					turnRight(360-h0-90);
				}
				else if (h0 >= 270) {
					turnLeft(h0-270);
				}
				ahead(d);
			}
		}
		else { // x0 = BW/2, we are in the middle of the X axis
			if (y0 > BH/2) {
				Y = y0 - BH/2;
				d = Y;
				if (h0 < 180) {
					turnRight(180-h0);
				}
				else if(h0 >= 180) {
					turnLeft(h0-180);
				}
				ahead(d);
			}
			else if (y0 < BH/2) {
				Y = BH/2 - y0;
				d = Y;
				if (h0 < 180) {
					turnLeft(180-h0);
				}
				else if (h0 >= 180) {
					turnRight(360-h0);
				}
				ahead(d);
			}
		}
		 
		// turns to heading = 90º to move in an advantageous path to avoid bullet fire from any of the 4 corners
		// now starts moving to avoid being hit, every so often it scans for an enemy robot
		double h;
		h = getHeading();
		if (h < 90) {
			turnRight(90-h);
		}
		else if (h >= 90 && h < 180) {
			turnLeft(h-90);
		}
		else if (h >= 180 && h < 270) {
			turnRight(270-h);
		}
		else if (h >= 270) {
			turnLeft(h-270);
		}
		double RWidth = getWidth();
		ahead(BW/2-RWidth);
		while (true) {
			ahead(180);
			turnRadarRight(360);
		}
	}

	
	public void onScannedRobot(ScannedRobotEvent SR) {
		double Alpha,Beta,H;
		// double R;
		H = getHeading();
		//out.println("Heading: " + H);
		Alpha = SR.getBearing(); // Alpha is the angle between the enemy robot and this robot's heading
		//out.println("Alpha: " + Alpha);
		Beta = getGunHeading(); // Beta is the angle between the gun in this roobot and the robot's heading
		//out.println("Beta: " + Beta);
		// when Alpha == Beta, the gun is pointing to the enemy robot
	 	// R = SR.getDistance();
		setAdjustGunForRobotTurn(true);
		setAdjustRadarForGunTurn(true);
		setAdjustRadarForRobotTurn(true);
		double AngTurn = H-Beta+Alpha;
		if (AngTurn >= 0 && AngTurn < 180) {
			turnGunRight(AngTurn);
		}
		else if (AngTurn >= -180 && AngTurn < 0) {
			turnGunLeft(Math.abs(AngTurn));
		}
		else if (AngTurn >= -360 && AngTurn < 180) {
			turnGunRight(360+AngTurn);
		}
		else if (AngTurn >= 180) { 
			turnGunLeft(360-AngTurn);
		}
		fire(3);
		
	}

	public void onHitRobot (HitRobotEvent HR) {
		back(100);
		turnRight(90);
		ahead(50);
		turnLeft(90);
	}


	public void onHitWall(HitWallEvent WH) {
		turnRight(180);
	}	
}
