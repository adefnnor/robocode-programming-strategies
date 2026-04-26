# Programming and Strategies Using Robocode

This repository contains the code developed for my Final Project (TFG) for my **Bachelor's Degree in Physics**, exploring **algorithmic strategies for autonomous combat robots** using Robocode.

Robocode is an open-source programming game where developers implement autonomous robots that compete in a virtual battlefield. Each robot must detect opponents, move strategically, aim its gun, and decide when to fire based on programmed logic.

The objective of this project is to design and evaluate **different tactical and algorithmic approaches** to robot combat, including movement strategies, targeting methods, and opponent-specific countermeasures.

## Implemented Robots

### CounterSittingDuck

A simple counter-strategy robot designed to defeat the classic sample robot [SittingDuck](https://robowiki.net/wiki/SittingDuck).

Strategy:

* Performs a full radar sweep to locate the opponent.
* Rotates the robot to align with the enemy bearing.
* Fires repeatedly once the gun is aligned.
* Handles gun heat cooldown to maintain a steady firing rate.

Since the opponent does not move or fire, the strategy focuses on **efficient targeting rather than movement**.

### CounterCorners

A robot designed to counter the predictable behavior of the sample robot [Corners](https://robowiki.net/wiki/Corners).
Strategy:

* Computes the robot's initial position relative to the battlefield center.
* Navigates toward the center of the battlefield to reduce vulnerability to corner attacks.
* Moves along a horizontal path to minimize exposure to corner-based firing.
* Continuously scans for enemies and adjusts the gun direction to match the enemy bearing.
* Fires at maximum power when aligned.

The implementation includes detailed handling of **multiple initial position cases** across the battlefield.

### CounterFire

A robot designed to counter the behavior of the sample robot [Fire](https://robowiki.net/wiki/Fire).

Strategy:

* Defines a rectangular patrol path located at the middle region of the battlefield.
* Moves along this rectangle while periodically scanning the battlefield.
* Uses half-side movements to trigger radar sweeps at regular intervals.
* Reorients the gun toward detected enemies using bearing calculations.
* Fires with high bullet power upon alignment.

The robot includes extensive logic to determine the **initial robot position relative to the patrol rectangle** and move efficiently toward it.

### Marksman

A more advanced robot focused on **accurate targeting and predictive firing**.

Strategy:

* Uses the `AdvancedRobot` API for improved control of radar, gun, and robot movement.
* Implements **radar locking** to maintain continuous tracking of detected enemies.
* Uses an **iterative predictive targeting algorithm** to estimate the enemy’s future position.
* Iteratively refines the predicted impact time by considering:
  * bullet speed
  * gun rotation speed
  * opponent velocity
  * opponent heading
* Adjusts gun direction accordingly before firing.

This robot represents the most sophisticated implementation in the project and demonstrates **predictive targeting in dynamic adversarial environments**.

### Telos

A positional control robot that prioritizes structured movement and controlled engagement zones.

Strategy:

* Uses the AdvancedRobot API to independently manage radar, gun, and movement systems.
* Navigates toward a predefined rectangular path within the battlefield, aligning itself with strategic edges and corners.
* Implements a state-based movement loop to patrol the area in a predictable but stable pattern.
* Locks radar upon detecting an opponent to maintain consistent tracking.
* Uses an iterative predictive targeting algorithm that accounts for both linear and circular enemy motion.
* Adjusts firepower dynamically based on distance, favoring precision at range and maximum damage at close quarters.
  
This robot emphasizes positional advantage and controlled movement combined with advanced predictive aiming.

### SpinCrusher

An aggressive orbiting robot designed for continuous pressure, evasive movement, and close-range dominance.

Strategy:

* Uses the AdvancedRobot API for fully decoupled radar, gun, and movement control.
* Maintains radar lock to ensure uninterrupted enemy tracking.
* Circles opponents at a preferred distance using adaptive orbiting, constantly adjusting angle and spacing.
* Incorporates wall avoidance and reactive direction changes when hit to improve survivability.
* Uses a predictive targeting system tailored for rotational enemy motion, iteratively estimating future positions.
* Dynamically scales firepower based on distance to balance energy efficiency and hit probability.
* Switches to a ramming and high-power firing tactic when the opponent is at very close range.
  
This robot blends mobility, aggression, and predictive firing into a fast-paced, pressure-oriented combat style.

## Concepts Explored

* Autonomous agent behavior
* Strategy design for adversarial environments
* Battlefield positioning
* Radar scanning strategies
* Gun heat management
* Predictive targeting algorithms
* Motion prediction using iterative methods

## Technologies

* Java
* Robocode
