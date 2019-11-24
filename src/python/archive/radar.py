#! /usr/bin/env python3

import sys
import time
import simpy
import march
from graphics import *


def random_walk(env):
    crayons = march.color_index()
    coords = march.starting_points()
    num_of_actors = 4
    num_set = list( range(0, num_of_actors) )
    actors = {}
    window = GraphWin("Vegvisir Random Walk", 800, 800)
    window.setBackground("black")
    for i in range(num_of_actors):
        x_coord, y_coord = coords[i]
        circle = Circle( Point(x_coord, y_coord), 10)
        circle.draw(window)
        circle.setFill(crayons[i])
        agent = march.agent( circle, i )
        #actors.append( march.agent( agent, i ) )
        filtered = list( filter( lambda x: x != i, num_set) )
        agent.set_neighbors( filtered )
        actors.update({i : agent})

    display = Text(Point(200, 700), "Hello")
    display.setTextColor("white")
    notDisplayed = True
    while True:
        try:
            key = window.getKey()
            print("Time is currently: {}".format(env.now))
            if key:
                for ag in list( actors.values() ):
                    if ag.id == 3 and env.now == 1:
                        ag.set_message("Broccoli")
                    has_update = ag.check_updates( actors )
                    if has_update:
                        ag.ixi.setFill("white")
                        display.setText("Had update")
                        if notDisplayed:
                            notDisplayed = False
                            display.draw(window)
                        time.sleep(0.25)
                        ag.ixi.setFill(crayons[ag.id])
                    has_update = False
                for ag in list( actors.values() ):
                    x_pos, y_pos = march.heading()
                    ag.ixi.move( x_pos, y_pos)
                #print(key)
            yield env.timeout(1)
        except GraphicsError as e:
            raise
            sys.exit(0)

def main():
    print("Program Started.")
    env = simpy.Environment()
    env.process( random_walk( env ) )
    env.run( until = 4 )

if __name__ == "__main__":
    main()
