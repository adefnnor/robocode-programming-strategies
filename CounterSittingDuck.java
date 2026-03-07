package VSsamples;
import robocode.*;

public class CounterSittingDuck extends Robot {

//sitting duck does nothing, so this robot finds sittingduck and starts firing bullets to that position

// in this case, the enemy does not fire back, therefore we don't need to worry about moving
	public void run() {
		for (int i = 0; i<20 ; i++) { //waiting for the gun to cool down. All guns start out a battle hot
			turnRight(1);
			turnLeft(1);
		}
		turnRadarRight(360);
	}

	public class bfp { //bullet fire power
		public static int b = 2;
	}

	public void onScannedRobot(ScannedRobotEvent S) {
		//out.println("Enemy found!");
		double B;
		B = S.getBearing();
		//out.println("Enemy at " + String.format("%.1f", B) + "º of Robot");
		turnRight(B);
		if (Math.abs(B) <= 0.5) { 
			if (getGunHeat() == 0) {
				out.println("Firing bullet");
				fire(bfp.b);
			}
		}
	}

	public void onBulletHit(BulletHitEvent BH) {
		out.println("A bullet has hit the enemy. Firing another bullet");
		if (getGunHeat() == 0) {
			fire(bfp.b);
		}
		else { //wait for the gun to cool down
			for (int i = 0; i<20 ; i++) {
			turnRight(1);
			turnLeft(1);
			}
			fire(bfp.b);
		}
	}
}