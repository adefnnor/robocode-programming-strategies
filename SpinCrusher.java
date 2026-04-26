package VSsamples;

import robocode.*;
import robocode.util.Utils;
import java.awt.geom.Point2D;

public class SpinCrusher extends AdvancedRobot {

    // initalization of opponent's data
    private double enemyX = -1;
    private double enemyY = -1;
    private double enemyVelocity = 0;
    private double enemyHeading = 0;
    private double enemyBearing = 0;
    private double enemyDistance = 400;
    private double enemyEnergy = 100;
    private double lastEnemyEnergy = 100;

    // initialization of SpinCrusher's movement state
    private int orbitDirection = 1; // +1 clockwise, -1 counter-clockwise
    private int moveDirection = 1; // +1 forward, -1 reverse
    private double lastRobotEnergy = 100;

    // Movement Constants
    private static final double PREFERRED_DISTANCE = 160;
    private static final double WALL_MARGIN = 60;
    private static final double ORBIT_SPEED = 6.0;

    public void run() {

        setAdjustGunForRobotTurn(true);
        setAdjustRadarForGunTurn(true);
        setAdjustRadarForRobotTurn(true);

        while (true) {
            // there is no initialization proces for this bot, everything it does is
            // reactive to the opponent's position, energy and movement
            checkWallCollision();
            checkHitByBullet();
            doRadarLock();
            doMovement();
            doGunTurn();
            checkRamOpportunity();
            execute();
        }
    }

    // radar lock is fundamental for this strategy. Upon round start, do a 360 sweep
    // and then lock on to target.
    private void doRadarLock() {
        if (enemyX >= 0) {
            double radarTurn = getHeading() - getRadarHeading() + enemyBearing;
            setTurnRadarRight(Utils.normalRelativeAngleDegrees(radarTurn) * 2.0);
        } else {
            setTurnRadarRight(360);
        }
    }

    // Small arcs around opponent at constant distance to maximize evasion and
    // accuracy, turns if it gets close to a wall or gets hit by a bullet
    private void doMovement() {
        if (enemyX < 0)
            return;

        double distanceCorrection = (enemyDistance - PREFERRED_DISTANCE) * 0.08;
        double turnAngle = enemyBearing + 90 * orbitDirection + distanceCorrection * orbitDirection;
        double wallAvoid = getWallAvoidAdjustment();
        turnAngle += wallAvoid;

        if (Math.abs(wallAvoid) > 25) {
            orbitDirection *= -1;
        }

        setTurnRight(Utils.normalRelativeAngleDegrees(turnAngle));
        setAhead(ORBIT_SPEED * moveDirection * 1000);
    }

	// constantly checks which wall is closest and if the distance goes beyond a
    // specific value, turns toward the cnetre
    private double getWallAvoidAdjustment() {
        double x = getX();
        double y = getY();
        double bfw = getBattleFieldWidth();
        double bfh = getBattleFieldHeight();

        double closest = Math.min(Math.min(x, bfw - x), Math.min(y, bfh - y));
        if (closest >= WALL_MARGIN)
            return 0;

        double severity = 1.0 - (closest / WALL_MARGIN);
        double angleToCenter = Math.toDegrees(
                Math.atan2(bfw / 2 - x, bfh / 2 - y)) - getHeading();
        return Utils.normalRelativeAngleDegrees(angleToCenter) * severity * 0.6;
    }

    // predictive targeting algorithm specifically designed for SpinBot
    private void doGunTurn() {
        if (enemyX < 0)
            return;

        double firePower = calculateFirePower();
        double bulletSpeed = 20 - 3 * firePower;

        double predictedX = enemyX;
        double predictedY = enemyY;
        double eHeadingRad = Math.toRadians(enemyHeading);

        for (int i = 0; i < 20; i++) {
            predictedX += Math.sin(eHeadingRad) * enemyVelocity;
            predictedY += Math.cos(eHeadingRad) * enemyVelocity;
            eHeadingRad += Math.toRadians(10);

            predictedX = Math.max(18, Math.min(getBattleFieldWidth() - 18, predictedX));
            predictedY = Math.max(18, Math.min(getBattleFieldHeight() - 18, predictedY));

            double dist = Point2D.distance(getX(), getY(), predictedX, predictedY);
            if (dist / bulletSpeed <= i + 1)
                break;
        }

        double angle = Math.toDegrees(Math.atan2(predictedX - getX(), predictedY - getY()));
        double gunTurn = Utils.normalRelativeAngleDegrees(angle - getGunHeading());

        setTurnGunRight(gunTurn);

        if (getGunHeat() == 0 && Math.abs(gunTurn) < 5 && enemyDistance < 700) {
            setFire(firePower);
        }
    }

    // bullet power in terms of distance, to maximizze accuracy and minimize energy
    // loss
    private double calculateFirePower() {
        if (enemyDistance < 100)
            return 3.0;
        if (enemyDistance < 180)
            return 2.5;
        if (enemyDistance < 280)
            return 2.0;
        if (enemyDistance < 400)
            return 1.5;
        return 1.0;
    }

    // if it hits a wall, reverses directino
    private void checkWallCollision() {
        double x = getX();
        double y = getY();
        double bfw = getBattleFieldWidth();
        double bfh = getBattleFieldHeight();

        boolean nearWall = x < WALL_MARGIN || y < WALL_MARGIN
                || x > bfw - WALL_MARGIN || y > bfh - WALL_MARGIN;

        if (nearWall && getVelocity() == 0) {
            moveDirection *= -1;
            orbitDirection *= -1;
            setBack(60);
            execute();
        }
    }

    // if it is hit by abullet, reverses direction
    private void checkHitByBullet() {
        double currentEnergy = getEnergy();
        double drop = lastRobotEnergy - currentEnergy;

        if (drop > 0.1 && drop < 3.5) {
            moveDirection *= -1;
            if (Math.random() > 0.5)
                orbitDirection *= -1;
        }

        lastRobotEnergy = currentEnergy;
    }

    private void checkRamOpportunity() {
        // if opponent gets to close, use the Ram + Fire tactic, only works when in
        // close range.
        // Important to fire and not only Ram, that would be disadvantageous!!
        double enemyDrop = lastEnemyEnergy - enemyEnergy;
        if (enemyDrop > 0.1 && enemyDrop < 3.5) {
            moveDirection *= -1;
        }
        lastEnemyEnergy = enemyEnergy;

        if (enemyDistance > 0 && enemyDistance < 50) {
            double ramAngle = Utils.normalRelativeAngleDegrees(
                    enemyBearing - getGunHeading() + getHeading());
            setTurnGunRight(ramAngle);
            if (getGunHeat() == 0)
                setFire(3.0);
            setAhead(100 * moveDirection);
        }
    }

    // when sacnning arobot, get's all information necessary for reactive movement
    // and predictive targeting
    public void onScannedRobot(ScannedRobotEvent e) {
        lastEnemyEnergy = enemyEnergy;
        enemyEnergy = e.getEnergy();
        enemyBearing = e.getBearing();
        enemyDistance = e.getDistance();
        enemyVelocity = e.getVelocity();
        enemyHeading = e.getHeading();

        double absBearing = Math.toRadians(getHeading() + e.getBearing());
        enemyX = getX() + Math.sin(absBearing) * e.getDistance();
        enemyY = getY() + Math.cos(absBearing) * e.getDistance();
    }
}