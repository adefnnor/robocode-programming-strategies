package VSsamples;

import robocode.AdvancedRobot;
import robocode.ScannedRobotEvent;
import robocode.Rules;
import robocode.util.Utils;

public class Telos extends AdvancedRobot {

    private boolean lockRadar = false;
    private double lastOppHeading = Double.NaN;
    private long lastScanTime = -1;
    private boolean justFired = false;
    private int moveState = 0;
    private boolean stateInitialized = false;

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
        DMOVE = 0;
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
                // out.println("on rectangle");
                ahead(Dbot);
                turnLeft(90); // on lower left corner
                // out.println("on lower left corner");
            }
        }

        else if (X == sideright) {
            if (Y >= sidebot && Y <= sidetop) { // robot is on the right side of the rectangle
                if (H <= 180) {
                    turnLeft(H);
                } else if (H > 180) {
                    turnRight(360 - H);
                }
                // out.println("on rectangle");
                ahead(Dtop);
                turnLeft(90); // on upper right corner
                // out.println("on upper right corner");
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
                // out.println("on rectangle");
                ahead(Dleft);
                turnLeft(90);
                ahead(BH / 4);
                turnRadarLeft(360);
                ahead(BH / 4);
                turnLeft(90); // on lower left corner
                // out.println("on lower left corner");
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
                // out.println("on rectangle");
                ahead(Dright);
                turnLeft(90);
                ahead(BH / 4);
                turnRadarLeft(360);
                ahead(BH / 4);
                turnLeft(90); // on upper right corner
                // out.println("on upper right corner");
            }
        }

        else if (X > sideleft && X < sideright) {
            if (Y > sidebot && Y < sidetop) { // robot is inside the rectangle
                // out.println("inside rectangle");
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
                    // out.println("on lower left corner");
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
                    // out.println("on upper right corner");
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
                    // out.println("on lower left corner");
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
                    // out.println("on upper right corner");
                }

            }

            else if (Y < sidebot || Y > sidetop) { // robot is outside the rectangle (either above or below it)
                if (Y < sidebot) { // below rectangle
                    // out.println("below rectangle");
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
                    // out.println("on upper right corner");

                } else if (Y > sidetop) { // above rectangle
                    // out.println("above rectangle");
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
                    // out.println("on lower left corner");
                }
            }
        }

        else if (X < sideleft) { // robot is left of the rectangle
            // out.println("left of rectangle");
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
                // out.println("on lower left corner");
            } else if (Y <= sidetop && Y >= sidebot) {
                turnRight(90);
                ahead(Dbot);
                turnLeft(90); // on lower left corner
                // out.println("on lower left corner");
            } else if (Y < sidebot) {
                turnLeft(90);
                ahead(Dbot);
                turnRight(90); // on lower left corner
                // out.println("on lower left corner");
            }
        }

        else if (X > sideright) { // robot is right of the rectangle
            // out.println("right of rectangle");
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
                // out.println("on upper right corner");
            } else if (Y <= sidetop && Y >= sidebot) {
                turnRight(90);
                ahead(Dtop);
                turnLeft(90); // on upper right corner
                // out.println("on upper right corner");
            } else if (Y < sidebot) {
                turnRight(90);
                ahead(Dbot);
                ahead(BH / 4);
                turnRadarLeft(360);
                ahead(BH / 4);
                turnLeft(90); // on upper right corner
                // out.println("on upper right corner");
            }
        }

        /*
         * identifies the place correctly and moves to the clossest side the the
         * "rectangle" then proceeds to one of two out of the total four corners, either
         * lower left corner or upper right corner.
         */

        while (true) {
            if (!stateInitialized) {
                switch (moveState) {
                    case 0:
                        setAhead(BW / 2);
                        break;
                    case 1:
                        setTurnLeft(90);
                        break;
                    case 2:
                        setAhead(BH / 2);
                        break;
                    case 3:
                        setTurnLeft(90);
                        break;
                }
                stateInitialized = true;
            }

            // Check if current action is complete
            if (getDistanceRemaining() == 0 && getTurnRemaining() == 0) {
                moveState = (moveState + 1) % 4;
                stateInitialized = false;
            }

            execute();
        }
    }

    public void onStatus(robocode.StatusEvent e) {
        if (!lockRadar) {
            setTurnRadarLeft(360);
        }
    }

    public void onScannedRobot(ScannedRobotEvent SR) {
        if (getTime() < 50) {
            return; // bot is not yet in position
        }

        double firePower;
        if (SR.getDistance() < 150) {
            firePower = 3.0; // point blank: max damage, travel time barely matters
        } else if (SR.getDistance() < 300) {
            firePower = 2.0; // medium range: balanced
        } else {
            firePower = 1.0; // long range: faster bullet, less time to accumulate prediction error
        }

        if (!lockRadar && !justFired) {
            lockRadar = true; // after scanning a Robot, if the radar is unlocked -> lock it
        }
        justFired = false;

        double radarTurn = getHeading() + SR.getBearing() - getRadarHeading();

        setTurnRadarRight(Utils.normalRelativeAngleDegrees(radarTurn));

        double dHeading = 0;
        if (!Double.isNaN(lastOppHeading) && lastScanTime >= 0) {
            long ticksElapsed = getTime() - lastScanTime;
            double rawDelta = Utils.normalRelativeAngle(Math.toRadians(SR.getHeading()) - lastOppHeading);
            dHeading = rawDelta / ticksElapsed; // per-tick turn rate
        }
        lastOppHeading = Math.toRadians(SR.getHeading());
        lastScanTime = getTime();

        double GunTurnBeta = GunTurnFunc(SR, firePower, dHeading);
        setTurnGunRight(GunTurnBeta); // turn gun according to the calculated result of GunTurnFunc

        if (getGunHeat() == 0) {
            if (SR.getDistance() < 150) {
                if (Math.abs(getGunTurnRemaining()) < 5.0) {
                    setFire(3.0);
                    lockRadar = false;
                    justFired = true;
                }
            } else {
                if (Math.abs(getGunTurnRemaining()) < 0.5) {
                    setFire(firePower);
                    lockRadar = false;
                    justFired = true;
                }
            }
        }
        // if radar is not locked, keep sweeping
        if (!lockRadar) {
            setTurnRadarLeft(360);
        }
    }

    // function that calculates the angle at witch to turn based on an iterative
    // method (considering gun turn speed and bullet travel time)
    public double GunTurnFunc(ScannedRobotEvent SR, double firePower, double dHeading) {

        double bulletSpeed = 20 - 3 * firePower;

        double HeadingR = Math.toRadians(getHeading());
        double GHeadingR = Math.toRadians(getGunHeading());
        double OppBearingR = Math.toRadians(SR.getBearing());
        double OppHeadingR = Math.toRadians(SR.getHeading());

        double Gamma = HeadingR + OppBearingR;

        double X = getX(), Y = getY();
        double OppX = X + SR.getDistance() * Math.sin(Gamma);
        double OppY = Y + SR.getDistance() * Math.cos(Gamma);

        double GTurnRateR = Math.toRadians(Rules.GUN_TURN_RATE);
        double time = SR.getDistance() / bulletSpeed;

        for (int i = 0; i < 10; i++) {
            double newOppX, newOppY;

            if (Math.abs(dHeading) > 0.00001) {
                // Circular arc prediction
                double radius = SR.getVelocity() / dHeading;
                newOppX = OppX + radius * (Math.sin(OppHeadingR + dHeading * time) - Math.sin(OppHeadingR));
                newOppY = OppY + radius * (Math.cos(OppHeadingR) - Math.cos(OppHeadingR + dHeading * time));
            } else {
                // Straight line
                double OppVx = SR.getVelocity() * Math.sin(OppHeadingR);
                double OppVy = SR.getVelocity() * Math.cos(OppHeadingR);
                newOppX = OppX + OppVx * time;
                newOppY = OppY + OppVy * time;
            }

            double iterativeAngleR = Math.atan2(newOppX - X, newOppY - Y);
            double GTurnR = Utils.normalRelativeAngle(iterativeAngleR - GHeadingR);
            double GTurnTime = Math.abs(GTurnR) / GTurnRateR;
            double dist = Math.hypot(newOppX - X, newOppY - Y);
            time = GTurnTime + dist / bulletSpeed;
        }

        double finalX, finalY;
        if (Math.abs(dHeading) > 0.00001) {
            finalX = OppX
                    + (SR.getVelocity() / dHeading) * (Math.sin(OppHeadingR + dHeading * time) - Math.sin(OppHeadingR));
            finalY = OppY
                    + (SR.getVelocity() / dHeading) * (Math.cos(OppHeadingR) - Math.cos(OppHeadingR + dHeading * time));
        } else {
            double OppVx = SR.getVelocity() * Math.sin(OppHeadingR);
            double OppVy = SR.getVelocity() * Math.cos(OppHeadingR);
            finalX = OppX + OppVx * time;
            finalY = OppY + OppVy * time;
        }

        double finalGAngleR = Math.atan2(finalX - X, finalY - Y);
        double finalGTurnR = Utils.normalRelativeAngle(finalGAngleR - GHeadingR);
        return Math.toDegrees(finalGTurnR);
    }
}
