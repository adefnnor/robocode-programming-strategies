package VSsamples;
import robocode.*;
//import java.awt.Color;

public class CounterFire extends Robot {
    
    // Runs around in a rectangle and scans everytime it travels half the length of a side

    public void run() {
        final double BW, BH;
        BW = getBattleFieldWidth();
        BH = getBattleFieldHeight();
        // out.println("BW " + BW);
        // out.println("BW " + BH);

        double X, Y, H;
        X = getX();
        Y = getY();
        H = getHeading();
        // out.println(H);

        // the sides of the rectangle to be travelled are half the battlefield's width
        // and height
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

        /*
         * on rectangle: left, right, bot, top.
         * inside rectangle: left < x < right and bot < y < top
         * outside rectangle: below, above, left (above, netx to, and below) and right
         * (above, netx to, and below)
         */

        for (int i = 1; i < 3; i++) {
            if (i == 1) {
                out.println(i);
                if (X == sideleft) {
                    if (Y >= sidebot && Y <= sidetop) { // robot is on the left side of the rectangle
                        if (H <= 180) {
                            turnRight(180 - H);
                        } else if (H > 180) {
                            turnLeft(H - 180);
                        }
                        out.println("on rectangle");
                        ahead(Dbot);
                        turnLeft(90); // on lower left corner
                        out.println("on lower left corner");
                    }
                }

                else if (X == sideright) {
                    if (Y >= sidebot && Y <= sidetop) { // robot is on the right side of the rectangle
                        if (H <= 180) {
                            turnLeft(H);
                        } else if (H > 180) {
                            turnRight(360 - H);
                        }
                        out.println("on rectangle");
                        ahead(Dtop);
                        turnLeft(90); // on upper right corner
                        out.println("on upper right corner");
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
                        out.println("on rectangle");
                        ahead(Dleft);
                        turnLeft(90);
                        ahead(BH/4);
                        turnRadarLeft(360);
                        ahead(BH/4);
                        turnLeft(90); // on lower left corner
                        out.println("on lower left corner");
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
                        out.println("on rectangle");
                        ahead(Dright);
                        turnLeft(90);
                        ahead(BH/4);
                        turnRadarLeft(360);
                        ahead(BH/4);
                        turnLeft(90); // on upper right corner
                        out.println("on upper right corner");
                    }
                }

                else if (X > sideleft && X < sideright) {
                    if (Y > sidebot && Y < sidetop) { // robot is inside the rectangle
                        out.println("inside rectangle");
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
                            out.println("on lower left corner");
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
                            out.println("on upper right corner");
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
                            ahead(BH/4);
                            turnRadarLeft(360);
                            ahead(BH/4);
                            turnLeft(90);
                            out.println("on lower left corner");
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
                            ahead(BH/4);
                            turnRadarLeft(360);
                            ahead(BH/4);
                            turnLeft(90);
                            out.println("on upper right corner");
                        }
                        
                    }

                    else if (Y < sidebot || Y > sidetop) { // robot is outside the rectangle (either above or below it)
                        if (Y < sidebot) { // below rectangle
                            out.println("below rectangle");
                            if (H <= 180) {
                                turnLeft(H);
                            } else {
                                turnRight(360 - H);
                            }
                            ahead(Dbot);
                            turnRight(90);
                            ahead(sideright - X);
                            turnLeft(90);
                            ahead(BH/4);
                            turnRadarLeft(360);
                            ahead(BH/4);
                            turnLeft(90);
                            out.println("on upper right corner");
                            
                        } else if (Y > sidetop) { // above rectangle
                            out.println("above rectangle");
                            if (H <= 180) {
                                turnRight(180 - H);
                            } else {
                                turnLeft(H - 180);
                            }
                            ahead(Dtop);
                            turnRight(90);
                            ahead(X - sideleft);
                            turnLeft(90);
                            ahead(BH/4);
                            turnRadarLeft(360);
                            ahead(BH/4);
                            turnLeft(90);
                            out.println("on lower left corner");
                        }
                    }
                }

                else if (X < sideleft) { // robot is left of the rectangle
                    out.println("left of rectangle");
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
                        ahead(BH/4);
                        turnRadarLeft(360);
                        ahead(BH/4);
                        turnLeft(90); // on lower left corner
                        out.println("on lower left corner");
                    } else if (Y <= sidetop && Y >= sidebot) {
                        turnRight(90);
                        ahead(Dbot);
                        turnLeft(90); // on lower left corner
                        out.println("on lower left corner");
                    } else if (Y < sidebot) {
                        turnLeft(90);
                        ahead(Dbot);
                        turnRight(90); // on lower left corner
                        out.println("on lower left corner");
                    }
                }

                else if (X > sideright) { // robot is right of the rectangle
                    out.println("right of rectangle");
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
                        out.println("on upper right corner");
                    } else if (Y <= sidetop && Y >= sidebot) {
                        turnRight(90);
                        ahead(Dtop);
                        turnLeft(90); // on upper right corner
                        out.println("on upper right corner");
                    } else if (Y < sidebot) {
                        turnRight(90);
                        ahead(Dbot);
                        ahead(BH/4);
                        turnRadarLeft(360);
                        ahead(BH/4);
                        turnLeft(90); // on upper  right corner
                        out.println("on upper  right corner");
                    }
                }

            }

            // up until this point everything works as intended
            // identifies the place correctly and moves to the clossest side the the "rectangle" then proceeds to one of two out of the total four corners, either lower left corner or upper right corner.
        }

        // starting from bottom left of rectangle or from top right of rectangle, loop, scanning every have-side
        
        while (true) {
            ahead(BW/4);
            turnRadarLeft(360);
            ahead(BW/4);
            turnLeft(90);
            ahead(BH/4);
            turnRadarLeft(360);
            ahead(BH/4);
            turnLeft(90);
        }
    }

    public void onScannedRobot(ScannedRobotEvent SR) {
        double Alpha, Beta, RH;
        // double R;
        RH = getHeading();
        // out.println("Heading: " + RH);
        Alpha = SR.getBearing(); // Alpha is the angle between the enemy robot and this robot's heading
        // out.println("Alpha: " + Alpha);
        Beta = getGunHeading(); // Beta is the angle of the gun's heading (from this robot)
        // out.println("Beta: " + Beta);
        // when Alpha == Beta, the gun is pointing to the enemy robot
        // R = SR.getDistance();
        // setAdjustGunForRobotTurn(true);
        // setAdjustRadarForGunTurn(true);
        // setAdjustRadarForRobotTurn(true);
        double AngTurn = RH - Beta + Alpha;
        if (AngTurn >= 0 && AngTurn < 180) {
            turnGunRight(AngTurn);
        } else if (AngTurn >= -180 && AngTurn < 0) {
            turnGunLeft(Math.abs(AngTurn));
        } else if (AngTurn >= -360 && AngTurn < -180) {
            turnGunRight(360 + AngTurn);
        } else if (AngTurn >= 180) {
            turnGunLeft(360 - AngTurn);
        } else if (AngTurn <-360) {
            if (Alpha >= -180) {
                turnGunLeft(Math.abs(AngTurn));
            } else {
                turnGunRight(Math.abs(AngTurn));
            }
        }
        fire(3);

    }
    
    public void onHitRobot(HitRobotEvent HR) {
        turnRight(90);
        ahead(getWidth()*3);
        turnLeft(90);
        ahead(getHeight()*3);
        turnLeft(90);
        ahead(getHeight()*3);
        turnRight(90);
    }

    public void onHitWall(HitWallEvent e) {
        back(15);
        turnLeft(90);
    }
}