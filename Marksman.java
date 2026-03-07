package VSsamples;

import robocode.AdvancedRobot;
import robocode.ScannedRobotEvent;
import robocode.Rules;
import robocode.util.Utils;

public class Marksman extends AdvancedRobot {

    private boolean lockRadar = false;

    public void run() {
        final double BW, BH;
        BW = getBattleFieldWidth();
        BH = getBattleFieldHeight();

        double X, Y, H;
        X = getX();
        Y = getY();
        H = getHeading();

        double sideleft, sideright, sidetop, sidebot;
        sideleft = BW / 4;
        sideright = 3 * sideleft;
        sidebot = BH / 4;
        sidetop = 3 * sidebot;

        double Dleft, Dright, Dtop, Dbot, DMOVE;
        Dleft = Math.abs(X - sideleft);
        Dright = Math.abs(sideright - X);
        Dtop = Math.abs(sidetop - Y);
        Dbot = Math.abs(Y - sidebot);

        setAdjustGunForRobotTurn(true);
        setAdjustRadarForGunTurn(true);
        setAdjustRadarForRobotTurn(true);

        if (X == sideleft) {
            if (Y >= sidebot && Y <= sidetop) { // robot is on the left side of the rectangle
                if (H <= 180) {
                    turnRight(180 - H);
                } else if (H > 180) {
                    turnLeft(H - 180);
                }
                //out.println("on rectangle");
                ahead(Dbot);
                turnLeft(90); // on lower left corner
                //out.println("on lower left corner");
            }
        }

        else if (X == sideright) {
            if (Y >= sidebot && Y <= sidetop) { // robot is on the right side of the rectangle
                if (H <= 180) {
                    turnLeft(H);
                } else if (H > 180) {
                    turnRight(360 - H);
                }
                //out.println("on rectangle");
                ahead(Dtop);
                turnLeft(90); // on upper right corner
                //out.println("on upper right corner");
            }

        }

        else if (Y == sidetop) {
            if (X > sideleft && X < sideright) { // robot is on the top side of the rectangle
                if (H <= 90) {
                    turnLeft(H + 90);
                } else if (H > 90 && H <= 270) {
                    turnRight(270 - H);
                } else if (H > 270) {
                    turnLeft(H - 270);
                }
                //out.println("on rectangle");
                ahead(Dleft);
                turnLeft(90);
                ahead(BH / 4);
                turnRadarLeft(360);
                ahead(BH / 4);
                turnLeft(90); // on lower left corner
                //out.println("on lower left corner");
            }
        }

        else if (Y == sidebot) {
            if (X >= sideleft && X <= sideright) { // robot is on the bottom side of the rectangle
                if (H < 90) {
                    turnRight(90 - H);
                } else if (H > 90 && H <= 270) {
                    turnLeft(H - 90);
                } else if (H > 270) {
                    turnRight(360 - H + 90);
                }
                //out.println("on rectangle");
                ahead(Dright);
                turnLeft(90);
                ahead(BH / 4);
                turnRadarLeft(360);
                ahead(BH / 4);
                turnLeft(90); // on upper right corner
                //out.println("on upper right corner");
            }
        }

        else if (X > sideleft && X < sideright) {
            if (Y > sidebot && Y < sidetop) { // robot is inside the rectangle
                //out.println("inside rectangle");
                DMOVE = Math.min(Dbot, Math.min(Dtop, Math.min(Dright, Dleft)));
                if (DMOVE == Dleft) {
                    if (H <= 90) {
                        turnLeft(180 - H);
                    } else if (H >= 270) {
                        turnLeft(H - 270);
                    } else {
                        turnRight(270 - H);
                    }
                    ahead(DMOVE);
                    turnLeft(90);
                    ahead(Y - sidebot);
                    turnLeft(90);
                    //out.println("on lower left corner");
                }
                if (DMOVE == Dright) {
                    if (H <= 90) {
                        turnRight(90 - H);
                    } else if (H >= 270) {
                        turnRight(360 - H + 90);
                    } else {
                        turnLeft(H - 90);
                    }
                    ahead(DMOVE);
                    turnLeft(90);
                    ahead(sidetop - Y);
                    turnLeft(90);
                    //out.println("on upper right corner");
                }
                if (DMOVE == Dtop) {
                    if (H <= 180) {
                        turnLeft(H);
                    } else {
                        turnRight(360 - H);
                    }
                    ahead(DMOVE);
                    turnLeft(90);
                    ahead(X - sideleft);
                    turnLeft(90);
                    ahead(BH / 4);
                    turnRadarLeft(360);
                    ahead(BH / 4);
                    turnLeft(90);
                    //out.println("on lower left corner");
                }
                if (DMOVE == Dbot) {
                    if (H <= 180) {
                        turnRight(180 - H);
                    } else {
                        turnLeft(H - 180);
                    }
                    ahead(DMOVE);
                    turnLeft(90);
                    ahead(sideright - X);
                    turnLeft(90);
                    ahead(BH / 4);
                    turnRadarLeft(360);
                    ahead(BH / 4);
                    turnLeft(90);
                    //out.println("on upper right corner");
                }

            }

            else if (Y < sidebot || Y > sidetop) { // robot is outside the rectangle (either above or below it)
                if (Y < sidebot) { // below rectangle
                    //out.println("below rectangle");
                    if (H <= 180) {
                        turnLeft(H);
                    } else {
                        turnRight(360 - H);
                    }
                    ahead(Dbot);
                    turnRight(90);
                    ahead(sideright - X);
                    turnLeft(90);
                    ahead(BH / 4);
                    turnRadarLeft(360);
                    ahead(BH / 4);
                    turnLeft(90);
                    //out.println("on upper right corner");

                } else if (Y > sidetop) { // above rectangle
                    //out.println("above rectangle");
                    if (H <= 180) {
                        turnRight(180 - H);
                    } else {
                        turnLeft(H - 180);
                    }
                    ahead(Dtop);
                    turnRight(90);
                    ahead(X - sideleft);
                    turnLeft(90);
                    ahead(BH / 4);
                    turnRadarLeft(360);
                    ahead(BH / 4);
                    turnLeft(90);
                    //out.println("on lower left corner");
                }
            }
        }

        else if (X < sideleft) { // robot is left of the rectangle
            //out.println("left of rectangle");
            if (H <= 90) {
                turnRight(90 - H);
            } else if (H > 90 && H <= 270) {
                turnLeft(H - 90);
            } else if (H > 270) {
                turnRight(360 - H + 90);
            }
            ahead(Dleft);
            // 3 cases: above rectangle, next to rectangle and below rectangle
            if (Y > sidetop) {
                turnRight(90);
                ahead(Dtop);
                ahead(BH / 4);
                turnRadarLeft(360);
                ahead(BH / 4);
                turnLeft(90); // on lower left corner
                //out.println("on lower left corner");
            } else if (Y <= sidetop && Y >= sidebot) {
                turnRight(90);
                ahead(Dbot);
                turnLeft(90); // on lower left corner
                //out.println("on lower left corner");
            } else if (Y < sidebot) {
                turnLeft(90);
                ahead(Dbot);
                turnRight(90); // on lower left corner
                //out.println("on lower left corner");
            }
        }

        else if (X > sideright) { // robot is right of the rectangle
            //out.println("right of rectangle");
            if (H <= 90) {
                turnLeft(H + 90);
            } else if (H > 90 && H <= 270) {
                turnRight(270 - H);
            } else if (H > 360) {
                turnLeft(H - 270);
            }
            ahead(Dright);
            // 3 cases: above rectangle, next to rectangle and below rectangle
            if (Y > sidetop) {
                turnLeft(90);
                ahead(Dtop);
                turnRight(90); // on upper right corner
                //out.println("on upper right corner");
            } else if (Y <= sidetop && Y >= sidebot) {
                turnRight(90);
                ahead(Dtop);
                turnLeft(90); // on upper right corner
                //out.println("on upper right corner");
            } else if (Y < sidebot) {
                turnRight(90);
                ahead(Dbot);
                ahead(BH / 4);
                turnRadarLeft(360);
                ahead(BH / 4);
                turnLeft(90); // on upper right corner
                //out.println("on upper  right corner");
            }
        }

        /* identifies the place correctly and moves to the clossest side the the
        "rectangle" then proceeds to one of two out of the total four corners, either
        lower left corner or upper right corner. */

        while (true) {
            ahead(BW / 4);
            if (!lockRadar) {
                turnRadarLeft(360);
            }
            ahead(BW / 4);
            turnLeft(90);
            ahead(BH / 4);
            if (!lockRadar) {
                turnRadarLeft(360);
            }
            ahead(BH / 4);
            turnLeft(90);
        }

        // while rada is unlocked, after every half-length of the rectangle, scans the battlefield

    }

    public void onScannedRobot(ScannedRobotEvent SR) {

        double firePower = 2.0;

        if (!lockRadar) {
            lockRadar = true; // after scanning a Robot, if the radar is unlocked -> lock it
        }

        if (lockRadar) {
            double radarTurn = getHeading() + SR.getBearing() - getRadarHeading();
            setTurnRadarRight(Utils.normalRelativeAngleDegrees(radarTurn));
        }
        
        double GunTurnBeta = GunTurnFunc(SR, firePower); 
        setTurnGunRight(GunTurnBeta); // turn gun according to the calculated result of GunTurnFunc

        if (getGunHeat() == 0 && Math.abs(getGunTurnRemaining()) < 0.5) {

            setFire(firePower);

            // unlock radar
            lockRadar = false;
        }
    }

    // function that calculates the angle at witch to turn based on an iterative method (considering gun turn speed and bullet travel time)
    public double GunTurnFunc(ScannedRobotEvent SR, double firePower) {

        double bulletSpeed = 20 - 3 * firePower;

        double HeadingR = Math.toRadians(getHeading());
        double GHeadingR = Math.toRadians(getGunHeading());

        double OppBearingR = Math.toRadians(SR.getBearing());
        double OppHeadingR = Math.toRadians(SR.getHeading());

        double Gamma = HeadingR + OppBearingR;

        double X = getX();
        double Y = getY(); //my X,Y coordinates

        double OppX = X + SR.getDistance() * Math.sin(Gamma);
        double OppY = Y + SR.getDistance() * Math.cos(Gamma); // opponent's X,Y coordiantes

        double OppVx = SR.getVelocity() * Math.sin(OppHeadingR);
        double OppVy = SR.getVelocity() * Math.cos(OppHeadingR); // opponent's Vx,Vy speed

        double GTurnRateR = Math.toRadians(Rules.GUN_TURN_RATE);

        double time = SR.getDistance() / bulletSpeed; // bullet fly time and first aproximation

        for (int i = 0; i < 10; i++) {
            double newOppX = OppX + OppVx * time; // X(t) = X(0) + Vx*t
            double newOppY = OppY + OppVy * time; // Y(t) = Y(0) + Vy*t
            double iterativeAngleR = Math.atan2(newOppX - X, newOppY - Y); // tan(theta) = (X'/Y')
            double GTurnR = Utils.normalRelativeAngle(iterativeAngleR - GHeadingR);
            double GTurnTime = Math.abs(GTurnR) / GTurnRateR;
            double dist = Math.hypot(newOppX - X, newOppY - Y); // sqrt(X'^2+Y'^2)
            time = GTurnTime + dist / bulletSpeed; 
            /* time it takes from scanning opposing robot until the bullet supposedly makes impact
            in that time t, the opposing robot will move R(t) = R(0) + V*t 
            this time get's more precise after each iteration*/
        }

        double finalX = OppX + OppVx * time;
        double finalY = OppY + OppVy * time;
        double finalGAngleR = Math.atan2(finalX - X, finalY - Y); //calculate the gun turn angle with the obtained iterative time

        double finalGTurnR = Utils.normalRelativeAngle(finalGAngleR - GHeadingR);

        return Math.toDegrees(finalGTurnR);
    }
}
